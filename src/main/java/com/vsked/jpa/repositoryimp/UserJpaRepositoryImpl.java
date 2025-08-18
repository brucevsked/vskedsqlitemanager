package com.vsked.jpa.repositoryimp;


import com.vsked.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class UserJpaRepositoryImpl implements UserRepository {

    @Autowired
    IUserJpaRepository iUserJpaRepository;
    @Autowired
    RoleJpaRepositoryImpl roleJpaRepositoryImpl;
    @Autowired
    CertificateJpaRepositoryImpl certificateJpaRepositoryImpl;

    @Override
    public User save(User user) {
        UserPo po=userToPo(user);
        UserPo savePo=iUserJpaRepository.save(po);
        return poToUser(savePo);
    }

    @Override
    public User findByUserName(String name) {
        UserPo po=iUserJpaRepository.findByName(name).orElse(null);
        return poToUser(po);
    }

    @Override
    public User findByCertificateId(Long certificateId) {
        UserPo po=iUserJpaRepository.findByCertificateId(certificateId).orElse(null);
        return poToUser(po);
    }

    public User poToUser(UserPo po){
        if(po==null){
            return null;
        }
        Long id=po.getId();
        String userName=po.getName();
        String password=po.getPassword();
        boolean accountNonExpired=po.isAccountNonExpired();
        boolean accountNonLock=po.isAccountNonLock();
        boolean credentialsNonExpired=po.isCredentialsNonExpired();
        boolean enable=po.isEnable();
        List<Role> roles=roleJpaRepositoryImpl.posToRoles(po.getRoles());
        Certificate certificate=certificateJpaRepositoryImpl.poToCertificate(po.getCertificate());
        return new User(id,userName,password,accountNonExpired,accountNonLock,credentialsNonExpired,enable,roles,certificate);
    }

    public UserPo userToPo(User user){
        if(user==null){
            return null;
        }

        UserPo po=new UserPo();
        po.setId(user.getId());
        po.setName(user.getUsername());
        po.setPassword(user.getPassword());
        po.setAccountNonExpired(user.isAccountNonExpired());
        po.setAccountNonLock(user.isAccountNonLocked());
        po.setCredentialsNonExpired(user.isCredentialsNonExpired());
        po.setEnable(user.isEnabled());
        List<RolePo> roles=roleJpaRepositoryImpl.rolesToPos(user.getRoles());
        CertificatePo certificate=certificateJpaRepositoryImpl.certificateToPo(user.getCertificate());
        po.setRoles(roles);
        po.setCertificate(certificate);
        return po;
    }
}
