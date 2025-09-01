package com.vsked.system.repository;

import com.vsked.system.domain.Navigation;
import com.vsked.system.domain.NavigationId;
import com.vsked.system.domain.NavigationName;
import com.vsked.system.domain.Page;
import java.util.List;

public interface NavigationRepository {

    void save(Navigation navigation);
    List<Navigation> findBy(List<NavigationId> navigationIds);
    List<Navigation> findAll();
    List<Navigation> findAllByIdNot(NavigationId navigationId);
    Boolean isExistBy(NavigationId navigationId);
    Boolean isExistBy(Navigation parentNavigation, NavigationId navigationId, NavigationName navigationName);

    Navigation getBy(NavigationId navigationId);
    Navigation getBy(Navigation parentNavigation,NavigationId navigationId,NavigationName navigationName);
    Long nextId();

    Page<Navigation> findAll(Page<Navigation> page);

}
