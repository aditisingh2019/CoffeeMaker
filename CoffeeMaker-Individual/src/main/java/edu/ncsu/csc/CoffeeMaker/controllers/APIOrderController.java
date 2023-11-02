package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Recipes.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 * @author Michelle Lemons
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIOrderController extends APIController {

    /**
     * RecipeService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private OrderService service;

    /**
     * REST API method to provide GET access to all orders in the system
     *
     * @return JSON representation of all ordera
     */
    @GetMapping ( BASE_PATH + "/orders" )
    public List<Order> getOrders () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to a specific orders, as indicated
     * by the path variable provided (the name of the recipe desired)
     *
     * @param name
     *            recipe name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/orders/{id}" )
    public ResponseEntity getOrder ( @PathVariable ( "id" ) final Long id ) {
        final Order order = service.findById( id );
        return null == order
                ? new ResponseEntity( errorResponse( "No order found with id: " + id ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( order, HttpStatus.OK );
    }

    /**
     * REST API method to allow creating a Order from the CoffeeMaker's
     * Inventory, by making a PUT request to the API endpoint and indicating the
     * order to be updated (as a path variable)
     *
     * @param o
     *            The order we need to add
     * @return Success if the order could be added
     */
    @PostMapping ( BASE_PATH + "/orders" )
    public ResponseEntity createOrder ( @RequestBody final Order order ) {

        final Order o = new Order();
        // for ( final Recipe r : order.getRecipes() ) {
        // o.addRecipe( r );
        // }

        service.save( o );

        return new ResponseEntity( successResponse( o.getId() + " successfully created" ), HttpStatus.OK );
    }

}
