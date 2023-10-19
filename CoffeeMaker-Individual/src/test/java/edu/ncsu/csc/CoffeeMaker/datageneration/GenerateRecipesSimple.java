package edu.ncsu.csc.CoffeeMaker.datageneration;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Test class designed to test the createRecipe function with varying Ingredient
 * objects for the newly created Recipe
 *
 * @author Yash Agarwal, Pratik Bairoliya, Anika Bhadriraju
 *
 */
@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class GenerateRecipesSimple {

    /*
     * RecipeService object designed to interact with the database for the
     * recipe object
     */
    @Autowired
    private RecipeService recipeService;

    /**
     * Test method for adding arbitrary Ingredients to a Recipe
     */
    @Test
    @Transactional
    public void testCreateRecipes () {

        recipeService.deleteAll();

        final Recipe r1 = new Recipe();

        // Creating Ingredients for the Recipe
        final Ingredient coffee1 = new Ingredient( "Coffee", 1 );

        final Ingredient milk1 = new Ingredient( "Milk", 0 );

        final Ingredient chocolate1 = new Ingredient( "Chocolate", 0 );

        final Ingredient sugar1 = new Ingredient( "Sugar", 0 );

        // Adding Ingredients to the Recipes
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( coffee1 );
        r1.addIngredient( milk1 );
        r1.addIngredient( chocolate1 );
        r1.addIngredient( sugar1 );

        final Ingredient coffee2 = new Ingredient( "Coffee", 1 );

        final Ingredient milk2 = new Ingredient( "Milk", 1 );

        final Ingredient chocolate2 = new Ingredient( "Chocolate", 0 );

        final Ingredient sugar2 = new Ingredient( "Sugar", 1 );

        final Recipe r2 = new Recipe();

        r2.setName( "Mocha" );
        r2.setPrice( 3 );
        r1.addIngredient( coffee2 );
        r1.addIngredient( milk2 );
        r1.addIngredient( chocolate2 );
        r1.addIngredient( sugar2 );

        recipeService.save( r1 );
        recipeService.save( r2 );

        Assert.assertEquals( "Creating two recipes should results in two recipes in the database", 2,
                recipeService.count() );

    }

}
