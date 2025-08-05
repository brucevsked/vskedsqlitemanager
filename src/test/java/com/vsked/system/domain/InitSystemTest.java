package com.vsked.system.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

public class InitSystemTest {

    private static final Logger log = LoggerFactory.getLogger(InitSystemTest.class);

    Long permissionId=40000000000000001L;
    Long roleId=50000000000000001L;
    Long userId=70000000000000001L;
    @Test
    public void init() {
        log.info("init system");
    }


    public void initResourceAndAttribute(){
        List<String> resourceList=new LinkedList<>();
        resourceList.add("com.vsked.system.domain.ResourceAttribute,资源属性");
        resourceList.add("com.vsked.system.domain.SystemResource,资源");
        resourceList.add("com.vsked.system.domain.Permission,权限");
        resourceList.add("com.vsked.system.domain.Role,角色");
        resourceList.add("com.vsked.system.domain.Certificate,证书");
        resourceList.add("com.vsked.system.domain.User,用户");
        resourceList.add("com.vsked.system.domain.Navigation,导航");
        resourceList.add("com.vsked.system.domain.Record,记录");

        List<List<String>> resourceAttributeList=new LinkedList<>();

        List<String> resourceList1=new LinkedList<>();
        resourceList1.add("id,资源属性编号");
        resourceList1.add("name,资源属性名称");
        resourceAttributeList.add(resourceList1);

        List<String> resourceList2=new LinkedList<>();
        resourceList2.add("id,资源编号");
        resourceList2.add("name,资源名称");
        resourceList2.add("resourceAttributes,资源属性集合");
        resourceAttributeList.add(resourceList2);

        List<String> resourceList3=new LinkedList<>();
        resourceList3.add("id,权限编号");
        resourceList3.add("type,权限类型");
        resourceList3.add("name,权限名称");
        resourceList3.add("systemResource,资源类权限");
        resourceList3.add("attribute,资源属性类权限");
        resourceList3.add("record,资源记录类权限");
        resourceAttributeList.add(resourceList3);

        List<String> resourceList4=new LinkedList<>();
        resourceList4.add("id,角色编号");
        resourceList4.add("name,角色名称");
        resourceList4.add("permissions,角色权限集合");
        resourceAttributeList.add(resourceList4);

        List<String> resourceList5=new LinkedList<>();
        resourceList5.add("id,证书编号");
        resourceList5.add("expireTime,证书过期时间");
        resourceAttributeList.add(resourceList5);

        List<String> resourceList6=new LinkedList<>();
        resourceList6.add("id,用户编号");
        resourceList6.add("name,用户名");
        resourceList6.add("loginState,登录状态");
        resourceList6.add("account,用户账户");
        resourceList6.add("certificates,用户证书集合");
        resourceList6.add("roles,用户角色集合");
        resourceAttributeList.add(resourceList6);

        List<String> resourceList7=new LinkedList<>();
        resourceList7.add("id,记录编号");
        resourceList7.add("type,记录类型");
        resourceAttributeList.add(resourceList7);

        List<String> resourceList8=new LinkedList<>();
        resourceList8.add("id,导航编号");
        resourceList8.add("name,导航名称");
        resourceList8.add("level,导航等级");
        resourceList8.add("address,导航地址");
        resourceList8.add("parent,父级导航");
        resourceAttributeList.add(resourceList8);

        Long resourceAttributeId=10000000000000001L;
        Long resourceId=30000000000000001L;

        String resourceStr="";
        String[] resourceStrArray=null;
        String resourceAttributeStr="";
        String [] resourceAttributeStrArray=null;

        for(int i=0;i<resourceList.size();i++){
            log.debug("{},{}",resourceId,resourceList.get(i));
            resourceStr=resourceList.get(i);
            resourceStrArray=resourceStr.split(",");
            ResourceId resourceId1=new ResourceId(resourceId);
            ResourceName resourceName1=new ResourceName(resourceStrArray[0]);
            Resource resource1=new Resource(resourceId1,resourceName1,null,null,null);


            PermissionId permissionId3=new PermissionId(permissionId);
            PermissionType permissionType3=new PermissionType(PermissionType.typeToNumber(PermissionTypeEnum.object));
            PermissionName permissionName3=new PermissionName(resourceStrArray[1]);
            Permission permission3=new Permission(permissionId3,permissionName3,PermissionContent.WRITE,permissionType3,resource1,null,null);

            //permissionManager.save(permission3);
            permissionId=permissionId+1;

            List<String> attributeListTemp=resourceAttributeList.get(i);
            for(int attributeId=0;attributeId<attributeListTemp.size();attributeId++){
                log.debug("{},{}",resourceAttributeId,attributeListTemp.get(attributeId));
                resourceAttributeStr=attributeListTemp.get(attributeId);
                resourceAttributeStrArray=resourceAttributeStr.split(",");
                ResourceAttributeId resourceAttributeId1=new ResourceAttributeId(resourceAttributeId);
                ResourceAttributeName resourceAttributeName1=new ResourceAttributeName(resourceAttributeStrArray[0]);
                ResourceAttribute resourceAttribute1=new ResourceAttribute(resourceAttributeId1,resourceAttributeName1,DataType.OBJECT);

                PermissionId permissionId1=new PermissionId(permissionId);
                PermissionType permissionType1=new PermissionType(PermissionType.typeToNumber(PermissionTypeEnum.attribute));
                PermissionName permissionName1=new PermissionName(resourceAttributeStrArray[1]);
                Permission permission1=new Permission(permissionId1,permissionName1,PermissionContent.WRITE,permissionType1,null,resourceAttribute1,null);

                //permissionManager.save(permission1);

                resource1.addAttribute(resourceAttribute1);

                resourceAttributeId=resourceAttributeId+1;
                permissionId=permissionId+1;
            }


            //resourceManager.save(resource1);
            resourceId=resourceId+1;
        }

    }

}
