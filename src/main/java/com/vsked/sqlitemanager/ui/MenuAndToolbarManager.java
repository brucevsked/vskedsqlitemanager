package com.vsked.sqlitemanager.ui;

import com.vsked.sqlitemanager.domain.VDatabaseFile;
import com.vsked.sqlitemanager.domain.VTable;
import com.vsked.sqlitemanager.domain.VTableList;
import com.vsked.sqlitemanager.services.ApplicationService;
import com.vsked.sqlitemanager.services.DatabaseService;
import com.vsked.sqlitemanager.services.I18N;
import com.vsked.sqlitemanager.services.TableService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import static com.vsked.sqlitemanager.ui.LanguageManager.switchLanguage;

public class MenuAndToolbarManager {

    private static final Logger log = LoggerFactory.getLogger(MenuAndToolbarManager.class);

    ApplicationMainUI applicationMainUI;
    Menu englishMenu = I18N.menuForKey("menu.english");
    Menu chineseMenu = I18N.menuForKey("menu.chinese");
    Menu fileExitMenu = I18N.menuForKey("menuItem.exit");
    Menu fileOpenMenu = I18N.menuForKey("menuItem.open");

    public MenuAndToolbarManager(ApplicationMainUI applicationMainUI) {
        this.applicationMainUI = applicationMainUI;
    }

    public MenuBar createMenuBar(Stage stage, ApplicationService applicationService, TreeItem<String> tablesItem) {
        MenuBar menuBar = new MenuBar();
        menuBar.setMinWidth(stage.getMaxWidth());

        Menu fileMenu = I18N.menuForKey("menu.file");


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



        return menuBar;
    }

