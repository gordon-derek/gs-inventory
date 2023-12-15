package com.globalshop.inventory.item.aggregate;


import com.globalshop.inventory.item.command.AddItemCommand;
import com.globalshop.inventory.item.command.AdjustItemCommand;
import com.globalshop.inventory.item.command.RemoveItemCommand;
import com.globalshop.inventory.item.domain.Item;
import com.globalshop.inventory.item.event.ItemAddedEvent;
import com.globalshop.inventory.item.event.ItemAdjustedEvent;
import com.globalshop.inventory.item.event.ItemRemovedEvent;
import org.axonframework.eventsourcing.eventstore.EventStoreException;
import org.axonframework.modelling.command.AggregateInvocationException;
import org.axonframework.modelling.command.AggregateNotFoundException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class ItemAggregateTests {
    private FixtureConfiguration<ItemAggregate> fixture;

    @BeforeEach
    public void setUp() {
        fixture = new AggregateTestFixture<>(ItemAggregate.class);
    }

    @Test
    void addItem() {
        UUID itemId = UUID.randomUUID();
        String description = "Honda CRF250R";
        int quantity = 5;
        fixture.givenNoPriorActivity()
                .when(new AddItemCommand(itemId, description, quantity))
                .expectEvents(new ItemAddedEvent(new Item(itemId, description, quantity)));
    }

    @Test
    void addDuplicateItem() {
        UUID itemId = UUID.randomUUID();
        String description = "Honda CRF250R";
        int quantity = 5;
        fixture.given(new ItemAddedEvent(new Item(itemId, description, quantity)))
                .when(new AddItemCommand(itemId, description, quantity))
                .expectException(EventStoreException.class);
    }

    @Test
    void adjustValidItem() {
        UUID itemId = UUID.randomUUID();
        String description = "Honda CRF450R";
        int quantity = 3;
        int updatedQuantity = 15;
        fixture.given(new ItemAddedEvent(new Item(itemId, description, quantity)))
                .when(new AdjustItemCommand(itemId, description, updatedQuantity))
                .expectEvents(new ItemAdjustedEvent(new Item(itemId, description, updatedQuantity)));
    }

    @Test
    void adjustNonExistentItem() {
        UUID itemId = UUID.randomUUID();
        String description = "Honda CRF450R";
        int quantity = 3;
        fixture.givenNoPriorActivity()
                .when(new AdjustItemCommand(itemId, description, quantity))
                .expectException(AggregateNotFoundException.class);
    }

    @Test
    void removeValidItem() {
        UUID itemId = UUID.randomUUID();
        String description = "Honda CRF450R";
        int quantity = 3;
        fixture.given(new ItemAddedEvent(new Item(itemId, description, quantity)))
                .when(new RemoveItemCommand(itemId))
                .expectEvents(new ItemRemovedEvent(itemId));
    }

    @Test
    void removeItemNotExists() {
        UUID itemId = UUID.randomUUID();
        String description = "Honda CRF450R";
        int quantity = 3;
        fixture.given()
                .when(new RemoveItemCommand(itemId))
                .expectException(AggregateNotFoundException.class);
    }

    @Test
    void removeAlreadyExistsItem() {
        UUID itemId = UUID.randomUUID();
        String description = "Honda CRF450R";
        int quantity = 3;
        fixture.given(new ItemAddedEvent(new Item(itemId, description, quantity)), new ItemRemovedEvent(itemId))
                .when(new RemoveItemCommand(itemId))
                .expectException(RuntimeException.class);
    }
}
