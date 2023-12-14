package com.globalshop.inventory.bin.domain;

import com.globalshop.inventory.item.domain.Item;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

@Data
public class Bin {
    private UUID id;
    private String description;
    private HashMap<String,Item> items;
    // defaulting to false in constructors
    // CQRS with event sourcing doesn't hard delete
    private boolean isDeleted;

    public Bin(UUID id, String description) {
        this.id = id;
        this.description = description;
        this.items = new HashMap<>();
        this.isDeleted = false;
    }

    public void removeItem(String itemId) {
        this.items.remove(itemId);
    }

    public void upsertItem(Item item) {
        this.items.put(item.getId().toString(), item);
    }
}
