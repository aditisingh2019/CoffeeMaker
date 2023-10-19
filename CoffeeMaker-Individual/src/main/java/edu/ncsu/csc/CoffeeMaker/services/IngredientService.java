package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.repositories.IngredientRepository;

/**
 * The IngredientService is used to handle CRUD operations on the Ingredient
 * model. In addition to all functionality from `Service`, we also have
 * functionality for retrieving a single Ingredient by name.
 *
 * @author Yash Agarwal, Pratik Bairoliya, Anika Bhadriraju
 *
 */
@Component
@Transactional
public class IngredientService extends Service<Ingredient, Long> {

    /**
     * IngredientRepository object designed to facilitate CRUD operations by the
     * Ingredient model
     */
    @Autowired
    private IngredientRepository ingredientRepository;

    /**
     * Getter method for the IngredientRepository instance
     * 
     * @return ingredientRepository Instance of the IngredientRepository object
     * 
     */
    @Override
    protected JpaRepository<Ingredient, Long> getRepository () {
        return ingredientRepository;
    }

    /**
     * Finds a ingredient object with the provided name. Spring will generate
     * code to make this happen.
     *
     * @param name
     *            String name of the Ingredient
     * @return Found ingredient, null if none.
     */
    public Ingredient findByIngredient ( final String name ) {

        return ingredientRepository.findByName( name );

    }

}
