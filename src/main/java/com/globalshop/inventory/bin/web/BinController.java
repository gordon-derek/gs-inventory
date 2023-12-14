package com.globalshop.inventory.bin.web;

import com.globalshop.inventory.bin.command.AddBinCommand;
import com.globalshop.inventory.bin.domain.Bin;
import com.globalshop.inventory.bin.command.AddItemToBinCommand;
import com.globalshop.inventory.bin.command.AdjustItemInBinCommand;
import com.globalshop.inventory.bin.command.RemoveItemFromBinCommand;
import com.globalshop.inventory.bin.query.FindBinQuery;
import com.globalshop.inventory.bin.query.GetAllBinsQuery;
import com.globalshop.inventory.common.dto.ItemDTO;
import com.globalshop.inventory.item.domain.Item;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/bins")
public class BinController {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public BinController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @GetMapping()
    public CompletableFuture<Bin[]> getAll() {
        return queryGateway.query(new GetAllBinsQuery(), ResponseTypes.instanceOf(Bin[].class));
    }

    @GetMapping("/{binId}")
    public CompletableFuture<Bin> getBin(@PathVariable("binId") UUID binId) {
        return queryGateway.query(new FindBinQuery(binId), ResponseTypes.instanceOf(Bin.class));
    }

    @PostMapping("/{binId}/items")
    public CompletableFuture<Bin> addItemToBin(@PathVariable("binId") UUID binId, @Valid @RequestBody ItemDTO item) {
        return commandGateway.send(new AddItemToBinCommand(binId, UUID.fromString(item.getId()), item.getDescription(), item.getQuantity()));
    }

    @PutMapping("/{binId}/items/{itemId}")
    public CompletableFuture<ResponseEntity<Void>> adjustItemInBin(
            @PathVariable("binId") UUID binId,
            @PathVariable("itemId") UUID itemId,
            @Valid @RequestBody Item item
    ) {
        // TODO: fix 500 when aggregate not found(404)
        return commandGateway.send(new AdjustItemInBinCommand(binId, new Item(itemId, item.getDescription(), item.getQuantity())));
    }

    @DeleteMapping("/{binId}/items/{itemId}")
    public CompletableFuture<Void> removeItem(@PathVariable("binId") UUID binId,
                                              @PathVariable("itemId") UUID itemId) {
        // TODO: fix 500 when aggregate not found(404)
        return commandGateway.send(new RemoveItemFromBinCommand(binId, itemId));
    }

    @PostMapping("/seed")
    public void seedBins() throws IOException, ParseException, ExecutionException, InterruptedException {
        // this is an unfortunate side effect of using a json store with something EDD
        // I can't just load from file to memory, as we need the actual events to build the aggregate
        // This should be a first time run only scenario, in the future, events should get processed and thats
        // how new bins would make their way into the aggregate

        File jsonStore = new File(System.getProperty("user.dir") + "/db/bin/store.json");
        JSONParser parser = new JSONParser();
        JSONArray seed;
        if(jsonStore.exists()) {
            seed = (JSONArray) parser.parse(new FileReader(jsonStore));
        }
        else {
            seed = (JSONArray) parser.parse(new FileReader(System.getProperty("user.dir") + "/db/bin/seed.json"));
        }
        for(Object o : seed) {
            JSONObject jBin = (JSONObject) o;
            String id = (String) jBin.get("id");
            String description = (String) jBin.get("description");
            commandGateway.send(new AddBinCommand(UUID.fromString(id), description)).get();
            if(jBin.containsKey("items")) {
                Map items = (Map) jBin.get("items");
                for(Object entry : items.entrySet()) {
                    System.out.println(entry);
                }
            }
        }
    }
}
