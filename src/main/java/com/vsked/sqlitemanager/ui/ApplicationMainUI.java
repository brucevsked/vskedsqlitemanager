package com.vsked.sqlitemanager.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
		Button btn = new Button();
		btn.setText("welcome to sqlite manager'");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(log.isTraceEnabled()) {
					log.trace("You click the button");
				}
			}
		});

		StackPane root = new StackPane();
		root.getChildren().add(btn);

		Scene scene = new Scene(root, 300, 250);

		stage.setTitle("Vsked SQLite Manager");
		stage.setScene(scene);
		stage.show();
	}
}
