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
        System.out.println("KindleCaptureApp: Starting application...");
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                KindleCaptureApp.class.getResource("/fxml/main-view.fxml")
            );
            
            if (fxmlLoader.getLocation() == null) {
                System.err.println("ERROR: Cannot find main-view.fxml");
                return;
            }
            
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            
            // CSSスタイルシートを適用
            try {
                scene.getStylesheets().add(
                    Objects.requireNonNull(
                        KindleCaptureApp.class.getResource("/css/style.css")
                    ).toExternalForm()
                );
            } catch (Exception e) {
                System.err.println("WARNING: Could not load CSS stylesheet: " + e.getMessage());
            }
            
            stage.setTitle("Kindle Capture - スクリーンショット & PDF作成");
            stage.setScene(scene);
            stage.setMinWidth(600);
            stage.setMinHeight(400);
            
            // ウィンドウを閉じる際の処理
            stage.setOnCloseRequest(event -> {
                System.out.println("KindleCaptureApp: Application closing...");
            });
            
            stage.show();
            System.out.println("KindleCaptureApp: Application window displayed successfully");
            
        } catch (Exception e) {
            System.err.println("ERROR: Failed to start application: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        System.out.println("KindleCaptureApp: main() called");
        System.out.println("Java version: " + System.getProperty("java.version"));
        System.out.println("JavaFX version: " + System.getProperty("javafx.version"));
        
        try {
            launch(args);
        } catch (Exception e) {
            System.err.println("FATAL ERROR: Application launch failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
