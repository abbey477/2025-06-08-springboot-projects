import java.io.*;
import java.net.*;
import java.nio.file.*;

public class SimplePDFDownloader {
    
    public static void main(String[] args) {
        String pdfUrl = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf";
        String directory = "downloads";
        
        try {
            // Download PDF
            String filePath = downloadPDF(pdfUrl, directory);
            
            // Validate PDF
            if (isValidPDF(filePath)) {
                System.out.println("SUCCESS: PDF downloaded and validated");
            } else {
                System.out.println("FAILED: Invalid PDF file");
            }
            
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
    
    /**
     * Download PDF to directory (with file validation)
     */
    public static String downloadPDF(String url, String directory) throws IOException {
        // Create directory
        Files.createDirectories(Paths.get(directory));
        
        // Get filename and create file path
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        String filePath = directory + "/" + fileName;
        
        // Create blank file and validate path
        createBlankFile(filePath);
        validateFilePath(filePath);
        
        // Download to the validated file
        try (InputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream out = new FileOutputStream(filePath)) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            long totalBytes = 0;
            
            System.out.print("Downloading");
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
                
                // Show progress every 64KB
                if (totalBytes % (8192 * 8) == 0) {
                    System.out.print(".");
                }
            }
            
            System.out.println(" Complete!");
            System.out.println("Downloaded: " + filePath + " (" + totalBytes + " bytes)");
        }
        
        return filePath;
    }
    
    /**
     * Create blank file in directory (overwrite if exists)
     */
    public static void createBlankFile(String filePath) throws IOException {
        File file = new File(filePath);
        
        // Delete existing file if it exists
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Deleted existing file: " + filePath);
            } else {
                throw new IOException("Could not delete existing file: " + filePath);
            }
        }
        
        // Create new blank file
        if (file.createNewFile()) {
            System.out.println("Created blank file: " + filePath);
        } else {
            throw new IOException("Could not create file: " + filePath);
        }
    }
    
    /**
     * Validate file path before writing
     */
    public static void validateFilePath(String filePath) throws IOException {
        File file = new File(filePath);
        
        // Check if file exists
        if (!file.exists()) {
            throw new IOException("File does not exist: " + filePath);
        }
        
        // Check if we can write to the file
        if (!file.canWrite()) {
            throw new IOException("Cannot write to file: " + filePath);
        }
        
        // Check if parent directory is writable
        File parentDir = file.getParentFile();
        if (!parentDir.canWrite()) {
            throw new IOException("Cannot write to directory: " + parentDir.getAbsolutePath());
        }
        
        System.out.println("File path validated: " + filePath);
    }
    
    /**
     * Validate PDF file
     */
    public static boolean isValidPDF(String filePath) throws IOException {
        File file = new File(filePath);
        
        // Check file exists and has content
        if (!file.exists() || file.length() == 0) {
            return false;
        }
        
        // Check PDF header
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] header = new byte[5];
            fis.read(header);
            String headerStr = new String(header);
            
            boolean isValid = headerStr.equals("%PDF-");
            System.out.println("File size: " + file.length() + " bytes");
            System.out.println("PDF header: " + (isValid ? "Valid" : "Invalid"));
            
            return isValid;
        }
    }
}