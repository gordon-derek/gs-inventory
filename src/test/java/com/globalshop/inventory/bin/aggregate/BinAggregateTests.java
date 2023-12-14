package com.globalshop.inventory.bin.aggregate;

import com.globalshop.inventory.bin.command.AddItemToBinCommand;
import com.globalshop.inventory.bin.command.AdjustItemInBinCommand;
import com.globalshop.inventory.bin.command.RemoveItemFromBinCommand;
import com.globalshop.inventory.bin.domain.Bin;
import com.globalshop.inventory.bin.event.BinAddedEvent;
import com.globalshop.inventory.bin.event.ItemAddedToBinEvent;
import com.globalshop.inventory.bin.event.ItemInBinAdjustedEvent;
import com.globalshop.inventory.bin.event.ItemRemovedFromBinEvent;
import com.globalshop.inventory.item.command.AddItemCommand;
import com.globalshop.inventory.item.command.AdjustItemCommand;
import com.globalshop.inventory.item.command.RemoveItemCommand;
import com.globalshop.inventory.item.domain.Item;
import com.globalshop.inventory.item.event.ItemAddedEvent;
import com.globalshop.inventory.item.event.ItemAdjustedEvent;
import com.globalshop.inventory.item.event.ItemRemovedEvent;
import org.axonframework.modelling.command.AggregateNotFoundException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class BinAggregateTests {
    private FixtureConfiguration<BinAggregate> fixture;
    private UUID binId;
    private String description;
    private Item item1;
    private Item item2;
    private Item item3;


    @BeforeEach
    public void setUp() {
        fixture = new AggregateTestFixture<>(BinAggregate.class);

        // Do this on every test to get fresh UUIDs and faker data if I get to autogen
        // bin
        binId = UUID.randomUUID();
        description = "Motorcycles";
        // items
        item1 = new Item(UUID.randomUUID(), "Honda CRF250R", 1);
        item2 = new Item(UUID.randomUUID(), "Triumph Trident 660", 1);
        item3 = new Item(UUID.randomUUID(), "Beta RR 125", 1);
    }

    @Test
    void addItemToBin() {
        Bin binWItem1 = new Bin(binId, description);
        binWItem1.upsertItem(item1);

        fixture.given(new BinAddedEvent(new Bin(binId, description)))
                .when(new AddItemToBinCommand(binId, item1.getId(), item1.getDescription(), item1.getQuantity()))
                .expectEvents(new ItemAddedToBinEvent(binWItem1));
    }

    @Test
    void addItemsToBinOfMultipleItems() {
        Bin binWTwoItems = new Bin(binId, description);
        binWTwoItems.upsertItem(item1);
        binWTwoItems.upsertItem((item2));

        Bin binWAllItems = new Bin(binId, description);
        HashMap<String, Item> items = new HashMap<>();
        items.put(item1.getId().toString(), item1);
        items.put(item2.getId().toString(), item2);
        items.put(item3.getId().toString(), item3);
        binWAllItems.setItems(items);

        //A little cheat but for testing purposes we can just add an event with the updated Items.
        fixture.given(new BinAddedEvent(new Bin(binId, description)), new ItemAddedToBinEvent(binWTwoItems))
                .when(new AddItemToBinCommand(binId, item3.getId(), item3.getDescription(), item3.getQuantity()))
                .expectEvents(new ItemAddedToBinEvent(binWAllItems));
    }

    @Test
    void addItemToNonExistantBin() {
        fixture.givenNoPriorActivity()
                .when(new AddItemToBinCommand(UUID.randomUUID(), item1.getId(), item1.getDescription(), item1.getQuantity()))
                .expectException(AggregateNotFoundException.class);
    }

    @Test
    void adjustItemInBin() {
        Bin binWTwoItems = new Bin(binId, description);
        binWTwoItems.upsertItem(item1);
        binWTwoItems.upsertItem((item2));
        Bin expectedBin = new Bin(binId, description);
        item1.setQuantity(12);
        expectedBin.upsertItem(item1);
        expectedBin.upsertItem(item2);

        fixture.given(new BinAddedEvent(new Bin(binId, description)), new ItemAddedToBinEvent(binWTwoItems))
                .when(new AdjustItemInBinCommand(binId, item1))
                .expectEvents(new ItemInBinAdjustedEvent(expectedBin));
    }

    @Test
    void adjustItemInBin_ItemNotExistsInBin() {
        fixture.given(new BinAddedEvent(new Bin(binId, description)))
                .when(new AdjustItemInBinCommand(binId, item1))
                .expectException(RuntimeException.class);
    }

    @Test
    void removeItemFromBin() {
        Bin binWTwoItems = new Bin(binId, description);
        binWTwoItems.upsertItem(item1);
        binWTwoItems.upsertItem((item2));
        Bin expectedBin = new Bin(binId, description);
        expectedBin.upsertItem(item2);

        fixture.given(new BinAddedEvent(new Bin(binId, description)), new ItemAddedToBinEvent(binWTwoItems))
                .when(new RemoveItemFromBinCommand(binId, item1.getId()))
                .expectEvents(new ItemRemovedFromBinEvent(expectedBin));
    }

    @Test
    void removeItemFromBin_ItemNotExistsInBin() {
        fixture.given(new BinAddedEvent(new Bin(binId, description)))
                .when(new RemoveItemFromBinCommand(binId, item1.getId()))
                .expectException(RuntimeException.class);
    }
}
