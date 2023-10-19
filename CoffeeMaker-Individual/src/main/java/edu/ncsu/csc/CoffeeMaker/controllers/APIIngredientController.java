package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Ingredients.
 *
 * @author Yash Agarwal, Pratik Bairoliya, Anika Bhadriraju
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIIngredientController extends APIController {

    /**
     * IngredientService object, to be autowired in by Spring to allow for
     * manipulating the Ingredient model
     */
    @Autowired
    private IngredientService service;

    /**
     * RecipeService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private RecipeService     recipeService;

    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private InventoryService  inventoryService;

    /**
     * REST API method to provide GET access to all ingredients in the system
     *
     * @return JSON representation of all ingredients
     */
    @GetMapping ( BASE_PATH + "/ingredients" )
    public List<Ingredient> getIngredients () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to a specific ingredient, as
     * indicated by the path variable provided (the name of the ingredient
     * desired)
     *
     * @param name
     *            ingredient name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/ingredients/{name}" )
    public ResponseEntity getIngredient ( @PathVariable ( "name" ) final String name ) {

        final Ingredient ingr = service.findByIngredient( name );
        return null == ingr
                ? new ResponseEntity( errorResponse( "No ingredient found with name " + name ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( ingr, HttpStatus.OK );
    }

    /**
     * REST API method to post a specific ingredient to the given recipe as
     * indicated by the path variable provided (the name of the recipe to post
     * to)
     *
     * @param path
     *            recipe name
     * @param ingr
     *            ingredient to add
     * @return response to the request
     */
    @PostMapping ( BASE_PATH + "/recipes/{name}/ingredients" )
    public ResponseEntity postIngredientRecipe ( @PathVariable ( "name" ) final String path,
            @RequestBody final Ingredient ingr ) {
        final Recipe r = recipeService.findByName( path );
        r.addIngredient( ingr );
        recipeService.save( r );
        return new ResponseEntity( successResponse( ingr.getName() + " successfully saved to " + path ),
                HttpStatus.OK );
    }

    /**
     * REST API method to post a specific ingredient to the given inventory.
     * This is used to create a new ingredient by automatically converting the
     * JSON RequestBody provided to a ingredient object. Invalid JSON will fail.
     *
     * @param ingr
     *            ingredient to post to inventory
     * @return response to the request
     */
    @PostMapping ( BASE_PATH + "/inventory/ingredients" )
    public ResponseEntity postIngredientInventory ( @RequestBody final Ingredient ingr ) {

        final Inventory ivt = inventoryService.getInventory();

        try {

            ivt.addIngredient( ingr );

        }
        catch ( final Exception e ) {
            if ( e.getMessage().equals( "Amount cannot be negative" ) ) {
                return new ResponseEntity( errorResponse( "No ingredient was added due to amount being negative" ),
                        HttpStatus.BAD_REQUEST );
            }
            else if ( e.getMessage().equals( "Duplicate Ingredient trying to be added" ) ) {
                return new ResponseEntity( errorResponse( "Duplicate Ingredient trying to be added" ),
                        HttpStatus.CONFLICT );
            }

        }
        inventoryService.save( ivt );
        return new ResponseEntity( successResponse( ingr.getName() + " successfully saved to Inventory" ),
                HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the ingredient model. This is
     * used to create a new ingredient by automatically converting the JSON
     * RequestBody provided to a ingredient object. Invalid JSON will fail.
     *
     * @param ingr
     *            The valid Ingredient to be saved.
     * @return ResponseEntity indicating success if the ingredient can be saved
     *         or an error is returned
     */
    @PostMapping ( BASE_PATH + "/ingredients" )
    public ResponseEntity createIngredient ( @RequestBody final Ingredient ingr ) {

        // if ( service.findByIngredient( ingr.getIngredient() )!= null ) {
        //
        // return new ResponseEntity(
        // errorResponse( "Ingredient with the name " + ingr.getIngredient() + "
        // already exists" ),
        // HttpStatus.CONFLICT );
        // }

        service.save( new Ingredient( ingr.getName(), ingr.getAmount() ) );
        return new ResponseEntity( successResponse( ingr.getName() + " successfully created" ), HttpStatus.OK );

    }

    /**
     * REST API method to allow deleting a ingredient from the database, by
     * making a DELETE request to the API endpoint and indicating the ingredient
     * to delete (as a path variable)
     *
     * @param name
     *            The name of the ingredient to delete
     * @return Success if the ingredient could be deleted; an error if the
     *         ingredient does not exist
     */
    @DeleteMapping ( BASE_PATH + "/ingredients/{name}" )
    public ResponseEntity deleteIngredient ( @PathVariable final String name ) {
        final Ingredient ingr = service.findByIngredient( name );
        if ( null == ingr ) {
            return new ResponseEntity( errorResponse( "No ingredient found for name " + name ), HttpStatus.NOT_FOUND );
        }
        service.delete( ingr );

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }

    /**
     * REST API endpoint to provide update access to CoffeeMaker's ingredients.
     * This will update the ingredients of the CoffeeMaker by changing the
     * amount of the specific ingredients.
     *
     * @param ingr
     *            ingredient that we need to update to
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/ingredients" )
    public ResponseEntity updateIngredient ( @RequestBody final Ingredient ingr ) {
        final Ingredient currIngr = service.findByIngredient( ingr.getName() );
        currIngr.setAmount( ingr.getAmount() );
        service.save( currIngr );
        return new ResponseEntity( currIngr, HttpStatus.OK );
    }

}
