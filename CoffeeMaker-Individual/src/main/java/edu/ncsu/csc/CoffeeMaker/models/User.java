package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * User for the coffee maker. User is tied to the database using Hibernate
 * libraries. See UserRepository and UserService for the other two pieces used
 * for database support.
 *
 * @author Kai Presler-Marshall
 * @author Aditi Singh
 */
@Entity
public class User extends DomainObject {

    /** User id */
    @Id
    @GeneratedValue
    private Long              id;

    /** List of User's orders. */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Order> orders;

    /** User's username for authentication. */
    private String            username;
    /** User's password for authentication. */
    private String            password;
    /** User's type for permissions. */
    private String            role;

    /**
     * Base constructor
     */
    public User () {
        // setId( id );
        // setUserName( userName );
        // setPasswordHash( passwordHash );
        // setUserType( "None" );
        this.orders = new ArrayList<Order>();
    }

    /**
     * Constructs an instance of a User.
     *
     * @param id
     *            the id of the User
     * @param userName
     *            the username of the User
     * @param passwordHash
     *            the password of the USer
     */
    public User ( final long id, final String userName, final String passwordHash ) {
        // setId( id );
        setUsername( userName );
        setPassword( passwordHash );
        setRole( "None" );
        this.orders = new ArrayList<Order>();
    }

    /**
     * Constructs an instance of a User.
     *
     * @param orders
     *            the orders of the User
     * @param userName2
     *            the username of the User
     * @param passwordHash2
     *            the password of the USer
     * @param type
     *            the role of the User
     */
    public User ( final List<Order> orders, final String userName2, final String passwordHash2, final String type ) {
        setUsername( userName2 );
        setPassword( passwordHash2 );
        setRole( type );

        this.orders = orders;

    }

    /**
     * Constructs an instance of a User.
     *
     *
     * @param userName2
     *            the username of the User
     * @param passwordHash2
     *            the password of the USer
     * @param type
     *            the role of the User
     */
    public User ( final String userName2, final String passwordHash2, final String type ) {
        setUsername( userName2 );
        setPassword( passwordHash2 );
        setRole( type );

        this.orders = new ArrayList<Order>();

    }

    /**
     * Returns the User's id.
     *
     * @return the id
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the user (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the User's username.
     *
     * @return the userName
     */
    public String getUsername () {
        return username;
    }

    /**
     * Sets the User's username.
     *
     * @param userName
     *            the userName to set
     */
    public void setUsername ( final String userName ) {
        this.username = userName;
    }

    /**
     * Returns the User's password.
     *
     * @return the passwordHash
     */
    public String getPassword () {
        return password;
    }

    /**
     * Sets the User's password.
     *
     * @param password
     *            the password to set users password to
     */
    public void setPassword ( final String password ) {
        this.password = password;
    }

    /**
     * Authenticates the User.
     *
     * @param userName
     *            the username of the user to authenticate
     * @param password
     *            the password of the password to authenticate
     * @return true if the user is valid
     */
    public boolean authenticate ( final String userName, final String password ) {
        return false;
    }

    /**
     * Resets the User's password.
     *
     * @param newPassword
     *            the new password to set this user to
     */
    public void resetPassword ( final String newPassword ) {
        setPassword( newPassword );
    }

    /**
     * Updates the User's profile.
     *
     * @param userName
     *            the new username to update the user's profile
     * @param newPassword
     *            the new password to update the user's profile
     */
    public void updateProfile ( final String userName, final String newPassword ) {
        this.username = userName;
        this.password = newPassword;
    }

    /**
     * Sets the type of the User.
     *
     * @param role
     *            the type to set user role to
     */
    public void setRole ( final String role ) {
        this.role = role;
    }

    /**
     * Returns the User's type.
     *
     * @return the user type
     */
    public String getRole () {
        return this.role;
    }

    /**
     * Returns the list of orders.
     *
     * @return the orders
     */
    public List<Order> getOrders () {
        return this.orders;
    }

    /**
     * Places an order.
     *
     * @param order
     *            the order to place
     * @return true if the order is successfully placed
     */
    public boolean placeOrder ( final Order order ) {
        orders.add( order );
        return true;
    }

    /**
     * Deletes an order from the list of orders.
     *
     * @param order
     *            the order to delete
     */
    public void deleteOrder ( final Order order ) {
        orders.remove( order );
    }

    /**
     * Adds an order to the list of orders of the specified user
     *
     * @param order
     *            The order we are adding to the orders list.
     */
    public void addOrder ( final Order order ) {
        orders.add( order );
    }

}
