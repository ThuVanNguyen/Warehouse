package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * A ProductImage.
 */
@Entity
@Table(name = "jhi_product_image")
@SequenceGenerator(name = "product_image_sequence", sequenceName = "product_image_seq", allocationSize = 1)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductImage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_image_sequence")
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "base_64", length = 2048)
    private String base64;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productImages", "client" }, allowSetters = true)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productImages", "client" }, allowSetters = true)
    private OrderHistory orderHistory;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductImage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public ProductImage type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBase64() {
        return this.base64;
    }

    public ProductImage base64(String base64) {
        this.setBase64(base64);
        return this;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ProductImage order(Order order) {
        this.setOrder(order);
        return this;
    }

    public OrderHistory getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(OrderHistory orderHistory) {
        this.orderHistory = orderHistory;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductImage)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductImage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductImage{" + "id=" + getId() + ", type='" + getType() + "'" + ", base64='" + getBase64() + "'" + "}";
    }
}
