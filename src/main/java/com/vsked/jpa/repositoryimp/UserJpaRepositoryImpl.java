//package com.vsked.jpa.repositoryimp;
//
//import com.vsked.jpa.po.RolePo;
//import com.vsked.jpa.po.UserPo;
//import com.vsked.jpa.repository.UserRepositoryJPA;
//import com.vsked.system.domain.Role;
//import com.vsked.system.domain.User;
//import com.vsked.system.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//import java.util.List;
//
//@Repository
//public class UserJpaRepositoryImpl implements UserRepository {
//
//    @Autowired
//    UserRepositoryJPA userRepositoryJPA;
//    @Autowired
//    RoleJpaRepositoryImpl roleJpaRepositoryImpl;
//
//    public User save(User user) {
//        UserPo po=userToPo(user);
//        UserPo savePo=userRepositoryJPA.save(po);
//        return poToUser(savePo);
//    }
//
//
//    public User findByUserName(String name) {
//        UserPo po=userRepositoryJPA.findByName(name).orElse(null);
//        return poToUser(po);
//    }
//
//    public User findByCertificateId(Long certificateId) {
//        UserPo po=userRepositoryJPA.findByCertificateId(certificateId).orElse(null);
//        return poToUser(po);
//    }
//
//    public User poToUser(UserPo po){
//        if(po==null){
//            return null;
//        }
//        Long id=po.getId();
//        String userName=po.getName();
//        String password=po.getPassword();
//        boolean accountNonExpired=po.isAccountNonExpired();
//        boolean accountNonLock=po.isAccountNonLock();
//        boolean credentialsNonExpired=po.isCredentialsNonExpired();
//        boolean enable=po.isEnable();
//        List<Role> roles=roleJpaRepositoryImpl.posToRoles(po.getRoles());
//        return new User(id,userName,password,accountNonExpired,accountNonLock,credentialsNonExpired,enable,roles);
//    }
//
//    public UserPo userToPo(User user){
//        if(user==null){
//            return null;
//        }
//
//        UserPo po=new UserPo();
//        po.setId(user.getId().getId());
//        po.setName(user.getName().getName());
//        List<RolePo> roles=roleJpaRepositoryImpl.rolesToPos(user.getRoles());
//        return po;
//    }
//}
