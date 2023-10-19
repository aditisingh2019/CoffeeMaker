package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long                   id;

    /** Recipe name */
    private String                 name;

    /** Recipe price */
    @Min ( 0 )
    private Integer                price;

    /** List of Ingredients for the Recipe */

    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )

    private final List<Ingredient> ingredients;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        this.ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Check if all ingredient fields in the recipe are 0
     *
     * @return true if all ingredient fields are 0, otherwise return false
     */
    public boolean checkRecipe () {

        for ( int i = 0; i < this.ingredients.size(); i++ ) {

            if ( ingredients.get( i ).getAmount().intValue() != 0 ) {
                return false;
            }

        }
        return true;
    }

    /**
     *
     * Adds an ingredient to the list
     *
     *
     * @param ingredient
     *            Ingredient added to the list
     */
    public void addIngredient ( final Ingredient ingredient ) {

        ingredients.add( ingredient );

    }

    /**
     *
     * Returns the list of ingredients
     *
     *
     *
     * @return list of ingredients
     *
     */
    public List<Ingredient> getIngredients () {

        return this.ingredients;

    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     *
     * deletes an ingredient from the list of ingredients in Recipe
     *
     *
     * @param ingredient
     *            Ingredient added to the list
     * @throws Exception
     *             that is thrown if there is a null ingredient
     */
    public void deleteIngredient ( final Ingredient ingredient ) throws Exception {

        if ( ingredient == null ) {
            System.out.println( "Invalid ingredient" );
            throw new Exception();
        }
        else {
            final int index = getIngredientIndex( ingredient.getName() );
            ingredients.remove( index );
        }

    }

    /**
     * Updates the fields to be equal to the passed Recipe
     *
     * @param r
     *            with updated fields
     */
    public void updateRecipe ( final Recipe r ) {

        setPrice( r.getPrice() );
        setName( r.getName() );
        final List<Ingredient> newIngredientsToAdd = new ArrayList<>();

        // Remove ingredients that are not in the new recipe
        final Iterator<Ingredient> iterator = this.ingredients.iterator();

        while ( iterator.hasNext() ) {
            final Ingredient oldIngredient = iterator.next();
            if ( !containsIngredient( r.getIngredients(), oldIngredient.getName() ) ) {

                iterator.remove();
            }
        }

        // Add or update ingredients from the new recipe
        for ( final Ingredient newIngredient : r.getIngredients() ) {
            final int index = getIngredientIndex( newIngredient.getName() );
            if ( index != -1 ) {
                // Ingredient with the same name already exists, update it
                this.ingredients.get( index ).setAmount( newIngredient.getAmount() );
            }
            else {
                // Ingredient is not in the list, add it to the
                // newIngredientsToAdd list
                newIngredientsToAdd.add( newIngredient );
            }
        }

        // Add the new ingredients to the list
        this.ingredients.addAll( newIngredientsToAdd );

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

    private boolean containsIngredient ( final List<Ingredient> ingredients, final String name ) {
        for ( final Ingredient ingredient : ingredients ) {
            if ( ingredient.getName().equals( name ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the name of the recipe.
     *
     * @return String
     */
    @Override
    public String toString () {
        String s = "";

        for ( int i = 0; i < this.ingredients.size() - 1; i++ ) {

            s += ingredients.get( i ) + ", ";

        }

        s += ingredients.get( ingredients.size() - 1 );

        return s;
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
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
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}
