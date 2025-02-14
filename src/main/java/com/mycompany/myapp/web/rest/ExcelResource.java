package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.DownloadFile;
import com.mycompany.myapp.enums.Status;
import com.mycompany.myapp.service.ExcelService;
import com.mycompany.myapp.utils.WarehouseDateUtils;
import com.mycompany.myapp.utils.WarehouseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/excel")
public class ExcelResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelResource.class);

    @Autowired
    private ExcelService excelService;

    @PostMapping("/import/bang-tuong")
    public ResponseEntity<Map<String, String>> importOrdersWithBangTuongWareHouse(@RequestParam("file") MultipartFile file) {
        try {
            excelService.importExcel(file, Status.BANG_TUONG_WARE_HOUSE, null, null);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "File imported successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error importing file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/import/loaded-vehicle")
    public ResponseEntity<Map<String, String>> importOrdersWithLoadedOnVehicle(
        @RequestParam("file") MultipartFile file,
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
        @RequestParam("arrangeVehicle") String arrangeVehicle
    ) {
        try {
            excelService.importExcel(file, Status.LOADED_ON_VEHICLE, date, arrangeVehicle);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "File imported successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error importing file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/export")
    public ResponseEntity<Resource> exportOrders(@RequestBody List<Long> orderIds) {
        try {
            DownloadFile downloadFile = excelService.exportExcel(orderIds);
            ByteArrayResource resource = downloadFile.getResource();
            return ResponseEntity.ok()
                .headers(
                    WarehouseUtils.getHeadersForDownload(WarehouseDateUtils.parse(new Date()) + "-" + downloadFile.getName() + ".xlsx")
                )
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
        } catch (IOException e) {
            LOG.error("Error download file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
