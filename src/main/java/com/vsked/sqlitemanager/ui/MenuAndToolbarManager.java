package com.vsked.sqlitemanager.ui;

import com.vsked.sqlitemanager.services.I18N;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Locale;

public class MenuAndToolbarManager {

    private ApplicationMainUI applicationMainUI;

    public MenuAndToolbarManager(ApplicationMainUI applicationMainUI) {
        this.applicationMainUI = applicationMainUI;
    }

    public MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setMinWidth(applicationMainUI.getPrimaryStage().getMaxWidth());

        Menu fileMenu = I18N.menuForKey("menu.file");
        Menu fileOpenMenu = I18N.menuForKey("menuItem.open");
        Menu fileExitMenu = I18N.menuForKey("menuItem.exit");

        fileExitMenu.setOnAction(event -> {
            if (applicationMainUI.getLog().isTraceEnabled()) {
                applicationMainUI.getLog().trace("You click the file exit menu from menu Item");
            }
            applicationMainUI.getLog().info("{}", event.toString());
            applicationMainUI.getApplicationService().exit();
        });

        Menu languageMenu = I18N.menuForKey("menu.language");
        Menu englishMenu = I18N.menuForKey("menu.english");
        Menu chineseMenu = I18N.menuForKey("menu.chinese");

        englishMenu.setOnAction(event -> {
            if (applicationMainUI.getLog().isTraceEnabled()) {
                applicationMainUI.getLog().trace("You click the english menu from menu Item");
            }
            applicationMainUI.switchLanguage(Locale.ENGLISH);
            applicationMainUI.getLog().info("{}", event);
        });

        chineseMenu.setOnAction(event -> {
            if (applicationMainUI.getLog().isTraceEnabled()) {
                applicationMainUI.getLog().trace("You click the chinese menu from menu Item");
            }
            applicationMainUI.switchLanguage(Locale.CHINESE);
            applicationMainUI.getLog().info("{}", event);
        });

        fileMenu.getItems().add(fileOpenMenu);
        fileMenu.getItems().add(fileExitMenu);

        languageMenu.getItems().add(englishMenu);
        languageMenu.getItems().add(chineseMenu);

        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(languageMenu);

        return menuBar;
    }

    public ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar();
        Button openBt = I18N.buttonForKey("button.open");
        Button newQueryBt = I18N.buttonForKey("button.newQuery");
        Button englishBt = I18N.buttonForKey("button.english");
        Button chineseBt = I18N.buttonForKey("button.chinese");
        Button exitBt = I18N.buttonForKey("button.exit");

        englishBt.setOnAction(applicationMainUI.getEnglishMenu().getOnAction());
        chineseBt.setOnAction(applicationMainUI.getChineseMenu().getOnAction());
        exitBt.setOnAction(applicationMainUI.getFileExitMenu().getOnAction());

        toolBar.getItems().addAll(openBt, newQueryBt, englishBt, chineseBt, exitBt);
        toolBar.setMinWidth(applicationMainUI.getPrimaryStage().getMaxWidth());

        return toolBar;
    }

    public GridPane createTopGridPane(MenuBar menuBar, ToolBar toolBar) {
        GridPane topGridPane = new GridPane();
        topGridPane.add(menuBar, 0, 0);
        topGridPane.add(toolBar, 0, 1);
        return topGridPane;
    }
}

