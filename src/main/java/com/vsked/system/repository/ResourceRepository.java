package com.vsked.system.repository;

import com.vsked.system.domain.Resource;
import com.vsked.system.domain.ResourceId;
import com.vsked.system.domain.ResourceName;

public interface ResourceRepository {

    Boolean isExistBy(ResourceName resourceName);
    Boolean isExistBy(ResourceId resourceId);
    Resource getBy(ResourceId resourceId);
    Resource getBy(ResourceName resourceName);
    void save(Resource resource);
    Long nextResourceId();

}
