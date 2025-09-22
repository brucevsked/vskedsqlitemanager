package com.vsked.jpa.po;

import com.vsked.system.domain.DataType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;

@Table(name = "resourceAttribute")
@Entity
public class ResourceAttributePO implements Serializable {

    @Transient
    private static final long serialVersionUID = -6468949012287221473L;

    @Id
    private Long id;
    private String name;
    private DataType dataType;

    public ResourceAttributePO() {
    }

    public ResourceAttributePO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return "ResourceAttributePO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dataType='" + dataType + '\'' +
                '}';
    }
}
