package com.globalshop.inventory.item.web;

import com.globalshop.inventory.item.command.AddItemCommand;
import com.globalshop.inventory.item.command.AdjustItemCommand;
import com.globalshop.inventory.item.command.RemoveItemCommand;
import com.globalshop.inventory.item.domain.Item;
import com.globalshop.inventory.item.query.FindItemQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Controller("/items")
public class ItemController {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public ItemController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping()
    public CompletableFuture<Item> addItem(@RequestBody Item item) {
        if(item.getId().toString().isEmpty()) {
            item.setId(UUID.randomUUID());
        }
        return commandGateway.send(new AddItemCommand(item.getId(), item.getDescription(), item.getQuantity()));
    }

    @PutMapping("/{itemId}")
    public CompletableFuture<Item> adjustItem(
            @PathVariable("itemId") UUID itemId,
            @RequestBody Item item
    ) {
        return commandGateway.send(new AdjustItemCommand(itemId, item.getDescription(), item.getQuantity()));
    }

    @DeleteMapping("/{itemId}")
    public CompletableFuture<Void> removeItem(@PathVariable("itemId") UUID itemId) {
        return commandGateway.send(new RemoveItemCommand(itemId));
    }

    @GetMapping("/{itemId}")
    public CompletableFuture<Item> getItem(@PathVariable("itemId") UUID itemId) {
        return queryGateway.query(new FindItemQuery(itemId), ResponseTypes.instanceOf(Item.class));
    }
}
