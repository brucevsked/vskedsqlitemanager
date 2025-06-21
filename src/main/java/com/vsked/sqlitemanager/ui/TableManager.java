package com.vsked.sqlitemanager.ui;

import com.vsked.sqlitemanager.domain.VPage;
import com.vsked.sqlitemanager.domain.VPageIndex;
import com.vsked.sqlitemanager.domain.VPageSize;
import com.vsked.sqlitemanager.domain.VTableName;
import com.vsked.sqlitemanager.services.TableService;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import java.util.List;
import java.util.Map;

public class TableManager {

    private ApplicationMainUI applicationMainUI;

    public TableManager(ApplicationMainUI applicationMainUI) {
        this.applicationMainUI = applicationMainUI;
    }

    public TableView<Map<String, String>> createResultTable(List<Map<String, String>> results) {
        TableView<Map<String, String>> resultTable = new TableView<>();
        if (!results.isEmpty()) {
            for (String key : results.get(0).keySet()) {
                TableColumn<Map<String, String>, String> column = new TableColumn<>(key);
                final String currentKey = key;
                column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(currentKey)));
                resultTable.getColumns().add(column);
            }
        }
        resultTable.setItems(FXCollections.observableArrayList(results));
        return resultTable;
    }

    public Pagination createPagination(VTableName currentTableName) {
        Pagination pagination = new Pagination();
        TableService tableService = new TableService(applicationMainUI.getDatabaseService());
        int pageCount = tableService.getTablePageCount(currentTableName, new VPage(new VPageIndex(0), new VPageSize(10)));
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(pageIndex -> {
            return applicationMainUI.createPage(new VPage(new VPageIndex(pageIndex), new VPageSize(10)), currentTableName);
        });
        return pagination;
    }
}

