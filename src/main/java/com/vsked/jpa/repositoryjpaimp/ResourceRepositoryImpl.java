package com.vsked.jpa.repositoryjpaimp;

import com.vsked.jpa.po.ResourceAttributePO;
import com.vsked.jpa.po.ResourcePO;
import com.vsked.system.domain.Resource;
import com.vsked.system.domain.ResourceAttribute;
import com.vsked.system.domain.ResourceId;
import com.vsked.system.domain.ResourceName;
import com.vsked.system.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class ResourceRepositoryImpl implements ResourceRepository {

    @Autowired
    ResourceRepository resourceSpringDataJPARepository;
    @Autowired
    ResourceAttributeRepositoryImpl resourceAttributeRepositoryImpl;

    public Resource getBy(ResourceId resourceId){
        ResourcePO po=getOptionalBy(resourceId.getId()).get();
        return poToResource(po);
    }

    public Optional<ResourcePO> getOptionalBy(Long resourceId) {
        return resourceSpringDataJPARepository.findById(resourceId);
    }

    public Optional<ResourcePO> getOptionalBy(String resourceName) {
        ResourcePO par1=new ResourcePO();
        par1.setName(resourceName);
        Example<ResourcePO> poExample=Example.of(par1);
        Optional<ResourcePO> po=resourceSpringDataJPARepository.findOne(poExample);
        return po;
    }

    public Boolean isExistBy(ResourceName resourceName){
        return getOptionalBy(resourceName.getName()).isPresent();
    }

    public Boolean isExistBy(ResourceId resourceId){
        return getOptionalBy(resourceId.getId()).isPresent();
    }

    public Resource getBy(ResourceName resourceName) {
        ResourcePO par1=new ResourcePO();
        par1.setName(resourceName.getName());
        Example<ResourcePO> poExample=Example.of(par1);
        Optional<ResourcePO> po=resourceSpringDataJPARepository.findOne(poExample);
        return poToResource(po.get());
    }

    public void save(Resource resource) {
        ResourcePO po=resourceToPo(resource);
        resourceSpringDataJPARepository.save(po);
    }

    public Long nextResourceId() {
        return resourceSpringDataJPARepository.count()+30000000000000000L+1;
    }


    public Resource poToResource(ResourcePO po){
        ResourceId id=new ResourceId(po.getId());
        ResourceName name=new ResourceName(po.getName());
        List<ResourceAttributePO> resourceAttributePOSet=po.getResourceAttributes();
        List<ResourceAttribute> attributeSet=resourceAttributeRepositoryImpl.posToResourceAttributes(resourceAttributePOSet);
        Resource systemResource=new Resource(id,name);
        systemResource.addAttributes(attributeSet);
        return systemResource;
    }

    public ResourcePO resourceToPo(Resource resource){
        Long id=resource.getId().getId();
        String name=resource.getName().getName();
        List<ResourceAttribute> resourceAttributeSet=resource.getResourceAttributes();
        List<ResourceAttributePO> resourceAttributePOSet=resourceAttributeRepositoryImpl.resourceAttributesToPos(resourceAttributeSet);
        return new ResourcePO(id,name,resourceAttributePOSet);
    }
}
