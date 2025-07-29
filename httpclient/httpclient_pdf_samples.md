# Apache HttpClient PDF Download Code Samples

> **File: httpclient-pdf-download-samples.md**

This document contains multiple complete code samples for downloading PDF files using Apache HttpClient with proxy settings. All methods use the GET method and return the response as InputStream without processing it.

## Maven Dependencies

First, add the required dependencies to your `pom.xml`:

```xml
<!-- For HttpClient 5.x (Recommended) -->
<dependency>
    <groupId>org.apache.httpcomponents.client5</groupId>
    <artifactId>httpclient5</artifactId>
    <version>5.2.1</version>
</dependency>

<!-- For HttpClient 4.x (Legacy) -->
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.14</version>
</dependency>
```

## Sample 1: Basic HttpClient 5.x with Proxy

```java
// Sample 1: Basic HttpClient 5.x with Proxy
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;

import java.io.IOException;
import java.io.InputStream;

/**
 * Basic PDF downloader using Apache HttpClient 5.x with proxy support
 */
public class PdfDownloader1 {
    
    /**
     * Downloads a PDF file from the given URL using a proxy server
     * 
     * @param pdfUrl The URL of the PDF file to download
     * @param proxyHost The hostname/IP of the proxy server
     * @param proxyPort The port number of the proxy server
     */
    public void downloadPdf(String pdfUrl, String proxyHost, int proxyPort) {
        // Create proxy host configuration
        HttpHost proxy = new HttpHost(proxyHost, proxyPort);
        
        // Set up proxy route planner to route all requests through the proxy
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        
        // Configure request timeouts to prevent hanging connections
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(5000, java.util.concurrent.TimeUnit.MILLISECONDS)  // Time to get connection from pool
                .setResponseTimeout(30000, java.util.concurrent.TimeUnit.MILLISECONDS)          // Time to wait for response
                .build();
        
        // Create HttpClient with proxy configuration and timeouts
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setRoutePlanner(routePlanner)      // Use proxy for all requests
                .setDefaultRequestConfig(config)    // Apply timeout settings
                .build()) {
            
            // Create GET request for the PDF URL
            HttpGet httpGet = new HttpGet(pdfUrl);
            
            // Set request headers to specify we want PDF content
            httpGet.setHeader("Accept", "application/pdf");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (PDF Downloader)");
            
            // Execute the request and get response
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                // Check if request was successful (HTTP 200)
                if (response.getCode() == 200) {
                    // Get the response content as InputStream
                    InputStream inputStream = response.getEntity().getContent();
                    
                    // TODO: Process your inputStream here
                    // Example: Save to file, parse content, etc.
                    
                    System.out.println("PDF downloaded successfully. Content-Length: " + 
                        response.getEntity().getContentLength());
                } else {
                    // Handle non-200 HTTP status codes
                    System.err.println("Failed to download PDF. Status: " + response.getCode());
                }
            }
        } catch (IOException e) {
            // Handle network errors, connection failures, etc.
            System.err.println("Error downloading PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

## Sample 2: HttpClient with Proxy Authentication

```java
// Sample 2: HttpClient with Proxy Authentication
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;

/**
 * PDF downloader with proxy authentication support
 */
public class PdfDownloader2 {
    
