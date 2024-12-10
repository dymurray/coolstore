package com.redhat.coolstore.service;

import io.smallrye.reactive.messaging.Incoming;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.ExceptionHandler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import com.redhat.coolstore.model.Order;
import com.redhat.coolstore.utils.Transformers;

@ApplicationScoped
public class OrderServiceMDB {

    @Inject
    OrderService orderService;

    @Inject
    CatalogService catalogService;

    @Incoming("orders")
    public void onMessage(String orderStr) {
        try {
            Order order = Transformers.jsonToOrder(orderStr);
            orderService.save(order);
            order.getItemList().forEach(orderItem -> {
                catalogService.updateInventoryItems(orderItem.getProductId(), orderItem.getQuantity());
            });
        } catch (Exception e) {
            handleException(e);
        }
    }

    @ExceptionHandler
    public void handleException(Exception e) {
        // Handle the exception using the Quarkus exception handling mechanism
    }
}