    public ToolBar createToolBar(Stage stage,TabPane centerTabPane) {
        ToolBar toolBar = new ToolBar();
        Button openBt = I18N.buttonForKey("button.open");
        Button newQueryBt = I18N.buttonForKey("button.newQuery");
        Button englishBt = I18N.buttonForKey("button.english");
        Button chineseBt = I18N.buttonForKey("button.chinese");
        Button exitBt = I18N.buttonForKey("button.exit");

        openBt.setOnAction(fileOpenMenu.getOnAction());

        newQueryBt.setOnAction(actionEvent -> {
            if (log.isTraceEnabled()) {
                log.trace("You click the new query button from toolbar");
            }

            log.info("{}", actionEvent);

            if (ApplicationMainUI.databaseService == null) {
                Alert alert = I18N.alertForKey(Alert.AlertType.INFORMATION, "alert.notExitOpenDatabaseFile.title", "alert.notExitOpenDatabaseFile.headerText", "alert.notExitOpenDatabaseFile.contentText");
                alert.show();
                return;
            }

            int queryCount = ApplicationMainUI.getGlobalQueryTabCount();
            String queryTabId = I18N.get("tab.query") + queryCount;
            Tab tab = new Tab(queryTabId);
            tab.setId("Query" + queryCount);
            tab.setText(queryTabId);
            GridPane queryTabGridPane = new GridPane();
            Button runQueryButton = I18N.buttonForKey("button.runQuery");
            Button stopQueryButton = I18N.buttonForKey("button.stopQuery");
            queryTabGridPane.add(runQueryButton, 0, 0);
            queryTabGridPane.add(stopQueryButton, 1, 0);

            ApplicationMainUI.currentQueryGridPane=queryTabGridPane;

            runQueryButton.setOnAction(actionEvent6 -> {
                if (log.isTraceEnabled()) {
                    log.trace("You click run query button from query {}", queryCount);
                }
                try {
                    TableView<Map<String, String>> resultTable = new TableView<>(); // 查询结果显示表格
                    Label resultLabel = new Label(); // 更新等操作后的提示文本
                    String sql = ApplicationMainUI.currentQueryTextArea.getText().trim();
                    TableService tableService = new TableService(ApplicationMainUI.databaseService);

                    GridPane queryGridPanelTmp = ApplicationMainUI.currentQueryGridPane;

                    // 清除旧内容（包括表格和标签）
                    queryGridPanelTmp.getChildren().removeIf(node -> node instanceof TableView || node instanceof Label);

                    if (sql.toLowerCase().startsWith("select")) {
                        // 执行查询
                        List<Map<String, String>> results = tableService.executeQuery(sql);
                        ObservableList<Map<String, String>> data = FXCollections.observableArrayList(results);

                        // 动态生成列
                        if (!results.isEmpty()) {
                            for (String key : results.get(0).keySet()) {
                                TableColumn<Map<String, String>, String> column = new TableColumn<>(key);
                                final String currentKey = key;
                                column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(currentKey)));
                                resultTable.getColumns().add(column);
                            }
                        }

                        resultTable.setItems(data);
                        queryGridPanelTmp.add(resultTable, 0, 2, 3, 1); // 显示表格
                    } else {
                        // 执行更新类语句（UPDATE / INSERT / DELETE）
                        int rowsAffected = tableService.executeUpdate(sql);
                        resultLabel.setText(I18N.get("queryPanel.textarea.tips.updateSuccess") + rowsAffected);
                        resultLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                        queryGridPanelTmp.add(resultLabel, 0, 2); // 显示更新成功信息
                    }

                } catch (SQLException e) {
                    log.error("Query execution failed", e);
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR, I18N.get("alert.sql.error") + e.getMessage());
                        alert.show();
                    });
                } catch (Exception e) {
                    log.error("Unexpected error", e);
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR, I18N.get("alert.unknow.error") + e.getMessage());
                        alert.show();
                    });
                }
                log.info("{}", actionEvent6);
            });

            stopQueryButton.setOnAction(actionEvent5 -> {
                if (log.isTraceEnabled()) {
                    log.trace("You click stop query button from query grid");
                }

                log.info("{}", actionEvent5);

            });

            TextArea ta = new TextArea();
            ta.setId("ta" + queryCount);
            ta.setFocusTraversable(true); //set can get focus
            // create a menu
            ContextMenu contextMenu = new ContextMenu();

            // create menuitems
            MenuItem runCurrentSelectedSqlMenuItem = I18N.menuItemForKey("queryPanel.textarea.contextMenu.runCurrentSelectedSql");
            MenuItem cutMenuItem = I18N.menuItemForKey("queryPanel.textarea.contextMenu.cut");
            MenuItem copyMenuItem = I18N.menuItemForKey("queryPanel.textarea.contextMenu.copy");
            MenuItem pasteMenuItem = I18N.menuItemForKey("queryPanel.textarea.contextMenu.paste");
            MenuItem selectAllMenuItem = I18N.menuItemForKey("queryPanel.textarea.contextMenu.selectAll");

            cutMenuItem.setOnAction(actionEvent4 -> {

                if (log.isTraceEnabled()) {
                    log.trace("you click cut menu from contextMenu");
                }

                log.info("{}", actionEvent4);

                TextArea textArea = ApplicationMainUI.currentQueryTextArea;
                textArea.cut();
            });

            copyMenuItem.setOnAction(actionEvent3 -> {
                if (log.isTraceEnabled()) {
                    log.trace("you click copy menu from contextMenu");
                }

                log.info("{}", actionEvent3);

                TextArea textArea = ApplicationMainUI.currentQueryTextArea;

                if (!textArea.getSelectedText().isEmpty()) {
                    ClipboardContent content = new ClipboardContent();
                    content.putString(textArea.getSelectedText());
                    Clipboard clipboard = Clipboard.getSystemClipboard();
                    clipboard.setContent(content);
                } else {
                    log.trace("you click copy menu from contextMenu.No text selected to copy.");
                }

            });

            pasteMenuItem.setOnAction(actionEvent2 -> {
                if (log.isTraceEnabled()) {
                    log.trace("you click paste menu from contextMenu");
                }

                log.info("{}", actionEvent2);

                TextArea textArea = ApplicationMainUI.currentQueryTextArea;
                textArea.paste();
            });

            selectAllMenuItem.setOnAction(actionEvent1 -> {
                if (log.isTraceEnabled()) {
                    log.trace("you click select all menu from contextMenu");
                }

                if (log.isDebugEnabled()) {
                    log.debug("{}", actionEvent1.getSource());
                }
                TextArea textArea = ApplicationMainUI.currentQueryTextArea;
                textArea.selectAll();

            });

            // add menu items to menu
            contextMenu.getItems().add(selectAllMenuItem);
            contextMenu.getItems().add(runCurrentSelectedSqlMenuItem);
            contextMenu.getItems().add(cutMenuItem);
            contextMenu.getItems().add(copyMenuItem);
            contextMenu.getItems().add(pasteMenuItem);

            ta.setContextMenu(contextMenu);

            queryTabGridPane.add(ta, 0, 1);

            tab.setContent(queryTabGridPane);
            centerTabPane.getTabs().add(tab);
            centerTabPane.getSelectionModel().select(tab);
            ta.requestFocus();
            ApplicationMainUI.currentQueryTextArea = ta;

        });

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

