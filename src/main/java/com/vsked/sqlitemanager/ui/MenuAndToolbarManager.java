package com.vsked.sqlitemanager.ui;

import com.vsked.sqlitemanager.domain.VDatabaseFile;
import com.vsked.sqlitemanager.domain.VTable;
import com.vsked.sqlitemanager.domain.VTableList;
import com.vsked.sqlitemanager.services.ApplicationService;
import com.vsked.sqlitemanager.services.DatabaseService;
import com.vsked.sqlitemanager.services.I18N;
import com.vsked.sqlitemanager.services.TableService;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Locale;
import static com.vsked.sqlitemanager.ui.LanguageManager.switchLanguage;

public class MenuAndToolbarManager {

    private static final Logger log = LoggerFactory.getLogger(MenuAndToolbarManager.class);

    ApplicationMainUI applicationMainUI;
    Menu englishMenu = I18N.menuForKey("menu.english");
    Menu chineseMenu = I18N.menuForKey("menu.chinese");
    Menu fileExitMenu = I18N.menuForKey("menuItem.exit");

    public MenuAndToolbarManager(ApplicationMainUI applicationMainUI) {
        this.applicationMainUI = applicationMainUI;
    }

    public MenuBar createMenuBar(Stage stage, ApplicationService applicationService, TreeItem<String> tablesItem) {
        MenuBar menuBar = new MenuBar();
        menuBar.setMinWidth(stage.getMaxWidth());

        Menu fileMenu = I18N.menuForKey("menu.file");
        Menu fileOpenMenu = I18N.menuForKey("menuItem.open");

        fileOpenMenu.setOnAction(event -> {
            if (log.isTraceEnabled()) {
                log.trace("You click the file open menu from menu Item");
            }
            log.info("{}", event);

            VDatabaseFile databaseFile = applicationService.openDataBaseFile(stage);

            ApplicationMainUI.databaseService = new DatabaseService(databaseFile);

            TableService tableService = new TableService(ApplicationMainUI.databaseService);
            VTableList vTableList = tableService.getTables();
            List<VTable> tableList = vTableList.getTables();
            for (VTable table : tableList) {
                TreeItem<String> tablesItemNode = new TreeItem<>(table.getTableName().getTableName());
                tablesItem.getChildren().add(tablesItemNode);
            }

            tablesItem.setExpanded(true);

        });


        fileExitMenu.setOnAction(event -> {
            if (log.isTraceEnabled()) {
                log.trace("You click the file exit menu from menu Item");
            }
            log.info("{}", event.toString());
            ApplicationService.exit();
        });

        Menu languageMenu = I18N.menuForKey("menu.language");


        englishMenu.setOnAction(event -> {
            if (log.isTraceEnabled()) {
                log.trace("You click the english menu from menu Item");
            }
            switchLanguage(Locale.ENGLISH);
            log.info("{}", event);
        });

        chineseMenu.setOnAction(event -> {
            if (log.isTraceEnabled()) {
                log.trace("You click the chinese menu from menu Item");
            }
            switchLanguage(Locale.CHINESE);
            log.info("{}", event);
        });

        fileMenu.getItems().add(fileOpenMenu);
        fileMenu.getItems().add(fileExitMenu);

        languageMenu.getItems().add(englishMenu);
        languageMenu.getItems().add(chineseMenu);

        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(languageMenu);

        return menuBar;
    }

    public ToolBar createToolBar(Stage stage) {
        ToolBar toolBar = new ToolBar();
        Button openBt = I18N.buttonForKey("button.open");
        Button newQueryBt = I18N.buttonForKey("button.newQuery");
        Button englishBt = I18N.buttonForKey("button.english");
        Button chineseBt = I18N.buttonForKey("button.chinese");
        Button exitBt = I18N.buttonForKey("button.exit");

        englishBt.setOnAction(englishMenu.getOnAction());
        chineseBt.setOnAction(chineseMenu.getOnAction());
        exitBt.setOnAction(fileExitMenu.getOnAction());

        toolBar.getItems().addAll(openBt, newQueryBt, englishBt, chineseBt, exitBt);
        toolBar.setMinWidth(stage.getMaxWidth());

        return toolBar;
    }

    public GridPane createTopGridPane(MenuBar menuBar, ToolBar toolBar) {
        GridPane topGridPane = new GridPane();
        topGridPane.add(menuBar, 0, 0);
        topGridPane.add(toolBar, 0, 1);
        return topGridPane;
    }
}

