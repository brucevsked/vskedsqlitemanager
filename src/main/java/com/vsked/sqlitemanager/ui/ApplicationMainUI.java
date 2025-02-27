package com.vsked.sqlitemanager.ui;

import com.vsked.sqlitemanager.domain.I18N;
import com.vsked.sqlitemanager.domain.VConnection;
import com.vsked.sqlitemanager.domain.VDatabaseFile;
import com.vsked.sqlitemanager.domain.VTable;
import com.vsked.sqlitemanager.domain.VTableList;
import com.vsked.sqlitemanager.services.ApplicationService;
import com.vsked.sqlitemanager.services.TableService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;


public class ApplicationMainUI extends Application {

	private static final Logger log = LoggerFactory.getLogger(ApplicationMainUI.class);

	ApplicationService applicationService=new ApplicationService();
	
	public static void main(String[] args) {
		if(log.isTraceEnabled()) {
			log.trace("welcome to vsked SQLite manager");
		}
		launch(args);
		
	}

	@Override
	public void start(Stage stage) throws Exception {

		// create content
		BorderPane content = new BorderPane();

		MenuBar menuBar = new MenuBar();
		menuBar.setMinWidth(stage.getMaxWidth());

		Menu fileMenu = I18N.menuForKey("menu.file");
		Menu fileOpenMenu = I18N.menuForKey("menuItem.open");
		Menu fileExitMenu = I18N.menuForKey("menuItem.exit");


		fileExitMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(log.isTraceEnabled()) {
					log.trace("You click the file exit menu from menu Item");
				}
				applicationService.exit();
			}
		});

		Menu languageMenu = I18N.menuForKey("menu.language");
		Menu englishMenu = I18N.menuForKey("menu.english");
		Menu chineseMenu = I18N.menuForKey("menu.chinese");

		englishMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(log.isTraceEnabled()) {
					log.trace("You click the english menu from menu Item");
				}
				switchLanguage(Locale.ENGLISH);
			}
		});

		chineseMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(log.isTraceEnabled()) {
					log.trace("You click the chinese menu from menu Item");
				}
				switchLanguage(Locale.CHINESE);
			}
		});

		fileMenu.getItems().add(fileOpenMenu);
		fileMenu.getItems().add(fileExitMenu);

		languageMenu.getItems().add(englishMenu);
		languageMenu.getItems().add(chineseMenu);

		menuBar.getMenus().add(fileMenu);
		menuBar.getMenus().add(languageMenu);

		ToolBar toolBar=new ToolBar();
		Button openBt=I18N.buttonForKey("button.open");
		Button queryBt=I18N.buttonForKey("button.newQuery");
		Button englishBt=I18N.buttonForKey("button.english");
		Button chineseBt=I18N.buttonForKey("button.chinese");
		Button exitBt=I18N.buttonForKey("button.exit");


		englishBt.setOnAction(englishMenu.getOnAction());

		chineseBt.setOnAction(chineseMenu.getOnAction());

		exitBt.setOnAction(fileExitMenu.getOnAction());

		toolBar.getItems().add(openBt);
		toolBar.getItems().add(queryBt);
		toolBar.getItems().add(englishBt);
		toolBar.getItems().add(chineseBt);
		toolBar.getItems().add(exitBt);

		toolBar.setMinWidth(stage.getMaxWidth());

		GridPane topGridPane=new GridPane();
		topGridPane.add(menuBar,0,0);
		topGridPane.add(toolBar,0,1);

		content.setTop(topGridPane);

		GridPane leftGridPane=new GridPane();
		TreeView<String> systemViewTree=new TreeView<>();
		TreeItem<String> rootItem=I18N.treeItemForKey("tree.system");

		TreeItem<String> tablesItem=I18N.treeItemForKey("tree.tables");
		TreeItem<String> viewsItem=I18N.treeItemForKey("tree.views");
		TreeItem<String> indexesItem=I18N.treeItemForKey("tree.indexes");
		TreeItem<String> triggersItem=I18N.treeItemForKey("tree.triggers");
		TreeItem<String> queriesItem=I18N.treeItemForKey("tree.queries");
		TreeItem<String> backupItem=I18N.treeItemForKey("tree.backup");

		rootItem.getChildren().add(tablesItem);
		rootItem.getChildren().add(viewsItem);
		rootItem.getChildren().add(indexesItem);
		rootItem.getChildren().add(triggersItem);
		rootItem.getChildren().add(queriesItem);
		rootItem.getChildren().add(backupItem);

		systemViewTree.setRoot(rootItem);

		systemViewTree.setShowRoot(false);


		leftGridPane.add(systemViewTree,0,0);
		content.setLeft(leftGridPane);

		fileOpenMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(log.isTraceEnabled()) {
					log.trace("You click the file open menu from menu Item");
				}
				VDatabaseFile databaseFile=applicationService.openDataBaseFile(stage);
				VConnection connection=new VConnection(databaseFile);
				TableService tableService=new TableService();
				VTableList vTableList=tableService.getTables(connection);
				List<VTable> tableList=vTableList.getTables();
				for(VTable table:tableList){
					TreeItem<String> tablesItemNode=new TreeItem<>(table.getTableName().getTableName());
					tablesItem.getChildren().add(tablesItemNode);
				}

			}
		});

		openBt.setOnAction(fileOpenMenu.getOnAction());


		Scene scene = new Scene(content, 500, 500);

		stage.titleProperty().bind(I18N.createStringBinding("window.title"));
		stage.setScene(scene);

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
			}
		});



		stage.show();
	}

	/**
	 * sets the given Locale in the I18N class and keeps count of the number of switches.
	 *
	 * @param locale
	 *         the new local to set
	 */
	private void switchLanguage(Locale locale) {
		I18N.setLocale(locale);
	}
}
