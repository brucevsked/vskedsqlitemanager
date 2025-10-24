package com.vsked.sqlitemanager.ui;

import com.vsked.sqlitemanager.domain.VTableName;
import com.vsked.sqlitemanager.services.I18N;
import javafx.scene.control.*;

public class TreeViewManager {

    private ApplicationMainUI applicationMainUI;

    public TreeViewManager(ApplicationMainUI applicationMainUI) {
        this.applicationMainUI = applicationMainUI;
    }

    public TreeView<String> createTreeView() {
        TreeView<String> systemViewTree = new TreeView<>();

        TreeItem<String> rootItem = I18N.treeItemForKey("tree.system");

        TreeItem<String> tablesItem = I18N.treeItemForKey("tree.tables");
        TreeItem<String> viewsItem = I18N.treeItemForKey("tree.views");
        TreeItem<String> indexesItem = I18N.treeItemForKey("tree.indexes");
        TreeItem<String> triggersItem = I18N.treeItemForKey("tree.triggers");
        TreeItem<String> queriesItem = I18N.treeItemForKey("tree.queries");
        TreeItem<String> backupItem = I18N.treeItemForKey("tree.backup");

        rootItem.getChildren().addAll(tablesItem, viewsItem, indexesItem, triggersItem, queriesItem, backupItem);
        systemViewTree.setRoot(rootItem);
        systemViewTree.setShowRoot(false);

        setupContextMenu(systemViewTree);

        return systemViewTree;
    }

    public void setupContextMenu(TreeView<String> treeView) {
        ContextMenu tableContextMenu = new ContextMenu();

        MenuItem addTableMenuItem = I18N.menuItemForKey("tree.contextMenu.addTable");
        addTableMenuItem.setOnAction(event -> applicationMainUI.showAddTableDialog());
        tableContextMenu.getItems().add(addTableMenuItem);

        MenuItem renameTableMenuItem = I18N.menuItemForKey("tree.contextMenu.renameTable");
        renameTableMenuItem.setOnAction(event -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null && selectedItem.getParent().getValue().equals(I18N.get("tree.tables"))) {
                VTableName oldTableName = new VTableName(selectedItem.getValue());
                applicationMainUI.showRenameTableDialog(oldTableName, selectedItem);
            }
        });
        tableContextMenu.getItems().add(renameTableMenuItem);

        MenuItem deleteTableMenuItem = I18N.menuItemForKey("tree.contextMenu.deleteTable");
        deleteTableMenuItem.setOnAction(event -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null && selectedItem.getParent().getValue().equals(I18N.get("tree.tables"))) {
                applicationMainUI.showDeleteTableDialog(new VTableName(selectedItem.getValue()), selectedItem);
            }
        });
        tableContextMenu.getItems().add(deleteTableMenuItem);

        MenuItem editTableStructureMenuItem = I18N.menuItemForKey("tree.contextMenu.editTable");
        editTableStructureMenuItem.setOnAction(event -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null && selectedItem.getParent().getValue().equals(I18N.get("tree.tables"))) {
                VTableName tableName = new VTableName(selectedItem.getValue());
                applicationMainUI.getTableStructureManager().showEditTableStructureDialog(tableName);
            }
        });
        tableContextMenu.getItems().add(editTableStructureMenuItem);

        treeView.setContextMenu(tableContextMenu);
    }


}

