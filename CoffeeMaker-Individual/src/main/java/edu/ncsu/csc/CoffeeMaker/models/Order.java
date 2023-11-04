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

/***
 * Recipe for the coffee maker.Recipe is tied to the database using Hibernate
 * libraries.See RecipeRepository and RecipeService for the other two
 * pieces*used for database support.**
 *
 * @author Kai Presler-Marshall
 */
@Entity
@Table ( name = "`order`" )
public class Order extends DomainObject {

    /** Order id */
    @Id
    @GeneratedValue
    private Long               id;

    /** Recipes and associated amounts */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Recipe> recipes;

    /** Payment */
    private Integer            payment;

    /** Order price */
    @Min ( 0 )
    private Integer            price;

    /** Order status */
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

    public Order ( final List<Recipe> recipes, final Integer payment ) {
        setStatus( "Placed" );
        this.recipes = recipes;
        setPrice();
        setPayment( payment );

    }

    public boolean checkPaymentValidity ( final Integer payment ) {
        if ( payment < getPrice() ) {
            return false;
        }
        return true;
    }

    @Override
    public Long getId () {
        return id;
    }

    public void setId ( final Long id ) {
        this.id = id;
    }

    public List<Recipe> getRecipes () {
        return recipes;
    }

    public boolean deleteOneRecipe ( final Recipe recipe ) {
        for ( final Recipe r : recipes ) {
            if ( r.equals( recipe ) ) {
                recipes.remove( r );
                return true;
            }
        }
        return false;

    }

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

    public void addRecipes ( final Recipe recipe, final int quantity ) {
        for ( int i = 0; i < quantity; i++ ) {
            recipes.add( recipe );
        }
    }

    public void addRecipe ( final Recipe recipe ) {
        recipes.add( recipe );
    }

    public Integer getPayment () {
        return payment;
    }

    public void setPayment ( final Integer payment ) {
        if ( !checkPaymentValidity( payment ) ) {
            throw new IllegalArgumentException( "Payment is not sufficient" );
        }
        this.payment = payment;

    }

    public Integer getPrice () {
        return price;
    }

    public void setPrice () {
        int orderPrice = 0;
        for ( final Recipe r : recipes ) {
            orderPrice += r.getPrice();
        }
        this.price = orderPrice;

    }

    public String getStatus () {
        return status;
    }

    public void setStatus ( final String status ) {
        this.status = status;

    }

    public void fulfillOrder () {
        setStatus( "In Progress" );
    }

    public void notifyCustomer () {
        setStatus( "Complete" );
    }

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
