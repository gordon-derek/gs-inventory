package com.globalshop.inventory.item.domain;

import lombok.Data;

import java.util.UUID;

@Data
public class Item {
    private UUID id;
    private String description;
    private Integer quantity;
    // defaulting to false in constructors
    // CQRS with event sourcing doesn't hard delete
    private boolean isDeleted;

    public Item(UUID id, String description, int quantity) {
        this.id = id;
        this.description = description;
        this.quantity = quantity;
        this.isDeleted = false;
    }
}
