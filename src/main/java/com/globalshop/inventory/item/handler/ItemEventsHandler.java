package com.globalshop.inventory.item.handler;

import com.globalshop.inventory.item.domain.Item;
import com.globalshop.inventory.item.event.ItemAddedEvent;
import com.globalshop.inventory.item.event.ItemAdjustedEvent;
import com.globalshop.inventory.item.event.ItemRemovedEvent;
import com.globalshop.inventory.item.query.FindItemQuery;
import com.globalshop.inventory.item.query.GetAllItemsQuery;
import com.globalshop.inventory.item.repository.ItemRepository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

@Service
public class ItemEventsHandler {
    private final ItemRepository items = new ItemRepository();

    /******** Event Handlers ********/

    @EventHandler
    public void on(ItemAddedEvent event) {
        items.addItem(event.getItem());
    }

    @EventHandler
    public void on(ItemAdjustedEvent event) {
        items.updateItem(event.getItem());
    }

    @EventHandler
    public void on(ItemRemovedEvent event) {
        Item item = items.getItem(event.getItemId());
        item.setDeleted(true);
        items.updateItem(item);
    }

    /******** Query Handlers ********/
    @QueryHandler
    public Item[] handle(GetAllItemsQuery query) {
        return items.getAll();
    }

    @QueryHandler
    public Item handle(FindItemQuery query) {
        // Do we want to return "deleted" items?
        Item item = items.getItem(query.getItemId());
        if(item == null) {
            throw new RuntimeException("Resource not found");
        }
        return item;
    }
}
