package com.globalshop.inventory.item.query;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
public class FindItemQuery {
    private final UUID itemId;
}
