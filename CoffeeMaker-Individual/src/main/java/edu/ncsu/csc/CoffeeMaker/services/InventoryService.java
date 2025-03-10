package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.repositories.InventoryRepository;

/**
 * The InventoryService is used to handle CRUD operations on the Inventory
 * model. In addition to all functionality in `Service`, we also manage the
 * Inventory singleton.
 *
 * @author Kai Presler-Marshall
 *
 */
@Component
@Transactional
public class InventoryService extends Service<Inventory, Long> {

    /**
     * InventoryRepository, to be autowired in by Spring and provide CRUD
     * operations on Inventory model.
     */
    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    protected JpaRepository<Inventory, Long> getRepository () {
        return inventoryRepository;
    }

    /**
     * Retrieves the singleton Inventory instance from the database, creating it
     * if it does not exist.
     *
     * @return the Inventory, either new or fetched
     */
    public synchronized Inventory getInventory () {
        final List<Inventory> inventoryList = findAll();
        if ( inventoryList != null && inventoryList.size() == 1 ) {
            return inventoryList.get( 0 );
        }
        else {
            // initialize the inventory with 0 of everything
            final Inventory i = new Inventory();

            final Ingredient coffee = new Ingredient( "Coffee", 0 );
            final Ingredient milk = new Ingredient( "Milk", 0 );
            final Ingredient sugar = new Ingredient( "Sugar", 0 );
            final Ingredient chocolate = new Ingredient( "Chocolate", 0 );

            i.addIngredient( coffee );
            i.addIngredient( milk );
            i.addIngredient( sugar );
            i.addIngredient( chocolate );
            save( i );
            return i;
        }
    }

    /**
     * Retrieves an ingredient from the inventory with the given name
     *
     * @param name
     *            name of ingredient to retrieve
     *
     * @return the Inventory, either new or fetched
     */
    public Ingredient getIngredient ( final String name ) {
        Inventory ivt = null;
        final List<Inventory> inventoryList = findAll();
        if ( inventoryList != null && inventoryList.size() == 1 ) {
            ivt = inventoryList.get( 0 );

            for ( final Ingredient i : ivt.getIngredients() ) {
                if ( i.getName().equals( name ) ) {
                    return i;
                }
            }
            return null;
        }
        else {
            return null;
        }

    }

}
