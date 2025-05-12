package com.vsked.sqlitemanager.ui;

import com.vsked.sqlitemanager.domain.SqliteDataType;
import com.vsked.sqlitemanager.domain.VTableColumnId;
import com.vsked.sqlitemanager.domain.VTableColumnName;
import com.vsked.sqlitemanager.domain.VTableColumnNotNull;
import com.vsked.sqlitemanager.domain.VTableColumnPk;
import com.vsked.sqlitemanager.domain.VTableDfltValue;
import com.vsked.sqlitemanager.domain.VTableTableColumnDataType;
import com.vsked.sqlitemanager.services.I18N;
import com.vsked.sqlitemanager.domain.VPage;
import com.vsked.sqlitemanager.domain.VDatabaseFile;
import com.vsked.sqlitemanager.domain.VPageIndex;
import com.vsked.sqlitemanager.domain.VPageSize;
import com.vsked.sqlitemanager.domain.VTable;
import com.vsked.sqlitemanager.domain.VTableColumn;
import com.vsked.sqlitemanager.viewmodel.TableColumnView;
import com.vsked.sqlitemanager.domain.VTableList;
import com.vsked.sqlitemanager.domain.VTableName;
import com.vsked.sqlitemanager.services.ApplicationService;
import com.vsked.sqlitemanager.services.DatabaseService;
import com.vsked.sqlitemanager.services.TableService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;
import java.util.Map;



public class ApplicationMainUI extends Application {

    private static final Logger log = LoggerFactory.getLogger(ApplicationMainUI.class);

    private Scene scene;
    private int globalQueryTabCount = 0;
    private TextArea currentQueryTextArea;


    private ApplicationService applicationService = new ApplicationService();
    private DatabaseService databaseService;

    public DatabaseService getDatabaseService() {
        return databaseService;
    }

    public void setDatabaseService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public int getGlobalQueryTabCount() {
        this.globalQueryTabCount = this.globalQueryTabCount + 1;
        return globalQueryTabCount;
    }

    public TextArea getCurrentQueryTextArea() {
        return currentQueryTextArea;
    }

