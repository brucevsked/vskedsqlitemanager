package com.vsked.jpa.po;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;

@Table(name = "requestId")
@Entity
public class RequestIdPO implements Serializable {

    @Transient
    private static final long serialVersionUID = 7407775017516043666L;

    @Id
    private Long id;

    public RequestIdPO() {
    }

    public RequestIdPO(Long id) {
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
