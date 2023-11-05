package edu.ncsu.csc.CoffeeMaker.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Order;

/**
 * OrderRepository is used to provide CRUD operations for the Order model.
 * Spring will generate appropriate code with JPA.
 *
 * @author Kai Presler-Marshall
 *
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Finds a order object with the provided name. Spring will generate code to
     * make this happen.
     *
     * @param id
     *            id of the order
     * @return Order Found order, null if none.
     */

    @Override
    Optional<Order> findById ( Long id );

}