    public void setCurrentQueryTextArea(TextArea currentQueryTextArea) {
        this.currentQueryTextArea = currentQueryTextArea;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public static void main(String[] args) {
        if (log.isTraceEnabled()) {
            log.trace("welcome to vsked SQLite manager");
        }
        launch(args);

    }


    @Override
    public void start(Stage stage){

        // create content
        BorderPane borderPane = new BorderPane();

        MenuBar menuBar = new MenuBar();
        menuBar.setMinWidth(stage.getMaxWidth());

        Menu fileMenu = I18N.menuForKey("menu.file");
        Menu fileOpenMenu = I18N.menuForKey("menuItem.open");
        Menu fileExitMenu = I18N.menuForKey("menuItem.exit");


        fileExitMenu.setOnAction(event -> {
            if (log.isTraceEnabled()) {
                log.trace("You click the file exit menu from menu Item");
            }
            log.info("{}",event.toString());
            applicationService.exit();
        });

        Menu languageMenu = I18N.menuForKey("menu.language");
        Menu englishMenu = I18N.menuForKey("menu.english");
        Menu chineseMenu = I18N.menuForKey("menu.chinese");

        englishMenu.setOnAction(event -> {
            if (log.isTraceEnabled()) {
                log.trace("You click the english menu from menu Item");
            }
            switchLanguage(Locale.ENGLISH);
            log.info("{}",event);
        });

        chineseMenu.setOnAction(event -> {
            if (log.isTraceEnabled()) {
                log.trace("You click the chinese menu from menu Item");
            }
            switchLanguage(Locale.CHINESE);
            log.info("{}",event);
        });

        fileMenu.getItems().add(fileOpenMenu);
        fileMenu.getItems().add(fileExitMenu);

        languageMenu.getItems().add(englishMenu);
        languageMenu.getItems().add(chineseMenu);

        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(languageMenu);

        ToolBar toolBar = new ToolBar();
        Button openBt = I18N.buttonForKey("button.open");
        Button newQueryBt = I18N.buttonForKey("button.newQuery");
        Button englishBt = I18N.buttonForKey("button.english");
        Button chineseBt = I18N.buttonForKey("button.chinese");
        Button exitBt = I18N.buttonForKey("button.exit");

        englishBt.setOnAction(englishMenu.getOnAction());

        chineseBt.setOnAction(chineseMenu.getOnAction());

        exitBt.setOnAction(fileExitMenu.getOnAction());

        toolBar.getItems().add(openBt);
        toolBar.getItems().add(newQueryBt);
        toolBar.getItems().add(englishBt);
        toolBar.getItems().add(chineseBt);
        toolBar.getItems().add(exitBt);

        toolBar.setMinWidth(stage.getMaxWidth());

        GridPane topGridPane = new GridPane();
        topGridPane.add(menuBar, 0, 0);
        topGridPane.add(toolBar, 0, 1);

        borderPane.setTop(topGridPane);

        GridPane leftGridPane = new GridPane();
        TreeView<String> systemViewTree = new TreeView<>();
        TreeItem<String> rootItem = I18N.treeItemForKey("tree.system");

        TreeItem<String> tablesItem = I18N.treeItemForKey("tree.tables");
        TreeItem<String> viewsItem = I18N.treeItemForKey("tree.views");
        TreeItem<String> indexesItem = I18N.treeItemForKey("tree.indexes");
        TreeItem<String> triggersItem = I18N.treeItemForKey("tree.triggers");
        TreeItem<String> queriesItem = I18N.treeItemForKey("tree.queries");
        TreeItem<String> backupItem = I18N.treeItemForKey("tree.backup");

        rootItem.getChildren().add(tablesItem);
        rootItem.getChildren().add(viewsItem);
        rootItem.getChildren().add(indexesItem);
        rootItem.getChildren().add(triggersItem);
        rootItem.getChildren().add(queriesItem);
        rootItem.getChildren().add(backupItem);


        GridPane centerGridPane = new GridPane();
        centerGridPane.setMinWidth(stage.getMaxWidth() - leftGridPane.getMaxWidth());
        centerGridPane.setMinHeight(leftGridPane.getMinHeight());


        TabPane centerTabPane = new TabPane();

        //TODO when change query set current text area component

        newQueryBt.setOnAction(actionEvent -> {
            if (log.isTraceEnabled()) {
                log.trace("You click the new query button from toolbar");
            }

            log.info("{}",actionEvent);

            if (getDatabaseService() == null) {
                Alert alert = I18N.alertForKey(Alert.AlertType.INFORMATION, "alert.notExitOpenDatabaseFile.title", "alert.notExitOpenDatabaseFile.headerText", "alert.notExitOpenDatabaseFile.contentText");
                alert.show();
                return;
            }

            int queryCount = getGlobalQueryTabCount();
            String queryTabId = I18N.get("tab.query") + queryCount;
            Tab tab = new Tab(queryTabId);
            tab.setId("Query" + queryCount);
            tab.setText(queryTabId);
            GridPane queryTabGridPane = new GridPane();
            Button runQueryButton = I18N.buttonForKey("button.runQuery");
            Button stopQueryButton = I18N.buttonForKey("button.stopQuery");
            queryTabGridPane.add(runQueryButton, 0, 0);
            queryTabGridPane.add(stopQueryButton, 1, 0);


            runQueryButton.setOnAction(actionEvent6 -> {
                if (log.isTraceEnabled()) {
                    log.trace("You click run query button from query {}" ,queryCount);
                }
                new Thread(() -> {
                    try {
                        String sql = getCurrentQueryTextArea().getText();
                        TableService tableService = new TableService(getDatabaseService());
                        // 执行查询并更新 UI
                        Platform.runLater(() -> {
                            // 更新查询结果
                        });
                    } catch (Exception e) {
                        log.error("Query execution failed", e);
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Query failed: " + e.getMessage());
                            alert.show();
                        });
                    }
                }).start();
                log.info("{}",actionEvent6);

            });

            stopQueryButton.setOnAction(actionEvent5 -> {
                if (log.isTraceEnabled()) {
                    log.trace("You click stop query button from query grid");
                }

                log.info("{}",actionEvent5);

            });

            TextArea ta = new TextArea();
            ta.setId("ta"+queryCount);
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

                log.info("{}",actionEvent4);

                TextArea textArea = getCurrentQueryTextArea();
                textArea.cut();
            });

