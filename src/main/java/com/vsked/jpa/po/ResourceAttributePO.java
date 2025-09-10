package com.vsked.jpa.po;

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

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name=" + name  +
                "}";
    }
}
