package com.globalshop.inventory.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;


@Data
public class ItemDTO {
    @NotBlank
    @UUID
    private String id;

    @NotBlank
    private String description;

    @Min(value=0)
    private int quantity;
}
