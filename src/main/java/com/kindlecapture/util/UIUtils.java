package com.kindlecapture.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * UI関連のユーティリティ
 */
public class UIUtils {
    
    /**
     * 情報ダイアログを表示
     * @param title タイトル
     * @param message メッセージ
     */
    public static void showInfoDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * エラーダイアログを表示
     * @param title タイトル
     * @param message メッセージ
     */
    public static void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * 警告ダイアログを表示
     * @param title タイトル
     * @param message メッセージ
     */
    public static void showWarningDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * 確認ダイアログを表示
     * @param title タイトル
     * @param message メッセージ
     * @return ユーザーがOKをクリックした場合はtrue
     */
    public static boolean showConfirmDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    /**
     * 例外情報を含むエラーダイアログを表示
     * @param title タイトル
     * @param message メッセージ
     * @param exception 例外
     */
    public static void showExceptionDialog(String title, String message, Throwable exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(exception.getMessage());
        
        // 詳細な例外情報をテキストエリアに表示
        javafx.scene.control.TextArea textArea = new javafx.scene.control.TextArea();
        textArea.setText(getStackTraceAsString(exception));
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        
        javafx.scene.layout.GridPane gridPane = new javafx.scene.layout.GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(new javafx.scene.control.Label("詳細:"), 0, 0);
        gridPane.add(textArea, 0, 1);
        
        alert.getDialogPane().setExpandableContent(gridPane);
        alert.showAndWait();
    }
    
    /**
     * スタックトレースを文字列として取得
     * @param throwable 例外
     * @return スタックトレース文字列
     */
    private static String getStackTraceAsString(Throwable throwable) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
    
    /**
     * 処理時間を人間が読みやすい形式で取得
     * @param milliseconds ミリ秒
     * @return 時間文字列
     */
    public static String formatDuration(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        if (hours > 0) {
            return String.format("%d時間%d分%d秒", hours, minutes % 60, seconds % 60);
        } else if (minutes > 0) {
            return String.format("%d分%d秒", minutes, seconds % 60);
        } else {
            return String.format("%d秒", seconds);
        }
    }
}
