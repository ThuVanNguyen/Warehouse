package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.enums.Status;
import jakarta.persistence.Column;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/** A DTO for the {@link com.mycompany.myapp.domain.OrderHistory} entity. */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderHistoryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -9181557586891286628L;

    private Long id;

    private String importVehicleNumber;

    private LocalDate receiptDate;

    private String warehouseReceiptCode;

    private String productCn;

    private String productVn;

    private Long piecesReceivedCount;

    private Long piecesWareHouseCount;

    private Double totalWeight;

    private Double totalCubicMeter;

    private String listCalculation;

    private BigDecimal unitPriceWeight;

    private BigDecimal unitPriceCubicMeter;

    private BigDecimal totalPrice;

    private String waybill;

    private String shippingAddressAndPhone;

    private String exportVehicleNumber;

    private LocalDate exportDate;

    private Double exportQuantity;

    private String note;

    private ClientDTO client;

    private Status status;

    private LocalDate bangTuongWareHouseDate;

    private LocalDate loadedOnVehicleDate;

    private LocalDate arrivedAtHNDate;

    private LocalDate deliveredDate;

    private LocalDate customerSignedDate;

    private LocalDate cancelDate;

    private String arrangeVehicle;

    private Set<ProductImageDTO> productImages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getWarehouseReceiptCode() {
        return warehouseReceiptCode;
    }

    public void setWarehouseReceiptCode(String warehouseReceiptCode) {
        this.warehouseReceiptCode = warehouseReceiptCode;
    }

    public String getImportVehicleNumber() {
        return importVehicleNumber;
    }

    public void setImportVehicleNumber(String importVehicleNumber) {
        this.importVehicleNumber = importVehicleNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Double getExportQuantity() {
        return exportQuantity;
    }

    public void setExportQuantity(Double exportQuantity) {
        this.exportQuantity = exportQuantity;
    }

    public LocalDate getExportDate() {
        return exportDate;
    }

    public void setExportDate(LocalDate exportDate) {
        this.exportDate = exportDate;
    }

    public String getExportVehicleNumber() {
        return exportVehicleNumber;
    }

    public void setExportVehicleNumber(String exportVehicleNumber) {
        this.exportVehicleNumber = exportVehicleNumber;
    }

    public Long getPiecesReceivedCount() {
        return piecesReceivedCount;
    }

    public void setPiecesReceivedCount(Long piecesReceivedCount) {
        this.piecesReceivedCount = piecesReceivedCount;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Double getTotalCubicMeter() {
        return totalCubicMeter;
    }

    public void setTotalCubicMeter(Double totalCubicMeter) {
        this.totalCubicMeter = totalCubicMeter;
    }

    public String getWaybill() {
        return waybill;
    }

    public void setWaybill(String waybill) {
        this.waybill = waybill;
    }

    public String getProductCn() {
        return productCn;
    }

    public void setProductCn(String productCn) {
        this.productCn = productCn;
    }

    public String getProductVn() {
        return productVn;
    }

    public void setProductVn(String productVn) {
        this.productVn = productVn;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public Long getPiecesWareHouseCount() {
        return piecesWareHouseCount;
    }

    public void setPiecesWareHouseCount(Long piecesWareHouseCount) {
        this.piecesWareHouseCount = piecesWareHouseCount;
    }

    public String getArrangeVehicle() {
        return arrangeVehicle;
    }

    public void setArrangeVehicle(String arrangeVehicle) {
        this.arrangeVehicle = arrangeVehicle;
    }

    public String getShippingAddressAndPhone() {
        return shippingAddressAndPhone;
    }

    public void setShippingAddressAndPhone(String shippingAddressAndPhone) {
        this.shippingAddressAndPhone = shippingAddressAndPhone;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getUnitPriceWeight() {
        return unitPriceWeight;
    }

    public void setUnitPriceWeight(BigDecimal unitPriceWeight) {
        this.unitPriceWeight = unitPriceWeight;
    }

    public BigDecimal getUnitPriceCubicMeter() {
        return unitPriceCubicMeter;
    }

    public void setUnitPriceCubicMeter(BigDecimal unitPriceCubicMeter) {
        this.unitPriceCubicMeter = unitPriceCubicMeter;
    }

    public String getListCalculation() {
        return listCalculation;
    }

    public void setListCalculation(String listCalculation) {
        this.listCalculation = listCalculation;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getBangTuongWareHouseDate() {
        return bangTuongWareHouseDate;
    }

    public void setBangTuongWareHouseDate(LocalDate bangTuongWareHouseDate) {
        this.bangTuongWareHouseDate = bangTuongWareHouseDate;
    }

    public LocalDate getLoadedOnVehicleDate() {
        return loadedOnVehicleDate;
    }

    public void setLoadedOnVehicleDate(LocalDate loadedOnVehicleDate) {
        this.loadedOnVehicleDate = loadedOnVehicleDate;
    }

    public LocalDate getArrivedAtHNDate() {
        return arrivedAtHNDate;
    }

    public void setArrivedAtHNDate(LocalDate arrivedAtHNDate) {
        this.arrivedAtHNDate = arrivedAtHNDate;
    }

    public LocalDate getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(LocalDate deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public LocalDate getCustomerSignedDate() {
        return customerSignedDate;
    }

    public void setCustomerSignedDate(LocalDate customerSignedDate) {
        this.customerSignedDate = customerSignedDate;
    }

    public LocalDate getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Set<ProductImageDTO> getProductImages() {
        return productImages;
    }

    public void setProductImages(Set<ProductImageDTO> productImages) {
        this.productImages = productImages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderHistoryDTO)) {
            return false;
        }

        OrderHistoryDTO orderHistoryDTO = (OrderHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "OrderHistoryDTO{" +
            "id=" +
            id +
            ", importVehicleNumber='" +
            importVehicleNumber +
            '\'' +
            ", receiptDate=" +
            receiptDate +
            ", warehouseReceiptCode='" +
            warehouseReceiptCode +
            '\'' +
            ", productCn='" +
            productCn +
            '\'' +
            ", productVn='" +
            productVn +
            '\'' +
            ", piecesReceivedCount=" +
            piecesReceivedCount +
            ", piecesWareHouseCount=" +
            piecesWareHouseCount +
            ", totalWeight=" +
            totalWeight +
            ", totalCubicMeter=" +
            totalCubicMeter +
            ", listCalculation='" +
            listCalculation +
            '\'' +
            ", unitPriceWeight=" +
            unitPriceWeight +
            ", unitPriceCubicMeter=" +
            unitPriceCubicMeter +
            ", totalPrice=" +
            totalPrice +
            ", waybill='" +
            waybill +
            '\'' +
            ", shippingAddressAndPhone='" +
            shippingAddressAndPhone +
            '\'' +
            ", exportVehicleNumber='" +
            exportVehicleNumber +
            '\'' +
            ", exportDate=" +
            exportDate +
            ", exportQuantity=" +
            exportQuantity +
            ", note='" +
            note +
            '\'' +
            ", client=" +
            client +
            ", status=" +
            status +
            ", bangTuongWareHouseDate=" +
            bangTuongWareHouseDate +
            ", loadedOnVehicleDate=" +
            loadedOnVehicleDate +
            ", arrivedAtHNDate=" +
            arrivedAtHNDate +
            ", deliveredDate=" +
            deliveredDate +
            ", customerSignedDate=" +
            customerSignedDate +
            ", cancelDate=" +
            cancelDate +
            ", arrangeVehicle='" +
            arrangeVehicle +
            '\'' +
            ", productImages=" +
            productImages +
            '}'
        );
    }
}
