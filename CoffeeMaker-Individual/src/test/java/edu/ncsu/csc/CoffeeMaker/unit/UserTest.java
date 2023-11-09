/**
 *
 */
package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.User;

public class UserTest {

    private User  user;
    private Order order1;
    private Order order2;

    @Before
    public void setUp () {
        user = new User( 1, "JohnDoe", "password" );

        order1 = new Order();
        order2 = new Order();

        final Recipe recipe1 = new Recipe();
        Recipe recipe2 = new Recipe();
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
        order1.addRecipe( recipe1 );
        order2.addRecipe( recipe2 );
    }

    @Test
    public void testUserId () {
        assertEquals( 1L, user.getId().longValue() );
    }

    @Test
    public void testUserName () {
        assertEquals( "JohnDoe", user.getUserName() );
        user.setUserName( "JaneSmith" );
        assertEquals( "JaneSmith", user.getUserName() );
    }

    @Test
    public void testPasswordHash () {
        assertEquals( "password", user.getPasswordHash() );
        user.setPasswordHash( "newPassword" );
        assertEquals( "newPassword", user.getPasswordHash() );
    }

    @Test
    public void testUserType () {
        assertEquals( "None", user.getUserType() );
        user.setUserType( "Admin" );
        assertEquals( "Admin", user.getUserType() );
    }

    @Test
    public void testGetOrders () {
        assertEquals( 0, user.getOrders().size() );

        user.placeOrder( order1 );
        user.placeOrder( order2 );

        final List<Order> orders = user.getOrders();
        assertEquals( 2, orders.size() );
        assertTrue( orders.contains( order1 ) );
        assertTrue( orders.contains( order2 ) );
    }

    @Test
    public void testPlaceOrder () {
        assertTrue( user.placeOrder( order1 ) );
        final List<Order> orders = user.getOrders();
        assertTrue( orders.contains( order1 ) );
        assertEquals( 1, orders.size() );
    }

    @Test
    public void testDeleteOrder () {
        user.placeOrder( order1 );
        user.placeOrder( order2 );

        user.deleteOrder( order1 );
        final List<Order> orders = user.getOrders();
        assertFalse( orders.contains( order1 ) );
        assertTrue( orders.contains( order2 ) );
        assertEquals( 1, orders.size() );
    }

    @Test
    public void testAuthenticate () {
        assertFalse( user.authenticate( "JohnDoe", "wrongPassword" ) );
        assertFalse( user.authenticate( "UnknownUser", "password" ) );
    }

    @Test
    public void testResetPassword () {
        user.resetPassword( "newPassword" );
        assertEquals( "newPassword", user.getPasswordHash() );
    }

    @Test
    public void testUpdateProfile () {
        user.updateProfile( "JaneSmith", "newPassword" );
        assertEquals( "JaneSmith", user.getUserName() );
        assertEquals( "newPassword", user.getPasswordHash() );
    }
}
