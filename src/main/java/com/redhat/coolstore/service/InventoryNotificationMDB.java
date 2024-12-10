package com.redhat.coolstore.service;

import com.redhat.coolstore.model.Order;
import com.redhat.coolstore.utils.Transformers;

import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Consume;
import io.smallrye.reactive.messaging.annotations.Stream;

import jakarta.inject.Inject;
import jakarta.naming.NamingException;

import java.util.logging.Logger;

public class InventoryNotificationMDB {

    private static final int LOW_THRESHOLD = 50;

    @Inject
    private CatalogService catalogService;

    private static final Logger LOGGER = Logger.getLogger(InventoryNotificationMDB.class.getName());

    @Consume("topic/orders")
    public void onMessage(String message) {
        try {
            LOGGER.info("received message inventory");
            Order order = Transformers.jsonToOrder(message);
            order.getItemList().forEach(orderItem -> {
                int old_quantity = catalogService.getCatalogItemById(orderItem.getProductId()).getInventory().getQuantity();
                int new_quantity = old_quantity - orderItem.getQuantity();
                if (new_quantity < LOW_THRESHOLD) {
                    LOGGER.info("Inventory for item " + orderItem.getProductId() + " is below threshold (" + LOW_THRESHOLD + "), contact supplier!");
                } else {
                    orderItem.setQuantity(new_quantity);
                }
            });
        } catch (Exception e) {
            LOGGER.severe("An exception occurred: " + e.getMessage());
        }
    }
}