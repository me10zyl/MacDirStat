package com.yilnz.macdirstat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MacDirStat extends Application {
	private static final Logger logger = LoggerFactory.getLogger(MacDirStat.class);
	public void start(Stage primaryStage) throws Exception {
		try {
			final Parent main = FXMLLoader.load(this.getClass().getResource("/macdirstat.fxml"));
			final Scene scene = new Scene(main);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			logger.error("primary error", e);
		}
	}
}
