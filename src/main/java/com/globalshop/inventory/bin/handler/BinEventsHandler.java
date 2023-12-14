package com.globalshop.inventory.bin.handler;

import com.globalshop.inventory.bin.domain.Bin;
import com.globalshop.inventory.bin.event.BinAddedEvent;
import com.globalshop.inventory.bin.event.ItemAddedToBinEvent;
import com.globalshop.inventory.bin.event.ItemInBinAdjustedEvent;
import com.globalshop.inventory.bin.event.ItemRemovedFromBinEvent;
import com.globalshop.inventory.bin.query.FindBinQuery;
import com.globalshop.inventory.bin.query.GetAllBinsQuery;
import com.globalshop.inventory.bin.repository.BinRepository;
import com.globalshop.inventory.item.domain.Item;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Service
public class BinEventsHandler {
    private final BinRepository bins;

    public BinEventsHandler() throws ParseException, IOException {
        bins = new BinRepository();
    }

    /******** Event Handlers ********/

    @EventHandler
    public void on(ItemAddedToBinEvent event) {
        bins.updateItem(event.getBin());
    }

    @EventHandler
    public void on(ItemInBinAdjustedEvent event) {
        bins.updateItem(event.getBin());
    }

    @EventHandler
    public void on(ItemRemovedFromBinEvent event) {
        bins.updateItem(event.getBin());
    }

    @EventHandler
    public void on(BinAddedEvent event) { bins.save(event.getBin());}

    /******** Query Handlers ********/

    @QueryHandler
    public Bin handle(FindBinQuery query) {
        // Do we want to return "deleted" items?
        Bin item = bins.getItem(query.getItemId());
        if(item == null) {
            throw new RuntimeException("Resource not found");
        }
        return item;
    }

    @QueryHandler
    public Bin[] handle(GetAllBinsQuery query) {
        return bins.getAll();
    }
}
