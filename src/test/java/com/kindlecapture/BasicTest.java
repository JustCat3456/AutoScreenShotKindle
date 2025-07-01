package com.kindlecapture;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 基本的なテストクラス
 * GitHub Actionsでのビルドエラーを回避するため
 */
public class BasicTest {

    @Test
    public void testApplicationMainClassExists() {
        assertDoesNotThrow(() -> {
            Class.forName("com.kindlecapture.KindleCaptureApp");
        }, "KindleCaptureApp class should exist");
    }

    @Test
    public void testControllerClassExists() {
        assertDoesNotThrow(() -> {
            Class.forName("com.kindlecapture.controller.MainController");
        }, "MainController class should exist");
    }

    @Test
    public void testServiceClassesExist() {
        assertDoesNotThrow(() -> {
            Class.forName("com.kindlecapture.service.KindleWindowService");
            Class.forName("com.kindlecapture.service.ScreenshotService");
            Class.forName("com.kindlecapture.service.PdfService");
        }, "Service classes should exist");
    }

    @Test
    public void testUtilityClassesExist() {
        assertDoesNotThrow(() -> {
            Class.forName("com.kindlecapture.util.FileUtils");
            Class.forName("com.kindlecapture.util.UIUtils");
        }, "Utility classes should exist");
    }
}
