package com.redhat.coolstore.service;

import java.util.logging.Logger;
import jakarta.inject.Inject;
import io.quarkus.funqy.FunqyContext;
import io.quarkus.funqy.emitter.Emitter;
import io.quarkus.funqy.emitter.Channel;

import com.redhat.coolstore.model.ShoppingCart;
import com.redhat.coolstore.utils.Transformers;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ShoppingCartOrderProcessor  {

    @Inject
    Logger log;

    @Inject
    @Channel("orders")
    private Emitter<String> ordersTopicEmitter;

    @Inject
    @io.quarkus.jms.JMSContext("orders-connection-factory")
    private io.quarkus.jms.JMSContext context;

    public void  process(ShoppingCart cart) {
        log.info("Sending order from processor: ");
        ordersTopicEmitter.send(Transformers.shoppingCartToJson(cart));
    }
}