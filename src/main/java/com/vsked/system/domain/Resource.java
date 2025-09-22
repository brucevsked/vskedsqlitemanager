package com.vsked.system.domain;

import java.util.Set;

public class Resource {

    private ResourceId id;
    private ResourceName name;
    private ResourceDescription description;

    private Set<ResourceAttribute> attributes;
    private Set<ResourceAction> actions;

    public Resource(ResourceId id, ResourceName name) {
        this.id = id;
        this.name = name;
    }

    public Resource(ResourceId id, ResourceName name, ResourceDescription description, Set<ResourceAttribute> attributes, Set<ResourceAction> actions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.attributes = attributes;
        this.actions = actions;
    }

    public ResourceId getId() {
        return id;
    }

    public ResourceName getName() {
        return name;
    }

    public ResourceDescription getDescription() {
        return description;
    }

    public Set<ResourceAttribute> getAttributes() {
        return attributes;
    }

    public Set<ResourceAction> getActions() {
        return actions;
    }

    public void addAttribute(ResourceAttribute attribute) {
        this.attributes.add(attribute);
    }

    public void addAttributes(Set<ResourceAttribute> attributes){
        this.attributes.addAll(attributes);
    }
}
