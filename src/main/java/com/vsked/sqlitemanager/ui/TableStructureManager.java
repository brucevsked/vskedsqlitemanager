package com.vsked.sqlitemanager.ui;

import com.vsked.sqlitemanager.domain.SqliteDataType;
import com.vsked.sqlitemanager.domain.VTableColumn;
import com.vsked.sqlitemanager.domain.VTableColumnId;
import com.vsked.sqlitemanager.domain.VTableColumnName;
import com.vsked.sqlitemanager.domain.VTableColumnNotNull;
import com.vsked.sqlitemanager.domain.VTableColumnPk;
import com.vsked.sqlitemanager.domain.VTableDfltValue;
import com.vsked.sqlitemanager.domain.VTableName;
import com.vsked.sqlitemanager.domain.VTableTableColumnDataType;
import com.vsked.sqlitemanager.services.I18N;
import com.vsked.sqlitemanager.services.TableService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;
import java.util.List;

public class TableStructureManager {
    private static final Logger log = LoggerFactory.getLogger(TableStructureManager.class);

    private ApplicationMainUI applicationMainUI;

    public TableStructureManager(ApplicationMainUI applicationMainUI) {
        this.applicationMainUI = applicationMainUI;
    }

    public void showEditTableStructureDialog(VTableName tableName) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("编辑表结构 - " + tableName.getTableName());

        TableService tableService = new TableService(applicationMainUI.getDatabaseService());
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

    public void showAddFieldDialog(VTableName tableName, ObservableList<VTableColumn> tableColumns) {
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

        // 禁用"添加"按钮直到输入有效
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
                TableService tableService = new TableService(applicationMainUI.getDatabaseService());
                tableService.addColumn(tableName, newColumn.getName().getName(), newColumn.getDataType().getDataType(), newColumn.getNotNull().isNotNull());

                // 更新表格视图
                tableColumns.add(newColumn);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "添加字段失败: " + e.getMessage());
                alert.show();
            }
        });
    }

    void showModifyFieldDialog(VTableName tableName, VTableColumn selectedColumn, ObservableList<VTableColumn> tableColumns) {
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

        // 禁用"修改"按钮直到输入有效
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
                TableService tableService = new TableService(applicationMainUI.getDatabaseService());
                //TODO add modify column
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

    public void saveTableChanges(VTableName tableName, ObservableList<VTableColumn> updatedColumns) throws SQLException {
        String tempTableName = tableName.getTableName() + "_temp";
        java.sql.Connection connection = null;
        java.sql.Statement stmt = null;

        try {
            // 获取数据库连接
            connection = applicationMainUI.getDatabaseService().getvConnection().getConnection();
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

    public void deleteField(VTableName tableName, String columnName, ObservableList<VTableColumn> tableColumns) throws SQLException {
        String tempTableName = tableName.getTableName() + "_temp";
        java.sql.Connection connection = null;
        java.sql.Statement stmt = null;

        try {
            // 获取数据库连接
            connection = applicationMainUI.getDatabaseService().getvConnection().getConnection();
            stmt = connection.createStatement();

            // 1. 创建临时表并复制数据（排除要删除的字段）
            StringBuilder createTempTableSql = new StringBuilder("CREATE TABLE " + tempTableName + " AS SELECT ");
            TableService tableService = new TableService(applicationMainUI.getDatabaseService());
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
            javafx.application.Platform.runLater(() -> {
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
}
