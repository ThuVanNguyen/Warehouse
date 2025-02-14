package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ProductImageRepository;
import com.mycompany.myapp.service.ProductImageService;
import com.mycompany.myapp.service.dto.ProductImageDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductImage}.
 */
@RestController
@RequestMapping("/api/product-images")
public class ProductImageResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductImageResource.class);

    private static final String ENTITY_NAME = "productImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductImageService productImageService;

    private final ProductImageRepository productImageRepository;

    public ProductImageResource(ProductImageService productImageService, ProductImageRepository productImageRepository) {
        this.productImageService = productImageService;
        this.productImageRepository = productImageRepository;
    }

    /**
     * {@code POST  /product-images} : Create a new productImage.
     *
     * @param productImageDTO the productImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productImageDTO, or with status {@code 400 (Bad Request)} if the productImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductImageDTO> createProductImage(@RequestBody ProductImageDTO productImageDTO) throws URISyntaxException {
        LOG.debug("REST request to save ProductImage : {}", productImageDTO);
        if (productImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new productImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productImageDTO = productImageService.save(productImageDTO);
        return ResponseEntity.created(new URI("/api/product-images/" + productImageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productImageDTO.getId().toString()))
            .body(productImageDTO);
    }

    /**
     * {@code PUT  /product-images/:id} : Updates an existing productImage.
     *
     * @param id              the id of the productImageDTO to save.
     * @param productImageDTO the productImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productImageDTO,
     * or with status {@code 400 (Bad Request)} if the productImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductImageDTO> updateProductImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductImageDTO productImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductImage : {}, {}", id, productImageDTO);
        if (productImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productImageDTO = productImageService.update(productImageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productImageDTO.getId().toString()))
            .body(productImageDTO);
    }

    /**
     * {@code PATCH  /product-images/:id} : Partial updates given fields of an existing productImage, field will ignore if it is null
     *
     * @param id              the id of the productImageDTO to save.
     * @param productImageDTO the productImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productImageDTO,
     * or with status {@code 400 (Bad Request)} if the productImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductImageDTO> partialUpdateProductImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductImageDTO productImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductImage partially : {}, {}", id, productImageDTO);
        if (productImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductImageDTO> result = productImageService.partialUpdate(productImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-images} : get all the productImages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productImages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductImageDTO>> getAllProductImages(
        @RequestParam(required = false, defaultValue = "") String keyword,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ProductImages");
        Page<ProductImageDTO> page = productImageService.findAll(keyword, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-images/:id} : get the "id" productImage.
     *
     * @param id the id of the productImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductImageDTO> getProductImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductImage : {}", id);
        Optional<ProductImageDTO> productImageDTO = productImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productImageDTO);
    }

    /**
     * {@code DELETE  /product-images/:id} : delete the "id" productImage.
     *
     * @param id the id of the productImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductImage : {}", id);
        productImageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
