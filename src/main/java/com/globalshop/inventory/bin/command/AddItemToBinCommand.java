package com.globalshop.inventory.bin.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AddItemToBinCommand {
    @TargetAggregateIdentifier
    private final UUID binId;
    private final UUID itemId;
    private final String description;
    private final Integer quantity;
}