package com.vsked.jpa.repositoryjpaimp;

import com.vsked.jpa.repository.ResourceAttributeRepositoryJPA;
import com.vsked.system.domain.DataType;
import com.vsked.system.domain.ResourceAttribute;
import com.vsked.system.domain.ResourceAttributeId;
import com.vsked.system.domain.ResourceAttributeName;
import com.vsked.system.repository.ResourceAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class ResourceAttributeRepositoryImpl implements ResourceAttributeRepository {

    @Autowired
    ResourceAttributeRepositoryJPA resourceAttributeSpringDataJPARepository;

    public ResourceAttribute getBy(ResourceAttributeId resourceAttributeId){
        ResourceAttributePO po=getOptionalBy(resourceAttributeId.getId()).get();
        return poToResourceAttribute(po);
    }

    public Boolean getIsExistBy(ResourceAttributeId resourceAttributeId) {
        return getOptionalBy(resourceAttributeId.getId()).isPresent();
    }

    public Optional<ResourceAttributePO> getOptionalBy(Long resourceAttributeId) {
        return resourceAttributeSpringDataJPARepository.findById(resourceAttributeId);
    }

    public void save(ResourceAttribute resourceAttribute) {
        ResourceAttributePO po=resourceAttributeToPo(resourceAttribute);
        resourceAttributeSpringDataJPARepository.save(po);
    }

    public Long nextResourceAttributeId() {
        return resourceAttributeSpringDataJPARepository.count()+10000000000000000L+1;
    }

    public ResourceAttribute poToResourceAttribute(ResourceAttributePO po){
        ResourceAttributeId id=new ResourceAttributeId(po.getId());
        ResourceAttributeName name=new ResourceAttributeName(po.getName());
        DataType dataType=po.getDataType();
        return new ResourceAttribute(id,name,dataType);
    }

    public ResourceAttributePO resourceAttributeToPo(ResourceAttribute resourceAttribute){
        Long id=resourceAttribute.getId().getId();
        String name=resourceAttribute.getName().getName();
        return new ResourceAttributePO(id,name);
    }

    public List<ResourceAttribute> posToResourceAttributes(List<ResourceAttributePO> pos){
        List<ResourceAttribute> ras=new LinkedList<>();
        for(ResourceAttributePO po:pos){
            ras.add(poToResourceAttribute(po));
        }
        return ras;
    }

    public List<ResourceAttributePO> resourceAttributesToPos(List<ResourceAttribute> ras){
        List<ResourceAttributePO> pos=new LinkedList<>();
        for(ResourceAttribute attribute:ras){
            pos.add(resourceAttributeToPo(attribute));
        }
        return pos;
    }
}
