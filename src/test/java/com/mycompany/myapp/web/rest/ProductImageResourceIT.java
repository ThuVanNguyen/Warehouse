package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductImageAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductImage;
import com.mycompany.myapp.repository.ProductImageRepository;
import com.mycompany.myapp.service.dto.ProductImageDTO;
import com.mycompany.myapp.service.mapper.ProductImageMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductImageResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_LEN = 1L;
    private static final Long UPDATED_LEN = 2L;

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_BASE_64 = "AAAAAAAAAA";
    private static final String UPDATED_BASE_64 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductImageMapper productImageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductImageMockMvc;

    private ProductImage productImage;

    private ProductImage insertedProductImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductImage createEntity() {
        return new ProductImage().type(DEFAULT_TYPE).base64(DEFAULT_BASE_64);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductImage createUpdatedEntity() {
        return new ProductImage().type(UPDATED_TYPE).base64(UPDATED_BASE_64);
    }

    @BeforeEach
    public void initTest() {
        productImage = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductImage != null) {
            productImageRepository.delete(insertedProductImage);
            insertedProductImage = null;
        }
    }

    @Test
    @Transactional
    void createProductImage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductImage
        ProductImageDTO productImageDTO = productImageMapper.toDto(productImage);
        var returnedProductImageDTO = om.readValue(
            restProductImageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productImageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductImageDTO.class
        );

        // Validate the ProductImage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProductImage = productImageMapper.toEntity(returnedProductImageDTO);
        assertProductImageUpdatableFieldsEquals(returnedProductImage, getPersistedProductImage(returnedProductImage));

        insertedProductImage = returnedProductImage;
    }

    @Test
    @Transactional
    void createProductImageWithExistingId() throws Exception {
        // Create the ProductImage with an existing ID
        productImage.setId(1L);
        ProductImageDTO productImageDTO = productImageMapper.toDto(productImage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productImageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductImage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductImages() throws Exception {
        // Initialize the database
        insertedProductImage = productImageRepository.saveAndFlush(productImage);

        // Get all the productImageList
        restProductImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].len").value(hasItem(DEFAULT_LEN.intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].base64").value(hasItem(DEFAULT_BASE_64)));
    }

    @Test
    @Transactional
    void getProductImage() throws Exception {
        // Initialize the database
        insertedProductImage = productImageRepository.saveAndFlush(productImage);

        // Get the productImage
        restProductImageMockMvc
            .perform(get(ENTITY_API_URL_ID, productImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productImage.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.len").value(DEFAULT_LEN.intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.base64").value(DEFAULT_BASE_64));
    }

    @Test
    @Transactional
    void getNonExistingProductImage() throws Exception {
        // Get the productImage
        restProductImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductImage() throws Exception {
        // Initialize the database
        insertedProductImage = productImageRepository.saveAndFlush(productImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productImage
        ProductImage updatedProductImage = productImageRepository.findById(productImage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductImage are not directly saved in db
        em.detach(updatedProductImage);
        updatedProductImage.type(UPDATED_TYPE).base64(UPDATED_BASE_64);
        ProductImageDTO productImageDTO = productImageMapper.toDto(updatedProductImage);

        restProductImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductImageToMatchAllProperties(updatedProductImage);
    }

    @Test
    @Transactional
    void putNonExistingProductImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productImage.setId(longCount.incrementAndGet());

        // Create the ProductImage
        ProductImageDTO productImageDTO = productImageMapper.toDto(productImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productImage.setId(longCount.incrementAndGet());

        // Create the ProductImage
        ProductImageDTO productImageDTO = productImageMapper.toDto(productImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productImage.setId(longCount.incrementAndGet());

        // Create the ProductImage
        ProductImageDTO productImageDTO = productImageMapper.toDto(productImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductImageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productImageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductImageWithPatch() throws Exception {
        // Initialize the database
        insertedProductImage = productImageRepository.saveAndFlush(productImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productImage using partial update
        ProductImage partialUpdatedProductImage = new ProductImage();
        partialUpdatedProductImage.setId(productImage.getId());

        restProductImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductImage))
            )
            .andExpect(status().isOk());

        // Validate the ProductImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductImageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductImage, productImage),
            getPersistedProductImage(productImage)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductImageWithPatch() throws Exception {
        // Initialize the database
        insertedProductImage = productImageRepository.saveAndFlush(productImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productImage using partial update
        ProductImage partialUpdatedProductImage = new ProductImage();
        partialUpdatedProductImage.setId(productImage.getId());

        partialUpdatedProductImage.type(UPDATED_TYPE).base64(UPDATED_BASE_64);

        restProductImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductImage))
            )
            .andExpect(status().isOk());

        // Validate the ProductImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductImageUpdatableFieldsEquals(partialUpdatedProductImage, getPersistedProductImage(partialUpdatedProductImage));
    }

    @Test
    @Transactional
    void patchNonExistingProductImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productImage.setId(longCount.incrementAndGet());

        // Create the ProductImage
        ProductImageDTO productImageDTO = productImageMapper.toDto(productImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productImageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productImage.setId(longCount.incrementAndGet());

        // Create the ProductImage
        ProductImageDTO productImageDTO = productImageMapper.toDto(productImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productImage.setId(longCount.incrementAndGet());

        // Create the ProductImage
        ProductImageDTO productImageDTO = productImageMapper.toDto(productImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductImageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productImageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductImage() throws Exception {
        // Initialize the database
        insertedProductImage = productImageRepository.saveAndFlush(productImage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productImage
        restProductImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, productImage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productImageRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ProductImage getPersistedProductImage(ProductImage productImage) {
        return productImageRepository.findById(productImage.getId()).orElseThrow();
    }

    protected void assertPersistedProductImageToMatchAllProperties(ProductImage expectedProductImage) {
        assertProductImageAllPropertiesEquals(expectedProductImage, getPersistedProductImage(expectedProductImage));
    }

    protected void assertPersistedProductImageToMatchUpdatableProperties(ProductImage expectedProductImage) {
        assertProductImageAllUpdatablePropertiesEquals(expectedProductImage, getPersistedProductImage(expectedProductImage));
    }
}
