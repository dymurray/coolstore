package com.redhat.coolstore.service;

import java.util.List;
import java.util.logging.Logger;

import jakarta.inject.Inject;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import jakarta.enterprise.context.ApplicationScoped;

import com.redhat.coolstore.model.*;

@ApplicationScoped
public class CatalogService {

    @Inject
    private EntityManager em;

    private static final Logger log = Logger.getLogger(CatalogService.class.getName());

    public CatalogService() {
    }

    public List<CatalogItemEntity> getCatalogItems() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CatalogItemEntity> criteria = cb.createQuery(CatalogItemEntity.class);
        Root<CatalogItemEntity> member = criteria.from(CatalogItemEntity.class);
        criteria.select(member);
        log.info("Retrieving catalog items");
        return em.createQuery(criteria).getResultList();
    }

    public CatalogItemEntity getCatalogItemById(String itemId) {
        log.info("Retrieving catalog item by id: " + itemId);
        return em.find(CatalogItemEntity.class, itemId);
    }

    public void updateInventoryItems(String itemId, int deducts) {
        log.info("Updating inventory items for item id: " + itemId);
        InventoryEntity inventoryEntity = getCatalogItemById(itemId).getInventory();
        int currentQuantity = inventoryEntity.getQuantity();
        inventoryEntity.setQuantity(currentQuantity-deducts);
        em.merge(inventoryEntity);
    }

}