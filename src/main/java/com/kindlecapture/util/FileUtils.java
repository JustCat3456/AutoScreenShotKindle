package com.kindlecapture.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ファイル操作ユーティリティ
 */
public class FileUtils {
    
    /**
     * ディレクトリが存在しない場合は作成
     * @param dirPath ディレクトリパス
     * @return 作成されたディレクトリのFile
     */
    public static File createDirectoryIfNotExists(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        return path.toFile();
    }
    
    /**
     * ファイルサイズを人間が読みやすい形式で取得
     * @param file ファイル
     * @return サイズ文字列
     */
    public static String getHumanReadableFileSize(File file) {
        long bytes = file.length();
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
    
    /**
     * 指定された拡張子のファイルリストを取得
     * @param directory ディレクトリ
     * @param extension 拡張子
     * @return ファイルリスト
     */
    public static List<File> getFilesByExtension(File directory, String extension) throws IOException {
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("指定されたパスはディレクトリではありません: " + directory);
        }
        
        return Files.walk(directory.toPath())
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().toLowerCase().endsWith(extension.toLowerCase()))
                .map(Path::toFile)
                .collect(Collectors.toList());
    }
    
    /**
     * ファイル名から拡張子を除去
     * @param filename ファイル名
     * @return 拡張子を除いたファイル名
     */
    public static String removeFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filename.substring(0, lastDotIndex);
        }
        return filename;
    }
    
    /**
     * 安全なファイル名を生成（無効な文字を除去）
     * @param filename 元のファイル名
     * @return 安全なファイル名
     */
    public static String sanitizeFilename(String filename) {
        return filename.replaceAll("[\\\\/:*?\"<>|]", "_");
    }
    
    /**
     * ファイルの存在チェックと重複回避
     * @param file 元のファイル
     * @return 重複しないファイル
     */
    public static File getUniqueFile(File file) {
        if (!file.exists()) {
            return file;
        }
        
        String name = removeFileExtension(file.getName());
        String extension = file.getName().substring(file.getName().lastIndexOf('.'));
        File parent = file.getParentFile();
        
        int counter = 1;
        File uniqueFile;
        do {
            String uniqueName = String.format("%s_%d%s", name, counter++, extension);
            uniqueFile = new File(parent, uniqueName);
        } while (uniqueFile.exists());
        
        return uniqueFile;
    }
    
    /**
     * ディレクトリの総ファイル数を取得
     * @param directory ディレクトリ
     * @return ファイル数
     */
    public static long countFiles(File directory) throws IOException {
        if (!directory.exists() || !directory.isDirectory()) {
            return 0;
        }
        
        return Files.walk(directory.toPath())
                .filter(Files::isRegularFile)
                .count();
    }
    
    /**
     * ディレクトリの総サイズを取得
     * @param directory ディレクトリ
     * @return 総サイズ（バイト）
     */
    public static long getDirectorySize(File directory) throws IOException {
        if (!directory.exists() || !directory.isDirectory()) {
            return 0;
        }
        
        return Files.walk(directory.toPath())
                .filter(Files::isRegularFile)
                .mapToLong(path -> {
                    try {
                        return Files.size(path);
                    } catch (IOException e) {
                        return 0;
                    }
                })
                .sum();
    }
}