    /**
     * Downloads a PDF file using a proxy server that requires authentication
     * 
     * @param pdfUrl The URL of the PDF file to download
     * @param proxyHost The hostname/IP of the proxy server
     * @param proxyPort The port number of the proxy server
     * @param proxyUsername Username for proxy authentication
     * @param proxyPassword Password for proxy authentication
     */
    public void downloadPdfWithAuth(String pdfUrl, String proxyHost, int proxyPort, 
                                   String proxyUsername, String proxyPassword) {
        
        // Create credentials provider for proxy authentication
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        
        // Set credentials for the specific proxy host and port
        credentialsProvider.setCredentials(
            new AuthScope(proxyHost, proxyPort),                                    // Scope: which host/port needs auth
            new UsernamePasswordCredentials(proxyUsername, proxyPassword.toCharArray()) // Credentials
        );
        
        // Configure proxy host
        HttpHost proxy = new HttpHost(proxyHost, proxyPort);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        
        // Set longer timeouts for authenticated proxy connections
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(10000, java.util.concurrent.TimeUnit.MILLISECONDS) // 10 seconds
                .setResponseTimeout(60000, java.util.concurrent.TimeUnit.MILLISECONDS)          // 60 seconds
                .build();
        
        // Create HttpClient with proxy authentication
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider) // Enable proxy authentication
                .setRoutePlanner(routePlanner)                       // Use proxy
                .setDefaultRequestConfig(config)                     // Apply timeouts
                .build()) {
            
            // Create GET request
            HttpGet httpGet = new HttpGet(pdfUrl);
            
            // Set headers for PDF and binary content acceptance
            httpGet.setHeader("Accept", "application/pdf, application/octet-stream");
            httpGet.setHeader("User-Agent", "Java HttpClient PDF Downloader/1.0");
            
            // Execute request through authenticated proxy
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                // Check for successful response (2xx status codes)
                if (response.getCode() >= 200 && response.getCode() < 300) {
                    // Get response content as InputStream
                    InputStream inputStream = response.getEntity().getContent();
                    
                    // TODO: Process your inputStream here
                    // The stream contains the PDF binary data
                    
                    System.out.println("PDF downloaded with authentication. Status: " + response.getCode());
                } else {
                    // Handle authentication failures or other HTTP errors
                    System.err.println("HTTP error code: " + response.getCode() + 
                        " - " + response.getReasonPhrase());
                }
            }
        } catch (IOException e) {
            // Handle network errors, authentication failures, etc.
            System.err.println("Error downloading PDF with auth: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

## Sample 3: Legacy HttpClient 4.x with Proxy

```java
// Sample 3: HttpClient 4.x (Legacy) with Proxy
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Legacy PDF downloader using Apache HttpClient 4.x for older projects
 */
public class PdfDownloader3 {
    
    /**
     * Downloads PDF using the legacy HttpClient 4.x API with proxy support
     * 
     * @param pdfUrl The URL of the PDF file to download
     * @param proxyHost The hostname/IP of the proxy server
     * @param proxyPort The port number of the proxy server
     */
    public void downloadPdfLegacy(String pdfUrl, String proxyHost, int proxyPort) {
        
        // Create proxy host configuration (HttpClient 4.x style)
        HttpHost proxy = new HttpHost(proxyHost, proxyPort, "http");
        
        // Configure request settings with proxy and timeouts (HttpClient 4.x API)
        RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)                    // Set proxy server
                .setSocketTimeout(30000)            // Socket read timeout (30 seconds)
                .setConnectTimeout(10000)           // Connection timeout (10 seconds)
                .setConnectionRequestTimeout(5000)  // Time to get connection from pool (5 seconds)
                .build();
        
        // Create HttpClient with configuration (HttpClient 4.x)
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(config)    // Apply proxy and timeout settings
                .build()) {
            
            // Create GET request
            HttpGet httpGet = new HttpGet(pdfUrl);
            
            // Apply configuration to this specific request
            httpGet.setConfig(config);
            
            // Set request headers
            httpGet.setHeader("Accept", "application/pdf");
            httpGet.setHeader("User-Agent", "Apache HttpClient 4.x PDF Downloader");
            
            // Execute request (HttpClient 4.x returns HttpResponse, not CloseableHttpResponse)
            HttpResponse response = httpClient.execute(httpGet);
            
            // Get status code from response (HttpClient 4.x API)
            int statusCode = response.getStatusLine().getStatusCode();
            
            // Check for successful response
            if (statusCode >= 200 && statusCode < 300) {
                // Get response content as InputStream
                InputStream inputStream = response.getEntity().getContent();
                
                // TODO: Process your inputStream here
                // Remember to close the stream when done
                
                System.out.println("PDF downloaded successfully with legacy client. Status: " + statusCode);
                
                // Close stream explicitly (HttpClient 4.x doesn't auto-close)
                inputStream.close();
            } else {
                // Handle HTTP error responses
                System.err.println("Failed to download PDF. HTTP Status: " + statusCode);
            }
        } catch (IOException e) {
            // Handle network and I/O errors
            System.err.println("Error in legacy PDF download: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

## Sample 4: Advanced with SSL and Custom Headers

```java
// Sample 4: Comprehensive with SSL and Custom Headers
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Advanced PDF downloader with SSL configuration and custom headers support
 */
public class PdfDownloader4 {
    
    /**
     * Downloads PDF with advanced SSL settings and custom headers
     * 
     * @param pdfUrl The URL of the PDF file to download
     * @param proxyHost The hostname/IP of the proxy server
     * @param proxyPort The port number of the proxy server
     * @param customHeaders Map of custom headers to add to the request
     */
    public void downloadPdfAdvanced(String pdfUrl, String proxyHost, int proxyPort,
                                   Map<String, String> customHeaders) {
        
        try {
            // Create SSL context that accepts all certificates
            // WARNING: This bypasses SSL certificate validation - use with caution in production
            SSLContext sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial(TrustAllStrategy.INSTANCE)  // Trust all certificates
                    .build();
            
            // Create SSL socket factory with relaxed hostname verification
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                    sslContext, 
                    NoopHostnameVerifier.INSTANCE  // Don't verify hostname against certificate
            );
            
            // Configure proxy routing
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            
            // Set comprehensive request configuration
            RequestConfig config = RequestConfig.custom()
                    .setConnectionRequestTimeout(15000, java.util.concurrent.TimeUnit.MILLISECONDS) // 15 seconds to get connection
                    .setResponseTimeout(120000, java.util.concurrent.TimeUnit.MILLISECONDS)         // 2 minutes for response
                    .setRedirectsEnabled(true)      // Follow HTTP redirects automatically
                    .setMaxRedirects(5)             // Maximum number of redirects to follow
                    .build();
            
            // Create HttpClient with SSL and proxy configuration
            try (CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(sslSocketFactory)  // Use custom SSL settings
                    .setRoutePlanner(routePlanner)          // Route through proxy
                    .setDefaultRequestConfig(config)        // Apply request configuration
                    .build()) {
                
                // Create GET request
                HttpGet httpGet = new HttpGet(pdfUrl);
                
                // Set comprehensive default headers to mimic browser behavior
                httpGet.setHeader("Accept", "application/pdf, application/octet-stream, */*");
                httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
                httpGet.setHeader("Accept-Language", "en-US,en;q=0.9");
                httpGet.setHeader("Accept-Encoding", "gzip, deflate");
                httpGet.setHeader("Connection", "keep-alive");
                
                // Add any custom headers provided by the caller
                if (customHeaders != null && !customHeaders.isEmpty()) {
                    for (Map.Entry<String, String> header : customHeaders.entrySet()) {
                        httpGet.setHeader(header.getKey(), header.getValue());
                        System.out.println("Added custom header: " + header.getKey() + " = " + header.getValue());
                    }
                }
                
                // Execute request with comprehensive error handling
                try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    // Check for successful response
                    if (response.getCode() >= 200 && response.getCode() < 300) {
                        // Get response content stream
                        InputStream inputStream = response.getEntity().getContent();
                        
                        // TODO: Process your inputStream here
                        // The stream contains the PDF binary data
                        
                        // Log successful download with response details
                        System.out.println("Advanced PDF download completed");
                        System.out.println("Content-Type: " + response.getEntity().getContentType());
                        System.out.println("Content-Length: " + response.getEntity().getContentLength());
                    } else {
                        // Handle HTTP error responses
                        System.err.println("HTTP " + response.getCode() + ": " + response.getReasonPhrase());
                    }
                }
            }
        } catch (Exception e) {
            // Handle SSL configuration errors, network errors, etc.
            System.err.println("Error in advanced PDF download: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

## Sample 5: Connection Pooling and Retry Logic

```java
// Sample 5: With Connection Pooling and Retry Logic
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;

/**
 * PDF downloader with connection pooling and automatic retry logic
 */
public class PdfDownloader5 {
    
    /**
     * Downloads PDF using connection pooling for better performance and retry logic for reliability
     * 
     * @param pdfUrl The URL of the PDF file to download
     * @param proxyHost The hostname/IP of the proxy server
     * @param proxyPort The port number of the proxy server
     */
    public void downloadPdfWithConnectionPool(String pdfUrl, String proxyHost, int proxyPort) {
        
        // Setup connection pooling for better performance with multiple requests
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(20);           // Maximum total connections in pool
        connectionManager.setDefaultMaxPerRoute(10); // Maximum connections per route/host
        
        // Configure proxy
        HttpHost proxy = new HttpHost(proxyHost, proxyPort);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        
        // Set moderate timeout values for pooled connections
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(8000, java.util.concurrent.TimeUnit.MILLISECONDS)  // 8 seconds to get connection from pool
                .setResponseTimeout(45000, java.util.concurrent.TimeUnit.MILLISECONDS)          // 45 seconds for response
                .build();
        
        // Create HttpClient with connection pooling and idle connection cleanup
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)                    // Use connection pool
                .setRoutePlanner(routePlanner)                             // Route through proxy
                .setDefaultRequestConfig(config)                           // Apply timeouts
                .evictIdleConnections(TimeValue.ofSeconds(30))             // Clean up idle connections after 30 seconds
                .build()) {
            
            // Create GET request
            HttpGet httpGet = new HttpGet(pdfUrl);
            
            // Set headers optimized for PDF download
            httpGet.setHeader("Accept", "application/pdf");
            httpGet.setHeader("User-Agent", "HttpClient with Connection Pool");
            httpGet.setHeader("Cache-Control", "no-cache");  // Ensure fresh content
            
            // Implement retry logic for resilient downloads
            int maxRetries = 3;  // Maximum number of retry attempts
            int attempt = 0;
            
            // Retry loop with exponential backoff
            while (attempt < maxRetries) {
                try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    // Check for successful response
                    if (response.getCode() >= 200 && response.getCode() < 300) {
                        // Success - get the PDF content
                        InputStream inputStream = response.getEntity().getContent();
                        
                        // TODO: Process your inputStream here
                        // Connection will be returned to pool automatically
                        
                        System.out.println("PDF downloaded with connection pooling on attempt: " + (attempt + 1));
                        System.out.println("Response headers: " + java.util.Arrays.toString(response.getHeaders()));
                        break;  // Exit retry loop on success
                        
                    } else if (response.getCode() >= 500 && attempt < maxRetries - 1) {
                        // Server error - retry if we have attempts left
                        System.out.println("Server error, retrying... Attempt: " + (attempt + 1));
                        
                        // Wait before retrying with exponential backoff
                        Thread.sleep(2000 * (attempt + 1)); // 2s, 4s, 6s delays
                        
                    } else {
                        // Client error or final attempt - don't retry
                        System.err.println("Failed after " + (attempt + 1) + " attempts. Status: " + response.getCode());
                        break;
                    }
                } catch (InterruptedException ie) {
                    // Handle thread interruption
                    Thread.currentThread().interrupt();
                    System.err.println("Download interrupted");
                    break;
                }
                attempt++;  // Increment attempt counter
            }
        } catch (IOException e) {
            // Handle connection pool errors, network errors, etc.
            System.err.println("Error in connection pool PDF download: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

## Sample 6: Usage Examples

```java
// Sample 6: Usage Example with comprehensive demonstration
/**
 * Demonstrates how to use all the PDF downloader classes
 */
public class PdfDownloaderExample {
    
    /**
     * Main method showing usage examples for all downloader types
     */
    public static void main(String[] args) {
        // Configuration - replace with your actual values
        String pdfUrl = "https://example.com/sample.pdf";  // URL of PDF to download
        String proxyHost = "proxy.company.com";            // Your proxy server hostname
        int proxyPort = 8080;                              // Your proxy server port
        
        System.out.println("Starting PDF download examples...\n");
        
        // Example 1: Basic download with simple proxy
        System.out.println("=== Example 1: Basic Download ===");
        PdfDownloader1 basicDownloader = new PdfDownloader1();
        basicDownloader.downloadPdf(pdfUrl, proxyHost, proxyPort);
        
        // Example 2: Download with proxy authentication
        System.out.println("\n=== Example 2: Download with Proxy Authentication ===");
        PdfDownloader2 authDownloader = new PdfDownloader2();
        authDownloader.downloadPdfWithAuth(pdfUrl, proxyHost, proxyPort, "username", "password");
        
        // Example 3: Legacy HttpClient 4.x approach
        System.out.println("\n=== Example 3: Legacy HttpClient 4.x ===");
        PdfDownloader3 legacyDownloader = new PdfDownloader3();
        legacyDownloader.downloadPdfLegacy(pdfUrl, proxyHost, proxyPort);
        
        // Example 4: Advanced download with custom headers
        System.out.println("\n=== Example 4: Advanced Download with Custom Headers ===");
        PdfDownloader4 advancedDownloader = new PdfDownloader4();
        
        // Create custom headers map for authentication or special requirements
        Map<String, String> headers = Map.of(
            "Authorization", "Bearer your-token-here",  // API token
            "X-API-Key", "your-api-key",               // API key
            "X-Client-Version", "1.0.0"                // Client version
        );
        advancedDownloader.downloadPdfAdvanced(pdfUrl, proxyHost, proxyPort, headers);
        
        // Example 5: Download with connection pooling and retry logic
        System.out.println("\n=== Example 5: Download with Connection Pooling ===");
        PdfDownloader5 poolDownloader = new PdfDownloader5();
        poolDownloader.downloadPdfWithConnectionPool(pdfUrl, proxyHost, proxyPort);
        
        System.out.println("\nAll PDF download examples completed!");
    }
}
```

## Key Features Summary

| Feature | Sample 1 | Sample 2 | Sample 3 | Sample 4 | Sample 5 |
|---------|----------|----------|----------|----------|----------|
| **HttpClient Version** | 5.x | 5.x | 4.x (Legacy) | 5.x | 5.x |
| **Basic Proxy** | ✅ | ✅ | ✅ | ✅ | ✅ |
| **Proxy Authentication** | ❌ | ✅ | ❌ | ❌ | ❌ |
| **Custom Headers** | Basic | Basic | Basic | ✅ Advanced | Basic |
| **SSL Configuration** | Default | Default | Default | ✅ Custom | Default |
| **Connection Pooling** | ❌ | ❌ | ❌ | ❌ | ✅ |
| **Retry Logic** | ❌ | ❌ | ❌ | ❌ | ✅ |
| **Redirect Handling** | Default | Default | Default | ✅ | Default |

## Important Notes

1. **Replace placeholder values** with your actual proxy settings and PDF URLs
2. **Add your InputStream processing logic** where the TODO comments indicate
3. **Handle sensitive credentials securely** - don't hardcode passwords in production
4. **SSL warnings** - Sample 4 disables certificate validation for testing purposes only
5. **Resource management** - All samples use try-with-resources for proper cleanup
6. **Error handling** - Each sample includes comprehensive exception handling

## Security Considerations

- **Proxy credentials**: Store securely, consider using environment variables
- **SSL certificates**: Don't disable validation in production environments
- **Input validation**: Validate URLs and proxy settings before use
- **Logging**: Avoid logging sensitive information like passwords or tokens

## Quick Start Guide

1. **Choose the appropriate sample** based on your requirements from the feature table above
2. **Add Maven dependencies** to your project
3. **Copy the relevant code** into your project
4. **Replace placeholder values** with your actual configuration
5. **Add your processing logic** in the TODO sections
6. **Test with your environment** and adjust timeouts if needed

## License

This code is provided as examples and can be freely used and modified for your projects.