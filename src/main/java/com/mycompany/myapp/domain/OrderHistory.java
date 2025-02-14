package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.enums.Status;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jhi_order_history")
@SequenceGenerator(name = "order_history_sequence", sequenceName = "order_history_seq", allocationSize = 1)
public class OrderHistory implements Serializable {

    @Serial
    private static final long serialVersionUID = -2338625953354658195L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_history_sequence")
    @Column(name = "id")
    private Long id;

    @Column(name = "import_vehicle_number")
    private String importVehicleNumber;

    @Column(name = "receipt_date")
    private LocalDate receiptDate;

    @Column(name = "warehouse_receipt_code")
    private String warehouseReceiptCode;

    @Column(name = "product_cn")
    private String productCn;

    @Column(name = "product_vn")
    private String productVn;

    @Column(name = "pieces_received_count")
    private Long piecesReceivedCount;

    @Column(name = "pieces_ware_house_count")
    private Long piecesWareHouseCount;

    @Column(name = "total_weight")
    private Double totalWeight;

    @Column(name = "total_cubic_meter")
    private Double totalCubicMeter;

    @Column(name = "list_calculation")
    private String listCalculation;

    @Column(name = "unit_price_weight")
    private BigDecimal unitPriceWeight;

    @Column(name = "unit_price_cubic_meter")
    private BigDecimal unitPriceCubicMeter;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "waybill")
    private String waybill;

    @Column(name = "shipping_address_and_phone")
    private String shippingAddressAndPhone;

    @Column(name = "export_vehicle_number")
    private String exportVehicleNumber;

    @Column(name = "export_date")
    private LocalDate exportDate;

    @Column(name = "export_quantity")
    private Double exportQuantity;

    @Column(name = "note")
    private String note;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "bang_tuong_ware_house_date")
    private LocalDate bangTuongWareHouseDate;

    @Column(name = "loaded_on_vehicle_date")
    private LocalDate loadedOnVehicleDate;

    @Column(name = "arrived_at_hn_date")
    private LocalDate arrivedAtHNDate;

    @Column(name = "delivered_date")
    private LocalDate deliveredDate;

    @Column(name = "customer_signed_date")
    private LocalDate customerSignedDate;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Client client;

    @Column(name = "arrange_vehicle")
    private String arrangeVehicle;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderHistory")
    @JsonIgnoreProperties(value = { "orderHistory" }, allowSetters = true)
    private Set<ProductImage> productImages = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public LocalDate getReceiptDate() {
        return this.receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public OrderHistory receiptDate(LocalDate receiptDate) {
        this.setReceiptDate(receiptDate);
        return this;
    }

    public String getWarehouseReceiptCode() {
        return this.warehouseReceiptCode;
    }

    public void setWarehouseReceiptCode(String warehouseReceiptCode) {
        this.warehouseReceiptCode = warehouseReceiptCode;
    }

    public OrderHistory warehouseReceiptCode(String warehouseReceiptCode) {
        this.setWarehouseReceiptCode(warehouseReceiptCode);
        return this;
    }

    public Long getPiecesReceivedCount() {
        return this.piecesReceivedCount;
    }

    public void setPiecesReceivedCount(Long piecesReceivedCount) {
        this.piecesReceivedCount = piecesReceivedCount;
    }

    public OrderHistory piecesReceivedCount(Long piecesReceivedCount) {
        this.setPiecesReceivedCount(piecesReceivedCount);
        return this;
    }

    public Double getTotalWeight() {
        return this.totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public OrderHistory totalWeight(Double totalWeight) {
        this.setTotalWeight(totalWeight);
        return this;
    }

    public Double getTotalCubicMeter() {
        return this.totalCubicMeter;
    }

    public void setTotalCubicMeter(Double totalCubicMeter) {
        this.totalCubicMeter = totalCubicMeter;
    }

    public OrderHistory totalCubicMeter(Double totalCubicMeter) {
        this.setTotalCubicMeter(totalCubicMeter);
        return this;
    }

    public String getWaybill() {
        return this.waybill;
    }

    public void setWaybill(String waybill) {
        this.waybill = waybill;
    }

    public OrderHistory waybill(String waybill) {
        this.setWaybill(waybill);
        return this;
    }

    public String getProductCn() {
        return productCn;
    }

    public void setProductCn(String productCn) {
        this.productCn = productCn;
    }

    public OrderHistory productCn(String productCn) {
        this.setProductCn(productCn);
        return this;
    }

    public String getProductVn() {
        return productVn;
    }

    public void setProductVn(String productVn) {
        this.productVn = productVn;
    }

    public OrderHistory productVn(String productVn) {
        this.setProductVn(productVn);
        return this;
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

    public String getImportVehicleNumber() {
        return importVehicleNumber;
    }

    public void setImportVehicleNumber(String importVehicleNumber) {
        this.importVehicleNumber = importVehicleNumber;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public OrderHistory client(Client client) {
        this.setClient(client);
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public OrderHistory status(Status status) {
        this.setStatus(status);
        return this;
    }

    public LocalDate getBangTuongWareHouseDate() {
        return bangTuongWareHouseDate;
    }

    public void setBangTuongWareHouseDate(LocalDate wareHouseDate) {
        this.bangTuongWareHouseDate = wareHouseDate;
    }

    public OrderHistory bangTuongWareHouseDate(LocalDate bangTuongWareHouseDate) {
        this.setBangTuongWareHouseDate(bangTuongWareHouseDate);
        return this;
    }

    public LocalDate getLoadedOnVehicleDate() {
        return loadedOnVehicleDate;
    }

    public void setLoadedOnVehicleDate(LocalDate loadedOnVehicleDate) {
        this.loadedOnVehicleDate = loadedOnVehicleDate;
    }

    public OrderHistory loadedOnVehicleDate(LocalDate loadedOnVehicleDate) {
        this.setLoadedOnVehicleDate(loadedOnVehicleDate);
        return this;
    }

    public LocalDate getArrivedAtHNDate() {
        return arrivedAtHNDate;
    }

    public void setArrivedAtHNDate(LocalDate arrivedAtHNDate) {
        this.arrivedAtHNDate = arrivedAtHNDate;
    }

    public OrderHistory arrivedAtHNDate(LocalDate arrivedAtHNDate) {
        this.setArrivedAtHNDate(arrivedAtHNDate);
        return this;
    }

    public LocalDate getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(LocalDate deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public OrderHistory deliveredDate(LocalDate deliveredDate) {
        this.setDeliveredDate(deliveredDate);
        return this;
    }

    public LocalDate getCustomerSignedDate() {
        return customerSignedDate;
    }

    public void setCustomerSignedDate(LocalDate customerSignedDate) {
        this.customerSignedDate = customerSignedDate;
    }

    public OrderHistory customerSignedDate(LocalDate customerSignedDate) {
        this.setCustomerSignedDate(customerSignedDate);
        return this;
    }

    public LocalDate getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public OrderHistory cancelDate(LocalDate cancelDate) {
        this.setCancelDate(cancelDate);
        return this;
    }

    public Long getPiecesWareHouseCount() {
        return piecesWareHouseCount;
    }

    public void setPiecesWareHouseCount(Long piecesWareHouseCount) {
        this.piecesWareHouseCount = piecesWareHouseCount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getArrangeVehicle() {
        return arrangeVehicle;
    }

    public void setArrangeVehicle(String arrangeVehicle) {
        this.arrangeVehicle = arrangeVehicle;
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

    public String getShippingAddressAndPhone() {
        return shippingAddressAndPhone;
    }

    public void setShippingAddressAndPhone(String shippingAddressAndPhone) {
        this.shippingAddressAndPhone = shippingAddressAndPhone;
    }

    public Set<ProductImage> getProductImages() {
        return this.productImages;
    }

    public void setProductImages(Set<ProductImage> productImages) {
        if (this.productImages != null) {
            this.productImages.forEach(i -> i.setOrderHistory(null));
        }
        if (productImages != null) {
            productImages.forEach(i -> i.setOrderHistory(this));
        }
        this.productImages = productImages;
    }

    public OrderHistory productImages(Set<ProductImage> productImages) {
        this.setProductImages(productImages);
        return this;
    }

    public OrderHistory addProductImages(ProductImage productImage) {
        this.productImages.add(productImage);
        productImage.setOrderHistory(this);
        return this;
    }

    public OrderHistory removeProductImages(ProductImage productImage) {
        this.productImages.remove(productImage);
        productImage.setOrderHistory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public String toString() {
        return (
            "OrderHistory{" +
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
            ", client=" +
            client +
            ", arrangeVehicle='" +
            arrangeVehicle +
            '\'' +
            ", productImages=" +
            productImages +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
