package com.globalshop.inventory.bin.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RemoveItemFromBinCommand {
    @TargetAggregateIdentifier
    private UUID binId;
    private UUID itemId;
}
