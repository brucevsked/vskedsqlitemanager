package com.vsked.sqlitemanager.ui;

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
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class QueryTabManager {

    private ApplicationMainUI applicationMainUI;

    public QueryTabManager(ApplicationMainUI applicationMainUI) {
        this.applicationMainUI = applicationMainUI;
    }

    public Tab createQueryTab(int queryCount) {
        String queryTabId = I18N.get("tab.query") + queryCount;
        Tab tab = new Tab(queryTabId);
        tab.setId("Query" + queryCount);
        tab.setText(queryTabId);

        GridPane queryTabGridPane = new GridPane();
        Button runQueryButton = I18N.buttonForKey("button.runQuery");
        Button stopQueryButton = I18N.buttonForKey("button.stopQuery");
        queryTabGridPane.add(runQueryButton, 0, 0);
        queryTabGridPane.add(stopQueryButton, 1, 0);

        TextArea ta = new TextArea();
        ta.setId("ta" + queryCount);
        ta.setFocusTraversable(true);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem runCurrentSelectedSqlMenuItem = I18N.menuItemForKey("queryPanel.textarea.contextMenu.runCurrentSelectedSql");
        MenuItem cutMenuItem = I18N.menuItemForKey("queryPanel.textarea.contextMenu.cut");
        MenuItem copyMenuItem = I18N.menuItemForKey("queryPanel.textarea.contextMenu.copy");
        MenuItem pasteMenuItem = I18N.menuItemForKey("queryPanel.textarea.contextMenu.paste");
        MenuItem selectAllMenuItem = I18N.menuItemForKey("queryPanel.textarea.contextMenu.selectAll");

        cutMenuItem.setOnAction(actionEvent4 -> {
            if (applicationMainUI.getLog().isTraceEnabled()) {
                applicationMainUI.getLog().trace("you click cut menu from contextMenu");
            }
            applicationMainUI.getLog().info("{}", actionEvent4);
            applicationMainUI.getCurrentQueryTextArea().cut();
        });

        copyMenuItem.setOnAction(actionEvent3 -> {
            if (applicationMainUI.getLog().isTraceEnabled()) {
                applicationMainUI.getLog().trace("you click copy menu from contextMenu");
            }
            applicationMainUI.getLog().info("{}", actionEvent3);
            TextArea textArea = applicationMainUI.getCurrentQueryTextArea();
            if (!textArea.getSelectedText().isEmpty()) {
                ClipboardContent content = new ClipboardContent();
                content.putString(textArea.getSelectedText());
                Clipboard clipboard = Clipboard.getSystemClipboard();
                clipboard.setContent(content);
            } else {
                applicationMainUI.getLog().trace("you click copy menu from contextMenu.No text selected to copy.");
            }
        });

        pasteMenuItem.setOnAction(actionEvent2 -> {
            if (applicationMainUI.getLog().isTraceEnabled()) {
                applicationMainUI.getLog().trace("you click paste menu from contextMenu");
            }
            applicationMainUI.getLog().info("{}", actionEvent2);
            applicationMainUI.getCurrentQueryTextArea().paste();
        });

        selectAllMenuItem.setOnAction(actionEvent1 -> {
            if (applicationMainUI.getLog().isTraceEnabled()) {
                applicationMainUI.getLog().trace("you click select all menu from contextMenu");
            }
            if (applicationMainUI.getLog().isDebugEnabled()) {
                applicationMainUI.getLog().debug("{}", actionEvent1.getSource());
            }
            applicationMainUI.getCurrentQueryTextArea().selectAll();
        });

        contextMenu.getItems().addAll(selectAllMenuItem, runCurrentSelectedSqlMenuItem, cutMenuItem, copyMenuItem, pasteMenuItem);
        ta.setContextMenu(contextMenu);

        queryTabGridPane.add(ta, 0, 1);
        tab.setContent(queryTabGridPane);

        runQueryButton.setOnAction(actionEvent6 -> {
            try {
                TableView<Map<String, String>> resultTable = new TableView<>();
                Label resultLabel = new Label();
                String sql = ta.getText().trim();
                TableService tableService = new TableService(applicationMainUI.getDatabaseService());

                // Clear old content
                queryTabGridPane.getChildren().removeIf(node -> node instanceof TableView || node instanceof Label);

                if (sql.toLowerCase().startsWith("select")) {
                    List<Map<String, String>> results = tableService.executeQuery(sql);
                    ObservableList<Map<String, String>> data = FXCollections.observableArrayList(results);

                    if (!results.isEmpty()) {
                        for (String key : results.get(0).keySet()) {
                            TableColumn<Map<String, String>, String> column = new TableColumn<>(key);
                            final String currentKey = key;
                            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(currentKey)));
                            resultTable.getColumns().add(column);
                        }
                    }

                    resultTable.setItems(data);
                    queryTabGridPane.add(resultTable, 0, 2, 3, 1);
                } else {
                    int rowsAffected = tableService.executeUpdate(sql);
                    resultLabel.setText(I18N.get("queryPanel.textarea.tips.updateSuccess") + rowsAffected);
                    resultLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    queryTabGridPane.add(resultLabel, 0, 2);
                }

            } catch (SQLException e) {
                applicationMainUI.getLog().error("Query execution failed", e);
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, I18N.get("alert.sql.error") + e.getMessage());
                    alert.show();
                });
            } catch (Exception e) {
                applicationMainUI.getLog().error("Unexpected error", e);
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, I18N.get("alert.unknow.error") + e.getMessage());
                    alert.show();
                });
            }
            applicationMainUI.getLog().info("{}", actionEvent6);
        });

        stopQueryButton.setOnAction(actionEvent5 -> {
            if (applicationMainUI.getLog().isTraceEnabled()) {
                applicationMainUI.getLog().trace("You click stop query button from query grid");
            }
            applicationMainUI.getLog().info("{}", actionEvent5);
        });

        tab.setOnSelectionChanged(event -> {
            if (tab.isSelected()) {
                applicationMainUI.setCurrentQueryTextArea(ta);
                applicationMainUI.setCurrentQueryGridPane(queryTabGridPane);
                ta.requestFocus();
            }
        });

        return tab;
    }

