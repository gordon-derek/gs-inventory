package com.globalshop.inventory.bin.aggregate;


import com.globalshop.inventory.bin.command.AddBinCommand;
import com.globalshop.inventory.bin.domain.Bin;
import com.globalshop.inventory.bin.command.AddItemToBinCommand;
import com.globalshop.inventory.bin.command.AdjustItemInBinCommand;
import com.globalshop.inventory.bin.command.RemoveItemFromBinCommand;
import com.globalshop.inventory.bin.event.BinAddedEvent;
import com.globalshop.inventory.bin.event.ItemAddedToBinEvent;
import com.globalshop.inventory.bin.event.ItemInBinAdjustedEvent;
import com.globalshop.inventory.bin.event.ItemRemovedFromBinEvent;
import com.globalshop.inventory.item.domain.Item;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.HashMap;
import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class BinAggregate {
    @AggregateIdentifier
    private UUID binId;
    private String description;
    private HashMap<String, Item> items;

    @CommandHandler
    public BinAggregate(AddBinCommand command) {
        apply(new BinAddedEvent(new Bin(command.getBinId(), command.getDescription())));
    }

    @EventSourcingHandler
    public void on(BinAddedEvent event) {
        Bin bin = event.getBin();
        this.binId = bin.getId();
        this.description = bin.getDescription();
        this.items = bin.getItems();
    }

    @CommandHandler
    public void handle(AddItemToBinCommand command) {
        if(items.get(command.getItemId().toString()) != null) {
            throw new IllegalArgumentException("Item Already Exists in Bin");
        }
        Bin bin = new Bin(command.getBinId(), description);
        bin.setItems(items);
        bin.upsertItem(new Item(command.getItemId(), command.getDescription(), command.getQuantity()));
        apply(new ItemAddedToBinEvent(bin));
    }

    @EventSourcingHandler
    public void on(ItemAddedToBinEvent event) {
        Bin bin = event.getBin();
        items = bin.getItems();
    }

    @CommandHandler
    public void handle(AdjustItemInBinCommand command) {
        Item curItem = items.get(command.getItem().getId().toString());
        Item cmdItem = command.getItem();
        if(curItem == null)
            throw new RuntimeException("Item doesn't exist");
        Bin adjusted = new Bin(binId, description);
        if(cmdItem.getDescription() != null) {
            curItem.setDescription(cmdItem.getDescription());
        }
        if(cmdItem.getQuantity() != null) {
            curItem.setQuantity(cmdItem.getQuantity());
        }
        adjusted.setItems(items);
        adjusted.upsertItem(curItem);
        apply(new ItemInBinAdjustedEvent(adjusted));
    }

    @EventSourcingHandler
    public void on(ItemInBinAdjustedEvent event) {
        items = event.getBin().getItems();;
    }

    @CommandHandler
    public void handle(RemoveItemFromBinCommand command) {
        if(items.get(command.getItemId().toString()) == null)
            throw new RuntimeException("Item doesn't exist");
        Bin bin = new Bin(binId, description);
        bin.setItems(items);
        bin.removeItem(command.getItemId().toString());
        apply(new ItemRemovedFromBinEvent(bin));
    }

    @EventSourcingHandler
    public void on(ItemRemovedFromBinEvent event) {
        items = event.getBin().getItems();
    }

    protected BinAggregate(){}
}
