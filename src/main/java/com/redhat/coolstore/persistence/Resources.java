package com.redhat.coolstore.persistence;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class Resources {

    @Inject
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }
}