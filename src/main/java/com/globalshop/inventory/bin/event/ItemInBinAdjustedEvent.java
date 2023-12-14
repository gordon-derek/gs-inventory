package com.globalshop.inventory.bin.event;

import com.globalshop.inventory.bin.domain.Bin;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemInBinAdjustedEvent {
    private final Bin bin;
}
