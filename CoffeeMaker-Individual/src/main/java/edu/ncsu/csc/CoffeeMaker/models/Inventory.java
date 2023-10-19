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
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long                   id;

    /** List of Ingredients for the Inventory */

    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )

    private final List<Ingredient> ingredients;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.

        this.ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Getter for the list of names in the Ingredients list
     *
     * @return arrayList of all ingredient names in the ingredient list
     */
    public ArrayList<String> getAllIngredients () {
        final ArrayList<String> ingrNames = new ArrayList<String>();

        for ( final Ingredient i : this.ingredients ) {
            ingrNames.add( i.getName() );
        }
        return ingrNames;

    }

    /**
     * Retrieve an Ingredient from it's name and update its amount by adding
     * that amount to the Ingredient's current amount
     *
     * @param name
     *            String Ingredient's name
     * @param amount
     *            int updated amount to add to amount field
     */
    public void updateInventory ( final String name, final int amount ) {
        if ( amount < 0 ) {
            throw new IllegalArgumentException( "Amount cannot be negative" );
        }

        boolean found = false;
        for ( int i = 0; i < this.ingredients.size(); i++ ) {
            if ( this.ingredients.get( i ).getName().equals( name ) ) {
                found = true;

                this.ingredients.get( i ).setAmount( this.ingredients.get( i ).getAmount() + amount );
            }
        }
        if ( !found ) {
            this.ingredients.add( new Ingredient( name, amount ) );

        }
    }

    /**
     * Retrieve an Ingredient from it's name and update its amount by adding
     * that amount to the Ingredient's current amount
     *
     * @param ingr
     *            ingredient to add to the inventory
     */
    public void addIngredient ( final Ingredient ingr ) {
        if ( ingr.getAmount() < 0 ) {
            throw new IllegalArgumentException( "Amount cannot be negative" );
        }

        for ( int i = 0; i < this.ingredients.size(); i++ ) {
            if ( this.ingredients.get( i ).getName().equals( ingr.getName() ) ) {

                throw new IllegalArgumentException( "Duplicate Ingredient trying to be added" );
            }
        }

        this.ingredients.add( ingr );

    }

    /**
     * Getter for the list of Ingredients in the Inventory
     *
     * @return ingredients List of the Ingredients in the Inventory
     */
    public List<Ingredient> getIngredients () {

        return this.ingredients;

    }

    /**
     * Retrieves the index of the Ingredient inside the list
     *
     * @param name
     *            String name of the Ingredient
     * @return count returns the index of the Ingredient in the list. Else -1
     *         for not found
     */
    public int getIngredientIndex ( final String name ) {
        int count = 0;
        for ( final Ingredient i : this.ingredients ) {
            if ( i.getName().equals( name ) ) {
                return count;
            }
            count++;
        }
        return -1;
    }

    /**
     * Retrieve the amount of an Ingredient from the list
     *
     * @param name
     *            String name of the Ingredient
     * @return amount amount for the Ingredient found in the list
     */
    public int getIngredientAmount ( final String name ) {
        for ( final Ingredient i : this.ingredients ) {
            if ( i.getName().equals( name ) ) {
                return i.getAmount();
            }
        }
        return -1;
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {

        for ( final Ingredient ingr : r.getIngredients() ) {
            if ( !this.getAllIngredients().contains( ingr.getName() ) ) {
                return false;
            }

            System.out.println( "Ingredient amount is:  " + ingr.getAmount().toString() + "Other ingredient amount is: "
                    + this.ingredients.get( getIngredientIndex( ingr.getName() ) ).getAmount().toString() );
            if ( this.ingredients.get( getIngredientIndex( ingr.getName() ) ).getAmount().intValue() < ingr
                    .getAmount() ) {
                return false;
            }
        }
        return true;

    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {

        if ( enoughIngredients( r ) ) {
            for ( final Ingredient ingr : r.getIngredients() ) {
                final int idx = getIngredientIndex( ingr.getName() );
                this.ingredients.get( idx ).setAmount( this.ingredients.get( idx ).getAmount() - ingr.getAmount() );
            }

            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        for ( final Ingredient ingr : this.ingredients ) {
            buf.append( ingr.getName() + ": " + ingr.getAmount().toString() + "\n" );
        }
        return buf.toString();
    }

    /**
     * Find an Ingredient by it's name and assign it's amount from the list
     *
     *
     * @param name
     *            String name of the Ingredient
     * @param amt
     *            amount of the Ingredient to be assigned
     */
    public void setInventory ( final String name, final int amt ) {
        for ( int i = 0; i < this.ingredients.size(); i++ ) {
            if ( this.ingredients.get( i ).getName().equals( name ) ) {

                this.ingredients.get( i ).setAmount( amt );
            }
        }
    }

}
