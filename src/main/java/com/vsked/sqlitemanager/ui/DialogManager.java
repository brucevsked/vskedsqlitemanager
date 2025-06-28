package com.vsked.sqlitemanager.ui;

import com.vsked.sqlitemanager.domain.SqliteDataType;
import com.vsked.sqlitemanager.domain.VTableColumn;
import com.vsked.sqlitemanager.domain.VTableName;
import com.vsked.sqlitemanager.services.I18N;
import com.vsked.sqlitemanager.services.TableService;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.GridPane;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DialogManager {

    private ApplicationMainUI applicationMainUI;

    public DialogManager(ApplicationMainUI applicationMainUI) {
        this.applicationMainUI = applicationMainUI;
    }

    public void showAddTableDialog() {
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
        addButton.setDisable(true);

        tableNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return tableNameField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(tableName -> {
            //TODO add table
//            try {
//                TableService tableService = new TableService(applicationMainUI.getDatabaseService());
//                tableService.createTable(new VTableName(tableName));
//
//                TreeItem<String> tablesNode = (TreeItem<String>) applicationMainUI.getSystemViewTree().getRoot().getChildren().get(0);
//                tablesNode.getChildren().add(new TreeItem<>(tableName));
//                tablesNode.setExpanded(true);
//
//            } catch (Exception e) {
//                Alert alert = new Alert(Alert.AlertType.ERROR, "创建表失败: " + e.getMessage());
//                alert.show();
//            }
        });
    }

    public void showRenameTableDialog(VTableName oldTableName, TreeItem<String> tableItem) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(I18N.get("dialog.title.renameTable"));
        dialog.setHeaderText(I18N.get("dialog.header.renameTable"));

        ButtonType renameButtonType = new ButtonType(I18N.get("dialog.title.renameTable"), ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(renameButtonType, ButtonType.CANCEL);

        TextField tableNameField = new TextField(oldTableName.getTableName());
        tableNameField.setPromptText("新表名");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("新表名:"), 0, 0);
        grid.add(tableNameField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        Node renameButton = dialog.getDialogPane().lookupButton(renameButtonType);
        renameButton.setDisable(true);

        tableNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            renameButton.setDisable(newValue.trim().isEmpty() || newValue.equals(oldTableName.getTableName()));
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == renameButtonType) {
                return tableNameField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newTableName -> {
            try {
                TableService tableService = new TableService(applicationMainUI.getDatabaseService());
                tableService.renameTable(oldTableName, new VTableName(newTableName));
                tableItem.setValue(newTableName);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "重命名失败: " + e.getMessage());
                alert.show();
            }
        });
    }

    public void showDeleteTableDialog(VTableName tableName, TreeItem<String> tableItem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(I18N.get("alert.confirmDeleteTable.title"));
        alert.setHeaderText(I18N.get("alert.confirmDeleteTable.header", tableName.getTableName()));
        alert.setContentText(I18N.get("alert.confirmDeleteTable.content", tableName.getTableName()));

        ButtonType buttonTypeYes = new ButtonType(I18N.get("alert.confirmDeleteTable.Yes"));
        ButtonType buttonTypeNo = new ButtonType(I18N.get("alert.confirmDeleteTable.No"), ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            try {
                TableService tableService = new TableService(applicationMainUI.getDatabaseService());
                tableService.deleteTable(tableName);
                tableItem.getParent().getChildren().remove(tableItem);
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "删除失败: " + e.getMessage());
                errorAlert.show();
            }
        }
    }

    public void showEditTableStructureDialog(VTableName tableName) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("编辑表结构 - " + tableName.getTableName());

        TableService tableService = new TableService(applicationMainUI.getDatabaseService());
        List<VTableColumn> tableColumns = tableService.getColumns(tableName);

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
        tableStructureView.setItems(FXCollections.observableArrayList(tableColumns));
        tableStructureView.setEditable(true);

        Button addButton = I18N.buttonForKey("button.addField");
        addButton.setOnAction(event -> {
            new QueryTabManager(applicationMainUI).showAddFieldDialog(tableName, tableStructureView.getItems());
        });

        Button modifyButton = I18N.buttonForKey("button.editField");
        modifyButton.setOnAction(event -> {
            VTableColumn selectedColumn = tableStructureView.getSelectionModel().getSelectedItem();
            if (selectedColumn != null) {
                new QueryTabManager(applicationMainUI).showModifyFieldDialog(tableName, selectedColumn, tableStructureView.getItems());
            } else {
                Alert alert = I18N.alertOnlyContentForKey(Alert.AlertType.WARNING, "alert.pleaseSelectField");
                alert.show();
            }
        });

        Button deleteButton = I18N.buttonForKey("button.deleteField");
        deleteButton.setOnAction(event -> {
            VTableColumn selectedColumn = tableStructureView.getSelectionModel().getSelectedItem();
            if (selectedColumn != null) {
                try {
                    applicationMainUI.deleteField(tableName, selectedColumn.getName().getName(), tableStructureView.getItems());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Alert alert = I18N.alertOnlyContentForKey(Alert.AlertType.WARNING, "alert.pleaseSelectField");
                alert.show();
            }
        });

        Button saveButton = I18N.buttonForKey("button.saveChange");
        saveButton.setOnAction(event -> {
            try {
                applicationMainUI.saveTableChanges(tableName, tableStructureView.getItems());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            dialog.close();
        });

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
}

