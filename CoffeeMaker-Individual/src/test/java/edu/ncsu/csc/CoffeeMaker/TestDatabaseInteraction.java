package edu.ncsu.csc.CoffeeMaker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class TestDatabaseInteraction {
    @Autowired
    private RecipeService recipeService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        recipeService.deleteAll();
    }

    /**
     * Tests the RecipeService class
     */
    @Test
    @Transactional
    public void testRecipes () {

        final Recipe teamCoffee = new Recipe();

        teamCoffee.setName( "Team Coffee" );
        teamCoffee.setPrice( 350 );
        final Ingredient coffee = new Ingredient( "Coffee", 2 );

        final Ingredient milk = new Ingredient( "Milk", 1 );

        final Ingredient pumpkinSpice = new Ingredient( "Pumpkin Spice", 1 );

        final Ingredient sugar = new Ingredient( "Sugar", 1 );
        teamCoffee.addIngredient( sugar );
        teamCoffee.addIngredient( milk );
        teamCoffee.addIngredient( pumpkinSpice );
        teamCoffee.addIngredient( coffee );
        recipeService.save( teamCoffee );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        assertEquals( teamCoffee.getName(), dbRecipe.getName() );
        assertEquals( teamCoffee.getPrice(), dbRecipe.getPrice() );
        assertEquals( teamCoffee.getIngredients(), dbRecipe.getIngredients() );
        final List<Ingredient> ingredientsTeam = teamCoffee.getIngredients();
        assertTrue( ingredientsTeam.contains( sugar ) );
        assertTrue( ingredientsTeam.contains( milk ) );
        assertTrue( ingredientsTeam.contains( pumpkinSpice ) );
        assertTrue( ingredientsTeam.contains( coffee ) );

    }

    /**
     * Tests the RecipeService class
     */
    @Test
    @Transactional
    public void testFindByName () {
        final Recipe teamCoffee = new Recipe();

        teamCoffee.setName( "Team Coffee" );
        teamCoffee.setPrice( 350 );
        final Ingredient coffee = new Ingredient( "Coffee", 2 );

        final Ingredient milk = new Ingredient( "Milk", 1 );

        final Ingredient pumpkinSpice = new Ingredient( "Pumpkin Spice", 1 );

        final Ingredient sugar = new Ingredient( "Sugar", 1 );
        teamCoffee.addIngredient( sugar );
        teamCoffee.addIngredient( milk );
        teamCoffee.addIngredient( pumpkinSpice );
        teamCoffee.addIngredient( coffee );

        recipeService.save( teamCoffee );

        final Recipe findByNameRecipe = recipeService.findByName( "Team Coffee" );
        assertEquals( teamCoffee.getName(), findByNameRecipe.getName() );
        assertEquals( teamCoffee.getPrice(), findByNameRecipe.getPrice() );
        final List<Ingredient> ingredientsTeam = teamCoffee.getIngredients();

        assertEquals( ingredientsTeam, findByNameRecipe.getIngredients() );

        assertTrue( findByNameRecipe.getIngredients().contains( sugar ) );
        assertTrue( findByNameRecipe.getIngredients().contains( milk ) );
        assertTrue( findByNameRecipe.getIngredients().contains( pumpkinSpice ) );
        assertTrue( findByNameRecipe.getIngredients().contains( coffee ) );

    }

    /**
     * Tests the RecipeService class
     */
    @Test
    @Transactional
    public void testEditDBRecipe () {
        final Recipe teamCoffee = new Recipe();

        teamCoffee.setName( "Team Coffee" );
        teamCoffee.setPrice( 350 );
        final Ingredient coffee = new Ingredient( "Coffee", 2 );

        final Ingredient milk = new Ingredient( "Milk", 1 );

        final Ingredient pumpkinSpice = new Ingredient( "Pumpkin Spice", 1 );

        final Ingredient sugar = new Ingredient( "Sugar", 1 );
        teamCoffee.addIngredient( sugar );
        teamCoffee.addIngredient( milk );
        teamCoffee.addIngredient( pumpkinSpice );
        teamCoffee.addIngredient( coffee );

        recipeService.save( teamCoffee );

        final Recipe findByNameRecipe = recipeService.findByName( "Team Coffee" );

        findByNameRecipe.getIngredients().get( 0 ).setAmount( 5 );
        recipeService.save( findByNameRecipe );

        final Recipe changedRecipe = recipeService.findByName( "Team Coffee" );

        assertEquals( changedRecipe.getName(), findByNameRecipe.getName() );
        assertEquals( changedRecipe.getPrice(), findByNameRecipe.getPrice() );

        final List<Ingredient> changedRecipeIngredients = changedRecipe.getIngredients();

        assertEquals( changedRecipeIngredients, findByNameRecipe.getIngredients() );

        final List<Recipe> dbRecipes = recipeService.findAll();
        assertEquals( 1, dbRecipes.size() );
        final Recipe dbRecipe = dbRecipes.get( 0 );
        assertEquals( "Team Coffee", dbRecipe.getName() );
        assertEquals( 350, dbRecipe.getPrice().intValue() );
        assertEquals( 5, dbRecipe.getIngredients().get( 0 ).getAmount().intValue() );
        assertEquals( 1, dbRecipe.getIngredients().get( 1 ).getAmount().intValue() );
        assertEquals( 1, dbRecipe.getIngredients().get( 2 ).getAmount().intValue() );
        assertEquals( 2, dbRecipe.getIngredients().get( 3 ).getAmount().intValue() );

    }

    /**
     * Tests the RecipeService class
     *
     * @author Yash Agarwal yagarwa2
     */
    @Test
    @Transactional
    public void testFindById () {
        final Recipe teamCoffee = new Recipe();

        teamCoffee.setName( "Team Coffee" );
        teamCoffee.setPrice( 350 );
        final Ingredient coffee = new Ingredient( "Coffee", 2 );

        final Ingredient milk = new Ingredient( "Milk", 1 );

        final Ingredient pumpkinSpice = new Ingredient( "Pumpkin Spice", 1 );

        final Ingredient sugar = new Ingredient( "Sugar", 1 );
        teamCoffee.addIngredient( sugar );
        teamCoffee.addIngredient( milk );
        teamCoffee.addIngredient( pumpkinSpice );
        teamCoffee.addIngredient( coffee );
        recipeService.save( teamCoffee );

        final Recipe findByNameRecipe = recipeService.findById( teamCoffee.getId() );
        assertEquals( teamCoffee.getName(), findByNameRecipe.getName() );
        assertEquals( teamCoffee.getPrice(), findByNameRecipe.getPrice() );
        final List<Ingredient> changedRecipeIngredients = findByNameRecipe.getIngredients();
        assertEquals( changedRecipeIngredients, findByNameRecipe.getIngredients() );

        // Checks if a null parameter is given should return null
        assertNull( recipeService.findById( null ) );

        final long wrongId = 0;
        assertNull( recipeService.findById( wrongId ) );

    }

    /**
     * Tests the RecipeService class
     *
     * @author Yash Agarwal yagarwa2
     */
    @Test
    @Transactional
    public void testExistById () {
        final Recipe teamCoffee = new Recipe();

        teamCoffee.setName( "Team Coffee" );
        teamCoffee.setPrice( 350 );
        final Ingredient coffee = new Ingredient( "Coffee", 2 );

        final Ingredient milk = new Ingredient( "Milk", 1 );

        final Ingredient pumpkinSpice = new Ingredient( "Pumpkin Spice", 1 );

        final Ingredient sugar = new Ingredient( "Sugar", 1 );
        teamCoffee.addIngredient( sugar );
        teamCoffee.addIngredient( milk );
        teamCoffee.addIngredient( pumpkinSpice );
        teamCoffee.addIngredient( coffee );

        recipeService.save( teamCoffee );

        assertTrue( recipeService.existsById( teamCoffee.getId() ) );

        // Check for an ID that doesn't exist
        final long wrongId = 0;
        assertFalse( recipeService.existsById( wrongId ) );

    }

    /**
     * Tests if the udpated recipe works with different amount of recipes in a
     * different order as well as the original recipe fully covers all lines of
     * the update Recipe method.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void testUpdateRecipe () throws Exception {
        final Recipe teamCoffee = new Recipe();

        teamCoffee.setName( "Team Coffee" );
        teamCoffee.setPrice( 350 );
        final Ingredient coffee = new Ingredient( "Coffee", 2 );

        final Ingredient milk = new Ingredient( "Milk", 1 );

        final Ingredient pumpkinSpice = new Ingredient( "Pumpkin Spice", 1 );

        final Ingredient sugar = new Ingredient( "Sugar", 1 );

        teamCoffee.addIngredient( sugar );
        teamCoffee.addIngredient( milk );
        teamCoffee.addIngredient( pumpkinSpice );
        teamCoffee.addIngredient( coffee );

        recipeService.save( teamCoffee );

        final Recipe findByNameRecipe = recipeService.findByName( "Team Coffee" );
        assertEquals( teamCoffee.getName(), findByNameRecipe.getName() );
        assertEquals( teamCoffee.getPrice(), findByNameRecipe.getPrice() );
        final List<Ingredient> ingredientsTeam = teamCoffee.getIngredients();

        assertEquals( ingredientsTeam, findByNameRecipe.getIngredients() );

        assertTrue( findByNameRecipe.getIngredients().contains( sugar ) );
        assertTrue( findByNameRecipe.getIngredients().contains( milk ) );
        assertTrue( findByNameRecipe.getIngredients().contains( pumpkinSpice ) );
        assertTrue( findByNameRecipe.getIngredients().contains( coffee ) );

        // new Coffee that we are going to update to

        final Recipe newCoffee = new Recipe();

        newCoffee.setName( "New Coffee" );
        newCoffee.setPrice( 500 );
        final Ingredient coffee2 = new Ingredient( "Coffee", 4 );

        final Ingredient milk2 = new Ingredient( "Milk", 2 );

        final Ingredient pumpkinSpice2 = new Ingredient( "Pumpkin Spice", 2 );

        final Ingredient sugar2 = new Ingredient( "Sugar", 2 );

        final Ingredient syrup2 = new Ingredient( "Syrup", 2 );

        newCoffee.addIngredient( coffee2 );
        newCoffee.addIngredient( milk2 );
        newCoffee.addIngredient( pumpkinSpice2 );
        newCoffee.addIngredient( sugar2 );
        newCoffee.addIngredient( syrup2 );

        teamCoffee.updateRecipe( newCoffee );

        recipeService.save( teamCoffee );

        final Recipe updatedRecipe = recipeService.findByName( "New Coffee" );

        assertEquals( updatedRecipe.getName(), newCoffee.getName() );
        assertEquals( updatedRecipe.getPrice(), newCoffee.getPrice() );
        assertEquals( 5, updatedRecipe.getIngredients().size() );

        final List<Ingredient> updated = updatedRecipe.getIngredients();

        assertTrue( updated.toString().contains( coffee2.getName() ) );
        assertTrue( updated.toString().contains( milk2.getName() ) );
        assertTrue( updated.toString().contains( pumpkinSpice2.getName() ) );
        assertTrue( updated.toString().contains( sugar2.getName() ) );
        assertTrue( updated.toString().contains( syrup2.getName() ) );
        teamCoffee.deleteIngredient( syrup2 );
        recipeService.save( teamCoffee );

        final Recipe updatedRecipe2 = recipeService.findByName( "New Coffee" );
        assertEquals( 4, updatedRecipe2.getIngredients().size() );

        final List<Ingredient> updated2 = updatedRecipe2.getIngredients();

        assertTrue( updated2.toString().contains( coffee2.getName() ) );
        assertTrue( updated2.toString().contains( milk2.getName() ) );
        assertTrue( updated2.toString().contains( pumpkinSpice2.getName() ) );
        assertTrue( updated2.toString().contains( sugar2.getName() ) );
        // we deleted this ingredient
        assertFalse( updated2.toString().contains( syrup2.getName() ) );

    }
}
