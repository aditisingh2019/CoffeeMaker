package edu.ncsu.csc.CoffeeMaker.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Order;

/**
 * RecipeRepository is used to provide CRUD operations for the Recipe model.
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
     * @param i
     *            Name of the recipe
     * @return ORder Found order, null if none.
     */

    @Override
    Optional<Order> findById ( Long id );

}
