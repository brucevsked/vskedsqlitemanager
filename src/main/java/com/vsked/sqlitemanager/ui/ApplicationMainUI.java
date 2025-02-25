package com.vsked.sqlitemanager.ui;

import com.vsked.sqlitemanager.domain.I18N;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;


public class ApplicationMainUI extends Application {

	private static final Logger log = LoggerFactory.getLogger(ApplicationMainUI.class);
	
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

		Menu fileMenu = I18N.menuForKey("menu.file");
		Menu fileOpenMenu = I18N.menuForKey("menuItem.open");
		Menu fileExitMenu = I18N.menuForKey("menuItem.exit");

		fileOpenMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(log.isTraceEnabled()) {
					log.trace("You click the file open menu");
				}

			}
		});

		fileExitMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(log.isTraceEnabled()) {
					log.trace("You click the file exit menu");
				}
				Platform.exit();
			}
		});

		Menu languageMenu = I18N.menuForKey("menu.language");
		Menu englishMenu = I18N.menuForKey("menu.english");
		Menu chineseMenu = I18N.menuForKey("menu.chinese");

		englishMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(log.isTraceEnabled()) {
					log.trace("You click the english");
				}
				switchLanguage(Locale.ENGLISH);
			}
		});

		chineseMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(log.isTraceEnabled()) {
					log.trace("You click the chinese");
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


		// at the top two buttons
		HBox hbox = new HBox(menuBar);
		hbox.setPadding(new Insets(5, 5, 5, 5));
		hbox.setSpacing(5);

		content.setTop(hbox);


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
