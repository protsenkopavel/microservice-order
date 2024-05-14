package net.protsenko.microserviceorder.repository;

import net.protsenko.microserviceorder.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
