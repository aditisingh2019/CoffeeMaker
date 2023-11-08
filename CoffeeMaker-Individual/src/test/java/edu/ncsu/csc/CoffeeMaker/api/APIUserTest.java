package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIUserTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService           userservice;

    @Autowired
    private RecipeService         recipeService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

    }

    @Test
    @Transactional
    public void testGetUser () throws Exception {
        final User user1 = new User();
        user1.setUserName( "john1" );
        user1.setPasswordHash( "password1" );

        mvc.perform( post( "/api/v1/users/" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( user1 ) ) );
        userservice.save( user1 );

        Assertions.assertEquals( 1, (int) userservice.count() );
        final Long id1 = user1.getId();

        final String users = mvc.perform( get( "/api/v1/users/" + id1 ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        assertTrue( users.contains( "john1" ) );

        final User user2 = new User();
        user1.setUserName( "john2" );
        user1.setPasswordHash( "password2" );
        final User user3 = new User();
        user1.setUserName( "john3" );
        user1.setPasswordHash( "password3" );

        mvc.perform( post( "/api/v1/users/" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( user2 ) ) );
        userservice.save( user2 );

        mvc.perform( post( "/api/v1/users/" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( user3 ) ) );

        userservice.save( user2 );
        userservice.save( user3 );

        Assertions.assertEquals( 3, (int) userservice.count() );

    }

    @Test
    @Transactional
    public void testGetUserOrders () throws Exception {
        final User user1 = new User();
        user1.setUserName( "john1" );
        user1.setPasswordHash( "password1" );

        mvc.perform( post( "/api/v1/users/" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( user1 ) ) );
        userservice.save( user1 );

        Assertions.assertEquals( 1, (int) userservice.count() );
        final Long id1 = user1.getId();

        final String users = mvc.perform( get( "/api/v1/users/" + id1 ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        assertTrue( users.contains( "john1" ) );

        // creating a recipe
        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );

        final Ingredient coffeeIngr = new Ingredient( "Coffee", 10 );
        final Ingredient milkIngr = new Ingredient( "Milk", 20 );
        final Ingredient sugarIngr = new Ingredient( "Sugar", 5 );
        final Ingredient chocolateIngr = new Ingredient( "Chocolate", 1 );

        recipe.addIngredient( coffeeIngr );
        recipe.addIngredient( milkIngr );
        recipe.addIngredient( sugarIngr );
        recipe.addIngredient( chocolateIngr );

        recipe.setPrice( 5 );

        recipeService.save( recipe );

        // creating an order
        final Order order1 = new Order();
        order1.setPayment( 10 );
        // recipe added the order
        order1.addRecipe( recipe );

        // posting the order to orders
        mvc.perform( post( "/api/v1/orders/" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( order1 ) ) );
        // posting the orders of a user to the user
        mvc.perform( post( "/api/v1/users/" + id1 + "/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( order1 ) ) );
        // trying to get the orders of a user from users
        final String userOrders = mvc.perform( get( "/api/v1/users/" + id1 + "/orders" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // figure out the assert for this
        assertTrue( userservice.count() == 1 );
        System.out.println( "**********USER ORDERS************" + userOrders );

    }
}
