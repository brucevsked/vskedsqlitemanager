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
import com.vsked.sqlitemanager.domain.VPageIndex;
import com.vsked.sqlitemanager.domain.VPageSize;
import com.vsked.sqlitemanager.domain.VTableColumn;
import com.vsked.sqlitemanager.viewmodel.TableColumnView;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;


public class ApplicationMainUI extends Application {

    private static final Logger log = LoggerFactory.getLogger(ApplicationMainUI.class);

    MenuAndToolbarManager menuAndToolbarManager;

    private Scene scene;
    private static int globalQueryTabCount = 0;
    public static TextArea currentQueryTextArea;
    public static GridPane currentQueryGridPane;

    private ApplicationService applicationService = new ApplicationService();
    static DatabaseService databaseService;

    public DatabaseService getDatabaseService() {
        return databaseService;
    }

    private TreeView<String> systemViewTree; // 类成员变量

    public static int getGlobalQueryTabCount() {
       globalQueryTabCount = globalQueryTabCount + 1;
        return globalQueryTabCount;
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
    public void start(Stage stage) {

        // create content
        BorderPane borderPane = new BorderPane();

        menuAndToolbarManager=new MenuAndToolbarManager(this);

        TreeItem<String> rootItem = I18N.treeItemForKey("tree.system");

        GridPane leftGridPane = new GridPane();

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

        MenuBar menuBar=menuAndToolbarManager.createMenuBar(stage, applicationService, tablesItem);

        TabPane centerTabPane = new TabPane();

        ToolBar toolbar=menuAndToolbarManager.createToolBar(stage,centerTabPane);
        GridPane topGridPane = menuAndToolbarManager.createTopGridPane(menuBar, toolbar);

        borderPane.setTop(topGridPane);

        systemViewTree = new TreeView<>(); // 初始化

        centerTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {

            if (newTab == null) {
                log.warn("Selected tab is null. Possibly a tab was closed.");
                return;
            }

            log.debug("current tab is:{}", newTab.getText());

            String newPanelId = newTab.getId();

            if (newPanelId != null && newPanelId.contains("Query")) {
                Node tabNode = newTab.getContent();
                GridPane queryGridPanel = (GridPane) tabNode;
                TextArea tempTextArea = (TextArea) queryGridPanel.lookup("#ta" + newPanelId.replace("Query", ""));
                ApplicationMainUI.currentQueryTextArea = tempTextArea;
                ApplicationMainUI.currentQueryGridPane = queryGridPanel;
                tempTextArea.requestFocus();
                log.debug("{}", tempTextArea.getText());
            } else {
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
                log.debug("{}", selectedItem);
                log.debug("{},{}", paramObservableValue, paramT1);
            }

            List<Tab> oldTabList = centerTabPane.getTabs();
            boolean isExistTab = false;

            for (Tab tab : oldTabList) {
                if (log.isDebugEnabled()) {
                    log.debug("{},{}", tab.getId(), selectedItem.getValue());
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


        ContextMenu tableContextMenu = new ContextMenu();
        MenuItem addTableMenuItem = I18N.menuItemForKey("tree.contextMenu.addTable"); // 国际化支持
        addTableMenuItem.setOnAction(event -> {
            showAddTableDialog();
        });
        tableContextMenu.getItems().add(addTableMenuItem);
        MenuItem renameTableMenuItem = I18N.menuItemForKey("tree.contextMenu.renameTable");
        renameTableMenuItem.setOnAction(event -> {
            TreeItem<String> selectedItem = systemViewTree.getSelectionModel().getSelectedItem();
            if (selectedItem != null && selectedItem.getParent().getValue().equals(tablesItem.getValue())) {
                VTableName oldTableName = new VTableName(selectedItem.getValue());
                showRenameTableDialog(oldTableName, selectedItem);
            }
        });
        tableContextMenu.getItems().add(renameTableMenuItem);
        MenuItem deleteTableMenuItem = I18N.menuItemForKey("tree.contextMenu.deleteTable");
        deleteTableMenuItem.setOnAction(event -> {
            TreeItem<String> selectedItem = systemViewTree.getSelectionModel().getSelectedItem();
            if (selectedItem != null && selectedItem.getParent().getValue().equals(tablesItem.getValue())) {
                showDeleteTableDialog(new VTableName(selectedItem.getValue()), selectedItem);
            }
        });
        tableContextMenu.getItems().add(deleteTableMenuItem);


        MenuItem editTableStructureMenuItem = I18N.menuItemForKey("tree.contextMenu.editTable");
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



        scene = new Scene(borderPane, 1000, 500);
        setScene(scene);


        stage.titleProperty().bind(I18N.createStringBinding("window.title"));
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            log.info("{}", event);
            Platform.exit();
        });

        stage.show();
    }

    void showDeleteTableDialog(VTableName tableName, TreeItem<String> tableItem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(I18N.get("alert.confirmDeleteTable.title"));
        alert.setHeaderText(I18N.get("alert.confirmDeleteTable.header", tableName.getTableName()));
        alert.setContentText(I18N.get("alert.confirmDeleteTable.content", tableName.getTableName()));

        ButtonType buttonTypeYes = new ButtonType(I18N.get("alert.confirmDeleteTable.Yes"));
        ButtonType buttonTypeNo = new ButtonType(I18N.get("alert.confirmDeleteTable.No"), ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        alert.showAndWait().ifPresent(type -> {
            if (type == buttonTypeYes) {
                try {
                    TableService tableService = new TableService(getDatabaseService());
                    tableService.deleteTable(tableName); // 调用服务层删除表

                    // 更新左侧树视图
                    tableItem.getParent().getChildren().remove(tableItem);

                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "删除失败: " + e.getMessage());
                    errorAlert.show();
                }
            }
        });
    }

    void showRenameTableDialog(VTableName oldTableName, TreeItem<String> tableItem) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(I18N.get("dialog.title.renameTable"));
        dialog.setHeaderText(I18N.get("dialog.header.renameTable"));

        ButtonType renameButtonType = new ButtonType(I18N.get("dialog.title.renameTable"), ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(renameButtonType, ButtonType.CANCEL);

        TextField tableNameField = new TextField(oldTableName.getTableName());
        tableNameField.setPromptText(I18N.get("newTable.title"));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label(I18N.get("newTable.title")), 0, 0);
        grid.add(tableNameField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        Node renameButton = dialog.getDialogPane().lookupButton(renameButtonType);
        renameButton.setDisable(true); // 初始禁用按钮

        // 输入非空验证
        tableNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            renameButton.setDisable(newValue.trim().isEmpty() || newValue.equals(oldTableName.getTableName()));
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == renameButtonType) {
                return tableNameField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newTableName -> {
            try {
                TableService tableService = new TableService(getDatabaseService());
                tableService.renameTable(oldTableName, new VTableName(newTableName));

                // 更新左侧树视图
                tableItem.setValue(newTableName);

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "重命名失败: " + e.getMessage());
                alert.show();
            }
        });
    }

