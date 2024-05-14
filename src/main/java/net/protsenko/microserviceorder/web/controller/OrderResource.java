package net.protsenko.microserviceorder.web.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.protsenko.microserviceorder.domain.Order;
import net.protsenko.microserviceorder.repository.OrderRepository;
import net.protsenko.microserviceorder.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class OrderResource {

    private static final String ENTITY_NAME = "order";

    @Value("${spring.application.name}")
    private String applicationName;

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderResource(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/orders")
    @Transactional
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) throws URISyntaxException {
        log.debug("REST request to save Order : {}", order);

        if (order.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new order cannot already have an ID");
        }

        final var result = orderRepository.save(order);
        orderService.createOrder(result);

        HttpHeaders headers = new HttpHeaders();
        String message = String.format("A new %s is created with identifier %s", ENTITY_NAME, result.getId());
        headers.add("X-" + applicationName + "-alert", message);
        headers.add("X-" + applicationName + "-params", result.getId());
        return ResponseEntity.created(new URI("/api/orders/" + result.getId())).headers(headers).body(result);
    }
}
