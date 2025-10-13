package com.vsked.jpa.po;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;

@Table(name = "navigation")
@Entity
public class NavigationPO implements Serializable {

    @Transient
    private static final long serialVersionUID = 7134845035061681037L;

    @Id
    private Long id;
    private String name;
    private Integer level;
    private String address;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private NavigationPO parent;

    public NavigationPO() {
    }

    public NavigationPO(NavigationPO parent,Long id, String name, Integer level, String address) {
        this.parent=parent;
        this.id = id;
        this.name = name;
        this.level = level;
        this.address = address;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public NavigationPO getParent() {
        return parent;
    }

    public void setParent(NavigationPO parent) {
        this.parent = parent;
    }


    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name=" + name +
                ", level=" + level +
                ", address=" + address +
                ", parent=" + parent +
                "}";
    }
}
