package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {
    /** User id */
    @Id
    @GeneratedValue
    public Long               id;
    /** OrdersList */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Order> orders;

    public String             userName;
    public String             passwordHash;
    public String             userType;

    // Constructor
    public User ( final long id, final String userName, final String passwordHash ) {
        this.id = id;
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.userType = "None";
        this.orders = new ArrayList<Order>();
    }

    /**
     * @return the id
     */
    public Long getId () {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * @return the userName
     */
    public String getUserName () {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName ( final String userName ) {
        this.userName = userName;
    }

    /**
     * @return the passwordHash
     */
    public String getPasswordHash () {
        return passwordHash;
    }

    /**
     * @param passwordHash
     *            the passwordHash to set
     */
    public void setPasswordHash ( final String passwordHash ) {
        this.passwordHash = passwordHash;
    }

    // dummy
    public boolean authenticate ( final String userName, final String password ) {
        return false;
    }

    public void resetPassword ( final String newPassword ) {
        this.passwordHash = newPassword;
    }

    public void updateProfile ( final String userName, final String newPassword ) {
        this.userName = userName;
        this.passwordHash = newPassword;
    }

    public void setUerType ( final String type ) {
        this.userType = type;
    }

    public String getUserType () {
        return this.userType;
    }

    public List<Order> getOrders () {
        return this.orders;
    }

    public boolean placeOrder ( final Order order ) {
        orders.add( order );
        return true;
    }

    public void deleteOrder ( final Order order ) {
        orders.remove( order );
    }

}
