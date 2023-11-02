package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

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
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIRecipeTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RecipeService         service;

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
    public void ensureRecipe () throws Exception {
        service.deleteAll();

        final Recipe r = new Recipe();
        final Ingredient coffeeIngr = new Ingredient( "Coffee", 3 );
        final Ingredient milkIngr = new Ingredient( "Milk", 4 );
        final Ingredient sugarIngr = new Ingredient( "Sugar", 8 );
        final Ingredient chocolateIngr = new Ingredient( "Chocolate", 5 );

        r.addIngredient( coffeeIngr );
        r.addIngredient( milkIngr );
        r.addIngredient( sugarIngr );
        r.addIngredient( chocolateIngr );
        r.setPrice( 10 );
        r.setName( "Mocha" );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

        service.deleteAll();

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

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        service.save( r1 );

        final Recipe r2 = createRecipe( name, 50, 3, 1, 1, 0 );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testAddRecipe15 () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

        final Recipe r4 = createRecipe( "Hot Chocolate", 75, 0, 2, 1, 2 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        Assertions.assertEquals( 3, service.count(), "Creating a fourth recipe should not get saved" );
    }

    @Test
    @Transactional
    public void testDeleteRecipe () throws Exception {

        // Checks API call when no recipes exist
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals( "[]", recipe );

        // Cannot delete a recipe that doesn't exist, bad gateway error
        final String recipeDelete = mvc.perform( delete( "/api/v1/recipes/Coffee" ) ).andDo( print() )
                .andExpect( status().is4xxClientError() ).andReturn().getResponse().getContentAsString();

        assertTrue( recipeDelete.contains( "failed" ) );

    }

    @Test
    @Transactional
    public void testDeleteRecipe2 () throws Exception {

        // Deletes one recipe using an API Delete Call
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        final String recipes = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( recipes.contains( "Coffee" ) );
        assertTrue( recipes.contains( "Mocha" ) );
        assertTrue( recipes.contains( "Latte" ) );
        mvc.perform( delete( "/api/v1/recipes/Coffee" ) ).andExpect( status().isOk() );

        final String recipesDeleted = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertTrue( recipesDeleted.contains( "Mocha" ) );
        assertTrue( recipesDeleted.contains( "Latte" ) );
    }

    @Test
    @Transactional
    public void testDeleteRecipe3 () throws Exception {

        // Deletes all recipes using an API delete call
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        final String recipes = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( recipes.contains( "Coffee" ) );
        assertTrue( recipes.contains( "Mocha" ) );
        assertTrue( recipes.contains( "Latte" ) );

        mvc.perform( delete( "/api/v1/recipes/Coffee" ) ).andExpect( status().isOk() );
        mvc.perform( delete( "/api/v1/recipes/Mocha" ) ).andExpect( status().isOk() );
        mvc.perform( delete( "/api/v1/recipes/Latte" ) ).andExpect( status().isOk() );

        final String recipesDeleted = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertEquals( "[]", recipesDeleted );

    }

    @Transactional
    private Recipe createRecipe ( final String name, final Integer price, final Integer coffee, final Integer milk,
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

    @Test
    @Transactional
    public void testUpdateRecipe () throws Exception {

        // Deletes all recipes using an API delete call
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe teamCoffee = new Recipe();

        teamCoffee.setName( "Coffee" );
        teamCoffee.setPrice( 350 );
        final Ingredient coffee = new Ingredient( "Coffee", 2 );

        final Ingredient milk = new Ingredient( "Milk", 1 );

        final Ingredient pumpkinSpice = new Ingredient( "Pumpkin Spice", 1 );

        final Ingredient sugar = new Ingredient( "Sugar", 1 );
        teamCoffee.addIngredient( sugar );
        teamCoffee.addIngredient( milk );
        teamCoffee.addIngredient( pumpkinSpice );
        teamCoffee.addIngredient( coffee );
        service.save( teamCoffee );

        final Recipe vanilla = new Recipe();
        vanilla.setName( "Coffee" );
        vanilla.setPrice( 450 );
        final Ingredient coffee2 = new Ingredient( "Coffee", 2 );

        final Ingredient milk2 = new Ingredient( "Milk", 1 );

        final Ingredient pumpkinSpice2 = new Ingredient( "Pumpkin Spice", 1 );

        final Ingredient sugar2 = new Ingredient( "Sugar", 1 );

        final Ingredient syrup2 = new Ingredient( "Syrup", 1 );
        vanilla.addIngredient( sugar2 );
        vanilla.addIngredient( milk2 );
        vanilla.addIngredient( syrup2 );
        vanilla.addIngredient( coffee2 );
        vanilla.addIngredient( pumpkinSpice2 );

        final String recipes = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( recipes.contains( "Coffee" ) );

        mvc.perform( put( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( vanilla ) ) ).andExpect( status().isOk() );

        final String updatedRecipeString = mvc.perform( get( "/api/v1/recipes/Coffee" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertTrue( updatedRecipeString.contains( sugar2.getName() ) );
        assertTrue( updatedRecipeString.contains( milk2.getName() ) );
        assertTrue( updatedRecipeString.contains( syrup2.getName() ) );
        assertTrue( updatedRecipeString.contains( coffee2.getName() ) );
        assertTrue( updatedRecipeString.contains( pumpkinSpice2.getName() ) );

        final Recipe vanilla2 = new Recipe();
        vanilla2.setName( "Coffee" );
        vanilla2.setPrice( 450 );
        final Ingredient coffee3 = new Ingredient( "Coffee", 2 );

        final Ingredient milk3 = new Ingredient( "Milk", 1 );

        vanilla2.addIngredient( coffee3 );
        vanilla2.addIngredient( milk3 );

        mvc.perform( put( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( vanilla2 ) ) ).andExpect( status().isOk() );

        final String updatedRecipeString2 = mvc.perform( get( "/api/v1/recipes/Coffee" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertFalse( updatedRecipeString2.contains( sugar2.getName() ) );
        assertTrue( updatedRecipeString2.contains( milk2.getName() ) );
        assertFalse( updatedRecipeString2.contains( syrup2.getName() ) );
        assertTrue( updatedRecipeString2.contains( coffee2.getName() ) );
        assertFalse( updatedRecipeString2.contains( pumpkinSpice2.getName() ) );
    }

}
