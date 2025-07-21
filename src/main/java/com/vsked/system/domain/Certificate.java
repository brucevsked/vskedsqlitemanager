package com.vsked.system.domain;

public class Certificate {

    private CertificateId id;
    private CertificateName name;
    private CertificateContent content;

    public Certificate(CertificateId id, CertificateName name, CertificateContent content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }

    public CertificateId getId() {
        return id;
    }

    public CertificateName getName() {
        return name;
    }

    public CertificateContent getContent() {
        return content;
    }
}
