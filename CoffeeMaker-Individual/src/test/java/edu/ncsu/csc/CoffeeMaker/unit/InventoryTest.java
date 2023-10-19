package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    @BeforeEach
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.updateInventory( "Coffee", 500 );
        ivt.updateInventory( "Chocolate", 500 );
        ivt.updateInventory( "Sugar", 500 );
        ivt.updateInventory( "Milk", 500 );

        inventoryService.save( ivt );
    }

    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        final Ingredient coffeeIngr = new Ingredient( "Coffee", 3 );
        final Ingredient milkIngr = new Ingredient( "Milk", 4 );
        final Ingredient sugarIngr = new Ingredient( "Sugar", 8 );
        final Ingredient chocolateIngr = new Ingredient( "Chocolate", 5 );

        recipe.addIngredient( coffeeIngr );
        recipe.addIngredient( milkIngr );
        recipe.addIngredient( sugarIngr );
        recipe.addIngredient( chocolateIngr );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals( 497, i.getIngredientAmount( "Coffee" ) );
        Assertions.assertEquals( 496, i.getIngredientAmount( "Milk" ) );
        Assertions.assertEquals( 492, i.getIngredientAmount( "Sugar" ) );
        Assertions.assertEquals( 495, i.getIngredientAmount( "Chocolate" ) );
    }

    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();
        ivt.updateInventory( "Coffee", 5 );
        ivt.updateInventory( "Chocolate", 3 );
        ivt.updateInventory( "Sugar", 7 );
        ivt.updateInventory( "Milk", 2 );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 505, ivt.getIngredientAmount( "Coffee" ),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 502, ivt.getIngredientAmount( "Milk" ),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 507, ivt.getIngredientAmount( "Sugar" ),
                "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 503, ivt.getIngredientAmount( "Chocolate" ),
                "Adding to the inventory should result in correctly-updated values chocolate" );

    }

    @Test
    @Transactional
    public void testAddInventory2 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.updateInventory( "Coffee", -5 );
            ivt.updateInventory( "Chocolate", 3 );
            ivt.updateInventory( "Sugar", 7 );
            ivt.updateInventory( "Milk", 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, ivt.getIngredientAmount( "Coffee" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 500, ivt.getIngredientAmount( "Milk" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 500, ivt.getIngredientAmount( "Sugar" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 500, ivt.getIngredientAmount( "Chocolate" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate" );
        }
    }

    @Test
    @Transactional
    public void testAddInventory3 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.updateInventory( "Chocolate", -3 );
            ivt.updateInventory( "Coffee", 5 );

            ivt.updateInventory( "Sugar", 7 );
            ivt.updateInventory( "Milk", 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, ivt.getIngredientAmount( "Coffee" ) );
            Assertions.assertEquals( 500, ivt.getIngredientAmount( "Milk" ) );

            Assertions.assertEquals( 500, ivt.getIngredientAmount( "Sugar" ) );
            Assertions.assertEquals( 500, ivt.getIngredientAmount( "Chocolate" ),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void testAddInventory4 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.updateInventory( "Sugar", -7 );
            ivt.updateInventory( "Coffee", 5 );
            ivt.updateInventory( "Chocolate", 3 );

            ivt.updateInventory( "Milk", 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, ivt.getIngredientAmount( "Coffee" ),
                    "Trying to update the Inventory with an invalid value for Sugar should result in no changes -- coffee" );
            Assertions.assertEquals( 500, ivt.getIngredientAmount( "Milk" ),
                    "Trying to update the Inventory with an invalid value for Sugar should result in no changes -- milk" );
            Assertions.assertEquals( 500, ivt.getIngredientAmount( "Sugar" ),
                    "Trying to update the Inventory with an invalid value for Sugar should result in no changes -- sugar" );
            Assertions.assertEquals( 500, ivt.getIngredientAmount( "Chocolate" ),
                    "Trying to update the Inventory with an invalid value for Sugar should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void testAddInventory5 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.updateInventory( "Milk", -2 );
            ivt.updateInventory( "Coffee", 5 );
            ivt.updateInventory( "Chocolate", 3 );
            ivt.updateInventory( "Sugar", 7 );

        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, ivt.getIngredientAmount( "Coffee" ),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- coffee" );
            Assertions.assertEquals( 500, ivt.getIngredientAmount( "Milk" ),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- milk" );
            Assertions.assertEquals( 500, ivt.getIngredientAmount( "Sugar" ),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- sugar" );
            Assertions.assertEquals( 500, ivt.getIngredientAmount( "Chocolate" ),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void testGetIngredientFromInventory () {
        final Inventory ivt = inventoryService.getInventory();
        Ingredient ingr = inventoryService.getIngredient( "Milk" );
        assertEquals( ingr.getAmount(), 500 );

        try {
            ivt.updateInventory( "Milk", 5 );
            ivt.updateInventory( "Coffee", 5 );
            ivt.updateInventory( "Chocolate", 3 );
            ivt.updateInventory( "Sugar", 7 );

        }
        catch ( final IllegalArgumentException iae ) {

        }

        ingr = inventoryService.getIngredient( "Milk" );
        assertEquals( ingr.getAmount(), 505 );

    }

    @Test
    @Transactional
    /**
     * @author Anika Bhadriraju Tests the enoughIngredients method for the
     *         inventory class. Tries a recipe where ingredients in the
     *         inventory are not enough.
     */
    public void testCheckEnough () {
        final Inventory ivt = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Mocha" );
        final Ingredient coffee1 = new Ingredient( "Coffee", 1 );
        final Ingredient milk1 = new Ingredient( "Milk", 20 );
        final Ingredient sugar1 = new Ingredient( "Sugar", 5 );
        final Ingredient chocolate1 = new Ingredient( "Chocolate", 10 );
        recipe.addIngredient( coffee1 );
        recipe.addIngredient( milk1 );
        recipe.addIngredient( sugar1 );
        recipe.addIngredient( chocolate1 );

        recipe.setPrice( 5 );

        Assertions.assertTrue( ivt.enoughIngredients( recipe ) );

        final Recipe recipeNotEnoughCoffee = new Recipe();
        recipeNotEnoughCoffee.setName( "Coffee" );

        final Ingredient coffee2 = new Ingredient( "Coffee", 501 );
        final Ingredient milk2 = new Ingredient( "Milk", 20 );
        final Ingredient sugar2 = new Ingredient( "Sugar", 5 );
        final Ingredient chocolate2 = new Ingredient( "Chocolate", 10 );
        recipeNotEnoughCoffee.addIngredient( coffee2 );
        recipeNotEnoughCoffee.addIngredient( milk2 );
        recipeNotEnoughCoffee.addIngredient( sugar2 );
        recipeNotEnoughCoffee.addIngredient( chocolate2 );

        recipeNotEnoughCoffee.setPrice( 5 );
        Assertions.assertFalse( ivt.enoughIngredients( recipeNotEnoughCoffee ) );

        final Recipe recipeNotEnoughMilk = new Recipe();

        final Ingredient coffee3 = new Ingredient( "Coffee", 1 );
        final Ingredient milk3 = new Ingredient( "Milk", 501 );
        final Ingredient sugar3 = new Ingredient( "Sugar", 5 );
        final Ingredient chocolate3 = new Ingredient( "Chocolate", 10 );
        recipeNotEnoughMilk.addIngredient( coffee3 );
        recipeNotEnoughMilk.addIngredient( milk3 );
        recipeNotEnoughMilk.addIngredient( sugar3 );
        recipeNotEnoughMilk.addIngredient( chocolate3 );
        recipeNotEnoughMilk.setName( "Milk Heavy" );

        recipe.setPrice( 5 );
        Assertions.assertFalse( ivt.enoughIngredients( recipeNotEnoughMilk ) );

        final Recipe recipeNotEnoughSugar = new Recipe();

        final Ingredient coffee4 = new Ingredient( "Coffee", 1 );
        final Ingredient milk4 = new Ingredient( "Milk", 20 );
        final Ingredient sugar4 = new Ingredient( "Sugar", 501 );
        final Ingredient chocolate4 = new Ingredient( "Chocolate", 10 );
        recipeNotEnoughSugar.addIngredient( coffee4 );
        recipeNotEnoughSugar.addIngredient( milk4 );
        recipeNotEnoughSugar.addIngredient( sugar4 );
        recipeNotEnoughSugar.addIngredient( chocolate4 );

        recipeNotEnoughSugar.setName( "Sugar Heavy" );

        recipe.setPrice( 5 );
        Assertions.assertFalse( ivt.enoughIngredients( recipeNotEnoughSugar ) );

        final Recipe recipeNotEnoughChocolate = new Recipe();
        recipeNotEnoughChocolate.setName( "Chocolate Heavy" );
        final Ingredient coffee5 = new Ingredient( "Coffee", 1 );
        final Ingredient milk5 = new Ingredient( "Milk", 20 );
        final Ingredient sugar5 = new Ingredient( "Sugar", 501 );
        final Ingredient chocolate5 = new Ingredient( "Chocolate", 501 );
        recipeNotEnoughChocolate.addIngredient( coffee5 );
        recipeNotEnoughChocolate.addIngredient( milk5 );
        recipeNotEnoughChocolate.addIngredient( sugar5 );
        recipeNotEnoughChocolate.addIngredient( chocolate5 );

        recipe.setPrice( 5 );
        Assertions.assertFalse( ivt.enoughIngredients( recipeNotEnoughChocolate ) );

    }

    @Test
    @Transactional
    /**
     * @author Anika Bhadriraju Tests the toString() method for the inventory
     *         class. Checks a valid inventory and an inventory with null values
     */
    public void testToString () {
        // tests toString with a valid inventory
        final Inventory ivt = inventoryService.getInventory();
        Assertions.assertEquals( "Coffee: 500\nMilk: 500\nSugar: 500\nChocolate: 500\n", ivt.toString() );

        // tests toString with a null inventory
        final Inventory ivtNull = new Inventory();
        Assertions.assertEquals( "", ivtNull.toString() );

    }

}
