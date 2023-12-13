package com.globalshop.inventory.item.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AdjustItemCommand {
    @TargetAggregateIdentifier
    private final UUID itemId;
    private final String description;
    private final Integer quantity;
}
