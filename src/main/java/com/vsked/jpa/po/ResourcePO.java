package com.vsked.jpa.po;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Table(name = "resource")
@Entity
public class ResourcePO implements Serializable {

    @Transient
    private static final long serialVersionUID = 1244771255166352533L;

    @Id
    private Long id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "resourceResourceAttribute",joinColumns = {@JoinColumn(name = "resourceId")},inverseJoinColumns = {@JoinColumn(name = "resourceAttributeId")})
    private List<ResourceAttributePO> resourceAttributes;

    public ResourcePO() {
    }

    public ResourcePO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ResourcePO(Long id, String name, List<ResourceAttributePO> resourceAttributes) {
        this.id = id;
        this.name = name;
        this.resourceAttributes = resourceAttributes;
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

    public List<ResourceAttributePO> getResourceAttributes() {
        return resourceAttributes;
    }

    public void setResourceAttributes(List<ResourceAttributePO> resourceAttributes) {
        this.resourceAttributes = resourceAttributes;
    }

    public String toString() {
        return "{" +
                "id=" + id +
                ", name=" + name +
                ", resourceAttributes=" + resourceAttributes +
                "}";
    }
}
