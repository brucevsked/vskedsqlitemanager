package com.vsked.sqlitemanager.ui;

import com.vsked.sqlitemanager.domain.I18N;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
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

		// at the top two buttons
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(5, 5, 5, 5));
		hbox.setSpacing(5);

		Button btnEn = I18N.buttonForKey("button.english");
		btnEn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(log.isTraceEnabled()) {
					log.trace("You click the english");
				}
				switchLanguage(Locale.ENGLISH);
			}
		});

		hbox.getChildren().add(btnEn);

		Button btnCn = I18N.buttonForKey("button.chinese");
		btnCn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(log.isTraceEnabled()) {
					log.trace("You click the chinese");
				}
				switchLanguage(Locale.CHINESE);
			}
		});

		hbox.getChildren().add(btnCn);

		content.setTop(hbox);


		Scene scene = new Scene(content, 500, 500);

		stage.titleProperty().bind(I18N.createStringBinding("window.title"));
		stage.setScene(scene);
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
