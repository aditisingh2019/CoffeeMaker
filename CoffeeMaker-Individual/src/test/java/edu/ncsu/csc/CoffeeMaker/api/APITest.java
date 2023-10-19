
package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import javax.transaction.Transactional;

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
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {
    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

    }

    @Test
    @Transactional

    public void testGetRecipes () throws UnsupportedEncodingException, Exception {
        final String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        if ( !recipe.contains( "Mocha" ) ) {
            final Recipe teamCoffee = new Recipe();

            teamCoffee.setName( "Mocha" );
            teamCoffee.setPrice( 350 );

            final Ingredient coffeeIngr = new Ingredient( "Coffee", 2 );
            final Ingredient milkIngr = new Ingredient( "Milk", 1 );
            final Ingredient sugarIngr = new Ingredient( "Sugar", 1 );
            final Ingredient chocolateIngr = new Ingredient( "Chocolate", 1 );

            teamCoffee.addIngredient( coffeeIngr );
            teamCoffee.addIngredient( milkIngr );
            teamCoffee.addIngredient( sugarIngr );
            teamCoffee.addIngredient( chocolateIngr );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( teamCoffee ) ) ).andExpect( status().isOk() );
        }
        final String recipe2 = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( recipe2.contains( "Mocha" ) );

        final Inventory inventory = new Inventory();

        inventory.updateInventory( "Coffee", 50 );
        inventory.updateInventory( "Milk", 50 );
        inventory.updateInventory( "Sugar", 50 );
        inventory.updateInventory( "Chocolate", 50 );
        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( inventory ) ) ).andExpect( status().isOk() );
        final String result = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( result.contains( "50" ) );

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", "Mocha" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 500 ) ) ).andExpect( status().isOk() ).andDo( print() );

    }

}