    void showEditTableStructureDialog(VTableName tableName) {
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

        tableStructureView.setEditable(true);


        Button addButton = I18N.buttonForKey("button.addField");
        addButton.setOnAction(event -> {
            showAddFieldDialog(tableName, tableStructureView.getItems());
        });

        Button modifyButton = I18N.buttonForKey("button.editField");
        modifyButton.setOnAction(event -> {
            VTableColumn selectedColumn = tableStructureView.getSelectionModel().getSelectedItem();
            if (selectedColumn == null) {
                Alert alert = I18N.alertOnlyContentForKey(Alert.AlertType.WARNING, "alert.pleaseSelectField");
                alert.show();
                return;
            }
            showModifyFieldDialog(tableName, selectedColumn, tableStructureView.getItems());
        });

        Button deleteButton = I18N.buttonForKey("button.deleteField");
        deleteButton.setOnAction(event -> {
            VTableColumn selectedColumn = tableStructureView.getSelectionModel().getSelectedItem();
            if (selectedColumn == null) {
                Alert alert = I18N.alertOnlyContentForKey(Alert.AlertType.WARNING, "alert.pleaseSelectField");
                alert.show();
                return;
            }
            try {
                deleteField(tableName, selectedColumn.getName().getName(), tableStructureView.getItems());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        Button saveButton = I18N.buttonForKey("button.saveChange");
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

    void showAddTableDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(I18N.get("dialog.title.addNewTable"));
        dialog.setHeaderText(I18N.get("dialog.title.setNewTableName"));

        ButtonType addButtonType = new ButtonType("添加", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField tableNameField = new TextField();
        tableNameField.setPromptText("表名");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("表名称:"), 0, 0);
        grid.add(tableNameField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true); // 初始禁用按钮

        // 输入非空验证
        tableNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return tableNameField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(tableName -> {
            try {
                TableService tableService = new TableService(getDatabaseService());
                tableService.createTable(new VTableName(tableName)); // 创建数据库中的新表

                // 更新左侧树视图
                TreeItem<String> tablesNode = (TreeItem<String>) systemViewTree.getRoot().getChildren().get(0); // 获取 "Tables"
                tablesNode.getChildren().add(new TreeItem<>(tableName));
                tablesNode.setExpanded(true);

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "创建表失败: " + e.getMessage());
                alert.show();
            }
        });
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
        dialog.setTitle(I18N.get("dialog.title.addField")+" - " + tableName.getTableName());
        dialog.setHeaderText(I18N.get("dialog.header.addFieldInfo"));

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

    TableView<Map> createPage(VPage page, VTableName currentTableName) {

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

}
