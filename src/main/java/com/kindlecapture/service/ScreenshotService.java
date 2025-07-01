package com.kindlecapture.service;

import com.sun.jna.platform.win32.WinDef;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * スクリーンショット撮影サービス
 */
public class ScreenshotService {
    
    private Robot robot;
    private static final DateTimeFormatter TIMESTAMP_FORMAT = 
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");
    
    public ScreenshotService() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Robotクラスの初期化に失敗しました", e);
        }
    }
    
    /**
     * Kindleウィンドウのスクリーンショットを撮影
     * @param outputDir 保存先ディレクトリ
     * @param windowHandle Kindleウィンドウハンドル
     * @return 保存されたファイルのパス
     */
    public String captureKindleWindow(File outputDir, WinDef.HWND windowHandle) throws IOException {
        if (windowHandle == null) {
            throw new IllegalArgumentException("ウィンドウハンドルがnullです");
        }
        
        // ウィンドウの座標を取得
        WinDef.RECT rect = new WinDef.RECT();
        com.sun.jna.platform.win32.User32.INSTANCE.GetWindowRect(windowHandle, rect);
        
        // ウィンドウをアクティブにして前面に表示
        com.sun.jna.platform.win32.User32.INSTANCE.SetForegroundWindow(windowHandle);
        com.sun.jna.platform.win32.User32.INSTANCE.ShowWindow(windowHandle, 
            com.sun.jna.platform.win32.WinUser.SW_RESTORE);
        
        try {
            Thread.sleep(500); // ウィンドウの描画完了を待機
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // スクリーンショット領域を定義
        Rectangle captureRect = new Rectangle(
            rect.left, rect.top, 
            rect.right - rect.left, rect.bottom - rect.top
        );
        
        // スクリーンショットを撮影
        BufferedImage screenshot = robot.createScreenCapture(captureRect);
        
        // ファイル名を生成（タイムスタンプ付き）
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String filename = String.format("kindle_page_%s.png", timestamp);
        File outputFile = new File(outputDir, filename);
        
        // 画像を保存
        ImageIO.write(screenshot, "PNG", outputFile);
        
        return outputFile.getAbsolutePath();
    }
    
    /**
     * 画面全体のスクリーンショットを撮影
     * @param outputDir 保存先ディレクトリ
     * @return 保存されたファイルのパス
     */
    public String captureFullScreen(File outputDir) throws IOException {
        // 画面サイズを取得
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle captureRect = new Rectangle(screenSize);
        
        // スクリーンショットを撮影
        BufferedImage screenshot = robot.createScreenCapture(captureRect);
        
        // ファイル名を生成
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String filename = String.format("fullscreen_%s.png", timestamp);
        File outputFile = new File(outputDir, filename);
        
        // 画像を保存
        ImageIO.write(screenshot, "PNG", outputFile);
        
        return outputFile.getAbsolutePath();
    }
    
    /**
     * 指定された領域のスクリーンショットを撮影
     * @param outputDir 保存先ディレクトリ
     * @param x X座標
     * @param y Y座標
     * @param width 幅
     * @param height 高さ
     * @return 保存されたファイルのパス
     */
    public String captureRegion(File outputDir, int x, int y, int width, int height) throws IOException {
        Rectangle captureRect = new Rectangle(x, y, width, height);
        
        // スクリーンショットを撮影
        BufferedImage screenshot = robot.createScreenCapture(captureRect);
        
        // ファイル名を生成
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String filename = String.format("region_%s.png", timestamp);
        File outputFile = new File(outputDir, filename);
        
        // 画像を保存
        ImageIO.write(screenshot, "PNG", outputFile);
        
        return outputFile.getAbsolutePath();
    }
    
    /**
     * 画像をトリミングして不要な部分を除去
     * @param imagePath 元の画像パス
     * @param cropMargin 除去するマージン（ピクセル）
     * @return トリミング後の画像パス
     */
    public String cropImage(String imagePath, int cropMargin) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(imagePath));
        
        int newWidth = Math.max(1, originalImage.getWidth() - (cropMargin * 2));
        int newHeight = Math.max(1, originalImage.getHeight() - (cropMargin * 2));
        
        BufferedImage croppedImage = originalImage.getSubimage(
            cropMargin, cropMargin, newWidth, newHeight
        );
        
        // 新しいファイル名を生成
        File originalFile = new File(imagePath);
        String nameWithoutExt = originalFile.getName().replaceFirst("[.][^.]+$", "");
        String extension = originalFile.getName().substring(originalFile.getName().lastIndexOf('.'));
        String croppedFilename = nameWithoutExt + "_cropped" + extension;
        File croppedFile = new File(originalFile.getParent(), croppedFilename);
        
        // トリミング後の画像を保存
        ImageIO.write(croppedImage, "PNG", croppedFile);
        
        return croppedFile.getAbsolutePath();
    }
    
    /**
     * 画像のサイズを変更
     * @param imagePath 元の画像パス
     * @param maxWidth 最大幅
     * @param maxHeight 最大高さ
     * @return リサイズ後の画像パス
     */
    public String resizeImage(String imagePath, int maxWidth, int maxHeight) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(imagePath));
        
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        // アスペクト比を保持してリサイズサイズを計算
        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;
        double ratio = Math.min(widthRatio, heightRatio);
        
        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);
        
        // リサイズ実行
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
        
        // 新しいファイル名を生成
        File originalFile = new File(imagePath);
        String nameWithoutExt = originalFile.getName().replaceFirst("[.][^.]+$", "");
        String extension = originalFile.getName().substring(originalFile.getName().lastIndexOf('.'));
        String resizedFilename = nameWithoutExt + "_resized" + extension;
        File resizedFile = new File(originalFile.getParent(), resizedFilename);
        
        // リサイズ後の画像を保存
        ImageIO.write(resizedImage, "PNG", resizedFile);
        
        return resizedFile.getAbsolutePath();
    }
}
