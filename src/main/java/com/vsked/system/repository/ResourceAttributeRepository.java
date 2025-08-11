package com.vsked.system.repository;

import com.vsked.system.domain.ResourceAttribute;
import com.vsked.system.domain.ResourceAttributeId;

public interface ResourceAttributeRepository {

    ResourceAttribute getBy(ResourceAttributeId resourceAttributeId);
    Boolean getIsExistBy(ResourceAttributeId resourceAttributeId);
    void save(ResourceAttribute resourceAttribute);
    Long nextResourceAttributeId();

}
