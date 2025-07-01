package com.kindlecapture.controller;

import com.kindlecapture.service.KindleWindowService;
import com.kindlecapture.service.ScreenshotService;
import com.kindlecapture.service.PdfService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.concurrent.Task;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private Button btnDetectKindle;
    @FXML private Button btnStartCapture;
    @FXML private Button btnStopCapture;
    @FXML private Button btnCreatePdf;
    @FXML private Button btnSelectFolder;
    @FXML private Label lblStatus;
    @FXML private Label lblFolderPath;
    @FXML private ProgressBar progressBar;
    @FXML private ListView<String> listViewImages;
    @FXML private ImageView imagePreview;
    @FXML private Slider sliderInterval;
    @FXML private Label lblInterval;
    @FXML private CheckBox checkAutoNext;

    private KindleWindowService kindleWindowService;
    private ScreenshotService screenshotService;
    private PdfService pdfService;
    private File selectedFolder;
    private List<String> capturedImages;
    private boolean isCapturing = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        kindleWindowService = new KindleWindowService();
        screenshotService = new ScreenshotService();
        pdfService = new PdfService();
        capturedImages = new ArrayList<>();

        setupUI();
        updateStatus("アプリケーションが起動しました");
    }

    private void setupUI() {
        btnStopCapture.setDisable(true);
        btnCreatePdf.setDisable(true);
        progressBar.setVisible(false);

        // インターバルスライダーの設定
        sliderInterval.setMin(1);
        sliderInterval.setMax(10);
        sliderInterval.setValue(3);
        lblInterval.setText("3秒");
        
        sliderInterval.valueProperty().addListener((obs, oldVal, newVal) -> {
            lblInterval.setText(String.format("%.0f秒", newVal.doubleValue()));
        });

        // リストビューの選択イベント
        listViewImages.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> previewImage(newVal)
        );
    }

    @FXML
    private void onDetectKindle() {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                updateMessage("Kindle Desktopを検索中...");
                return kindleWindowService.findKindleWindow();
            }

            @Override
            protected void succeeded() {
                if (getValue()) {
                    updateStatus("Kindle Desktopが見つかりました");
                    btnStartCapture.setDisable(false);
                } else {
                    updateStatus("Kindle Desktopが見つかりません");
                    showAlert("エラー", "Kindle Desktopアプリケーションが起動していません。\nKindle Desktopを起動してから再試行してください。");
                }
            }
        };

        progressBar.setVisible(true);
        progressBar.progressProperty().bind(task.progressProperty());
        lblStatus.textProperty().bind(task.messageProperty());

        new Thread(task).start();
    }

    @FXML
    private void onSelectFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("画像保存フォルダを選択");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        selectedFolder = directoryChooser.showDialog(btnSelectFolder.getScene().getWindow());
        if (selectedFolder != null) {
            lblFolderPath.setText(selectedFolder.getAbsolutePath());
            updateStatus("保存フォルダが選択されました: " + selectedFolder.getName());
        }
    }

    @FXML
    private void onStartCapture() {
        if (selectedFolder == null) {
            showAlert("エラー", "保存フォルダを選択してください。");
            return;
        }

        isCapturing = true;
        btnStartCapture.setDisable(true);
        btnStopCapture.setDisable(false);
        progressBar.setVisible(true);

        Task<Void> captureTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int interval = (int) sliderInterval.getValue() * 1000;
                final int[] count = {0}; // 配列を使用してfinal制約を回避

                while (isCapturing && !isCancelled()) {
                    try {
                        String filename = screenshotService.captureKindleWindow(
                            selectedFolder, 
                            kindleWindowService.getKindleWindowHandle()
                        );
                        
                        if (filename != null) {
                            count[0]++;
                            final String finalFilename = filename;
                            final int currentCount = count[0];
                            Platform.runLater(() -> {
                                capturedImages.add(finalFilename);
                                listViewImages.getItems().add(new File(finalFilename).getName());
                                updateStatus(String.format("スクリーンショット保存完了: %d枚", currentCount));
                            });

                            // 自動ページ送り機能
                            if (checkAutoNext.isSelected()) {
                                kindleWindowService.sendNextPageKey();
                            }
                        }

                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        break;
                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            updateStatus("エラー: " + e.getMessage());
                            stopCapture();
                        });
                        break;
                    }
                }
                return null;
            }
        };

        new Thread(captureTask).start();
    }

    @FXML
    private void onStopCapture() {
        stopCapture();
    }

    private void stopCapture() {
        isCapturing = false;
        btnStartCapture.setDisable(false);
        btnStopCapture.setDisable(true);
        progressBar.setVisible(false);
        
        if (!capturedImages.isEmpty()) {
            btnCreatePdf.setDisable(false);
            updateStatus(String.format("キャプチャ完了: %d枚の画像が保存されました", capturedImages.size()));
        }
    }

    @FXML
    private void onCreatePdf() {
        if (capturedImages.isEmpty()) {
            showAlert("エラー", "PDF作成用の画像がありません。");
            return;
        }

        Task<String> pdfTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                updateMessage("PDF作成中...");
                return pdfService.createPdfFromImages(capturedImages, selectedFolder);
            }

            @Override
            protected void succeeded() {
                String pdfPath = getValue();
                updateStatus("PDF作成完了: " + new File(pdfPath).getName());
                showAlert("完了", "PDFファイルが作成されました:\n" + pdfPath);
            }

            @Override
            protected void failed() {
                updateStatus("PDF作成に失敗しました");
                showAlert("エラー", "PDF作成中にエラーが発生しました:\n" + getException().getMessage());
            }
        };

        progressBar.setVisible(true);
        progressBar.progressProperty().bind(pdfTask.progressProperty());
        lblStatus.textProperty().bind(pdfTask.messageProperty());

        new Thread(pdfTask).start();
    }

    @FXML
    private void onClearImages() {
        capturedImages.clear();
        listViewImages.getItems().clear();
        imagePreview.setImage(null);
        btnCreatePdf.setDisable(true);
        updateStatus("画像リストをクリアしました");
    }

    private void previewImage(String filename) {
        if (filename != null && selectedFolder != null) {
            try {
                File imageFile = new File(selectedFolder, filename);
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    imagePreview.setImage(image);
                }
            } catch (Exception e) {
                updateStatus("画像のプレビューに失敗しました: " + e.getMessage());
            }
        }
    }

    private void updateStatus(String message) {
        Platform.runLater(() -> lblStatus.setText(message));
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
