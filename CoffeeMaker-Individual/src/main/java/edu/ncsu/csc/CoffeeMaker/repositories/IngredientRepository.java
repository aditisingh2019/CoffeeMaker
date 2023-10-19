package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

/**
 * IngredientRepository is used to provide CRUD operations for the Ingredient
 * model.
 *
 * @author Yash Agarwal, Pratik Bairoliya, Anika Bhadriraju
 *
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    /**
     * Finds a ingredient object with the provided name. Spring will generate
     * code to make this happen.
     *
     * @param name
     *            Type of Ingredient
     * @return Found ingredient, null if none.
     */
    Ingredient findByName ( String name );
}
