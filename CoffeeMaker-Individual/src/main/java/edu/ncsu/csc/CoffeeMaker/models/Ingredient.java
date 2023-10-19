package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * Ingredient class for abtritrary Ingredient for a Recipe. Ingredient is tied
 * to the database using Hibernate libraries. See IngredientRepository and
 * IngredientService for the other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Ingredient extends DomainObject {

    /** Ingredient id */

    @Id

    @GeneratedValue
    private Long    id;

    /** Ingredient Name */

    private String  name;

    /** Ingredient price */
    @Min ( 0 )
    private Integer amount;

    /**
     * Constructor class for a default Ingredient with specified type and amount
     *
     * @param name
     *            Type of the Ingredient using enumeration
     * @param amount
     *            Requirement to use the Ingredient
     */
    public Ingredient ( final String name, final Integer amount ) {

        this.amount = amount;

        this.name = name;

    }

    /**
     * Constructor that calls the parent class's constructor - DomainObject
     */
    public Ingredient () {

        super();

    }

    /**
     * Getter for the type of Ingredient
     *
     * @return ingredient Enumeration field for the Ingredient type
     */
    public String getName () {

        return name;

    }

    /**
     * Setter for the type of Ingredient
     *
     *
     * @param name
     *            name of the Ingredient to set
     */
    public void setName ( final String name ) {

        this.name = name;

    }

    /**
     * Getter for the amount
     *
     * @return amount Number required for the Ingredient
     */
    public Integer getAmount () {

        return amount;

    }

    /**
     * Setter for the amount
     *
     * @param amount
     *            Integer reassigned value for amount
     */
    public void setAmount ( final Integer amount ) {

        this.amount = amount;

    }

    /**
     * Setter for the id
     *
     * @param id
     *            Long set the id field from the database
     */
    public void setId ( final Long id ) {

        this.id = id;

    }

    /**
     * Getter for the id
     *
     */
    @Override
    public Long getId () {

        // TODO Auto-generated method stub

        return this.id;

    }

    /**
     * Converts the fields of the Ingredients into a string for outputting
     *
     * @return string of all the fields of the Ingredient
     */
    @Override
    public String toString () {

        return "Ingredient [id=" + id + ", ingredient=" + name + ", amount=" + amount + "]";

    }

}
