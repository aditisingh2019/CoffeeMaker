package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
    private Long             id;

    /** List of Ingredients for the Inventory */

    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )

    private List<Ingredient> ingredients;

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
    public void setId ( Long id ) {
        this.id = id;
    }

    /**
     * Getter for the list of names in the Ingredients list
     *
     * @return arrayList of all ingredient names in the ingredient list
     */
    public ArrayList<String> getAllIngredients () {
        ArrayList<String> ingrNames = new ArrayList<String>();

        for ( Ingredient i : this.ingredients ) {
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
    public void updateInventory ( String name, int amount ) {
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
    public void addIngredient ( Ingredient ingr ) {
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
    public int getIngredientIndex ( String name ) {
        int count = 0;
        for ( Ingredient i : this.ingredients ) {
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
    public int getIngredientAmount ( String name ) {
        for ( Ingredient i : this.ingredients ) {
            if ( i.getName().equals( name ) ) {
                return i.getAmount();
            }
        }
        return -1;
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param o
     *            order to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( Order o ) {
        HashMap<String, Integer> orderIngredients = new HashMap<String, Integer>();
        for ( Recipe r : o.getRecipes() ) {
            for ( Ingredient i : r.getIngredients() ) {
                if ( !this.getAllIngredients().contains( i.getName() ) ) {
                    return false;
                }
                if ( orderIngredients.get( i.getName() ) == null ) {
                    orderIngredients.put( i.getName(), i.getAmount() );
                }
                else {
                    orderIngredients.replace( i.getName(),
                            ( orderIngredients.get( i.getName() ).intValue() + i.getAmount() ) );
                }
            }
        }

        for ( Entry<String, Integer> e : orderIngredients.entrySet() ) {
            System.out.println( "Ingredient amount is:  " + e.getValue().toString() + "Other ingredient amount is: "
                    + this.ingredients.get( getIngredientIndex( e.getKey() ) ).getAmount().toString() );
            if ( this.ingredients.get( getIngredientIndex( e.getKey() ) ).getAmount().intValue() < e.getValue()
                    .intValue() ) {
                return false;
            }
        }
        return true;

    }

    /**
     * Removes the ingredients used to make the specified order. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param o
     *            order to make
     * @return true if order is made.
     */
    public boolean useIngredients ( Order o ) {

        if ( enoughIngredients( o ) ) {
            for ( Recipe r : o.getRecipes() ) {
                for ( Ingredient ingr : r.getIngredients() ) {
                    int idx = getIngredientIndex( ingr.getName() );
                    this.ingredients.get( idx ).setAmount( this.ingredients.get( idx ).getAmount() - ingr.getAmount() );
                }
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
        StringBuffer buf = new StringBuffer();
        for ( Ingredient ingr : this.ingredients ) {
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
    public void setInventory ( String name, int amt ) {
        for ( int i = 0; i < this.ingredients.size(); i++ ) {
            if ( this.ingredients.get( i ).getName().equals( name ) ) {

                this.ingredients.get( i ).setAmount( amt );
            }
        }
    }

}
