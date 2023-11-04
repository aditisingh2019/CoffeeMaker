package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

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
public class APIUserController extends APIController {

    /**
     * UserService object, to be autowired in by Spring to allow for
     * manipulating the User model
     */
    @Autowired
    private UserService service;

    /**
     * REST API method to provide GET access to a specific users, as indicated
     * by the path variable provided (the name of the user desired)
     *
     * @param name
     *            user name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/users/{id}" )
    public ResponseEntity getUser ( @PathVariable ( "id" ) final Long id ) {
        final User user = service.findById( id );
        return null == user
                ? new ResponseEntity( errorResponse( "No user found with id: " + id ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( user, HttpStatus.OK );
    }

    /**
     * REST API method to provide GET access to a specific user's orders, as
     * indicated by the path variable provided (the name of the user desired)
     *
     * @param name
     *            user name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/users/{id}/orders" )
    public ResponseEntity getUserOrders ( @PathVariable ( "id" ) final Long id ) {
        final User user = service.findById( id );
        return null == user
                ? new ResponseEntity( errorResponse( "No user found with id: " + id ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( user.getOrders(), HttpStatus.OK );
    }

}
