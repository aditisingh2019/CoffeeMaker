package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    @Autowired
    private RecipeService service;

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        final Ingredient coffee = new Ingredient( "Coffee", 2 );

        final Ingredient milk = new Ingredient( "Milk", 1 );

        final Ingredient pumpkinSpice = new Ingredient( "Pumpkin Spice", 1 );

        final Ingredient sugar = new Ingredient( "Sugar", 1 );
        r1.addIngredient( sugar );
        r1.addIngredient( milk );
        r1.addIngredient( pumpkinSpice );
        r1.addIngredient( coffee );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );

        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
    }

    @Test
    @Transactional
    public void testNoRecipes () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        final Ingredient coffee = new Ingredient( "Coffee", -12 );

        final Ingredient milk = new Ingredient( "Milk", 1 );

        final Ingredient pumpkinSpice = new Ingredient( "Pumpkin Spice", 1 );

        final Ingredient sugar = new Ingredient( "Sugar", 1 );
        r1.addIngredient( sugar );
        r1.addIngredient( milk );
        r1.addIngredient( pumpkinSpice );
        r1.addIngredient( coffee );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r1.addIngredient( sugar );
        r1.addIngredient( milk );
        r1.addIngredient( pumpkinSpice );
        r1.addIngredient( coffee );

        final List<Recipe> recipes = List.of( r1, r2 );

        try {
            service.saveAll( recipes );
            Assertions.assertEquals( 0, service.count(),
                    "Trying to save a collection of elements where one is invalid should result in neither getting saved" );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof ConstraintViolationException );
        }

    }

    @Test
    @Transactional
    public void testAddRecipe1 () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
        Assertions.assertNotNull( service.findByName( name ) );

    }

    /* Test2 is done via the API for different validation */

    @Test
    @Transactional
    public void testAddRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, -50, 3, 1, 1, 0 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative price" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe4 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, -3, 1, 1, 2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of coffee" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe5 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, -1, 1, 2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of milk" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe6 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, -1, 2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of sugar" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe7 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, -2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of chocolate" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe13 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );

    }

    @Test
    @Transactional
    public void testAddRecipe14 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

    }

    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        // Adding and deleting one recipe

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        final List<Recipe> dbRecipesBeforeDelete = service.findAll();
        assertEquals( 1, dbRecipesBeforeDelete.size() );

        final Recipe dbRecipe = dbRecipesBeforeDelete.get( 0 );

        assertEquals( r1.getName(), dbRecipe.getName() );
        assertEquals( r1.getPrice(), dbRecipe.getPrice() );
        assertEquals( r1.getIngredients(), dbRecipe.getIngredients() );

        final Recipe findByNameRecipe = service.findByName( "Coffee" );
        assertEquals( r1.getName(), findByNameRecipe.getName() );
        assertEquals( r1.getPrice(), findByNameRecipe.getPrice() );
        assertEquals( r1.getIngredients(), findByNameRecipe.getIngredients() );

        service.delete( r1 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final List<Recipe> dbRecipes = service.findAll();
        assertEquals( 0, dbRecipes.size() );

    }

    @Test
    @Transactional
    public void testDeleteRecipe2 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        // Adding and deleting 3 recipes
        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        final List<Recipe> dbRecipesBeforeDelete = service.findAll();
        assertEquals( 3, dbRecipesBeforeDelete.size() );

        final Recipe dbRecipe = dbRecipesBeforeDelete.get( 0 );

        assertEquals( r1.getName(), dbRecipe.getName() );
        assertEquals( r1.getPrice(), dbRecipe.getPrice() );
        assertEquals( r1.getIngredients(), dbRecipe.getIngredients() );

        final Recipe findByNameRecipe = service.findByName( "Mocha" );
        assertEquals( r2.getName(), findByNameRecipe.getName() );
        assertEquals( r2.getPrice(), findByNameRecipe.getPrice() );
        assertEquals( r2.getIngredients(), findByNameRecipe.getIngredients() );

        service.deleteAll();

        Assertions.assertEquals( 0, service.count(), "`service.deleteAll()` should remove everything" );

        final List<Recipe> dbRecipes = service.findAll();
        assertEquals( 0, dbRecipes.size() );

    }

    @Test
    @Transactional
    public void testDeleteRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        // Deleting more than 1 but less than all and checking leftover recipes
        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        service.delete( r1 );
        service.delete( r2 );

        Assertions.assertEquals( 1, service.count(), "`service.delete() calls should only remove recipe 1 and 2" );

        final List<Recipe> dbRecipes = service.findAll();
        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        assertEquals( r3.getName(), dbRecipe.getName() );
        assertEquals( r3.getPrice(), dbRecipe.getPrice() );
        assertEquals( r3.getIngredients(), dbRecipe.getIngredients() );

        final Recipe findByNameRecipe = service.findByName( "Latte" );
        assertEquals( r3.getName(), findByNameRecipe.getName() );
        assertEquals( r3.getPrice(), findByNameRecipe.getPrice() );
        assertEquals( r3.getIngredients(), findByNameRecipe.getIngredients() );

    }

    @Test
    @Transactional
    public void testEditRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        r1.setPrice( 70 );

        service.save( r1 );

        final Recipe retrieved = service.findByName( "Coffee" );

        Assertions.assertEquals( 70, (int) retrieved.getPrice() );
        Assertions.assertEquals( 3, (int) retrieved.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( 1, (int) retrieved.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( 1, (int) retrieved.getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( 0, (int) retrieved.getIngredients().get( 3 ).getAmount() );

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't duplicate it" );

    }

    @Test
    @Transactional
    /**
     * Tests a recipe by saving it to the database then updating the recipe and
     * saving it and making sure the contents are updated Uses the update method
     * from Recipe.java
     *
     * @author Pratik Bairoliya pbairol@ncsu.edu
     */
    public void testUpdateRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        // Updating recipe using updateRecipe method
        // Checking database for update
        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe retrieved = service.findByName( "Coffee" );
        Assertions.assertEquals( r1.getPrice(), (int) retrieved.getPrice() );
        Assertions.assertEquals( r1.getIngredients().get( 0 ).getAmount(),
                (int) retrieved.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( r1.getIngredients().get( 1 ).getAmount(),
                (int) retrieved.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( r1.getIngredients().get( 2 ).getAmount(),
                (int) retrieved.getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( r1.getIngredients().get( 3 ).getAmount(),
                (int) retrieved.getIngredients().get( 3 ).getAmount() );

        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        r1.updateRecipe( r2 );
        service.save( r1 );

        final Recipe updatedRecipe = service.findByName( "Mocha" );
        Assertions.assertEquals( r2.getPrice(), (int) updatedRecipe.getPrice() );
        Assertions.assertEquals( r2.getIngredients().get( 0 ).getAmount(),
                (int) updatedRecipe.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( r2.getIngredients().get( 1 ).getAmount(),
                (int) updatedRecipe.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( r2.getIngredients().get( 2 ).getAmount(),
                (int) updatedRecipe.getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( r2.getIngredients().get( 3 ).getAmount(),
                (int) updatedRecipe.getIngredients().get( 3 ).getAmount() );
    }

    @Test
    @Transactional
    /**
     * Tests if two recipes are equal to each other and goes through all the if
     * conditions of the method in Recipe.java
     *
     * @author Pratik Bairoliya pbairol@ncsu.edu
     */
    public void testRecipeEquals () {
        // Testing equals method for a recipe
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        // Deleting more than 1 but less than all and checking leftover recipes
        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );

        // This recipe is actually the exact same
        final Recipe r2 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );

        Assertions.assertTrue( r1.equals( r2 ) );
        // This recipe is the exact same except the name hence why it is false
        // when we do .equals
        final Recipe r3 = createRecipe( "Mocha", 50, 3, 1, 1, 0 );
        Assertions.assertFalse( r1.equals( r3 ) );

        // This recipe is the exact same except the name is now null that is why
        // it is false when we do .equals()
        final Recipe r4 = null;
        Assertions.assertFalse( r1.equals( r4 ) );

        // this recipe is not even a recipe it is a seperate object and that is
        // why it is false when we do .equals()
        final Integer r5 = 2;
        Assertions.assertFalse( r1.equals( r5 ) );

        // this now compares a seperate recipe where the name is null but the
        // other recipe is not null.
        // (r6 is being compared to r1

        final Recipe r6 = createRecipe( null, 50, 3, 1, 1, 0 );
        Assertions.assertFalse( r6.equals( r1 ) );

    }

    @Test
    @Transactional
    /**
     * Tests the checkRecipe method and the hashCode method in Reciepe.java
     *
     * @author Pratik Bairoliya pbairol@ncsu.edu
     */
    public void testRecipeValid () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        // Checks the hash code and toString method for a recipe
        final Recipe r1 = createRecipe( "Coffee", 0, 0, 0, 0, 0 );

        service.save( r1 );
        Assertions.assertTrue( r1.checkRecipe() );

        final Recipe r2 = createRecipe( "Mocha", 50, 3, 2, 1, 1 );

        service.save( r2 );
        Assertions.assertFalse( r2.checkRecipe() );

        Assertions.assertEquals( "Mocha", r2.getName() );

        Assertions.assertNotEquals( r1.hashCode(), r2.hashCode() );

        final Recipe r3 = r1;
        Assertions.assertEquals( r3.hashCode(), r1.hashCode() );

    }

    @Transactional
    private Recipe createRecipe ( final String name, final int price, final Integer coffee, final Integer milk,
            final Integer sugar, final Integer chocolate ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        final Ingredient coffeeIngr = new Ingredient( "Coffee", coffee );
        final Ingredient milkIngr = new Ingredient( "Milk", milk );
        final Ingredient sugarIngr = new Ingredient( "Sugar", sugar );
        final Ingredient chocolateIngr = new Ingredient( "Chocolate", chocolate );

        recipe.addIngredient( coffeeIngr );
        recipe.addIngredient( milkIngr );
        recipe.addIngredient( sugarIngr );
        recipe.addIngredient( chocolateIngr );

        return recipe;
    }

}
