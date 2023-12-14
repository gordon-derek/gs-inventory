package com.globalshop.inventory.item.repository;

import com.globalshop.inventory.item.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class ItemRepository {
    private Map<String, Item> store;

    public ItemRepository() {
        store = new HashMap<>();
    }

    public void addItem(Item item) {
        store.put(item.getId().toString(), item);
    }

    public void removeItem(UUID id) {
        store.remove(id.toString());
    }

    public Item getItem(UUID id) {
        return store.get(id.toString());
    }

    public Item[] getAll() { return store.values().toArray(new Item[0]); }

    public void updateItem(Item item) {
        store.replace(item.getId().toString(), item);
    }
}
