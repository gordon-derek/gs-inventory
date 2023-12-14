package com.globalshop.inventory.item.web;

import com.globalshop.inventory.item.command.AddItemCommand;
import com.globalshop.inventory.item.command.AdjustItemCommand;
import com.globalshop.inventory.item.command.RemoveItemCommand;
import com.globalshop.inventory.item.domain.Item;
import com.globalshop.inventory.common.dto.ItemDTO;
import com.globalshop.inventory.item.query.FindItemQuery;
import com.globalshop.inventory.item.query.GetAllItemsQuery;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public ItemController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @GetMapping()
    public CompletableFuture<Item[]> getItems() {
        return queryGateway.query(new GetAllItemsQuery(), ResponseTypes.instanceOf(Item[].class));
    }

    @PostMapping()
    public CompletableFuture<Void> addItem(@Valid @RequestBody ItemDTO item) {
        return commandGateway.send(new AddItemCommand(UUID.fromString(item.getId()), item.getDescription(), item.getQuantity()));
    }

    @GetMapping("/{itemId}")
    public CompletableFuture<Item> getItem(@PathVariable("itemId") UUID itemId) {
        return queryGateway.query(new FindItemQuery(itemId), ResponseTypes.instanceOf(Item.class));
    }

    @PutMapping("/{itemId}")
    public CompletableFuture<ResponseEntity<Item>> adjustItem(
            @PathVariable("itemId") UUID itemId,
            @RequestBody Item item
    ) {
        // TODO: fix 500 when aggregate not found(404)
        return commandGateway.send(new AdjustItemCommand(itemId, item.getDescription(), item.getQuantity()));
    }

    @DeleteMapping("/{itemId}")
    public CompletableFuture<Void> removeItem(@PathVariable("itemId") UUID itemId) {
        // TODO: fix 500 when aggregate not found(404)
        return commandGateway.send(new RemoveItemCommand(itemId));
    }
}
