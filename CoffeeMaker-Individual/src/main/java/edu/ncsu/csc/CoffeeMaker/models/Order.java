package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;

/**
 * Order for the coffee maker. Order is tied to the database using Hibernate
 * libraries.See OrderRepository and Order Service for the other two pieces used
 * for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
@Table ( name = "`order`" )
public class Order extends DomainObject {

    /** Order's id. */
    @Id
    @GeneratedValue
    private Long               id;

    /** List of recipes in the order. */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Recipe> recipes;

    /** Order's payment. */
    private Integer            payment;

    /** Order's price. */
    @Min ( 0 )
    private Integer            price;

    /** Order's status. */
    private String             status;

    /**
     * Creates a default order for the coffee maker.
     */
    public Order () {
        setStatus( "Placed" );
        this.recipes = new ArrayList<Recipe>();
        setPrice();
        setPayment( 0 );
    }

    /**
     * Constructs an instance of an Order with specific parameters
     *
     * @param recipes
     *            the list of recipes in the order
     * @param payment
     *            the payment for the order
     */
    public Order ( final List<Recipe> recipes, final Integer payment ) {
        setStatus( "Placed" );
        this.recipes = recipes;
        setPrice();
        setPayment( payment );

    }

    /**
     * Checks if the provided payment is valid for this order
     *
     * @param payment
     *            the payment to check
     * @return true if the payment is valid
     */
    public boolean checkPaymentValidity ( final Integer payment ) {
        if ( payment < getPrice() ) {
            return false;
        }
        return true;
    }

    /**
     * Returns the Order's id
     *
     * @return the id
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the Order's id
     *
     * @param id
     *            the id to set
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the recipes in the order
     *
     * @return the list of recipes
     */
    public List<Recipe> getRecipes () {
        return recipes;
    }

    /**
     * Deletes one instance of a recipe from the Order
     *
     * @param recipe
     *            the recipe to delete
     * @return true if the recipe is successfully deleted
     */
    public boolean deleteOneRecipe ( final Recipe recipe ) {
        for ( final Recipe r : recipes ) {
            if ( r.equals( recipe ) ) {
                recipes.remove( r );
                return true;
            }
        }
        return false;

    }

    /**
     * Deletes all instances of a recipe from the Order
     *
     * @param recipe
     *            the recipe to delete
     */
    public void deleteAllOfOne ( final Recipe recipe ) {
        if ( recipe.getName() == null || recipe.getName().length() == 0 ) {
            return;
        }
        while ( recipes.toString().contains( recipe.getName() ) ) {
            for ( int i = 0; i < recipes.size(); i++ ) {
                if ( recipes.get( i ).getName().equals( recipe.getName() ) ) {
                    recipes.remove( i );
                }
            }
        }

    }

    /**
     * Adds the passed in number of instances of a recipe to the Order
     *
     * @param recipe
     *            the recipe to add
     * @param quantity
     *            the number of instances of the recipe to add
     */
    public void addRecipes ( final Recipe recipe, final int quantity ) {
        for ( int i = 0; i < quantity; i++ ) {
            recipes.add( recipe );
        }
    }

    /**
     * Adds a single instance of a recipe to the Order
     *
     * @param recipe
     *            the recipe to add
     */
    public void addRecipe ( final Recipe recipe ) {
        recipes.add( recipe );
    }

    /**
     * Returns the payment of the Order
     *
     * @return the payment
     */
    public Integer getPayment () {
        return payment;
    }

    /**
     * Sets the payment of the Order
     *
     * @param payment
     *            the payment to set
     */
    public void setPayment ( final Integer payment ) {
        if ( !checkPaymentValidity( payment ) ) {
            throw new IllegalArgumentException( "Payment is not sufficient" );
        }
        this.payment = payment;

    }

    /**
     * Returns the price of the order
     *
     * @return the price
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the price of the order
     */
    public void setPrice () {
        int orderPrice = 0;
        for ( final Recipe r : recipes ) {
            orderPrice += r.getPrice();
        }
        this.price = orderPrice;

    }

    /**
     * Returns the status of the order
     *
     * @return status the status
     */
    public String getStatus () {
        return status;
    }

    /**
     * Sets the status of the order
     *
     * @param status
     *            the status to set
     */
    public void setStatus ( final String status ) {
        this.status = status;

    }

    /**
     * Updates the order status for a created order.
     */
    public void createOrder () {
        setStatus( "Created" );
    }

    /**
     * Updates the order status for a cancelled order.
     */
    public void cancelOrder () {
        setStatus( "Cancelled" );
    }

    @Override
    public String toString () {
        return "Order [id=" + id + ", recipes=" + recipes + ", payment=" + payment + ", price=" + price + ", status="
                + status + "]";
    }

    @Override
    public int hashCode () {
        return Objects.hash( id, payment, price, recipes, status );
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Order other = (Order) obj;
        return Objects.equals( id, other.id ) && Objects.equals( payment, other.payment )
                && Objects.equals( price, other.price ) && Objects.equals( recipes, other.recipes )
                && Objects.equals( status, other.status );
    }

}
