package com.vsked.jpa.repositoryimp;

import com.vsked.jpa.po.MenuPo;
import com.vsked.jpa.repository.MenuRepositoryJPA;
import com.vsked.system.domain.Navigation;
import com.vsked.system.repository.NavigationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.LinkedList;
import java.util.List;

@Repository
public class MenuJpaRepositoryImpl implements NavigationRepository {

    @Autowired
    MenuRepositoryJPA menuRepositoryJPA;

    public Navigation poToMenu(MenuPo po){
        if(po==null){
            return null;
        }
        Long id=po.getId();
        String name=po.getName();
        String url=po.getUrl();
        Navigation parent=poToMenu(po.getParent());
        Integer type=po.getType();
        return new Navigation(parent,name,url);
    }

    public MenuPo menuToPo(Navigation menu){
        if(menu==null){
            return null;
        }
        MenuPo po=new MenuPo();
        po.setId(menu.getId().getId());
        po.setName(menu.getName().getName());
        po.setUrl(menu.getAddress().getAddress());
        po.setParent(menuToPo(menu.getParent()));
//        po.setType(menu.get);
        return po;
    }

    public List<Navigation> posToMenus(List<MenuPo> pos){
        List<Navigation> datas=new LinkedList<>();
        for(MenuPo po:pos){
            datas.add(poToMenu(po));
        }
        return datas;
    }

    public List<MenuPo> menusToPos(List<Navigation> menus){
        List<MenuPo> datas=new LinkedList<>();
        for(Navigation menu:menus){
            datas.add(menuToPo(menu));
        }
        return datas;
    }
}
