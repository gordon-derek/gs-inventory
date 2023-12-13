package com.globalshop.inventory.item.event;

import com.globalshop.inventory.item.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemAddedEvent {
    private final Item item;
}
