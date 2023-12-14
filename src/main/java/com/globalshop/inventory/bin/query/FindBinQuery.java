package com.globalshop.inventory.bin.query;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
public class FindBinQuery {
    private final UUID itemId;
}
