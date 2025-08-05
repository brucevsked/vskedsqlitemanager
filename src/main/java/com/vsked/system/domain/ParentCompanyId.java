package com.vsked.system.domain;

public class ParentCompanyId {
    private Long parentId;
    public ParentCompanyId(Long parentId){
        this.parentId = parentId;
    }
    public Long getParentId(){
        return parentId;
    }
}
