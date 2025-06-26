package com.vsked.sqlitemanager.services;

import com.vsked.sqlitemanager.domain.VDatabaseFile;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationService.class);

    public VDatabaseFile openDataBaseFile(Stage stage){
        FileChooser fileChooser=new FileChooser();
        File file=fileChooser.showOpenDialog(stage);
        VDatabaseFile databaseFile=new VDatabaseFile(file);
        if(log.isTraceEnabled()){
            log.info(file.getAbsolutePath());
        }
        return databaseFile;
    }

    public static void exit(){
        Platform.exit();
    }
}
