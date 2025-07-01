package com.kindlecapture.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.Loader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * PDF作成サービス
 */
public class PdfService {
    
    private static final DateTimeFormatter TIMESTAMP_FORMAT = 
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
    /**
     * 画像リストからPDFを作成
     * @param imagePaths 画像ファイルパスのリスト
     * @param outputDir PDF保存先ディレクトリ
     * @return 作成されたPDFファイルのパス
     */
    public String createPdfFromImages(List<String> imagePaths, File outputDir) throws IOException {
        if (imagePaths == null || imagePaths.isEmpty()) {
            throw new IllegalArgumentException("画像リストが空です");
        }
        
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String pdfFilename = String.format("kindle_book_%s.pdf", timestamp);
        File pdfFile = new File(outputDir, pdfFilename);
        
        try (PDDocument document = new PDDocument()) {
            for (String imagePath : imagePaths) {
                addImageToDocument(document, imagePath);
            }
            
            document.save(pdfFile);
        }
        
        return pdfFile.getAbsolutePath();
    }
    
    /**
     * PDFドキュメントに画像を追加
     * @param document PDFドキュメント
     * @param imagePath 画像ファイルパス
     */
    private void addImageToDocument(PDDocument document, String imagePath) throws IOException {
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new IllegalArgumentException("画像ファイルが存在しません: " + imagePath);
        }
        
        // 画像をロード
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        PDImageXObject pdImage = PDImageXObject.createFromByteArray(
            document, 
            imageToByteArray(bufferedImage, "PNG"), 
            "image"
        );
        
        // ページサイズを画像に合わせて計算
        PDRectangle pageSize = calculatePageSize(bufferedImage);
        PDPage page = new PDPage(pageSize);
        document.addPage(page);
        
        // 画像をページに配置
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            // 画像のスケールを計算
            float scaleX = pageSize.getWidth() / bufferedImage.getWidth();
            float scaleY = pageSize.getHeight() / bufferedImage.getHeight();
            float scale = Math.min(scaleX, scaleY);
            
            float scaledWidth = bufferedImage.getWidth() * scale;
            float scaledHeight = bufferedImage.getHeight() * scale;
            
            // ページの中央に画像を配置
            float x = (pageSize.getWidth() - scaledWidth) / 2;
            float y = (pageSize.getHeight() - scaledHeight) / 2;
            
            contentStream.drawImage(pdImage, x, y, scaledWidth, scaledHeight);
        }
    }
    
    /**
     * 画像サイズに基づいてページサイズを計算
     * @param image 画像
     * @return PDRectangle ページサイズ
     */
    private PDRectangle calculatePageSize(BufferedImage image) {
        float width = image.getWidth();
        float height = image.getHeight();
        
        // 一般的な読書用のページサイズに調整
        float maxWidth = 595; // A4幅（ポイント）
        float maxHeight = 842; // A4高さ（ポイント）
        
        // アスペクト比を保持してスケール
        float scaleX = maxWidth / width;
        float scaleY = maxHeight / height;
        float scale = Math.min(scaleX, scaleY);
        
        return new PDRectangle(width * scale, height * scale);
    }
    
    /**
     * BufferedImageをバイト配列に変換
     * @param image 画像
     * @param format 画像フォーマット
     * @return バイト配列
     */
    private byte[] imageToByteArray(BufferedImage image, String format) throws IOException {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }
    
    /**
     * 既存のPDFに画像を追加
     * @param existingPdfPath 既存のPDFファイルパス
     * @param imagePaths 追加する画像パスのリスト
     * @param outputPath 出力PDFパス
     */
    public void addImagesToPdf(String existingPdfPath, List<String> imagePaths, String outputPath) 
            throws IOException {
        try (PDDocument document = Loader.loadPDF(new File(existingPdfPath))) {
            for (String imagePath : imagePaths) {
                addImageToDocument(document, imagePath);
            }
            
            document.save(outputPath);
        }
    }
    
    /**
     * PDFファイルを分割
     * @param pdfPath 元のPDFファイルパス
     * @param outputDir 分割されたファイルの保存先
     * @param pagesPerFile 1ファイルあたりのページ数
     */
    public void splitPdf(String pdfPath, File outputDir, int pagesPerFile) throws IOException {
        try (PDDocument document = Loader.loadPDF(new File(pdfPath))) {
            int totalPages = document.getNumberOfPages();
            int fileCount = 0;
            
            for (int i = 0; i < totalPages; i += pagesPerFile) {
                try (PDDocument splitDoc = new PDDocument()) {
                    int endPage = Math.min(i + pagesPerFile, totalPages);
                    
                    for (int j = i; j < endPage; j++) {
                        splitDoc.addPage(document.getPage(j));
                    }
                    
                    String filename = String.format("split_%03d.pdf", ++fileCount);
                    File outputFile = new File(outputDir, filename);
                    splitDoc.save(outputFile);
                }
            }
        }
    }
    
    /**
     * 複数のPDFファイルを結合
     * @param pdfPaths PDFファイルパスのリスト
     * @param outputPath 結合後のPDFファイルパス
     */
    public void mergePdfs(List<String> pdfPaths, String outputPath) throws IOException {
        try (PDDocument mergedDoc = new PDDocument()) {
            for (String pdfPath : pdfPaths) {
                try (PDDocument doc = Loader.loadPDF(new File(pdfPath))) {
                    for (int i = 0; i < doc.getNumberOfPages(); i++) {
                        mergedDoc.addPage(doc.getPage(i));
                    }
                }
            }
            
            mergedDoc.save(outputPath);
        }
    }
    
    /**
     * PDFファイルの情報を取得
     * @param pdfPath PDFファイルパス
     * @return PDFの情報文字列
     */
    public String getPdfInfo(String pdfPath) throws IOException {
        try (PDDocument document = Loader.loadPDF(new File(pdfPath))) {
            StringBuilder info = new StringBuilder();
            info.append("ページ数: ").append(document.getNumberOfPages()).append("\n");
            
            if (document.getDocumentInformation() != null) {
                var docInfo = document.getDocumentInformation();
                info.append("タイトル: ").append(docInfo.getTitle()).append("\n");
                info.append("作成者: ").append(docInfo.getAuthor()).append("\n");
                info.append("作成日: ").append(docInfo.getCreationDate()).append("\n");
            }
            
            return info.toString();
        }
    }
}
