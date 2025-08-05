package com.vsked.system.domain;

public class Record {

    private RecordId id;
    private RecordTypeId type;

    public Record(RecordId id, RecordTypeId type) {
        this.id = id;
        this.type = type;
    }

    public RecordId getId() {
        return id;
    }

    public RecordTypeId getType() {
        return type;
    }

    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", \"type\":" + type +
                "}";
    }
}
