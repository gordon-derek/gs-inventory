package com.globalshop.inventory.item.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ItemRemovedEvent {
    private final UUID itemId;
}
