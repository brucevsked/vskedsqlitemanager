package com.vsked.system.domain;

public class ResourceAttribute {
    private ResourceAttributeId id;
    private ResourceAttributeName name;
    private DataType dataType;

    public ResourceAttribute(ResourceAttributeId id, ResourceAttributeName name, DataType dataType) {
        this.id = id;
        this.name = name;
        this.dataType = dataType;
    }

    public ResourceAttributeId getId() {
        return id;
    }

    public ResourceAttributeName getName() {
        return name;
    }

    public DataType getDataType() {
        return dataType;
    }
}
