package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;
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
 * @author Aditi Singh
 * @author Harshita Gupta
 * @author Sumaita Rahman
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
    private UserService  userService;
    /**
     * OrderService object, to be autowired in by Spring to allow for
     * manipulating the Order model
     */
    @Autowired
    private OrderService orderService;

    /**
     * REST API method to provide GET access to a specific users, as indicated
     * by the path variable provided (the name of the user desired)
     *
     * @param id
     *            user id
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/users/{id}" )
    public ResponseEntity getUser ( @PathVariable ( "id" ) final Long id ) {
        final User user = userService.findById( id );
        return null == user
                ? new ResponseEntity( errorResponse( "No user found with id: " + id ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( user, HttpStatus.OK );
    }

    /**
     * REST API method to provide GET access to a specific user's orders, as
     * indicated by the path variable provided (the name of the user desired)
     *
     * @param id
     *            user id
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/users/{id}/orders" )
    public ResponseEntity getUserOrders ( @PathVariable ( "id" ) final Long id ) {
        final User user = userService.findById( id );
        return null == user
                ? new ResponseEntity( errorResponse( "No user found with id: " + id ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( user.getOrders(), HttpStatus.OK );
    }

    /**
     * REST API method to allow creating a User by making a POST request to the
     * API endpoint and indicating the order to be updated (as a path variable)
     *
     * @param user
     *            The user we need to add
     * @return Success if the order could be added
     */
    @PostMapping ( BASE_PATH + "/users" )
    public ResponseEntity addUser ( @RequestBody final User user ) {
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println( "USER FROM FRONT END" + user.toString() );
        final User u = new User( user.getUsername(), encoder.encode( user.getPassword() ), user.getRole() );
        System.out.println( "USER FROM BACK END" + u.toString() );
        userService.save( u );
        return new ResponseEntity( successResponse( " successfully added user with id " + u.getId() ), HttpStatus.OK );
    }

    /**
     * REST API method to allow a user to place an Order from the CoffeeMaker's
     * Inventory, by making a POST request to the API endpoint and indicating
     * the order to be updated (as a path variable)
     *
     * @param order
     *            The order we need to add
     * @param userID
     *            The userId we want to add the order to
     * @return Success if the order could be added
     */
    @PostMapping ( BASE_PATH + "/users/{userID}/orders/" )

    public ResponseEntity placeUsersOrder ( @RequestBody final Order order,
            @PathVariable ( "userID" ) final Long userID ) {

        final User user = userService.findById( userID );
        if ( null == user ) {
            return new ResponseEntity( errorResponse( "No user found with id: " + userID ), HttpStatus.NOT_FOUND );
        }
        final Order o = new Order( order.getRecipes(), order.getPayment(), order.getPrice() );
        user.addOrder( order );
        orderService.save( order );
        userService.save( user );
        return new ResponseEntity( successResponse( o.getId() + " successfully placed" ), HttpStatus.OK );
    }

    /**
     * REST API method to provide GET access to all users in the system
     *
     * @return JSON representation of all users
     */
    @GetMapping ( BASE_PATH + "/users" )
    public List<User> getAllUsers () {
        return userService.findAll();
    }

}
