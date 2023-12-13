package com.globalshop.inventory.item.aggregate;


import com.globalshop.inventory.item.command.AddItemCommand;
import com.globalshop.inventory.item.command.AdjustItemCommand;
import com.globalshop.inventory.item.command.RemoveItemCommand;
import com.globalshop.inventory.item.domain.Item;
import com.globalshop.inventory.item.event.ItemAddedEvent;
import com.globalshop.inventory.item.event.ItemAdjustedEvent;
import com.globalshop.inventory.item.event.ItemRemovedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class ItemAggregate {
    @AggregateIdentifier
    private UUID itemId;
    private String description;
    private Integer quantity = 0;
    private boolean isDeleted;

    @CommandHandler
    public ItemAggregate(AddItemCommand command) {
        AggregateLifecycle.apply(new ItemAddedEvent(new Item(command.getItemId(), command.getDescription(), command.getQuantity())));
    }

    @EventSourcingHandler
    public void on(ItemAddedEvent event) {
        Item item = event.getItem();
        itemId = item.getId();
        description = item.getDescription();
        quantity = item.getQuantity();
        isDeleted = false;
    }

    @CommandHandler
    public void handle(AdjustItemCommand command) {
        // error out if no aggregate found
        if(itemId == null)
            throw new RuntimeException("not found");
        Item adjusted = new Item(itemId, description, quantity);
        if(command.getQuantity() != null)
            adjusted.setQuantity(command.getQuantity());
        if(command.getDescription() != null)
            adjusted.setDescription(command.getDescription());
        apply(new ItemAdjustedEvent(adjusted));
    }

    @EventSourcingHandler
    public void on(ItemAdjustedEvent event) {
        description = event.getItem().getDescription();
        quantity = event.getItem().getQuantity();
    }

    @CommandHandler
    public void handle(RemoveItemCommand command) {
        // error out if no aggregate found
        if(itemId == null) {
            throw new RuntimeException("not found");
        } else if(isDeleted) {
            throw new RuntimeException("already deleted");
        }

        apply(new ItemRemovedEvent(itemId));
    }

    @EventSourcingHandler
    public void on(ItemRemovedEvent event) {
        isDeleted = true;
    }

    protected ItemAggregate(){}
}
