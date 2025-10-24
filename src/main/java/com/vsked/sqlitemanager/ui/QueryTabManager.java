package com.vsked.sqlitemanager.ui;

import com.vsked.sqlitemanager.domain.VTableColumn;
import com.vsked.sqlitemanager.domain.VTableName;
import com.vsked.sqlitemanager.services.I18N;
import com.vsked.sqlitemanager.services.TableService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class QueryTabManager {
    private static final Logger log = LoggerFactory.getLogger(QueryTabManager.class);

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
            if (log.isTraceEnabled()) {
                log.trace("you click cut menu from contextMenu");
            }
            log.info("{}", actionEvent4);
            ApplicationMainUI.currentQueryTextArea.cut();
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
            ApplicationMainUI.currentQueryTextArea.paste();
        });

        selectAllMenuItem.setOnAction(actionEvent1 -> {
            if (log.isTraceEnabled()) {
                log.trace("you click select all menu from contextMenu");
            }
            if (log.isDebugEnabled()) {
                log.debug("{}", actionEvent1.getSource());
            }
            ApplicationMainUI.currentQueryTextArea.selectAll();
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

        tab.setOnSelectionChanged(event -> {
            if (tab.isSelected()) {
                ApplicationMainUI.currentQueryTextArea = ta;
                ApplicationMainUI.currentQueryGridPane = queryTabGridPane;
                ta.requestFocus();
            }
        });

        return tab;
    }

    public void showAddFieldDialog(VTableName tableName, ObservableList<VTableColumn> items) {
        applicationMainUI.getTableStructureManager().showAddFieldDialog(tableName, items);
    }

    public void showModifyFieldDialog(VTableName tableName, VTableColumn selectedColumn, ObservableList<VTableColumn> items) {
        applicationMainUI.getTableStructureManager().showModifyFieldDialog(tableName, selectedColumn, items);
    }

}

