package com.vsked.system.domain;

import java.util.Set;

public class ResourceAction {
    private ResourceActionId id;
    private ResourceActionName name;
    private Set<ResourceActionParameter> parameters;

    public ResourceAction(ResourceActionId id, ResourceActionName name, Set<ResourceActionParameter> parameters) {
        this.id = id;
        this.name = name;
        this.parameters = parameters;
    }

    public ResourceActionId getId() {
        return id;
    }

    public ResourceActionName getName() {
        return name;
    }

    public Set<ResourceActionParameter> getParameters() {
        return parameters;
    }
}
