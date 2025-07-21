package com.vsked.system.domain;

public class ResourceActionParameter {

    private ResourceActionParameterId id;
    private ResourceActionParameterName name;
    private DataType dataType;
    private ResourceActionParameterSeq seq;

    public ResourceActionParameter(ResourceActionParameterId id, ResourceActionParameterName name, DataType dataType, ResourceActionParameterSeq seq) {
        this.id = id;
        this.name = name;
        this.dataType = dataType;
        this.seq = seq;
    }

    public ResourceActionParameterId getId() {
        return id;
    }

    public ResourceActionParameterName getName() {
        return name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public ResourceActionParameterSeq getSeq() {
        return seq;
    }
}
