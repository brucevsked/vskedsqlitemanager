package com.vsked.jpa.po;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "certificate")
@Entity
public class CertificatePO implements Serializable {

    @Transient
    private static final long serialVersionUID = -3054004465172100952L;

    @Id
    private Long id;
    private Date expireTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public CertificatePO() {
    }

    public CertificatePO(Long id, Date expireTime) {
        this.id = id;
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "CertificatePO{" +
                "id=" + id +
                ", expireTime=" + expireTime +
                '}';
    }
}
