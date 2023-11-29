package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Controller class for the URL mappings for CoffeeMaker. The controller returns
 * the approprate HTML page in the /src/main/resources/templates folder. For a
 * larger application, this should be split across multiple controllers.
 *
 * @author Kai Presler-Marshall
 */
@Controller
public class MappingController {
    @Autowired
    private UserService userService;

    /**
     * On a GET request to /index, the IndexController will return
     * /src/main/resources/templates/index.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/index", "/" } )
    public String index ( final Model model ) {
        return "index";
    }

    /**
     * On a GET request to /staff, the StaffController will return
     * /src/main/resources/templates/staff.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/staff/home", "staff.html" } )
    public String staffPage ( final Authentication authentication, final Model model ) {

        final String username = authentication.getName();

        model.addAttribute( "username", username );
        return "staff";
    }

    /**
     * On a GET request to /recipe, the RecipeController will return
     * /src/main/resources/templates/recipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/staff/recipe", "/recipe.html" } )
    public String addRecipePage ( final Model model ) {
        return "recipe";
    }

    /**
     * On a GET request to /deleterecipe, the DeleteRecipeController will return
     * /src/main/resources/templates/deleterecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/staff/deleterecipe", "/deleterecipe.html" } )
    public String deleteRecipeForm ( final Model model ) {
        return "deleterecipe";
    }

    /**
     * On a GET request to /editrecipe, the EditRecipeController will return
     * /src/main/resources/templates/editrecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/staff/editrecipe", "/editrecipe.html" } )
    public String editRecipeForm ( final Model model ) {
        return "editrecipe";
    }

    /**
     * Handles a GET request for inventory. The GET request provides a view to
     * the client that includes the list of the current ingredients in the
     * inventory and a form where the client can enter more ingredients to add
     * to the inventory.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/manager/inventory", "/inventory.html" } )
    public String inventoryForm ( final Model model ) {
        return "inventory";
    }

    /**
     * Handles a GET request for inventory. The GET request provides a view to
     * the client that includes the list of the past orders anyone has ever
     * ordered in the it shows the manager the stats of all orders and profit
     * made
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/manager/orderhistory", "/orderhistory.html" } )
    public String orderhistory ( final Model model ) {
        return "orderhistory";
    }

    /**
     * On a GET request to /vieworder, the EditRecipeController will return
     * /src/main/resources/templates/editrecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/staff/vieworder", "/vieworder.html" } )
    public String viewOrderPage ( final Model model ) {
        return "vieworder";
    }

    /**
     * On a GET request to /staff, the CustomerController will return
     * /src/main/resources/templates/staff.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/customer/home", "customer.html" } )
    public String customerPage ( final Authentication authentication, final Model model ) {
        final String username = authentication.getName();

        model.addAttribute( "username", username );
        return "customer";
    }

    /**
     * On a GET request to /managerhome, the CustomerController will return
     * /src/main/resources/templates/manager.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/manager/home", "manager.html" } )
    public String managerPage ( final Authentication authentication, final Model model ) {
        final String username = authentication.getName();

        model.addAttribute( "username", username );
        return "manager";
    }

    /**
     * On a GET request to /placeorder, the MakeCoffeeController will return
     * /src/main/resources/templates/placeorder.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/customer/placeorder", "placeorder.html" } )
    public String placeOrderForm ( final Authentication authentication, final Model model ) {
        final String username = authentication.getName();
        final User user = userService.findByUsername( username );
        model.addAttribute( "userId", user.getId() );
        return "placeorder";
    }

    /**
     * On a GET request to /orderstatus, the MakeCoffeeController will return
     * /src/main/resources/templates/makecoffee.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/customer/orderstatus", "orderstatus.html" } )
    public String orderstatusPage ( final Authentication authentication, final Model model ) {
        final String username = authentication.getName();
        final User user = userService.findByUsername( username );
        model.addAttribute( "userId", user.getId() );
        System.out.println( user.getId() );
        return "orderstatus";
    }

    /**
     * On a GET request to /registry, the MakeCoffeeController will return
     * /src/main/resources/templates/registry.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/registry", "/registry.html" } )
    public String registration ( final Model model ) {
        return "registry";
    }

    /**
     * On a GET request to /fulfillOrder, the MakeCoffeeController will return
     * /src/main/resources/templates/fulfillorder.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/staff/fulfillorder", "/fulfillorder.html" } )
    public String fulfillOrderPage ( final Model model ) {
        return "fulfillorder";
    }

    @GetMapping ( { "/tempLogin", "/tempLogin.html" } )
    public String tempLogin ( final Model model ) {
        return "tempLogin";
    }

    @GetMapping ( { "/login", "/login.html" } )
    public String login ( final Model model ) {
        return "login";
    }

    @GetMapping ( { "/privacy", "privacy.html" } )
    public String privacy ( final Model model ) {
        return "privacy";
    }

}
