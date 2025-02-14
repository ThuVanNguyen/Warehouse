package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.OrderHistoryRepository;
import com.mycompany.myapp.service.OrderHistoryService;
import com.mycompany.myapp.service.dto.OrderDTO;
import com.mycompany.myapp.service.dto.OrderHistoryDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Order}.
 */
@RestController
@RequestMapping("/api/order-history")
public class OrderHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderHistoryResource.class);

    private static final String ENTITY_NAME = "order_history";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderHistoryService orderHistoryService;

    private final OrderHistoryRepository orderHistoryRepository;

    public OrderHistoryResource(OrderHistoryService orderHistoryService, OrderHistoryRepository orderHistoryRepository) {
        this.orderHistoryService = orderHistoryService;
        this.orderHistoryRepository = orderHistoryRepository;
    }

    /**
     * {@code POST  /order-history} : Create a new orderHistory.
     *
     * @param orderHistoryDTO the orderHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderHistoryDTO, or with status {@code 400 (Bad Request)} if the order has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderHistoryDTO> createOrderHistory(@RequestBody OrderHistoryDTO orderHistoryDTO) throws URISyntaxException {
        LOG.debug("REST request to save OrderHistory : {}", orderHistoryDTO);
        if (orderHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderHistoryDTO = orderHistoryService.save(orderHistoryDTO);
        return ResponseEntity.created(new URI("/api/order-history/" + orderHistoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderHistoryDTO.getId().toString()))
            .body(orderHistoryDTO);
    }

    /**
     * {@code PUT  /order-history/:id} : Updates an existing orderHistory.
     *
     * @param id       the id of the orderHistoryDTO to save.
     * @param orderHistoryDTO the orderHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the orderHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderHistoryDTO> updateOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderHistoryDTO orderHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderHistory : {}, {}", id, orderHistoryDTO);
        if (orderHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderHistoryDTO = orderHistoryService.update(orderHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderHistoryDTO.getId().toString()))
            .body(orderHistoryDTO);
    }

    /**
     * {@code PATCH  /order-history/:id} : Partial updates given fields of an existing orderHistory, field will ignore if it is null
     *
     * @param id       the id of the orderHistoryDTO to save.
     * @param orderHistoryDTO the orderHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the orderHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderHistoryDTO> partialUpdateOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderHistoryDTO orderHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderHistory partially : {}, {}", id, orderHistoryDTO);
        if (orderHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderHistoryDTO> result = orderHistoryService.partialUpdate(orderHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-history} : get all the orderHistory.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderHistoryDTO>> getAllOrders(
        @RequestParam(required = false, defaultValue = "") String keyword,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of OrderHistory");
        Page<OrderHistoryDTO> page = orderHistoryService.findAll(keyword, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-history/:id} : get the "id" orderHistory.
     *
     * @param id the id of the orderHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderHistoryDTO> getOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderHistory : {}", id);
        Optional<OrderHistoryDTO> orderDTO = orderHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderDTO);
    }

    /**
     * {@code DELETE  /orders/:id} : delete the "id" order.
     *
     * @param id the id of the orderHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderHistory : {}", id);
        orderHistoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
