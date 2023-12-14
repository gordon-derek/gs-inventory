package com.globalshop.inventory.item.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class ItemTest {
    @Test
    public void allArgsConstructorProperlyInitializes() {
        UUID id = UUID.randomUUID();
        String description = "Motorcycle";
        int quantity = 10;

        Item item = new Item(id, description, quantity);

        assertAll("All Fields of Item are the same as parameters in construction",
                () -> assertEquals(id, item.getId()),
                () -> assertEquals(description, item.getDescription()),
                () -> assertEquals(quantity, item.getQuantity())
        );
    }
}
