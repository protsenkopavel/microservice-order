package net.protsenko.microserviceorder.service;

import lombok.extern.slf4j.Slf4j;
import net.protsenko.microserviceorder.domain.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final RestTemplate restTemplate;

    @Value("${spring.application.microservice-customer.url}")
    private String customerBaseUrl;

    private static final String CUSTOMER_ORDER_URL = "customerOrders/";

    public OrderServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void createOrder(Order order) {
        final var url = customerBaseUrl + CUSTOMER_ORDER_URL + order.getCustomerId();
        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        log.info("Order Request URL: {}", url);

        try {
            final var request = new HttpEntity<>(order, headers);
            final var responseEntity = restTemplate.postForEntity(url, request, Order.class);

            if (responseEntity.getStatusCode().isError()) {
                log.error("For Order ID: {}, error response: {} is received to create Order in Customer Microservice",
                        order.getId(),
                        responseEntity.getStatusCode());
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        String.format("For Order UUID: %s, Customer Microservice Message: %s", order.getId(),
                                responseEntity.getStatusCodeValue()));
            }
            if (responseEntity.hasBody()) {
                log.error("Order From Response: {}", responseEntity.getBody().getId());
            }
        } catch (Exception e) {
            log.error("For Order ID: {}, cannot create Order in Customer Microservice for reason",
                    order.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("For Order UUID: %s, Customer Microservice Response: ", order.getId()));
        }
    }

}

