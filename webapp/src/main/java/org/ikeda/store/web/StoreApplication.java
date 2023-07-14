package org.ikeda.store.web;

import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.ApplicationPath;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/webApi")
public class StoreApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<>();
        classes.add(StoreController.class);
        return classes;
    }
}
