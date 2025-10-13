package com.vsked.jpa.po;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;

@Entity
@Table(name = "record")
public class RecordPO implements Serializable {

    @Transient//此字段不需要持久化到数据库
    private static final long serialVersionUID = 9164581675313224050L;

    @Id
    private Long id;//实际上是各表中记录ID

    public RecordPO() {
    }

    public RecordPO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                "}";
    }
}