public void showAddFieldDialog(VTableName tableName, ObservableList<VTableColumn> items) {
    Dialog<VTableColumn> dialog = new Dialog<>();
    dialog.setTitle("添加字段 - " + tableName.getTableName());
    dialog.setHeaderText("请输入新字段的信息");

    ButtonType addButtonType = new ButtonType("添加", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

    TextField fieldName = new TextField();
    fieldName.setPromptText("字段名称");

    TextField fieldType = new TextField();
    fieldType.setPromptText("数据类型");

    CheckBox notNullCheckBox = new CheckBox("非空");

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.add(new Label("字段名称:"), 0, 0);
    grid.add(fieldName, 1, 0);
    grid.add(new Label("数据类型:"), 0, 1);
    grid.add(fieldType, 1, 1);
    grid.add(notNullCheckBox, 1, 2);

    dialog.getDialogPane().setContent(grid);

    // 初始禁用按钮直到输入有效
    Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
    addButton.setDisable(true);

    // 输入验证
    fieldName.textProperty().addListener((obs, oldVal, newVal) -> {
        addButton.setDisable(newVal.trim().isEmpty() || fieldType.getText().trim().isEmpty());
    });

    fieldType.textProperty().addListener((obs, oldVal, newVal) -> {
        addButton.setDisable(newVal.trim().isEmpty() || fieldName.getText().trim().isEmpty());
    });

    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == addButtonType) {
            return new VTableColumn(
                    new VTableColumnId(items.size()), // 假设 ID 是当前字段数量
                    new VTableColumnName(fieldName.getText()),
                    new VTableTableColumnDataType(fieldType.getText()),
                    new VTableColumnNotNull(notNullCheckBox.isSelected()),
                    new VTableDfltValue(null),
                    new VTableColumnPk(0)
            );
        }
        return null;
    });

    dialog.showAndWait().ifPresent(newColumn -> {
        try {
            // 添加字段到数据库
            TableService tableService = new TableService(applicationMainUI.getDatabaseService());
            tableService.addColumn(tableName,
                                   newColumn.getName().getName(),
                                   newColumn.getDataType().getDataType(),
                                   newColumn.getNotNull().isNotNull());

            // 更新表格视图
            items.add(newColumn);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "添加字段失败: " + e.getMessage());
            alert.show();
        }
    });
}

public void showModifyFieldDialog(VTableName tableName, VTableColumn selectedColumn, ObservableList<VTableColumn> items) {
    Dialog<VTableColumn> dialog = new Dialog<>();
    dialog.setTitle("修改字段 - " + tableName.getTableName());
    dialog.setHeaderText("请修改字段的信息");

    ButtonType modifyButtonType = new ButtonType("修改", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, ButtonType.CANCEL);

    TextField fieldName = new TextField(selectedColumn.getName().getName());
    fieldName.setPromptText("字段名称");

    TextField fieldType = new TextField(selectedColumn.getDataType().getDataType());
    fieldType.setPromptText("数据类型");

    CheckBox notNullCheckBox = new CheckBox("非空");
    notNullCheckBox.setSelected(selectedColumn.getNotNull().isNotNull());

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.add(new Label("字段名称:"), 0, 0);
    grid.add(fieldName, 1, 0);
    grid.add(new Label("数据类型:"), 0, 1);
    grid.add(fieldType, 1, 1);
    grid.add(notNullCheckBox, 1, 2);

    dialog.getDialogPane().setContent(grid);

    // 初始禁用按钮直到输入有效
    Node modifyButton = dialog.getDialogPane().lookupButton(modifyButtonType);
    modifyButton.setDisable(true);

    // 输入验证：字段名或类型不能为空
    fieldName.textProperty().addListener((obs, oldVal, newVal) -> {
        modifyButton.setDisable(newVal.trim().isEmpty() || fieldType.getText().trim().isEmpty());
    });

    fieldType.textProperty().addListener((obs, oldVal, newVal) -> {
        modifyButton.setDisable(newVal.trim().isEmpty() || fieldName.getText().trim().isEmpty());
    });

    // 设置默认值
    modifyButton.setDisable(
        fieldName.getText().trim().isEmpty() || fieldType.getText().trim().isEmpty()
    );

    // 返回修改后的字段对象
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

    dialog.showAndWait().ifPresent(modifiedColumn -> {
        try {
            // 调用服务层修改字段
            TableService tableService = new TableService(applicationMainUI.getDatabaseService());
            tableService.modifyColumn(
                tableName,
                selectedColumn.getName().getName(),
                modifiedColumn.getName().getName(),
                modifiedColumn.getDataType().getDataType(),
                modifiedColumn.getNotNull().isNotNull()
            );

            // 更新表格视图
            int index = items.indexOf(selectedColumn);
            if (index != -1) {
                items.set(index, modifiedColumn); // 替换旧字段
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "修改字段失败: " + e.getMessage());
            alert.show();
        }
    });
}

}

