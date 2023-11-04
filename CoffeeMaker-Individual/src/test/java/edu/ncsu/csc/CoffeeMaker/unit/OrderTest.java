package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

public class OrderTest {

    private Order  order;
    private Recipe recipe1;
    private Recipe recipe2;

    @Before
    public void setUp () {
        // Create instances of Order and Recipe for testing
        order = new Order();
        recipe2 = new Recipe();

        recipe1 = new Recipe();

        recipe1.setName( "Coffee" );
        recipe1.setPrice( 50 );
        final Ingredient coffee = new Ingredient( "Coffee", 2 );

        final Ingredient milk = new Ingredient( "Milk", 1 );

        final Ingredient pumpkinSpice = new Ingredient( "Pumpkin Spice", 1 );

        final Ingredient sugar = new Ingredient( "Sugar", 1 );
        recipe1.addIngredient( sugar );
        recipe1.addIngredient( milk );
        recipe1.addIngredient( pumpkinSpice );
        recipe1.addIngredient( coffee );

        recipe2 = new Recipe();

        recipe2.setName( "Tea" );
        recipe2.setPrice( 30 );
        final Ingredient tea = new Ingredient( "Tea", 2 );

        recipe2.addIngredient( tea );
        recipe2.addIngredient( milk );
        recipe2.addIngredient( sugar );

    }

    @Test
    public void testAddRecipe () {
        order.addRecipe( recipe1 );
        assertEquals( 1, order.getRecipes().size() );
        assertTrue( order.getRecipes().contains( recipe1 ) );
    }

    @Test
    public void testAddRecipes () {
        order.addRecipes( recipe2, 3 );
        assertEquals( 3, order.getRecipes().size() );
        assertTrue( order.getRecipes().contains( recipe2 ) );
    }

    @Test
    public void testDeleteOneRecipe () {
        order.addRecipe( recipe1 );
        assertTrue( order.deleteOneRecipe( recipe1 ) );
        assertEquals( 0, order.getRecipes().size() );
    }

    @Test
    public void testDeleteAllOfOne () {
        order.addRecipe( recipe1 );
        order.addRecipe( recipe1 );
        order.addRecipe( recipe2 );
        order.deleteAllOfOne( recipe1 );
        assertEquals( 1, order.getRecipes().size() );
        assertFalse( order.getRecipes().contains( recipe1 ) );
    }

    @Test
    public void testOrderConstructor () {
        final List<Recipe> recipes = new ArrayList<>();
        recipes.add( recipe1 );
        recipes.add( recipe2 );
        final Order newOrder = new Order( recipes, 100 ); // Assuming payment of
                                                          // 100
        assertEquals( "Placed", newOrder.getStatus() );
        assertEquals( 2, newOrder.getRecipes().size() );
        assertEquals( 80, newOrder.getPrice().intValue() ); // Assuming recipe
                                                            // prices are 50 and
                                                            // 30
        assertEquals( 100, newOrder.getPayment().intValue() );
    }

    @Test
    public void testInsufficientPayment () {
        final List<Recipe> recipes = new ArrayList<>();
        recipes.add( recipe1 );
        try {
            new Order( recipes, 30 ); // Payment is less
                                      // than
            // the recipe price
        }
        catch ( final IllegalArgumentException e ) {
            Assertions.assertEquals( e.getMessage(), "Payment is not sufficient" );
        }

    }

    @Test
    public void testAddExistingRecipe () {
        order.addRecipe( recipe1 );
        order.addRecipe( recipe1 ); // Adding the same recipe again
        assertEquals( 2, order.getRecipes().size() );
    }

    @Test
    public void testDeleteNonExistingRecipe () {
        order.addRecipe( recipe1 );
        final Recipe nonExistingRecipe = new Recipe();
        final boolean result = order.deleteOneRecipe( nonExistingRecipe );
        assertFalse( result );
    }

    @Test
    public void testDeleteAllOfNonExistingRecipe () {
        order.addRecipe( recipe1 );
        final Recipe nonExistingRecipe = new Recipe();
        order.deleteAllOfOne( nonExistingRecipe );
        assertEquals( 1, order.getRecipes().size() );
    }

    @Test
    public void testAddRecipeWithIngredients () {
        order.addRecipe( recipe1 );
        // Check if the order contains all the ingredients of the added recipe
        assertTrue( order.getRecipes().get( 0 ).getIngredients().containsAll( recipe1.getIngredients() ) );
    }

    @Test
    public void testCancelOrder () {
        final Order order = new Order();
        order.cancelOrder();
        assertEquals( "Cancelled", order.getStatus() );
    }

    @Test
    public void testFulfillOrder () {
        final Order order = new Order();
        order.fulfillOrder();
        assertEquals( "In Progress", order.getStatus() );
    }

    @Test
    public void testNotifyCustomer () {
        final Order order = new Order();
        order.notifyCustomer();
        assertEquals( "Complete", order.getStatus() );
    }

    @Test
    public void testEmptyOrder () {
        assertEquals( 0, new Order().getRecipes().size() );
        assertEquals( 0, new Order().getPrice().intValue() );
        assertEquals( 0, new Order().getPayment().intValue() );
        assertEquals( "Placed", new Order().getStatus() );
    }

    @Test
    public void testEqualsDifferentOrder () {
        final Order order1 = new Order();
        final Order order2 = null;
        assertFalse( order1.equals( order2 ) );
    }

    @Test
    public void testEqualsSameOrder () {
        final Order order1 = new Order();
        final Order order2 = new Order();
        order2.setId( order1.getId() );
        order2.setPayment( order1.getPayment() );
        order2.setPrice();

        // added
        order2.setStatus( order1.getStatus() );
        assertTrue( order1.equals( order2 ) );
    }

    @Test
    public void testHashCodeConsistency () {
        final Order order = new Order();
        final int firstHashCode = order.hashCode();
        final int secondHashCode = order.hashCode();
        assertEquals( firstHashCode, secondHashCode );
    }

}
