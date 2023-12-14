package com.globalshop.inventory.bin.repository;

import com.globalshop.inventory.bin.domain.Bin;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class BinRepository {
    private final Map<String, Bin> store;

    public BinRepository() {
        store = new HashMap<>();
    }

    public void save(Bin bin) {
        store.put(bin.getId().toString(), bin);
    }

    public void removeItem(UUID id) {
        store.remove(id.toString());
    }

    public Bin getItem(UUID id) {
        return store.get(id.toString());
    }

    public Bin[] getAll() { return store.values().toArray(new Bin[0]); }

    public void updateItem(Bin bin) {
        store.replace(bin.getId().toString(), bin);
    }
}
