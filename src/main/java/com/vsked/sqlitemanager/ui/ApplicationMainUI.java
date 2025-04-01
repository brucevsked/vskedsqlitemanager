package com.vsked.sqlitemanager.ui;

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
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ApplicationMainUI extends Application {

	private static final Logger log = LoggerFactory.getLogger(ApplicationMainUI.class);

	private Scene scene;
	private int globalQueryTabCount=0;
	private TextArea currentQueryTextArea;


	private ApplicationService applicationService=new ApplicationService();
	private DatabaseService databaseService;

	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	public int getGlobalQueryTabCount() {
		this.globalQueryTabCount=this.globalQueryTabCount+1;
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
		if(log.isTraceEnabled()) {
			log.trace("welcome to vsked SQLite manager");
		}
		launch(args);
		
	}


	@Override
	public void start(Stage stage) throws Exception {

		// create content
		BorderPane borderPane = new BorderPane();


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
		Button newQueryBt=I18N.buttonForKey("button.newQuery");
		Button englishBt=I18N.buttonForKey("button.english");
		Button chineseBt=I18N.buttonForKey("button.chinese");
		Button exitBt=I18N.buttonForKey("button.exit");

		englishBt.setOnAction(englishMenu.getOnAction());

		chineseBt.setOnAction(chineseMenu.getOnAction());

		exitBt.setOnAction(fileExitMenu.getOnAction());

		toolBar.getItems().add(openBt);
		toolBar.getItems().add(newQueryBt);
		toolBar.getItems().add(englishBt);
		toolBar.getItems().add(chineseBt);
		toolBar.getItems().add(exitBt);

		toolBar.setMinWidth(stage.getMaxWidth());

		GridPane topGridPane=new GridPane();
		topGridPane.add(menuBar,0,0);
		topGridPane.add(toolBar,0,1);

		borderPane.setTop(topGridPane);

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


		GridPane centerGridPane=new GridPane();
		centerGridPane.setMinWidth(stage.getMaxWidth()-leftGridPane.getMaxWidth());
		centerGridPane.setMinHeight(leftGridPane.getMinHeight());


		TabPane centerTabPane=new TabPane();

		//TODO when change query set current text area component

		newQueryBt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if(log.isTraceEnabled()) {
					log.trace("You click the new query button from toolbar");
				}
				if(getDatabaseService()==null){
					Alert alert = I18N.alertForKey(Alert.AlertType.INFORMATION,"alert.notExitOpenDatabaseFile.title","alert.notExitOpenDatabaseFile.headerText","alert.notExitOpenDatabaseFile.contentText");
					alert.show();
					return;
				}

				int queryCount=getGlobalQueryTabCount();
                String queryTabId="Query"+queryCount;
				Tab tab=new Tab(queryTabId);
				tab.setId(queryTabId);
				GridPane queryTabGridPane=new GridPane();
				Button runQueryButton=I18N.buttonForKey("button.runQuery");
				Button stopQueryButton=I18N.buttonForKey("button.stopQuery");
				queryTabGridPane.add(runQueryButton,0,0);
				queryTabGridPane.add(stopQueryButton,1,0);


				runQueryButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
						if(log.isTraceEnabled()) {
							log.trace("You click run query button from query"+queryCount);
						}
					}
				});

				stopQueryButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
						if(log.isTraceEnabled()) {
							log.trace("You click stop query button from query grid");
						}
					}
				});

				TextArea ta=new TextArea();
				// create a menu
				ContextMenu contextMenu = new ContextMenu();

				// create menuitems
				MenuItem runCurrentSelectedSqlMenuItem = I18N.menuItemForKey("queryPanel.textarea.contextMenu.runCurrentSelectedSql");
				MenuItem cutMenuItem = I18N.menuItemForKey("queryPanel.textarea.contextMenu.cut");
				MenuItem copyMenuItem = I18N.menuItemForKey("queryPanel.textarea.contextMenu.copy");
				MenuItem pasteMenuItem = I18N.menuItemForKey("queryPanel.textarea.contextMenu.paste");
				MenuItem selectAllMenuItem = I18N.menuItemForKey("queryPanel.textarea.contextMenu.selectAll");

				cutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent actionEvent) {
						if(log.isTraceEnabled()){
							log.trace("you click cut menu from contextMenu");
						}

					}
				});

				copyMenuItem.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent actionEvent) {
						if(log.isTraceEnabled()){
							log.trace("you click copy menu from contextMenu");
						}

					}
				});

				pasteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent actionEvent) {
						if(log.isTraceEnabled()){
							log.trace("you click paste menu from contextMenu");
						}

					}
				});

				selectAllMenuItem.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent actionEvent) {
						if(log.isTraceEnabled()){
							log.trace("you click select all menu from contextMenu");
						}

						if(log.isDebugEnabled()){
							log.debug("{}",actionEvent.getSource());
						}

						MenuItem tempMenu= (MenuItem) actionEvent.getSource();
						getCurrentQueryTextArea().selectAll();

					}
				});

				// add menu items to menu
				contextMenu.getItems().add(runCurrentSelectedSqlMenuItem);
				contextMenu.getItems().add(cutMenuItem);
				contextMenu.getItems().add(copyMenuItem);
				contextMenu.getItems().add(pasteMenuItem);
				contextMenu.getItems().add(selectAllMenuItem);



				ta.setContextMenu(contextMenu);

				queryTabGridPane.add(ta,0,1);

				tab.setContent(queryTabGridPane);
				centerTabPane.getTabs().add(tab);
				centerTabPane.getSelectionModel().select(tab);
				ta.requestFocus();
				setCurrentQueryTextArea(ta);
			}
		});

		centerTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
					if (newTab != null) {
						List<TreeItem<String>> tableItems=tablesItem.getChildren();
						for(TreeItem<String> item:tableItems){
							if(item.getValue().equals(newTab.getText())){
								systemViewTree.getSelectionModel().select(item);
							}
						}
					}
		});

		centerGridPane.add(centerTabPane,0,0);

		centerGridPane.setStyle("-fx-background-color: #0000ff");
		centerGridPane.setVisible(true);
		borderPane.setCenter(centerGridPane);
		borderPane.setVisible(true);


		systemViewTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem> paramObservableValue, TreeItem paramT1, TreeItem selectedItem) {
				if(log.isTraceEnabled()) {
					log.trace("You click the tree Item from system tree view");
				}
				if(log.isDebugEnabled()){
					log.debug(selectedItem+"");
				}

				List<Tab> oldTabList=centerTabPane.getTabs();
				boolean isExistTab=false;

				for(Tab tab:oldTabList){
                    if(log.isDebugEnabled()){
						log.debug(tab.getId()+"|"+selectedItem.getValue());
					}

					if(tab.getId().equals(selectedItem.getValue())){
						isExistTab=true;
						centerTabPane.getSelectionModel().select(tab);
						break;
					}
				}
                //when you click table sub node
				if(selectedItem.getParent().getValue().equals(tablesItem.getValue())){
					//when not exist table tab
					if(isExistTab==false){

						Tab tab=new Tab(selectedItem.getValue().toString());
						GridPane tableGridPane=new GridPane();

						VTableName currentTableName=new VTableName(selectedItem.getValue().toString());

                        VPage page=new VPage(new VPageIndex(0),new VPageSize(10));

						Pagination pagination=new Pagination();

						TableService tableService=new TableService(getDatabaseService());
						int pageCount= tableService.getTablePageCount(currentTableName,page);
						pagination.setPageCount(pageCount);
						pagination.setPageFactory(new Callback<Integer, Node>() {
							@Override
							public Node call(Integer pageIndex) {
								log.info("PageIndex:"+pageIndex);

								return createPage(new VPage(new VPageIndex(pageIndex),page.getPageSize()),currentTableName);
							}
						});


						// pagination width change with window
//						getScene().widthProperty().addListener(new ChangeListener<Number>() {
//							@Override
//							public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//								pagination.setPrefWidth(newValue.doubleValue());
//							}
//						});

						tableGridPane.add(pagination,0,1);//column,row


						tab.setContent(tableGridPane);
						tab.setClosable(true);
						tab.setId(selectedItem.getValue()+"");

						log.info("tab id is:{}",selectedItem.getValue());

						centerTabPane.getTabs().add(tab);

						centerTabPane.getSelectionModel().select(tab);
					}

				}

			}
		});

		systemViewTree.setRoot(rootItem);

		systemViewTree.setShowRoot(false);


		leftGridPane.add(systemViewTree,0,0);
		borderPane.setLeft(leftGridPane);

		fileOpenMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(log.isTraceEnabled()) {
					log.trace("You click the file open menu from menu Item");
				}
				VDatabaseFile databaseFile=applicationService.openDataBaseFile(stage);

				setDatabaseService(new DatabaseService(databaseFile));

				TableService tableService=new TableService(getDatabaseService());
				VTableList vTableList=tableService.getTables();
				List<VTable> tableList=vTableList.getTables();
				for(VTable table:tableList){
					TreeItem<String> tablesItemNode=new TreeItem<>(table.getTableName().getTableName());
					tablesItem.getChildren().add(tablesItemNode);
				}

				tablesItem.setExpanded(true);

			}
		});

		openBt.setOnAction(fileOpenMenu.getOnAction());

		scene = new Scene(borderPane, 1000, 500);
		setScene(scene);


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


	private TableView<Map> createPage(VPage page,VTableName currentTableName) {

		TableView<Map> tableView=new TableView<>();

		TableService tableService=new TableService(getDatabaseService());
		List<VTableColumn> tableColumns=tableService.getColumns(currentTableName);
		List<TableColumnView> tableViewColumns=tableService.getTableViewColumns(tableColumns);
		for(TableColumnView tableColumnView :tableViewColumns){
			TableColumn<Map, String> keyColumn = new TableColumn<>(tableColumnView.getName());
			keyColumn.setCellValueFactory(new MapValueFactory<>(tableColumnView.getName()));
			tableView.getColumns().add(keyColumn);
		}

		VPage vTablePage =page;

		vTablePage=tableService.getData(currentTableName,vTablePage);
		List<Map> tableDatas=vTablePage.getData();
		ObservableList<Map> dataList=FXCollections.observableArrayList(tableDatas);

		tableView.setItems(dataList);
		return tableView;
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
