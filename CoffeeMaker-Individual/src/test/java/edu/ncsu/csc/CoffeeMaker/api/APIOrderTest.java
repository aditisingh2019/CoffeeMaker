package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
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
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
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

	/**
	 * Sets up the tests.
	 */
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();

		// orderservice.deleteAll();
	}

	@Test
	@Transactional
	public void placingOrder1Recipe() throws Exception {

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
	public void testGetOrders() throws Exception {

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
	public void testGetOrder() throws Exception {

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
	public void testDeleteOrder() throws Exception {

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

		final Order order1 = new Order();
		order1.setPayment(10);
		order1.addRecipe(recipe);
		// order1.setId(123L);

		final Order order2 = new Order();
		order2.setPayment(20);
		order2.addRecipe(vanilla);
		// order2.setId(456L);

//		mvc.perform(post("/api/v1/orders/").contentType(MediaType.APPLICATION_JSON)
//				.content(TestUtils.asJsonString(order1)));
//
//		Assertions.assertEquals(1, (int) orderservice.count());
//
//		mvc.perform(post("/api/v1/orders/").contentType(MediaType.APPLICATION_JSON)
//				.content(TestUtils.asJsonString(order2)));
//
//		

		orderservice.save(order1);
		orderservice.save(order2);
		Assertions.assertEquals(2, (int) orderservice.count());

		final String orders = mvc.perform(get("/api/v1/orders")).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();

		assertTrue(orders.contains("Delicious Not-Coffee"));
		assertTrue(orders.contains("Coffee"));

		Long id1 = order1.getId();
		Long id2 = order2.getId();

		mvc.perform(delete("/api/v1/orders/" + id1)).andExpect(status().isOk());
		mvc.perform(delete("/api/v1/orders/" + id2)).andExpect(status().isOk());

		Assertions.assertEquals(0, (int) orderservice.count());

	}

	@Test
	@Transactional
	public void testCreateOrder() throws Exception {

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

		// orderservice.save(order3);

		final String orders = mvc.perform(get("/api/v1/orders")).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		orderservice.save(order3);

//		mvc.perform(put("/api/v1/orders/" + id3).contentType(MediaType.APPLICATION_JSON)
//				.content(TestUtils.asJsonString(id3))).andExpect(status().isOk());

//		mvc.perform(
//				post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(order3)));

		Long id3 = order3.getId();
		System.out.println("*************************************" + id3);

		mvc.perform(put("/api/v1/orders/" + id3)).andExpect(status().isOk());

	}

}