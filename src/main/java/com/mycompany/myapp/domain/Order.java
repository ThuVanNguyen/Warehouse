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

/** A Order. */
@Entity
@Table(name = "jhi_order")
@SequenceGenerator(name = "order_sequence", sequenceName = "order_seq", allocationSize = 1)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence")
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

    //số kiện đơn hàng
    @Column(name = "pieces_received_count")
    private Long piecesReceivedCount;

    //số kiện nhập kho
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

    @Column(name = "arrange_vehicle")
    private String arrangeVehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Client client;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    private Set<ProductImage> productImages = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Order receiptDate(LocalDate receiptDate) {
        this.setReceiptDate(receiptDate);
        return this;
    }

    public String getWarehouseReceiptCode() {
        return warehouseReceiptCode;
    }

    public void setWarehouseReceiptCode(String warehouseReceiptCode) {
        this.warehouseReceiptCode = warehouseReceiptCode;
    }

    public Order warehouseReceiptCode(String warehouseReceiptCode) {
        this.setWarehouseReceiptCode(warehouseReceiptCode);
        return this;
    }

    public Long getPiecesReceivedCount() {
        return piecesReceivedCount;
    }

    public void setPiecesReceivedCount(Long piecesReceivedCount) {
        this.piecesReceivedCount = piecesReceivedCount;
    }

    public Order piecesReceivedCount(Long piecesReceivedCount) {
        this.setPiecesReceivedCount(piecesReceivedCount);
        return this;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Order totalWeight(Double totalWeight) {
        this.setTotalWeight(totalWeight);
        return this;
    }

    public Double getTotalCubicMeter() {
        return totalCubicMeter;
    }

    public void setTotalCubicMeter(Double totalCubicMeter) {
        this.totalCubicMeter = totalCubicMeter;
    }

    public Order totalCubicMeter(Double totalCubicMeter) {
        this.setTotalCubicMeter(totalCubicMeter);
        return this;
    }

    public String getWaybill() {
        return waybill;
    }

    public void setWaybill(String waybill) {
        this.waybill = waybill;
    }

    public Order waybill(String waybill) {
        this.setWaybill(waybill);
        return this;
    }

    public String getProductCn() {
        return productCn;
    }

    public void setProductCn(String productCn) {
        this.productCn = productCn;
    }

    public Order productCn(String productCn) {
        this.setProductCn(productCn);
        return this;
    }

    public String getProductVn() {
        return productVn;
    }

    public void setProductVn(String productVn) {
        this.productVn = productVn;
    }

    public Order productVn(String productVn) {
        this.setProductVn(productVn);
        return this;
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

    public String getArrangeVehicle() {
        return arrangeVehicle;
    }

    public void setArrangeVehicle(String arrangeVehicle) {
        this.arrangeVehicle = arrangeVehicle;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Order client(Client client) {
        this.setClient(client);
        return this;
    }

    public Long getPiecesWareHouseCount() {
        return piecesWareHouseCount;
    }

    public void setPiecesWareHouseCount(Long piecesWareHouseCount) {
        this.piecesWareHouseCount = piecesWareHouseCount;
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

    public Set<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(Set<ProductImage> productImages) {
        if (this.productImages != null) {
            this.productImages.forEach(i -> i.setOrder(null));
        }
        if (productImages != null) {
            productImages.forEach(i -> i.setOrder(this));
        }
        this.productImages = productImages;
    }

    @Override
    public String toString() {
        return (
            "Order{" +
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
            ", arrangeVehicle='" +
            arrangeVehicle +
            '\'' +
            ", client=" +
            client +
            ", productImages=" +
            productImages +
            '}'
        );
    }

    public Order productImages(Set<ProductImage> productImages) {
        this.setProductImages(productImages);
        return this;
    }

    public Order addProductImages(ProductImage productImage) {
        this.productImages.add(productImage);
        productImage.setOrder(this);
        return this;
    }

    public Order removeProductImages(ProductImage productImage) {
        this.productImages.remove(productImage);
        productImage.setOrder(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return getId() != null && getId().equals(((Order) o).getId());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
