package com.globalshop.inventory.bin.command;

import com.globalshop.inventory.item.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AdjustItemInBinCommand {
    @TargetAggregateIdentifier
    private final UUID binId;
    private final Item item;
}
