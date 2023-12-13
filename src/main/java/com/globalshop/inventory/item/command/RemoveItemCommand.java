package com.globalshop.inventory.item.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RemoveItemCommand {
    @TargetAggregateIdentifier
    private UUID itemId;
}
