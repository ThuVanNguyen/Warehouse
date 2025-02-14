package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Client;
import com.mycompany.myapp.domain.DownloadFile;
import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.enums.Status;
import com.mycompany.myapp.repository.ClientRepository;
import com.mycompany.myapp.repository.OrderRepository;
import com.mycompany.myapp.utils.ExcelUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelService {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void importExcel(MultipartFile file, Status status, Date date, String arrangeVehicle) throws IOException {
        InputStream is = file.getInputStream();
        Workbook workbook = WorkbookFactory.create(is);
        Sheet sheet = workbook.getSheetAt(0);

        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null) continue;
            Order order = new Order();

            // import_vehicle_number
            String importVehicleNumber = ExcelUtils.getCellValue(sheet, rowNum, 0);
            if (StringUtils.isNotBlank(importVehicleNumber)) {
                order.setImportVehicleNumber(importVehicleNumber);
            }

            // receipt_date
            String receiptDate = ExcelUtils.getCellValue(sheet, rowNum, 1);
            if (StringUtils.isNotBlank(receiptDate)) {
                try {
                    if (receiptDate.contains("E")) {
                        double scientificValue = Double.parseDouble(receiptDate);
                        receiptDate = String.format("%.0f", scientificValue);
                    }
                    order.setReceiptDate(LocalDate.parse(receiptDate, DateTimeFormatter.ofPattern("yyyyMMdd")));
                } catch (NumberFormatException e) {
                    LOG.error("Lỗi chuyển đổi giá trị dạng khoa học: {}", receiptDate);
                } catch (DateTimeParseException e) {
                    LOG.error("Lỗi phân tích ngày tháng: {}", receiptDate);
                }
            } else {
                continue;
            }

            // warehouse_receipt_code
            String warehouseReceiptCode = ExcelUtils.getCellValue(sheet, rowNum, 2);
            if (!Objects.equals(warehouseReceiptCode, "")) {
                order.setWarehouseReceiptCode(warehouseReceiptCode);
            } else {
                continue;
            }

            // client_code
            String clientCode = ExcelUtils.getCellValue(sheet, rowNum, 3);
            if (StringUtils.isNotBlank(clientCode)) {
                Client client = clientRepository
                    .findByCode(clientCode)
                    .orElseGet(() -> {
                        Client newClient = new Client();
                        newClient.setCode(clientCode);
                        return clientRepository.save(newClient);
                    });
                order.setClient(client);
            }

            // productCn
            String nameCn = ExcelUtils.getCellValue(sheet, rowNum, 4);
            if (StringUtils.isNotBlank(nameCn)) {
                order.setProductCn(nameCn);
            }

            // productVn
            String nameVn = ExcelUtils.getCellValue(sheet, rowNum, 5);
            if (StringUtils.isNotBlank(nameVn)) {
                order.setProductVn(nameVn);
            }

            // pieces_received
            String piecesReceived = ExcelUtils.getCellValue(sheet, rowNum, 6);
            if (StringUtils.isNotBlank(piecesReceived)) {
                try {
                    Long piecesReceivedCount = piecesReceived.contains(".")
                        ? (long) Double.parseDouble(piecesReceived)
                        : Long.parseLong(piecesReceived);

                    order.setPiecesReceivedCount(piecesReceivedCount);
                } catch (NumberFormatException e) {
                    LOG.error("Lỗi chuyển đổi giá trị: {}", piecesReceived);
                }
            }

            // pieces_ware_house
            String piecesWareHouse = ExcelUtils.getCellValue(sheet, rowNum, 7);
            if (StringUtils.isNotBlank(piecesWareHouse)) {
                try {
                    Long piecesWareHouseCount = piecesWareHouse.contains(".")
                        ? (long) Double.parseDouble(piecesWareHouse)
                        : Long.parseLong(piecesWareHouse);

                    order.setPiecesWareHouseCount(piecesWareHouseCount);
                } catch (NumberFormatException e) {
                    LOG.error("Lỗi chuyển đổi giá trị piecesLoaded: {}", piecesWareHouse);
                }
            }

            // total_weight
            String totalWeight = ExcelUtils.getCellValue(sheet, rowNum, 8);
            if (StringUtils.isNotBlank(totalWeight)) {
                try {
                    order.setTotalWeight(Double.valueOf(totalWeight));
                } catch (NumberFormatException e) {
                    LOG.error("Lỗi chuyển đổi giá trị totalWeight: {}", totalWeight);
                }
            }

            // total_cubic_meter
            String totalCubicMeter = ExcelUtils.getCellValue(sheet, rowNum, 9);
            if (StringUtils.isNotBlank(totalCubicMeter)) {
                try {
                    order.setTotalCubicMeter(Double.valueOf(totalCubicMeter));
                } catch (NumberFormatException e) {
                    LOG.error("Lỗi chuyển đổi giá trị totalCubicMeter: {}", totalCubicMeter);
                }
            }

            // list_calculation
            String listCalculation = ExcelUtils.getCellValue(sheet, rowNum, 10);
            if (StringUtils.isNotBlank(listCalculation)) {
                order.setListCalculation(listCalculation);
            }

            // unit_price_weight
            String unitPriceWeight = ExcelUtils.getCellValue(sheet, rowNum, 11);
            if (StringUtils.isNotBlank(unitPriceWeight)) {
                try {
                    order.setUnitPriceWeight(BigDecimal.valueOf(Long.parseLong(unitPriceWeight)));
                } catch (NumberFormatException e) {
                    LOG.error("Lỗi chuyển đổi giá trị unitPriceWeight: {}", unitPriceWeight);
                }
            }

            // unit_price_cubic_meter
            String unitPriceCubicMeter = ExcelUtils.getCellValue(sheet, rowNum, 12);
            if (StringUtils.isNotBlank(unitPriceCubicMeter)) {
                try {
                    order.setUnitPriceCubicMeter(BigDecimal.valueOf(Long.parseLong(unitPriceCubicMeter)));
                } catch (NumberFormatException e) {
                    LOG.error("Lỗi chuyển đổi giá trị unitPriceCubicMeter: {}", unitPriceCubicMeter);
                }
            }

            // total_price
            String totalPrice = ExcelUtils.getCellValue(sheet, rowNum, 13);
            if (StringUtils.isNotBlank(totalPrice)) {
                try {
                    order.setTotalPrice(BigDecimal.valueOf(Long.parseLong(totalPrice)));
                } catch (NumberFormatException e) {
                    LOG.error("Lỗi chuyển đổi giá trị totalPrice: {}", totalPrice);
                }
            }

            // waybill
            String wayBill = ExcelUtils.getCellValue(sheet, rowNum, 14);
            if (StringUtils.isNotBlank(wayBill)) {
                order.setWaybill(wayBill);
            }

            // shipping_address_and_phone
            String shippingAddressAndPhone = ExcelUtils.getCellValue(sheet, rowNum, 15);
            if (StringUtils.isNotBlank(shippingAddressAndPhone)) {
                order.setShippingAddressAndPhone(shippingAddressAndPhone);
            }

            // export_vehicle_number
            String exportVehicleNumber = ExcelUtils.getCellValue(sheet, rowNum, 16);
            if (StringUtils.isNotBlank(exportVehicleNumber)) {
                order.setExportVehicleNumber(exportVehicleNumber);
            }

            // export_date
            String exportDate = ExcelUtils.getCellValue(sheet, rowNum, 17);
            if (StringUtils.isNotBlank(exportDate)) {
                try {
                    if (exportDate.contains("E")) {
                        double scientificValue = Double.parseDouble(exportDate);
                        exportDate = String.format("%.0f", scientificValue);
                    }
                    order.setExportDate(LocalDate.parse(exportDate, DateTimeFormatter.ofPattern("yyyyMMdd")));
                } catch (NumberFormatException e) {
                    LOG.error("Lỗi chuyển đổi giá trị dạng khoa học: {}", exportDate);
                } catch (DateTimeParseException e) {
                    LOG.error("Lỗi phân tích ngày tháng: {}", exportDate);
                }
            }

            // export_quantity
            String exportQuantity = ExcelUtils.getCellValue(sheet, rowNum, 18);
            if (StringUtils.isNotBlank(exportQuantity)) {
                order.setExportQuantity(Double.valueOf(exportQuantity));
            }

            // note
            String note = ExcelUtils.getCellValue(sheet, rowNum, 19);
            if (StringUtils.isNotBlank(note)) {
                order.setNote(note);
            }

            order.setStatus(status);
            if (Status.LOADED_ON_VEHICLE.equals(status)) {
                order.setLoadedOnVehicleDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                order.setArrangeVehicle(arrangeVehicle);
            }

            // ignore duplicated records for now
            orderRepository.save(order);
            LOG.info("created order with code: {}", order.getWarehouseReceiptCode());
            // Check exist record
            // orderRepository
            //     .findByWarehouseReceiptCodeAndProductNameCnAndWaybill(warehouseReceiptCode, nameCn)
            //     .ifPresentOrElse(
            //         existing -> {
            //             existing.setReceiptDate(order.getReceiptDate());
            //             existing.setWarehouseReceiveNote(order.getWarehouseReceiveNote());
            //             existing.setPiecesReceivedCount(order.getPiecesReceivedCount());
            //             existing.setPiecesLoadedCount(order.getPiecesLoadedCount());
            //             existing.setTotalWeight(order.getTotalWeight());
            //             existing.setTotalCubicMeter(order.getTotalCubicMeter());
            //             existing.setExtraFee(order.getExtraFee());
            //             existing.setInlandFee(order.getInlandFee());
            //             existing.setExtraFeeDetail(order.getExtraFeeDetail());
            //             existing.setWaybill(order.getWaybill());
            //             existing.setNote(order.getNote());
            //             existing.setWarehouse(order.getWarehouse());
            //             existing.setInlandLogistic(order.getInlandLogistic());
            //             existing.setProductCn(order.getProductCn());
            //             existing.setProductVn(order.getProductVn());
            //             existing.setUnit(order.getUnit());
            //             existing.setStatus(order.getStatus());
            //             if (Status.LOADED_ON_VEHICLE.equals(status)) {
            //                 order.setLoadedOnVehicleDate(order.getLoadedOnVehicleDate());
            //                 order.setArrangeVehicle(order.getArrangeVehicle());
            //             }
            //             orderRepository.save(existing);
            //         },
            //         () -> orderRepository.save(order)
            //     );
        }
        workbook.close();
    }

    @Transactional
    public DownloadFile exportExcel(List<Long> orderIds) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); Workbook workbook = new XSSFWorkbook()) {
            List<Order> orders = orderIds == null || orderIds.isEmpty() ? orderRepository.findAll() : orderRepository.findAllById(orderIds);
            Sheet sheet = workbook.createSheet("Order");
            Row headerRow = sheet.createRow(0);
            Optional<User> currentUserOpt = userService.getUserWithAuthorities();
            String langKey = currentUserOpt.map(User::getLangKey).orElse("en");
            String[] colHeader = getColumnHeaders(langKey);
            for (int i = 0; i < colHeader.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(colHeader[i]);
            }
            CellStyle dateCellStyle = workbook.createCellStyle();
            CreationHelper creationHelper = workbook.getCreationHelper();
            dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));
            int rowIndex = 1;
            for (Order order : orders) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(order.getImportVehicleNumber() != null ? order.getImportVehicleNumber() : "");
                row.createCell(1).setCellValue(order.getReceiptDate() != null ? order.getReceiptDate().toString() : "");
                Cell receiptDateCell = row.createCell(1);
                if (order.getReceiptDate() != null) {
                    receiptDateCell.setCellValue(Date.from(order.getReceiptDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    receiptDateCell.setCellStyle(dateCellStyle);
                }
                row.createCell(2).setCellValue(order.getWarehouseReceiptCode() != null ? order.getWarehouseReceiptCode() : "");
                row.createCell(3).setCellValue(order.getClient() != null ? order.getClient().getCode() : "");
                row.createCell(4).setCellValue(order.getProductCn());
                row.createCell(5).setCellValue(order.getProductVn());
                row.createCell(6).setCellValue(order.getPiecesReceivedCount() != null ? order.getPiecesReceivedCount() : 0.0);
                row.createCell(7).setCellValue(order.getPiecesWareHouseCount() != null ? order.getPiecesWareHouseCount() : 0.0);
                row.createCell(8).setCellValue(order.getTotalWeight() != null ? order.getTotalWeight() : 0.0);
                row.createCell(9).setCellValue(order.getTotalCubicMeter() != null ? order.getTotalCubicMeter() : 0.0);
                row.createCell(10).setCellValue(order.getListCalculation() != null ? order.getListCalculation() : "");
                row.createCell(11).setCellValue(String.valueOf(order.getUnitPriceWeight() != null ? order.getUnitPriceWeight() : 0));
                row
                    .createCell(12)
                    .setCellValue(String.valueOf(order.getUnitPriceCubicMeter() != null ? order.getUnitPriceCubicMeter() : 0));
                row.createCell(13).setCellValue(String.valueOf(order.getTotalPrice() != null ? order.getTotalPrice() : 0));
                row.createCell(14).setCellValue(order.getWaybill() != null ? order.getWaybill() : "");
                row.createCell(15).setCellValue(order.getShippingAddressAndPhone() != null ? order.getShippingAddressAndPhone() : "");

                row.createCell(16).setCellValue(order.getExportVehicleNumber() != null ? order.getExportVehicleNumber() : "");
                row.createCell(17).setCellValue(order.getExportDate() != null ? order.getExportDate().toString() : "");
                Cell exportDateCell = row.createCell(17);
                if (order.getExportDate() != null) {
                    exportDateCell.setCellValue(Date.from(order.getExportDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    exportDateCell.setCellStyle(dateCellStyle);
                }
                row.createCell(18).setCellValue(order.getExportQuantity() != null ? order.getExportQuantity() : 0);
                row.createCell(19).setCellValue(order.getNote() != null ? order.getNote() : "");
                row.createCell(20).setCellValue(convertStatusToLanguage(order.getStatus(), langKey));
                row.createCell(21).setCellValue(order.getLoadedOnVehicleDate() != null ? order.getLoadedOnVehicleDate().toString() : "");
                row.createCell(22).setCellValue(order.getArrivedAtHNDate() != null ? order.getArrivedAtHNDate().toString() : "");
                row.createCell(23).setCellValue(order.getDeliveredDate() != null ? order.getDeliveredDate().toString() : "");
                row.createCell(24).setCellValue(order.getCustomerSignedDate() != null ? order.getCustomerSignedDate().toString() : "");
                row.createCell(25).setCellValue(order.getCancelDate() != null ? order.getCancelDate().toString() : "");
                row.createCell(26).setCellValue(order.getArrangeVehicle() != null ? order.getArrangeVehicle() : "");
            }
            for (int i = 0; i < colHeader.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(outputStream);

            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
            return new DownloadFile(resource, "orders.xlsx");
        }
    }

    private String convertStatusToLanguage(Status status, String langKey) {
        Map<String, Map<Status, String>> statusTranslations = Map.of(
            "en",
            Map.of(
                Status.BANG_TUONG_WARE_HOUSE,
                "At Bang Tuong Warehouse",
                Status.LOADED_ON_VEHICLE,
                "Loaded on vehicle",
                Status.ARRIVED_AT_HN,
                "Arrived at Hanoi",
                Status.DELIVERED,
                "Delivered",
                Status.CUSTOMER_SIGNED,
                "Customer signed",
                Status.CANCELED,
                "Canceled"
            ),
            "zh-cn",
            Map.of(
                Status.BANG_TUONG_WARE_HOUSE,
                "在邦通仓库",
                Status.LOADED_ON_VEHICLE,
                "已装车",
                Status.ARRIVED_AT_HN,
                "已到达河内",
                Status.DELIVERED,
                "已交货",
                Status.CUSTOMER_SIGNED,
                "客户签署",
                Status.CANCELED,
                "已取消"
            ),
            "vi",
            Map.of(
                Status.BANG_TUONG_WARE_HOUSE,
                "Tại kho Bằng Tường",
                Status.LOADED_ON_VEHICLE,
                "Đã chất lên xe",
                Status.ARRIVED_AT_HN,
                "Đã đến Hà Nội",
                Status.DELIVERED,
                "Đã giao hàng",
                Status.CUSTOMER_SIGNED,
                "Khách hàng đã ký nhận",
                Status.CANCELED,
                "Khách hàng đã hủy"
            )
        );

        return statusTranslations.getOrDefault(langKey, statusTranslations.get("en")).getOrDefault(status, status.getValue());
    }

    private String[] getColumnHeaders(String langKey) {
        Map<String, String[]> columnHeaders = Map.of(
            "en",
            new String[] {
                "Import Vehicle Number",
                "Warehouse Receipt Date",
                "Receipt Code",
                "Customer",
                "Product CN",
                "Product VN",
                "Received Pieces",
                "Ware House Pieces",
                "Total Weight",
                "Total Cubic Meter",
                "List Calculation",
                "Unit Price Weight",
                "Unit Price Cubic Meter",
                "Total Price",
                "Waybill",
                "Shipping Address And Phone",
                "Export Vehicle Number",
                "Export Date",
                "Export Quantity",
                "Note",
                "Status",
                "Loaded on Vehicle Date",
                "Arrived at Hanoi Date",
                "Delivery Date",
                "Customer Signed Date",
                "Cancelled Date",
                "Vehicle Arranged",
            },
            "zh-cn",
            new String[] {
                "进口车号",
                "仓库接收日期",
                "收货码",
                "客户",
                "中文产品",
                "越南语产品",
                "装载件数",
                "库存件数",
                "总重",
                "总方数",
                "计算列表",
                "每公斤单价",
                "每立方米单价",
                "变成金钱",
                "运单",
                "退货地址和电话号码",
                "出口车辆号码",
                "出口日期",
                "出口数量",
                "笔记",
                "状态",
                "装车日期",
                "到达河内日期",
                "交货日期",
                "客户签字日期",
                "取消日期",
                "车辆安排",
            },
            "vi",
            new String[] {
                "Biển số xe nhập",
                "Ngày nhận ở kho Bằng Tường",
                "Mã phiếu nhận",
                "Khách hàng",
                "Sản phẩm tiếng Trung",
                "Sản phẩm tiếng Việt",
                "Số lượng kiện",
                "Số lượng kiện lưu kho",
                "Tổng Kg",
                "Tổng khối",
                "Công thức tính",
                "Đơn giá kg",
                "Đơn giá khối",
                "Thành tiền",
                "Vận đơn",
                "Địa chỉ trả hàng và số điện thoại",
                "Biển số xe xuất",
                "Ngày xuất",
                "Số lượng xuất",
                "Ghi chú",
                "Trạng thái",
                "Ngày hàng lên xe",
                "Ngày đến Hà Nội",
                "Ngày vận chuyển",
                "Ngày khách hàng kí",
                "Ngày hủy",
                "Xếp xe",
            }
        );
        return columnHeaders.getOrDefault(langKey, columnHeaders.get("en"));
    }
}
