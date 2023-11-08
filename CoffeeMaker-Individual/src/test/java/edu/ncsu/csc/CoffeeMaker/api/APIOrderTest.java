package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class APIOrderTest {

	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST API
	 */
	private MockMvc mvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private OrderService orderservice;

	@Autowired
	private InventoryService inventoryservice;

	@Autowired

	/**
	 * Sets up the tests.
	 */
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	@Transactional
	public void placingOrder1Recipe() throws Exception {

		// creating a new recipe
		final Recipe recipe = new Recipe();
		recipe.setName("Delicious Not-Coffee");

		// creating the ingredients for the recipe
		final Ingredient coffeeIngr = new Ingredient("Coffee", 10);
		final Ingredient milkIngr = new Ingredient("Milk", 20);
		final Ingredient sugarIngr = new Ingredient("Sugar", 5);
		final Ingredient chocolateIngr = new Ingredient("Chocolate", 1);

		// adding the ingredients to the recipe
		recipe.addIngredient(coffeeIngr);
		recipe.addIngredient(milkIngr);
		recipe.addIngredient(sugarIngr);
		recipe.addIngredient(chocolateIngr);

		recipe.setPrice(5);

		final Order order1 = new Order();
		order1.setPayment(10);
		order1.addRecipe(recipe);

		mvc.perform(post("/api/v1/orders/").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(order1)));

		Assertions.assertEquals(1, (int) orderservice.count());

	}

	@Test
	@Transactional
	public void placingOrderManyRecipes() throws Exception {

		final Recipe teamCoffee = new Recipe();

		teamCoffee.setName("Coffee");
		teamCoffee.setPrice(3);
		final Ingredient coffee = new Ingredient("Coffee", 2);

		final Ingredient milk = new Ingredient("Milk", 1);

		final Ingredient pumpkinSpice = new Ingredient("Pumpkin Spice", 1);

		final Ingredient sugar = new Ingredient("Sugar", 1);
		teamCoffee.addIngredient(sugar);
		teamCoffee.addIngredient(milk);
		teamCoffee.addIngredient(pumpkinSpice);
		teamCoffee.addIngredient(coffee);

		final Recipe vanilla = new Recipe();
		vanilla.setName("Coffee");
		vanilla.setPrice(4);
		final Ingredient coffee2 = new Ingredient("Coffee", 2);

		final Ingredient milk2 = new Ingredient("Milk", 1);

		final Ingredient sugar2 = new Ingredient("Sugar", 1);

		final Ingredient syrup2 = new Ingredient("Syrup", 1);
		vanilla.addIngredient(sugar2);
		vanilla.addIngredient(milk2);
		vanilla.addIngredient(syrup2);
		vanilla.addIngredient(coffee2);

		final Recipe pumpkinSpiceCoffee = new Recipe();
		pumpkinSpiceCoffee.setName("Pumpkin Spice Coffee");
		pumpkinSpiceCoffee.setPrice(4);

		final List<Recipe> recipes = new ArrayList<Recipe>();
		recipes.add(teamCoffee);
		recipes.add(vanilla);

		final Order bigOrder = new Order(recipes, 20);
		mvc.perform(post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(bigOrder)));

		Assertions.assertEquals(1, (int) orderservice.count());
		Assertions.assertEquals(2, orderservice.findAll().get(0).getRecipes().size());
		try {
			new Order(recipes, 10);
		} catch (final IllegalArgumentException e) {
			Assertions.assertEquals(e.getMessage(), "Payment is not sufficient");
		}

	}

	@Test
	@Transactional
	public void testGetOrders1Order() throws Exception {

		final Recipe recipe = new Recipe();
		recipe.setName("Delicious Not-Coffee");

		final Ingredient coffeeIngr = new Ingredient("Coffee", 10);
		final Ingredient milkIngr = new Ingredient("Milk", 20);
		final Ingredient sugarIngr = new Ingredient("Sugar", 5);
		final Ingredient chocolateIngr = new Ingredient("Chocolate", 1);

		recipe.addIngredient(coffeeIngr);
		recipe.addIngredient(milkIngr);
		recipe.addIngredient(sugarIngr);
		recipe.addIngredient(chocolateIngr);

		recipe.setPrice(5);

		final Order order1 = new Order();
		order1.setPayment(10);
		order1.addRecipe(recipe);

		mvc.perform(post("/api/v1/orders/").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(order1)));

		Assertions.assertEquals(1, (int) orderservice.count());

		final String orders = mvc.perform(get("/api/v1/orders")).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();

		assertTrue(orders.contains("Delicious Not-Coffee"));

	}

	@Test
	@Transactional
	public void testGetOrdersManyOrders() throws Exception {

		final Recipe recipe1 = new Recipe();
		recipe1.setName("Delicious Not-Coffee");

		final Ingredient coffeeIngr1 = new Ingredient("Coffee", 10);
		final Ingredient milkIngr1 = new Ingredient("Milk", 20);
		final Ingredient sugarIngr1 = new Ingredient("Sugar", 5);
		final Ingredient chocolateIngr1 = new Ingredient("Chocolate", 1);

		recipe1.addIngredient(coffeeIngr1);
		recipe1.addIngredient(milkIngr1);
		recipe1.addIngredient(sugarIngr1);
		recipe1.addIngredient(chocolateIngr1);

		recipe1.setPrice(5);

		final Order order1 = new Order();
		order1.setPayment(10);
		order1.addRecipe(recipe1);

		mvc.perform(post("/api/v1/orders/").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(order1)));

		Assertions.assertEquals(1, (int) orderservice.count());

		final Recipe recipe2 = new Recipe();
		recipe2.setName("Chai");

		final Ingredient coffeeIngr2 = new Ingredient("Coffee", 30);
		final Ingredient milkIngr2 = new Ingredient("Milk", 40);
		final Ingredient sugarIngr2 = new Ingredient("Sugar", 50);
		final Ingredient chocolateIngr2 = new Ingredient("Chocolate", 10);

		recipe2.addIngredient(coffeeIngr2);
		recipe2.addIngredient(milkIngr2);
		recipe2.addIngredient(sugarIngr2);
		recipe2.addIngredient(chocolateIngr2);

		recipe2.setPrice(20);
		final Recipe recipe3 = new Recipe();
		recipe3.setName("Matcha");

		final Ingredient coffeeIngr3 = new Ingredient("Coffee", 5);
		final Ingredient milkIngr3 = new Ingredient("Milk", 45);
		final Ingredient sugarIngr3 = new Ingredient("Sugar", 90);
		final Ingredient chocolateIngr3 = new Ingredient("Chocolate", 20);

		recipe3.addIngredient(coffeeIngr3);
		recipe3.addIngredient(milkIngr3);
		recipe3.addIngredient(sugarIngr3);
		recipe3.addIngredient(chocolateIngr3);
		recipe3.setPrice(30);

		final Order order2 = new Order();
		order2.setPayment(20);
		order2.addRecipe(recipe2);
		// order2.addRecipe(recipe3);

		final Order order3 = new Order();
		order3.setPayment(40);
		order3.addRecipe(recipe3);

		mvc.perform(post("/api/v1/orders/").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(order2)));

		Assertions.assertEquals(2, (int) orderservice.count());

		mvc.perform(post("/api/v1/orders/").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(order3)));

		Assertions.assertEquals(3, (int) orderservice.count());

		final String orders = mvc.perform(get("/api/v1/orders")).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();

		assertTrue(orders.contains("Delicious Not-Coffee"));
		assertTrue(orders.contains("Chai"));
		assertTrue(orders.contains("Matcha"));

	}

	@Test
	@Transactional
	public void testGetOrder1Order() throws Exception {

		final Recipe recipe = new Recipe();
		recipe.setName("Delicious Not-Coffee");

		final Ingredient coffeeIngr = new Ingredient("Coffee", 10);
		final Ingredient milkIngr = new Ingredient("Milk", 20);
		final Ingredient sugarIngr = new Ingredient("Sugar", 5);
		final Ingredient chocolateIngr = new Ingredient("Chocolate", 1);

		recipe.addIngredient(coffeeIngr);
		recipe.addIngredient(milkIngr);
		recipe.addIngredient(sugarIngr);
		recipe.addIngredient(chocolateIngr);

		recipe.setPrice(5);

		final Order order1 = new Order();
		order1.setPayment(10);
		order1.addRecipe(recipe);

		mvc.perform(post("/api/v1/orders/").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(order1)));

		Assertions.assertEquals(1, (int) orderservice.count());
		orderservice.save(order1);
		Long id1 = order1.getId();

		final String orders = mvc.perform(get("/api/v1/orders/" + id1)).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		assertTrue(orders.contains("Delicious Not-Coffee"));

	}

	@Test
	@Transactional
	public void testGetOrderManyOrders() throws Exception {

		final Recipe recipe1 = new Recipe();
		recipe1.setName("Delicious Not-Coffee");

		final Ingredient coffeeIngr = new Ingredient("Coffee", 10);
		final Ingredient milkIngr = new Ingredient("Milk", 20);
		final Ingredient sugarIngr = new Ingredient("Sugar", 5);
		final Ingredient chocolateIngr = new Ingredient("Chocolate", 1);

		recipe1.addIngredient(coffeeIngr);
		recipe1.addIngredient(milkIngr);
		recipe1.addIngredient(sugarIngr);
		recipe1.addIngredient(chocolateIngr);

		recipe1.setPrice(5);

		final Order order1 = new Order();
		order1.setPayment(10);
		order1.addRecipe(recipe1);

		orderservice.save(order1);
		Long id1 = order1.getId();
		Assertions.assertEquals(1, (int) orderservice.count());

		final String order1String = mvc.perform(get("/api/v1/orders/" + id1)).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		assertTrue(order1String.contains("Delicious Not-Coffee"));

		final Recipe recipe2 = new Recipe();
		recipe2.setName("Chai");
		recipe2.setPrice(43);

		final Ingredient coffeeIngr2 = new Ingredient("Coffee", 30);
		final Ingredient milkIngr2 = new Ingredient("Milk", 40);
		final Ingredient sugarIngr2 = new Ingredient("Sugar", 50);
		final Ingredient chocolateIngr2 = new Ingredient("Chocolate", 10);

		recipe2.addIngredient(coffeeIngr2);
		recipe2.addIngredient(milkIngr2);
		recipe2.addIngredient(sugarIngr2);
		recipe2.addIngredient(chocolateIngr2);

		final Recipe recipe3 = new Recipe();
		recipe3.setName("Matcha");
		recipe3.setPrice(10);

		final Ingredient coffeeIngr3 = new Ingredient("Coffee", 5);
		final Ingredient milkIngr3 = new Ingredient("Milk", 45);
		final Ingredient sugarIngr3 = new Ingredient("Sugar", 90);
		final Ingredient chocolateIngr3 = new Ingredient("Chocolate", 20);

		recipe3.addIngredient(coffeeIngr3);
		recipe3.addIngredient(milkIngr3);
		recipe3.addIngredient(sugarIngr3);
		recipe3.addIngredient(chocolateIngr3);

		final Order order2 = new Order();
		order2.setPayment(100);
		order2.addRecipe(recipe2);

		mvc.perform(post("/api/v1/orders/").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(order2)));

		final Order order3 = new Order();
		order3.setPayment(100);
		order3.addRecipe(recipe3);

		mvc.perform(post("/api/v1/orders/").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(order3)));

		Assertions.assertEquals(3, (int) orderservice.count());

		orderservice.save(order2);
		orderservice.save(order3);

		Long id2 = order2.getId();
		Long id3 = order3.getId();

		final String order2String = mvc.perform(get("/api/v1/orders/" + id2)).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		assertTrue(order2String.contains("Chai"));

		final String order3String = mvc.perform(get("/api/v1/orders/" + id3)).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		assertTrue(order3String.contains("Matcha"));

	}

	@Test
	@Transactional
	public void testDeleteOrderMany() throws Exception {

		final Recipe recipe1 = new Recipe();
		recipe1.setName("Delicious Not-Coffee");

		final Ingredient coffeeIngr = new Ingredient("Coffee", 10);
		final Ingredient milkIngr = new Ingredient("Milk", 20);
		final Ingredient sugarIngr = new Ingredient("Sugar", 5);
		final Ingredient chocolateIngr = new Ingredient("Chocolate", 1);

		recipe1.addIngredient(coffeeIngr);
		recipe1.addIngredient(milkIngr);
		recipe1.addIngredient(sugarIngr);
		recipe1.addIngredient(chocolateIngr);

		recipe1.setPrice(5);

		final Recipe recipe2 = new Recipe();
		recipe2.setName("Coffee");
		recipe2.setPrice(4);
		final Ingredient coffee2 = new Ingredient("Coffee", 2);
		final Ingredient milk2 = new Ingredient("Milk", 1);
		final Ingredient sugar2 = new Ingredient("Sugar", 1);
		final Ingredient syrup2 = new Ingredient("Syrup", 1);
		recipe2.addIngredient(sugar2);
		recipe2.addIngredient(milk2);
		recipe2.addIngredient(syrup2);
		recipe2.addIngredient(coffee2);

		final Order order1 = new Order();
		order1.setPayment(10);
		order1.addRecipe(recipe1);

		final Order order2 = new Order();
		order2.setPayment(20);
		order2.addRecipe(recipe2);

		orderservice.save(order1);
		orderservice.save(order2);
		Assertions.assertEquals(2, (int) orderservice.count());

		final Recipe recipe3 = new Recipe();
		recipe3.setName("Chai");

		final Ingredient coffeeIngr3 = new Ingredient("Coffee", 30);
		final Ingredient milkIngr3 = new Ingredient("Milk", 40);
		final Ingredient sugarIngr3 = new Ingredient("Sugar", 50);
		final Ingredient chocolateIngr3 = new Ingredient("Chocolate", 10);

		recipe3.addIngredient(coffeeIngr3);
		recipe3.addIngredient(milkIngr3);
		recipe3.addIngredient(sugarIngr3);
		recipe3.addIngredient(chocolateIngr3);
		recipe3.setPrice(10);

		final Recipe recipe4 = new Recipe();
		recipe4.setName("Matcha");

		final Ingredient coffeeIngr4 = new Ingredient("Coffee", 5);
		final Ingredient milkIngr4 = new Ingredient("Milk", 45);
		final Ingredient sugarIngr4 = new Ingredient("Sugar", 90);
		final Ingredient chocolateIngr4 = new Ingredient("Chocolate", 20);

		recipe4.addIngredient(coffeeIngr4);
		recipe4.addIngredient(milkIngr4);
		recipe4.addIngredient(sugarIngr4);
		recipe4.addIngredient(chocolateIngr4);
		recipe4.setPrice(23);

		final Order order3 = new Order();
		order3.setPayment(20);
		order3.addRecipe(recipe3);

		final Order order4 = new Order();
		order4.setPayment(40);
		order4.addRecipe(recipe4);

		orderservice.save(order3);
		orderservice.save(order4);

		Assertions.assertEquals(4, (int) orderservice.count());

		final String orders = mvc.perform(get("/api/v1/orders")).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();

		assertTrue(orders.contains("Delicious Not-Coffee"));
		assertTrue(orders.contains("Coffee"));
		assertTrue(orders.contains("Chai"));
		assertTrue(orders.contains("Matcha"));

		Long id1 = order1.getId();
		Long id2 = order2.getId();
		Long id3 = order3.getId();
		Long id4 = order4.getId();

		mvc.perform(delete("/api/v1/orders/" + id1)).andExpect(status().isOk());

		Assertions.assertEquals(3, (int) orderservice.count());

		mvc.perform(delete("/api/v1/orders/" + id2)).andExpect(status().isOk());

		Assertions.assertEquals(2, (int) orderservice.count());

		mvc.perform(delete("/api/v1/orders/" + id3)).andExpect(status().isOk());

		Assertions.assertEquals(1, (int) orderservice.count());

		mvc.perform(delete("/api/v1/orders/" + id4)).andExpect(status().isOk());

		Assertions.assertEquals(0, (int) orderservice.count());

	}

	@Test
	@Transactional
	public void testCreateOrder1Recipe() throws Exception {

		final Recipe recipe = new Recipe();
		recipe.setName("Delicious Not-Coffee");

		final Ingredient coffeeIngr = new Ingredient("Coffee", 10);
		final Ingredient milkIngr = new Ingredient("Milk", 20);
		final Ingredient sugarIngr = new Ingredient("Sugar", 5);
		final Ingredient chocolateIngr = new Ingredient("Chocolate", 1);

		recipe.addIngredient(coffeeIngr);
		recipe.addIngredient(milkIngr);
		recipe.addIngredient(sugarIngr);
		recipe.addIngredient(chocolateIngr);

		recipe.setPrice(5);

		final Order order3 = new Order();
		order3.setPayment(10);
		order3.addRecipe(recipe);

		orderservice.save(order3);

		Long id3 = order3.getId();

		final Inventory inventory = inventoryservice.getInventory();

		inventory.updateInventory("Coffee", 100);
		inventory.updateInventory("Syrup", 100);
		inventory.updateInventory("Sugar", 100);
		inventory.updateInventory("Milk", 100);
		inventory.updateInventory("Chocolate", 100);

		inventoryservice.save(inventory);

		mvc.perform(put("/api/v1/orders/" + id3)).andExpect(status().isOk());
		assertEquals(orderservice.count(), 1);

		final String orderString = mvc.perform(get("/api/v1/orders/" + id3)).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		assertTrue(orderString.contains("Delicious Not-Coffee"));

	}

	@Test
	@Transactional
	public void testCreateOrderManyRecipes() throws Exception {

		final Recipe recipe1 = new Recipe();
		recipe1.setName("Delicious Not-Coffee");

		final Ingredient coffeeIngr = new Ingredient("Coffee", 10);
		final Ingredient milkIngr = new Ingredient("Milk", 20);
		final Ingredient sugarIngr = new Ingredient("Sugar", 5);
		final Ingredient chocolateIngr = new Ingredient("Chocolate", 1);

		recipe1.addIngredient(coffeeIngr);
		recipe1.addIngredient(milkIngr);
		recipe1.addIngredient(sugarIngr);
		recipe1.addIngredient(chocolateIngr);

		recipe1.setPrice(5);

		final Order order1 = new Order();
		order1.setPayment(10);
		order1.addRecipe(recipe1);

		final Recipe recipe2 = new Recipe();
		recipe2.setName("Coffee");
		recipe2.setPrice(4);
		final Ingredient coffee2 = new Ingredient("Coffee", 2);
		final Ingredient milk2 = new Ingredient("Milk", 1);
		final Ingredient sugar2 = new Ingredient("Sugar", 1);
		final Ingredient syrup2 = new Ingredient("Syrup", 1);
		recipe2.addIngredient(sugar2);
		recipe2.addIngredient(milk2);
		recipe2.addIngredient(syrup2);
		recipe2.addIngredient(coffee2);

		recipe1.setPrice(5);

		final Order order2 = new Order();
		order2.setPayment(10);
		order2.addRecipe(recipe2);

		orderservice.save(order1);
		orderservice.save(order2);

		Long id1 = order1.getId();
		Long id2 = order1.getId();

		final Inventory inventory = inventoryservice.getInventory();

		inventory.updateInventory("Coffee", 1000);
		inventory.updateInventory("Syrup", 1000);
		inventory.updateInventory("Sugar", 1000);
		inventory.updateInventory("Milk", 1000);
		inventory.updateInventory("Chocolate", 1000);

		inventoryservice.save(inventory);

		mvc.perform(put("/api/v1/orders/" + id1)).andExpect(status().isOk());
		mvc.perform(put("/api/v1/orders/" + id2)).andExpect(status().isOk());

		assertEquals(orderservice.count(), 2);

		final String order1String = mvc.perform(get("/api/v1/orders/" + id1)).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		assertTrue(order1String.contains("Delicious Not-Coffee"));

		final String order2String = mvc.perform(get("/api/v1/orders/" + id2)).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		assertTrue(order2String.contains("Coffee"));

	}

}