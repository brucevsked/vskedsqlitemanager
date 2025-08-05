package com.vsked.system.domain;

/**
 * record type id,actual is resource id
 */
public class RecordTypeId {

    private Long id;

    public RecordTypeId(Long id) {
        if(id==null){
            throw new IllegalArgumentException("record type id not be null！");
        }
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String toString() {
        return "" +id;
    }
}
