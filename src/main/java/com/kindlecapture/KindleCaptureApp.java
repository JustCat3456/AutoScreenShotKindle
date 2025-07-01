package com.kindlecapture;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Kindle Capture Application
 * Kindle Desktopのスクリーンショットを撮影し、PDFを作成するアプリケーション
 */
public class KindleCaptureApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
            KindleCaptureApp.class.getResource("/fxml/main-view.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        
        // CSSスタイルシートを適用
        scene.getStylesheets().add(
            Objects.requireNonNull(
                KindleCaptureApp.class.getResource("/css/style.css")
            ).toExternalForm()
        );
        
        stage.setTitle("Kindle Capture - スクリーンショット & PDF作成");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