            copyMenuItem.setOnAction(actionEvent3 -> {
                if (log.isTraceEnabled()) {
                    log.trace("you click copy menu from contextMenu");
                }

                log.info("{}",actionEvent3);

                TextArea textArea = getCurrentQueryTextArea();

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

                log.info("{}",actionEvent2);

                TextArea textArea = getCurrentQueryTextArea();
                textArea.paste();
            });

            selectAllMenuItem.setOnAction(actionEvent1 -> {
                if (log.isTraceEnabled()) {
                    log.trace("you click select all menu from contextMenu");
                }

                if (log.isDebugEnabled()) {
                    log.debug("{}", actionEvent1.getSource());
                }
                TextArea textArea=getCurrentQueryTextArea();
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
            setCurrentQueryTextArea(ta);
        });

        centerTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {

            log.trace("you have change panel ---------------");
            log.debug("current tab is:{}", newTab.getText());
            log.info("{}",oldTab);
            log.info("{}",observable);

            String newPanelId = newTab.getId();

            if (newPanelId.contains("Query")) {
                Node tabNode = newTab.getContent();
                GridPane queryGridPanel = (GridPane) tabNode;
				TextArea tempTextArea= (TextArea) queryGridPanel.lookup("#ta"+newPanelId.replace("Query",""));
				setCurrentQueryTextArea(tempTextArea);
				tempTextArea.requestFocus();
				log.debug("{}",tempTextArea.getText());
            }else{
                List<TreeItem<String>> tableItems = tablesItem.getChildren();
                for (TreeItem<String> item : tableItems) {
                    if (item.getValue().equals(newTab.getText())) {
                        systemViewTree.getSelectionModel().select(item);
                    }
                }
            }
        });

        centerGridPane.add(centerTabPane, 0, 0);

        centerGridPane.setStyle("-fx-background-color: #0000ff");
        centerGridPane.setVisible(true);
        borderPane.setCenter(centerGridPane);
        borderPane.setVisible(true);


        systemViewTree.getSelectionModel().selectedItemProperty().addListener((ChangeListener<TreeItem>) (paramObservableValue, paramT1, selectedItem) -> {
            if (log.isTraceEnabled()) {
                log.trace("You click the tree Item from system tree view");
            }

            if (log.isDebugEnabled()) {
                log.debug("{}",selectedItem);
                log.debug("{},{}",paramObservableValue,paramT1);
            }

            List<Tab> oldTabList = centerTabPane.getTabs();
            boolean isExistTab = false;

            for (Tab tab : oldTabList) {
                if (log.isDebugEnabled()) {
                    log.debug("{},{}",tab.getId(),selectedItem.getValue());
                }

                if (tab.getId().equals(selectedItem.getValue())) {
                    isExistTab = true;
                    centerTabPane.getSelectionModel().select(tab);
                    break;
                }
            }
            //when you click table sub node
            if (selectedItem.getParent().getValue().equals(tablesItem.getValue())) {
                //when not exist table tab
                if (!isExistTab) {

                    Tab tab = new Tab(selectedItem.getValue().toString());
                    GridPane tableGridPane = new GridPane();

                    VTableName currentTableName = new VTableName(selectedItem.getValue().toString());

                    VPage page = new VPage(new VPageIndex(0), new VPageSize(10));

                    Pagination pagination = new Pagination();

                    TableService tableService = new TableService(getDatabaseService());
                    int pageCount = tableService.getTablePageCount(currentTableName, page);
                    pagination.setPageCount(pageCount);
                    pagination.setPageFactory(pageIndex -> {

                        log.info("PageIndex:{}", pageIndex);

                        return createPage(new VPage(new VPageIndex(pageIndex), page.getPageSize()), currentTableName);
                    });

                    tableGridPane.add(pagination, 0, 1);//column,row


                    tab.setContent(tableGridPane);
                    tab.setClosable(true);
                    tab.setId(selectedItem.getValue() + "");

                    log.info("tab id is:{}", selectedItem.getValue());

                    centerTabPane.getTabs().add(tab);

                    centerTabPane.getSelectionModel().select(tab);
                }

            }

        });

        systemViewTree.setRoot(rootItem);

        systemViewTree.setShowRoot(false);

        ContextMenu treeContextMenu = new ContextMenu();
        MenuItem refreshItem = new MenuItem("Refresh");
        refreshItem.setOnAction(event -> {
            // 刷新树形结构
            //TODO refresh tree
        });
        treeContextMenu.getItems().add(refreshItem);
        systemViewTree.setContextMenu(treeContextMenu);

        ContextMenu tableContextMenu = new ContextMenu();
        MenuItem editTableStructureMenuItem = new MenuItem("编辑表结构");
        editTableStructureMenuItem.setOnAction(event -> {
            TreeItem<String> selectedItem = systemViewTree.getSelectionModel().getSelectedItem();
            if (selectedItem != null && selectedItem.getParent().getValue().equals(tablesItem.getValue())) {
                VTableName tableName = new VTableName(selectedItem.getValue());
                showEditTableStructureDialog(tableName);
            }
        });
        tableContextMenu.getItems().add(editTableStructureMenuItem);

        systemViewTree.setContextMenu(tableContextMenu);

        leftGridPane.add(systemViewTree, 0, 0);
        borderPane.setLeft(leftGridPane);

        fileOpenMenu.setOnAction(event -> {
            if (log.isTraceEnabled()) {
                log.trace("You click the file open menu from menu Item");
            }

            log.info("{}",event);

            VDatabaseFile databaseFile = applicationService.openDataBaseFile(stage);

            setDatabaseService(new DatabaseService(databaseFile));

            TableService tableService = new TableService(getDatabaseService());
            VTableList vTableList = tableService.getTables();
            List<VTable> tableList = vTableList.getTables();
            for (VTable table : tableList) {
                TreeItem<String> tablesItemNode = new TreeItem<>(table.getTableName().getTableName());
                tablesItem.getChildren().add(tablesItemNode);
            }

            tablesItem.setExpanded(true);

        });

        openBt.setOnAction(fileOpenMenu.getOnAction());

        scene = new Scene(borderPane, 1000, 500);
        setScene(scene);


        stage.titleProperty().bind(I18N.createStringBinding("window.title"));
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            log.info("{}",event);
            Platform.exit();
        });

        stage.show();
    }
    private void showEditTableStructureDialog(VTableName tableName) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("编辑表结构 - " + tableName.getTableName());

        TableService tableService = new TableService(getDatabaseService());
        List<VTableColumn> tableColumns = tableService.getColumns(tableName);

        // 创建 TableView 显示字段信息
        TableView<VTableColumn> tableStructureView = new TableView<>();
        TableColumn<VTableColumn, String> columnNameCol = new TableColumn<>("字段名称");
        columnNameCol.setCellValueFactory(param -> param.getValue().getName().getNameProperty());

        TableColumn<VTableColumn, String> columnTypeCol = new TableColumn<>("数据类型");
        columnTypeCol.setCellValueFactory(param -> param.getValue().getDataType().getDataTypeProperty());
        columnTypeCol.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(SqliteDataType.TYPES)));

        columnTypeCol.setOnEditCommit(event -> {
            VTableColumn row = event.getRowValue();
            row.getDataType().setDataType(event.getNewValue());
        });

        TableColumn<VTableColumn, Boolean> notNullCol = new TableColumn<>("非空");
        notNullCol.setCellValueFactory(param -> param.getValue().getNotNull().isNotNullProperty());
        notNullCol.setCellFactory(CheckBoxTableCell.forTableColumn(notNullCol));
        notNullCol.setEditable(true);

        notNullCol.setOnEditCommit(event -> {
            VTableColumn row = event.getRowValue();
            row.getNotNull().setNotNull(event.getNewValue());
        });

        tableStructureView.getColumns().addAll(columnNameCol, columnTypeCol, notNullCol);

        ObservableList<VTableColumn> observableColumns = FXCollections.observableArrayList(tableColumns);
        tableStructureView.setItems(observableColumns);

        //启用表格编辑功能
        tableStructureView.setEditable(true);

        // 添加按钮
        Button addButton = new Button("添加字段");
        addButton.setOnAction(event -> {
            showAddFieldDialog(tableName, tableStructureView.getItems());
        });

        Button modifyButton = new Button("修改字段");
        modifyButton.setOnAction(event -> {
            VTableColumn selectedColumn = tableStructureView.getSelectionModel().getSelectedItem();
            if (selectedColumn == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "请先选择一个字段！");
                alert.show();
                return;
            }
            showModifyFieldDialog(tableName, selectedColumn, tableStructureView.getItems());
        });

        Button deleteButton = new Button("删除字段");
        deleteButton.setOnAction(event -> {
            VTableColumn selectedColumn = tableStructureView.getSelectionModel().getSelectedItem();
            if (selectedColumn == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "请先选择一个字段！");
                alert.show();
                return;
            }
            try {
                deleteField(tableName, selectedColumn.getName().getName(), tableStructureView.getItems());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        Button saveButton = new Button("保存更改");
        saveButton.setOnAction(event -> {
            try {
                saveTableChanges(tableName, tableStructureView.getItems());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            dialog.close();
        });

        // 布局
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(tableStructureView, 0, 0, 4, 1);
        gridPane.add(addButton, 0, 1);
        gridPane.add(modifyButton, 1, 1);
        gridPane.add(deleteButton, 2, 1);
        gridPane.add(saveButton, 3, 1);

        dialog.getDialogPane().setContent(gridPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
        dialog.show();
    }

    public void addColumn(VTableName tableName, String columnName, String dataType, boolean isNotNull) throws SQLException {
        String sql = "ALTER TABLE " + tableName.getTableName() + " ADD COLUMN " + columnName + " " + dataType;
        if (isNotNull) {
            sql += " NOT NULL";
        }
        try (Statement stmt = databaseService.getvConnection().getConnection().createStatement()) {
            stmt.execute(sql);
        }
    }
    public void saveTableChanges(VTableName tableName, ObservableList<VTableColumn> updatedColumns) throws SQLException {
        String tempTableName = tableName.getTableName() + "_temp";
        Connection connection = null;
        Statement stmt = null;

        try {
            // 获取数据库连接
            connection = databaseService.getvConnection().getConnection();
            stmt = connection.createStatement();

            // 1. 创建临时表并复制数据（应用所有字段的修改）
            StringBuilder createTempTableSql = new StringBuilder("CREATE TABLE " + tempTableName + " (");
            for (VTableColumn column : updatedColumns) {
                createTempTableSql.append(column.getName().getName()) // 字段名称
                        .append(" ") // 空格分隔
                        .append(column.getDataType().getDataType()); // 数据类型

                if (column.getNotNull().isNotNull()) {
                    createTempTableSql.append(" NOT NULL"); // 非空约束
                }

                if (column.getPk().getPrimaryKey() == 1) {
                    createTempTableSql.append(" PRIMARY KEY"); // 主键约束
                }

                createTempTableSql.append(", ");
            }

            // 去掉最后一个逗号和空格
            createTempTableSql.setLength(createTempTableSql.length() - 2);
            createTempTableSql.append(")");

            stmt.execute(createTempTableSql.toString());

            // 2. 复制数据到临时表
            StringBuilder copyDataSql = new StringBuilder("INSERT INTO " + tempTableName + " SELECT ");
            for (VTableColumn column : updatedColumns) {
                copyDataSql.append(column.getName().getName()).append(", ");
            }

            // 去掉最后一个逗号和空格
            copyDataSql.setLength(copyDataSql.length() - 2);
            copyDataSql.append(" FROM ").append(tableName.getTableName());
            stmt.execute(copyDataSql.toString());

            // 3. 删除原表
            String dropTableSql = "DROP TABLE " + tableName.getTableName();
            stmt.execute(dropTableSql);

            // 4. 重命名临时表为原表名
            String renameTableSql = "ALTER TABLE " + tempTableName + " RENAME TO " + tableName.getTableName();
            stmt.execute(renameTableSql);
        } catch (SQLException e) {
            throw new SQLException("Failed to save table changes: " + e.getMessage(), e);
        } finally {
            // 关闭资源
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }
    private void showModifyFieldDialog(VTableName tableName, VTableColumn selectedColumn, ObservableList<VTableColumn> tableColumns) {
        // 创建对话框
        Dialog<VTableColumn> dialog = new Dialog<>();
        dialog.setTitle("修改字段 - " + tableName.getTableName());
        dialog.setHeaderText("请修改字段的信息");

        // 设置按钮
        ButtonType modifyButtonType = new ButtonType("修改", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, ButtonType.CANCEL);

        // 创建输入控件
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField fieldName = new TextField(selectedColumn.getName().getName());
        fieldName.setPromptText("字段名称");

        TextField fieldType = new TextField(selectedColumn.getDataType().getDataType());
        fieldType.setPromptText("数据类型");

        CheckBox notNullCheckBox = new CheckBox("非空");
        notNullCheckBox.setSelected(selectedColumn.getNotNull().isNotNull());

        grid.add(new Label("字段名称:"), 0, 0);
        grid.add(fieldName, 1, 0);
        grid.add(new Label("数据类型:"), 0, 1);
        grid.add(fieldType, 1, 1);
        grid.add(notNullCheckBox, 1, 2);

        // 设置对话框内容
        dialog.getDialogPane().setContent(grid);

        // 禁用“修改”按钮直到输入有效
        Node modifyButton = dialog.getDialogPane().lookupButton(modifyButtonType);
        modifyButton.setDisable(true);

        // 监听输入有效性
        fieldName.textProperty().addListener((observable, oldValue, newValue) -> {
            modifyButton.setDisable(newValue.trim().isEmpty() || fieldType.getText().trim().isEmpty());
        });
        fieldType.textProperty().addListener((observable, oldValue, newValue) -> {
            modifyButton.setDisable(newValue.trim().isEmpty() || fieldName.getText().trim().isEmpty());
        });

        // 转换结果
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifyButtonType) {
                return new VTableColumn(
                        selectedColumn.getId(), // 保留原有 ID
                        new VTableColumnName(fieldName.getText()),
                        new VTableTableColumnDataType(fieldType.getText()),
                        new VTableColumnNotNull(notNullCheckBox.isSelected()),
                        selectedColumn.getDfltValue(), // 保留默认值
                        selectedColumn.getPk() // 保留主键信息
                );
            }
            return null;
        });

        // 显示对话框并处理结果
        dialog.showAndWait().ifPresent(modifiedColumn -> {
            try {
                // 修改字段到数据库
                TableService tableService = new TableService(getDatabaseService());
                //TODO add mofify column
//                tableService.modifyColumn(
//                        tableName,
//                        selectedColumn.getName().getName(),
//                        modifiedColumn.getName().getName(),
//                        modifiedColumn.getDataType().getDataType(),
//                        modifiedColumn.getNotNull().isNotNull()
//                );

                // 更新表格视图
                int index = tableColumns.indexOf(selectedColumn);
                if (index != -1) {
                    tableColumns.set(index, modifiedColumn); // 替换旧字段
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "修改字段失败: " + e.getMessage());
                alert.show();
            }
        });
    }

    private void showAddFieldDialog(VTableName tableName, ObservableList<VTableColumn> tableColumns) {
        // 创建对话框
        Dialog<VTableColumn> dialog = new Dialog<>();
        dialog.setTitle("添加字段 - " + tableName.getTableName());
        dialog.setHeaderText("请输入新字段的信息");

        // 设置按钮
        ButtonType addButtonType = new ButtonType("添加", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // 创建输入控件
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField fieldName = new TextField();
        fieldName.setPromptText("字段名称");

        TextField fieldType = new TextField();
        fieldType.setPromptText("数据类型");

        CheckBox notNullCheckBox = new CheckBox("非空");

        grid.add(new Label("字段名称:"), 0, 0);
        grid.add(fieldName, 1, 0);
        grid.add(new Label("数据类型:"), 0, 1);
        grid.add(fieldType, 1, 1);
        grid.add(notNullCheckBox, 1, 2);

        // 设置对话框内容
        dialog.getDialogPane().setContent(grid);

        // 禁用“添加”按钮直到输入有效
        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        // 监听输入有效性
        fieldName.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || fieldType.getText().trim().isEmpty());
        });
        fieldType.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || fieldName.getText().trim().isEmpty());
        });

        // 转换结果
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new VTableColumn(
                        new VTableColumnId(tableColumns.size()), // 假设新字段的 ID 是当前字段数量
                        new VTableColumnName(fieldName.getText()),
                        new VTableTableColumnDataType(fieldType.getText()),
                        new VTableColumnNotNull(notNullCheckBox.isSelected()),
                        new VTableDfltValue(null),
                        new VTableColumnPk(0)
                );
            }
            return null;
        });

        // 显示对话框并处理结果
        dialog.showAndWait().ifPresent(newColumn -> {
            try {
                // 添加字段到数据库
                TableService tableService = new TableService(getDatabaseService());
                tableService.addColumn(tableName, newColumn.getName().getName(), newColumn.getDataType().getDataType(), newColumn.getNotNull().isNotNull());

                // 更新表格视图
                tableColumns.add(newColumn);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "添加字段失败: " + e.getMessage());
                alert.show();
            }
        });
    }

    public void modifyColumn(VTableName tableName, String oldColumnName, String newColumnName, String newDataType, boolean isNotNull) throws SQLException {
        String tempTableName = tableName.getTableName() + "_temp";
        String createTempTableSql = "CREATE TABLE " + tempTableName + " AS SELECT * FROM " + tableName.getTableName();
        executeUpdate(createTempTableSql);

        String dropTableSql = "DROP TABLE " + tableName.getTableName();
        executeUpdate(dropTableSql);

        String createNewTableSql = "CREATE TABLE " + tableName.getTableName() + "(";
        // TODO: 根据字段列表动态生成 SQL
        createNewTableSql += ")";
        executeUpdate(createNewTableSql);

        String copyDataSql = "INSERT INTO " + tableName.getTableName() + " SELECT ";
        // TODO: 动态生成插入字段列表
        copyDataSql += " FROM " + tempTableName;
        executeUpdate(copyDataSql);

        String dropTempTableSql = "DROP TABLE " + tempTableName;
        executeUpdate(dropTempTableSql);
    }

    private void executeUpdate(String sql) throws SQLException {
        try (Statement stmt = databaseService.getvConnection().getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    public void deleteField(VTableName tableName, String columnName, ObservableList<VTableColumn> tableColumns) throws SQLException {
        String tempTableName = tableName.getTableName() + "_temp";
        Connection connection = null;
        Statement stmt = null;

        try {
            // 获取数据库连接
            connection = databaseService.getvConnection().getConnection();
            stmt = connection.createStatement();

            // 1. 创建临时表并复制数据（排除要删除的字段）
            StringBuilder createTempTableSql = new StringBuilder("CREATE TABLE " + tempTableName + " AS SELECT ");
            TableService tableService = new TableService(getDatabaseService());
            List<VTableColumn> columns = tableService.getColumns(tableName);

            for (VTableColumn column : columns) {
                if (!column.getName().getName().equals(columnName)) {
                    createTempTableSql.append(column.getName().getName()).append(", ");
                }
            }

            // 去掉最后一个逗号和空格
            createTempTableSql.setLength(createTempTableSql.length() - 2);
            createTempTableSql.append(" FROM ").append(tableName.getTableName());
            stmt.execute(createTempTableSql.toString());

            // 2. 删除原表
            String dropTableSql = "DROP TABLE " + tableName.getTableName();
            stmt.execute(dropTableSql);

            // 3. 重命名临时表为原表名
            String renameTableSql = "ALTER TABLE " + tempTableName + " RENAME TO " + tableName.getTableName();
            stmt.execute(renameTableSql);

            // 4. 更新表格视图
            Platform.runLater(() -> {
                tableColumns.removeIf(column -> column.getName().getName().equals(columnName));
            });
        } catch (SQLException e) {
            throw new SQLException("Failed to delete field: " + columnName, e);
        } finally {
            // 关闭资源
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    public void deleteColumn(VTableName tableName, String columnName) throws SQLException {
        String tempTableName = tableName.getTableName() + "_temp";
        String createTempTableSql = "CREATE TABLE " + tempTableName + " AS SELECT * FROM " + tableName.getTableName();
        executeUpdate(createTempTableSql);

        String dropTableSql = "DROP TABLE " + tableName.getTableName();
        executeUpdate(dropTableSql);

        String createNewTableSql = "CREATE TABLE " + tableName.getTableName() + "(";
        // TODO: 根据字段列表动态生成 SQL，排除指定字段
        createNewTableSql += ")";
        executeUpdate(createNewTableSql);

        String copyDataSql = "INSERT INTO " + tableName.getTableName() + " SELECT ";
        // TODO: 动态生成插入字段列表
        copyDataSql += " FROM " + tempTableName;
        executeUpdate(copyDataSql);

        String dropTempTableSql = "DROP TABLE " + tempTableName;
        executeUpdate(dropTempTableSql);
    }

    private TableView<Map> createPage(VPage page, VTableName currentTableName) {

        TableView<Map> tableView = new TableView<>();

        TableService tableService = new TableService(getDatabaseService());
        List<VTableColumn> tableColumns = tableService.getColumns(currentTableName);
        List<TableColumnView> tableViewColumns = tableService.getTableViewColumns(tableColumns);
        for (TableColumnView tableColumnView : tableViewColumns) {
            TableColumn<Map, String> keyColumn = new TableColumn<>(tableColumnView.getName());
            keyColumn.setCellValueFactory(new MapValueFactory<>(tableColumnView.getName()));
            tableView.getColumns().add(keyColumn);
        }

        VPage vTablePage = page;

        vTablePage = tableService.getData(currentTableName, vTablePage);
        List<Map> tableDatas = vTablePage.getData();
        ObservableList<Map> dataList = FXCollections.observableArrayList(tableDatas);

        tableView.setItems(dataList);
        return tableView;
    }


    /**
     * sets the given Locale in the I18N class and keeps count of the number of switches.
     *
     * @param locale the new local to set
     */
    private void switchLanguage(Locale locale) {
        I18N.setLocale(locale);
    }
}
