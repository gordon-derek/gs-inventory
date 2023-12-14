package com.globalshop.inventory.bin.event;

import com.globalshop.inventory.bin.domain.Bin;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BinAddedEvent {
    private final Bin bin;
}
