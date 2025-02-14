package com.mycompany.myapp.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class WarehouseUtils {

    public static HttpHeaders getHeadersForDownload(String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        return headers;
    }
}
