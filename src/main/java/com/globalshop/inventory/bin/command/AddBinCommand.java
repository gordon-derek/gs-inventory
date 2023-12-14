package com.globalshop.inventory.bin.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

// Do not include in controller, currently only for seeding aggregates, end users should not have access
@Data
@AllArgsConstructor
public class AddBinCommand {
    @TargetAggregateIdentifier
    private final UUID binId;
    private String description;
}
