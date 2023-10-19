package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIIngredientTest {
    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private IngredientService     service;
    @Autowired
    private RecipeService         recipeService;
    @Autowired
    private InventoryService      inventoryService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
        recipeService.deleteAll();
        inventoryService.deleteAll();
    }

    @Test
    @Transactional
    public void postIngredientRecipe () throws Exception {
        service.deleteAll();
        recipeService.deleteAll();

        final Ingredient coffee = new Ingredient( "Coffee", 3 );
        final Ingredient sugar = new Ingredient( "Sugar", 4 );
        final Ingredient milk = new Ingredient( "Milk", 5 );

        final Recipe r = new Recipe();
        r.setName( "testCoffee" );
        recipeService.save( r );
        mvc.perform( post( "/api/v1/recipes/testCoffee/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( coffee ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/recipes/testCoffee/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( sugar ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/recipes/testCoffee/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( milk ) ) ).andExpect( status().isOk() );

        final Recipe dbR = recipeService.findByName( "testCoffee" );

        assertEquals( 3, dbR.getIngredients().size() );

    }

    @Test
    @Transactional
    public void postIngredientInventory () throws Exception {
        service.deleteAll();
        inventoryService.deleteAll();

        final Ingredient coffee = new Ingredient( "Coffee", 3 );
        final Ingredient sugar = new Ingredient( "Sugar", 4 );
        final Ingredient milk = new Ingredient( "Milk", 5 );

        final Inventory ivt = new Inventory();

        inventoryService.save( ivt );
        mvc.perform( post( "/api/v1/inventory/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( coffee ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/inventory/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( sugar ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/inventory/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( milk ) ) ).andExpect( status().isOk() );

        final Inventory dbI = inventoryService.findAll().get( 0 );
        assertEquals( 3, dbI.getIngredients().size() );

    }

    @Test
    @Transactional
    public void postIngredient () throws Exception {
        service.deleteAll();

        final Ingredient coffee = new Ingredient( "Coffee", 3 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( coffee ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void getIngredients () throws Exception {

        final Ingredient coffee = new Ingredient( "Coffee", 3 );
        final Ingredient milkIngr = new Ingredient( "Milk", 4 );
        final Ingredient chocolateIngr = new Ingredient( "Chocolate", 5 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( coffee ) ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( milkIngr ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( chocolateIngr ) ) ).andExpect( status().isOk() );

        final String ingredientList = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertTrue( ingredientList.contains( "Coffee" ) );
        assertTrue( ingredientList.contains( "Milk" ) );
        assertTrue( ingredientList.contains( "Chocolate" ) );
    }

    @Test
    @Transactional
    public void getSingleIngredient () throws Exception {

        final Ingredient coffeeIngr = new Ingredient( "Coffee", 3 );
        final Ingredient milkIngr = new Ingredient( "Milk", 4 );
        final Ingredient chocolateIngr = new Ingredient( "Chocoloate", 5 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( coffeeIngr ) ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( milkIngr ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( chocolateIngr ) ) ).andExpect( status().isOk() );

        final String ingredientList = mvc.perform( get( "/api/v1/ingredients/Coffee" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertTrue( ingredientList.contains( "Coffee" ) );
        assertFalse( ingredientList.contains( "Milk" ) );
    }

    @Test
    @Transactional
    public void updateIngredient () throws Exception {

        final Ingredient coffeeIngr = new Ingredient( "Coffee", 3 );
        final Ingredient milkIngr = new Ingredient( "Milk", 4 );
        Ingredient chocolateIngr = new Ingredient( "Chocolate", 5 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( coffeeIngr ) ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( milkIngr ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( chocolateIngr ) ) ).andExpect( status().isOk() );
        chocolateIngr = new Ingredient( "Chocolate", 10 );

        mvc.perform( put( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( chocolateIngr ) ) ).andExpect( status().isOk() );

        final String ingredientList = mvc.perform( get( "/api/v1/ingredients/Chocolate" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertTrue( ingredientList.contains( "10" ) );
    }

    @Test
    @Transactional
    public void deleteIngredient () throws Exception {

        final Ingredient coffeeIngr = new Ingredient( "Coffee", 3 );
        final Ingredient milkIngr = new Ingredient( "Milk", 4 );
        final Ingredient chocolateIngr = new Ingredient( "Chocolate", 5 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( coffeeIngr ) ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( milkIngr ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( chocolateIngr ) ) ).andExpect( status().isOk() );
        // delete by id and not name
        mvc.perform( delete( "/api/v1/ingredients/Chocolate" ) ).andExpect( status().isOk() );

        final String ingredientList = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertTrue( ingredientList.contains( "Coffee" ) );
        assertTrue( ingredientList.contains( "Milk" ) );
    }

}
