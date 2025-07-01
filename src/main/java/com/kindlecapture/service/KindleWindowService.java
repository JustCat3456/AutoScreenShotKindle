package com.kindlecapture.service;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.IntByReference;

import java.awt.event.KeyEvent;
import java.awt.Robot;
import java.awt.AWTException;

/**
 * Kindle Desktop ウィンドウの検出と操作を行うサービス
 */
public class KindleWindowService {
    
    private WinDef.HWND kindleWindowHandle;
    private Robot robot;
    
    public KindleWindowService() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Robotクラスの初期化に失敗しました", e);
        }
    }
    
    /**
     * Kindle Desktopのウィンドウを検索する
     * @return 見つかった場合はtrue
     */
    public boolean findKindleWindow() {
        kindleWindowHandle = null;
        
        // Kindle for PC のウィンドウタイトルパターン
        String[] kindleTitles = {
            "Kindle",
            "Kindle for PC",
            "Amazon Kindle"
        };
        
        for (String title : kindleTitles) {
            kindleWindowHandle = User32.INSTANCE.FindWindow(null, title);
            if (kindleWindowHandle != null) {
                break;
            }
        }
        
        // ウィンドウタイトルが部分一致する場合も検索
        if (kindleWindowHandle == null) {
            kindleWindowHandle = findWindowByPartialTitle("Kindle");
        }
        
        return kindleWindowHandle != null;
    }
    
    /**
     * 部分的なタイトルでウィンドウを検索
     */
    private WinDef.HWND findWindowByPartialTitle(String partialTitle) {
        final WinDef.HWND[] result = new WinDef.HWND[1];
        
        User32.INSTANCE.EnumWindows(new WinUser.WNDENUMPROC() {
            @Override
            public boolean callback(WinDef.HWND hwnd, com.sun.jna.Pointer data) {
                char[] windowText = new char[512];
                User32.INSTANCE.GetWindowText(hwnd, windowText, 512);
                String title = Native.toString(windowText);
                
                if (title.toLowerCase().contains(partialTitle.toLowerCase()) && 
                    User32.INSTANCE.IsWindowVisible(hwnd)) {
                    result[0] = hwnd;
                    return false; // 検索を停止
                }
                return true; // 検索を続行
            }
        }, null);
        
        return result[0];
    }
    
    /**
     * Kindleウィンドウをアクティブにする
     */
    public boolean activateKindleWindow() {
        if (kindleWindowHandle == null) {
            return false;
        }
        
        // ウィンドウを前面に表示
        User32.INSTANCE.SetForegroundWindow(kindleWindowHandle);
        User32.INSTANCE.ShowWindow(kindleWindowHandle, WinUser.SW_RESTORE);
        
        return true;
    }
    
    /**
     * Kindleウィンドウの位置とサイズを取得
     */
    public WinDef.RECT getKindleWindowRect() {
        if (kindleWindowHandle == null) {
            return null;
        }
        
        WinDef.RECT rect = new WinDef.RECT();
        User32.INSTANCE.GetWindowRect(kindleWindowHandle, rect);
        return rect;
    }
    
    /**
     * Kindleウィンドウのクライアント領域を取得
     */
    public WinDef.RECT getKindleClientRect() {
        if (kindleWindowHandle == null) {
            return null;
        }
        
        WinDef.RECT rect = new WinDef.RECT();
        User32.INSTANCE.GetClientRect(kindleWindowHandle, rect);
        return rect;
    }
    
    /**
     * 次のページに進むキーを送信
     */
    public void sendNextPageKey() {
        if (kindleWindowHandle == null) {
            return;
        }
        
        activateKindleWindow();
        
        try {
            Thread.sleep(100); // ウィンドウアクティベーションの待機
            
            // 右矢印キーまたはスペースキーでページ送り
            robot.keyPress(KeyEvent.VK_RIGHT);
            robot.keyRelease(KeyEvent.VK_RIGHT);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 前のページに戻るキーを送信
     */
    public void sendPrevPageKey() {
        if (kindleWindowHandle == null) {
            return;
        }
        
        activateKindleWindow();
        
        try {
            Thread.sleep(100);
            
            // 左矢印キーでページ戻り
            robot.keyPress(KeyEvent.VK_LEFT);
            robot.keyRelease(KeyEvent.VK_LEFT);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Kindleウィンドウハンドルを取得
     */
    public WinDef.HWND getKindleWindowHandle() {
        return kindleWindowHandle;
    }
    
    /**
     * Kindleウィンドウが存在するかチェック
     */
    public boolean isKindleWindowValid() {
        return kindleWindowHandle != null && User32.INSTANCE.IsWindow(kindleWindowHandle);
    }
}
