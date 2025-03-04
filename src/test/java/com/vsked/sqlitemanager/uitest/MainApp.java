package com.vsked.sqlitemanager.uitest;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * this example from
 * <a href="https://blog.51cto.com/u_16213416/8990665">exampleCodeFrom</a>
 */
public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // create TableView
        TableView<Map> tableView = new TableView<>();

        // create table column
        TableColumn<Map, String> keyColumn = new TableColumn<>("Key");
        TableColumn<Map, String> valueColumn = new TableColumn<>("Value");
        TableColumn<Map, String> a1Column = new TableColumn<>("a1");

        // set column data binding
        keyColumn.setCellValueFactory(new MapValueFactory<>("key"));
        valueColumn.setCellValueFactory(new MapValueFactory<>("value"));
        a1Column.setCellValueFactory(new MapValueFactory<>("a1"));

        // add column to TableView
        tableView.getColumns().add(keyColumn);
        tableView.getColumns().add(valueColumn);
        tableView.getColumns().add(a1Column);

        // create datasource for table view
        ObservableList<Map> data = FXCollections.observableArrayList();
        Map<String, String> map1 = new HashMap<>();
        map1.put("key", "A");
        map1.put("value", "Apple");
        map1.put("a1", "fff1");
        Map<String, String> map2 = new HashMap<>();
        map2.put("key", "B");
        map2.put("value", "Banana");
        map2.put("a1", "Banana");
        data.add(map1);
        data.add(map2);

        // binding datasource to TableView
        tableView.setItems(data);

        // create scene and show
        Scene scene = new Scene(tableView, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
