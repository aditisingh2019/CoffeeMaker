package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.List;

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
import edu.ncsu.csc.CoffeeMaker.services.OrderService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIOrderTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private OrderService          service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    @Test
    @Transactional
    public void creatingOrder1Recipe () throws Exception {

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

        final List<Recipe> recipes = new ArrayList<Recipe>();
        recipes.add( recipe );

        final Order order1 = new Order( recipes, 10 );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( order1 ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void creatingOrderManyRecipes () throws Exception {

        final Recipe teamCoffee = new Recipe();

        teamCoffee.setName( "Coffee" );
        teamCoffee.setPrice( 3 );
        final Ingredient coffee = new Ingredient( "Coffee", 2 );

        final Ingredient milk = new Ingredient( "Milk", 1 );

        final Ingredient pumpkinSpice = new Ingredient( "Pumpkin Spice", 1 );

        final Ingredient sugar = new Ingredient( "Sugar", 1 );
        teamCoffee.addIngredient( sugar );
        teamCoffee.addIngredient( milk );
        teamCoffee.addIngredient( pumpkinSpice );
        teamCoffee.addIngredient( coffee );

        final Recipe vanilla = new Recipe();
        vanilla.setName( "Coffee" );
        vanilla.setPrice( 4 );
        final Ingredient coffee2 = new Ingredient( "Coffee", 2 );

        final Ingredient milk2 = new Ingredient( "Milk", 1 );

        final Ingredient pumpkinSpice2 = new Ingredient( "Pumpkin Spice", 1 );

        final Ingredient sugar2 = new Ingredient( "Sugar", 1 );

        final Ingredient syrup2 = new Ingredient( "Syrup", 1 );
        vanilla.addIngredient( sugar2 );
        vanilla.addIngredient( milk2 );
        vanilla.addIngredient( syrup2 );
        vanilla.addIngredient( coffee2 );

        final Recipe pumpkinSpiceCoffee = new Recipe();
        pumpkinSpiceCoffee.setName( "Pumpkin Spice Coffee" );
        pumpkinSpiceCoffee.setPrice( 4 );
        pumpkinSpiceCoffee.addIngredient( milk2 );
        pumpkinSpiceCoffee.addIngredient( syrup2 );
        pumpkinSpiceCoffee.addIngredient( coffee2 );
        pumpkinSpiceCoffee.addIngredient( pumpkinSpice2 );

        final List<Recipe> recipes = new ArrayList<Recipe>();
        recipes.add( teamCoffee );
        recipes.add( vanilla );
        recipes.add( pumpkinSpiceCoffee );

        final Order bigOrder = new Order( recipes, 20 );
        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( bigOrder ) ) );

        Assertions.assertEquals( 1, (int) service.count() );
        Assertions.assertEquals( 3, service.findAll().get( 0 ).getRecipes().size() );
        Assertions.assertTrue( service.findAll().toString().contains( "Coffee" ) );
        try {
            new Order( recipes, 10 );
        }
        catch ( final IllegalArgumentException e ) {
            Assertions.assertEquals( e.getMessage(), "Payment is not sufficient" );
        }

    }

}
