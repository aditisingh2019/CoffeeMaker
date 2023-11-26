package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Orders.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 * @author Michelle Lemons
 * @author Aditi Singh
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@RestController
public class APIOrderController extends APIController {

	/**
	 * OrderService object, to be autowired in by Spring to allow for manipulating
	 * the Order model
	 */
	@Autowired
	private OrderService service;

	/**
	 * InventoryService object, to be autowired in by Spring to allow for
	 * manipulating the Inventory model
	 */
	@Autowired
	private InventoryService inventoryService;

	/**
	 * REST API method to provide GET access to all orders in the system
	 *
	 * @return JSON representation of all ordera
	 */
	@GetMapping(BASE_PATH + "/orders")
	public List<Order> getOrders() {
		return service.findAll();
	}

	/**
	 * REST API method to provide GET access to a specific orders, as indicated by
	 * the path variable provided (the name of the recipe desired)
	 *
	 * @param id order id
	 * @return response to the request
	 */
	@GetMapping(BASE_PATH + "/orders/{id}")
	public ResponseEntity getOrder(@PathVariable("id") final Long id) {
		final Order order = service.findById(id);
		return null == order ? new ResponseEntity(errorResponse("No order found with id: " + id), HttpStatus.NOT_FOUND)
				: new ResponseEntity(order, HttpStatus.OK);
	}

	/**
	 * REST API method to allow creating a Order from the CoffeeMaker's Inventory,
	 * by making a PUT request to the API endpoint and indicating the order to be
	 * updated (as a path variable)
	 *
	 * @param order The order we need to add
	 * @return Success if the order could be added
	 */
	@PostMapping(BASE_PATH + "/orders")
	public ResponseEntity placeOrder(@RequestBody final Order order) {

		final Order o = new Order(order.getRecipes(), order.getPayment(), order.getPrice());
		service.save(order);
		return new ResponseEntity(successResponse(o.getId() + " successfully placed"), HttpStatus.OK);
	}

	/**
	 * REST API method to provide PUT access to a specific orders, as indicated by
	 * the path variable provided (the id of the order desired)
	 *
	 * @param id order id
	 * @return response to the request
	 */
	@PutMapping(BASE_PATH + "/orders/{id}")
	public ResponseEntity createOrder(@PathVariable("id") final Long id) {
		final Order order = service.findById(id);
		if (order == null) {
			return new ResponseEntity(errorResponse("No order selected"), HttpStatus.NOT_FOUND);
		}

		final Inventory inventory = inventoryService.getInventory();
		if (inventory.enoughIngredients(order)) {
			// if status is placed, change to in progress and use ingredients
			// and
			// save
			// else if status is in progress, change status to created
			if (order.getStatus().equals("Placed")) {
				inventory.useIngredients(order);
				inventoryService.save(inventory);
				order.setStatus("In progress");

				service.save(order);
				return new ResponseEntity(successResponse("Order " + order.getId() + " is in progress"), HttpStatus.OK);
			} else if (order.getStatus().equals("In progress")) {
				order.setStatus("Ready for pickup");

				service.save(order);
				return new ResponseEntity(successResponse("Order " + order.getId() + " is ready for pickup"),
						HttpStatus.OK);
			}

		} else {
			order.setStatus("Cancelled");
			service.save(order);
			return new ResponseEntity("Not enough inventory. " + order.getId() + " cancelled.", HttpStatus.CONFLICT);
		}

		return new ResponseEntity("Erorr with finding order", HttpStatus.CONFLICT);
	}

	/**
	 * REST API method to provide PUT access to a specific orders, as indicated by
	 * the path variable provided (the name of the recipe desired)
	 *
	 * @param id order id
	 * @return response to the request
	 */
	@DeleteMapping(BASE_PATH + "/orders/{id}")
	public ResponseEntity deleteOrder(@PathVariable("id") final Long id) {
		final Order order = service.findById(id);
		if (null == order) {
			return new ResponseEntity(errorResponse("No order found for id " + id), HttpStatus.NOT_FOUND);
		}
		service.delete(order);

		return new ResponseEntity(successResponse(id + " was deleted successfully"), HttpStatus.OK);
	}

}
