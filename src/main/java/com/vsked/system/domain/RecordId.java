package com.vsked.system.domain;

/**
 * data record unique id
 * for permission one number is a database record
 */
public class RecordId {

    private Long id;

    public RecordId(Long id) {
        if(id==null){
            throw new IllegalArgumentException("record id not be null！");
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
