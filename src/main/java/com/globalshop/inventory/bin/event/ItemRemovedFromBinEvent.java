package com.globalshop.inventory.bin.event;

import com.globalshop.inventory.bin.domain.Bin;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ItemRemovedFromBinEvent {
    private final Bin bin;
}
