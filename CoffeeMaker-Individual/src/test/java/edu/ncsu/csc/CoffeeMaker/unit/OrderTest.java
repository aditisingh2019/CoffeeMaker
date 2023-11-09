package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = TestConfig.class)
public class OrderTest {

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private OrderService orderService;

	@BeforeEach
	public void setUp() {
		orderService.deleteAll();
		recipeService.deleteAll();
	}

	@Test
	@Transactional
	public void testAddRecipe() {
		final Order order = new Order();
		final Recipe recipe2 = createRecipe("Coffee", 50, 3, 1, 1, 0);

		final Recipe recipe1 = createRecipe("Tea", 50, 3, 1, 1, 0);

		recipeService.save(recipe1);
		recipeService.save(recipe2);
		order.addRecipe(recipe1);
		orderService.save(order);
		assertEquals(1, order.getRecipes().size());
		assertTrue(order.getRecipes().contains(recipe1));
		final Order order2 = new Order();
		order2.addRecipe(recipe1);
		orderService.save(order2);
	}

	@Test
	@Transactional
	public void testDiffAddRecipe() {
		final Order order = new Order();
		final Recipe recipe2 = createRecipe("Coffee", 50, 3, 1, 1, 0);

		final Recipe recipe1 = createRecipe("Tea", 50, 3, 1, 1, 0);

		recipeService.save(recipe1);
		recipeService.save(recipe2);

		order.addRecipe(recipe1);
		orderService.save(order);
		final List<Recipe> recipes1 = orderService.findAll().get(0).getRecipes();
		Assertions.assertEquals(recipe1.getId(), recipes1.get(0).getId());
		assertEquals(1, order.getRecipes().size());
		assertTrue(order.getRecipes().contains(recipe1));
		final Order order2 = new Order();
		order2.addRecipe(recipe1);
		orderService.save(order2);
		final List<Recipe> recipes2 = orderService.findAll().get(1).getRecipes();
		Assertions.assertEquals(recipe1.getId(), recipes2.get(0).getId());
		final Order order3 = new Order();
		order3.addRecipe(recipe1);
		order3.addRecipe(recipe1);
		order3.addRecipe(recipe2);
		orderService.save(order3);

		final List<Recipe> recipes3 = orderService.findAll().get(2).getRecipes();
		Assertions.assertEquals(recipe1.getId(), recipes3.get(0).getId());
		Assertions.assertEquals(recipe1.getId(), recipes3.get(1).getId());
		Assertions.assertEquals(recipe2.getId(), recipes3.get(2).getId());
		Assertions.assertTrue(true);

	}

	@Test
	@Transactional
	public void testAddRecipes() {
		final Order order = new Order();
		final Recipe recipe2 = createRecipe("Coffee", 50, 3, 1, 1, 0);

		order.addRecipes(recipe2, 3);
		assertEquals(3, order.getRecipes().size());
		assertTrue(order.getRecipes().contains(recipe2));
	}

	@Test
	@Transactional
	public void testDeleteOneRecipe() {
		final Order order = new Order();

		final Recipe recipe1 = createRecipe("Tea", 50, 3, 1, 1, 0);
		order.addRecipe(recipe1);
		assertTrue(order.deleteOneRecipe(recipe1));
		assertEquals(0, order.getRecipes().size());
	}

	@Test
	@Transactional
	public void testOrderConstructor() {
		final Recipe recipe2 = createRecipe("Coffee", 50, 3, 1, 1, 0);

		final Recipe recipe1 = createRecipe("Tea", 50, 3, 1, 1, 0);
		final List<Recipe> recipes = new ArrayList<>();
		recipes.add(recipe1);
		recipes.add(recipe2);
		final Order newOrder = new Order(recipes, 100); // Assuming payment of
														// 100
		assertEquals("Placed", newOrder.getStatus());
		assertEquals(2, newOrder.getRecipes().size());
		assertEquals(100, newOrder.getPrice().intValue()); // Assuming recipe
															// prices are 50
															// and
															// 30
		assertEquals(100, newOrder.getPayment().intValue());
	}

	@Test
	@Transactional
	public void testInsufficientPayment() {

		final Recipe recipe1 = createRecipe("Tea", 50, 3, 1, 1, 0);
		final List<Recipe> recipes = new ArrayList<>();
		recipes.add(recipe1);
		try {
			new Order(recipes, 30); // Payment is less
									// than
			// the recipe price
		} catch (final IllegalArgumentException e) {
			Assertions.assertEquals(e.getMessage(), "Payment is not sufficient");
		}

	}

	@Test
	@Transactional
	public void testAddExistingRecipe() {
		final Order order = new Order();

		final Recipe recipe1 = createRecipe("Tea", 50, 3, 1, 1, 0);
		order.addRecipe(recipe1);
		order.addRecipe(recipe1); // Adding the same recipe again
		assertEquals(2, order.getRecipes().size());
	}

	@Test
	@Transactional
	public void testDeleteNonExistingRecipe() {
		final Order order = new Order();

		final Recipe recipe1 = createRecipe("Tea", 50, 3, 1, 1, 0);
		order.addRecipe(recipe1);
		final Recipe nonExistingRecipe = new Recipe();
		final boolean result = order.deleteOneRecipe(nonExistingRecipe);
		assertFalse(result);
	}

	@Test
	@Transactional
	public void testDeleteAllOfNonExistingRecipe() {
		final Order order = new Order();

		final Recipe recipe1 = createRecipe("Tea", 50, 3, 1, 1, 0);
		order.addRecipe(recipe1);
		final Recipe nonExistingRecipe = new Recipe();
		order.deleteAllOfOne(nonExistingRecipe);
		assertEquals(1, order.getRecipes().size());
	}

	@Test
	@Transactional
	public void testAddRecipeWithIngredients() {
		final Order order = new Order();

		final Recipe recipe1 = createRecipe("Tea", 50, 3, 1, 1, 0);
		order.addRecipe(recipe1);
		// Check if the order contains all the ingredients of the added recipe
		assertTrue(order.getRecipes().get(0).getIngredients().containsAll(recipe1.getIngredients()));
	}

	@Test
	@Transactional
	public void testCancelOrder() {
		final Order order = new Order();
		order.cancelOrder();
		assertEquals("Cancelled", order.getStatus());
	}

	@Test
	@Transactional
	public void testNotifyCustomer() {
		final Order order = new Order();
		order.createOrder();
		assertEquals("Created", order.getStatus());
	}

	@Test
	@Transactional
	public void testEmptyOrder() {
		assertEquals(0, new Order().getRecipes().size());
		assertEquals(0, new Order().getPrice().intValue());
		assertEquals(0, new Order().getPayment().intValue());
		assertEquals("Placed", new Order().getStatus());
	}

	@Test
	@Transactional
	public void testEqualsDifferentOrder() {
		final Order order1 = new Order();
		final Order order2 = null;
		assertFalse(order1.equals(order2));
	}

	@Test
	@Transactional
	public void testEqualsSameOrder() {
		final Order order1 = new Order();
		final Order order2 = new Order();
		order2.setId(order1.getId());
		order2.setPayment(order1.getPayment());

		// added
		order2.setStatus(order1.getStatus());
		assertTrue(order1.equals(order2));
	}

	@Test
	@Transactional
	public void testHashCodeConsistency() {
		final Order order = new Order();
		final int firstHashCode = order.hashCode();
		final int secondHashCode = order.hashCode();
		assertEquals(firstHashCode, secondHashCode);
	}

	@Transactional
	private Recipe createRecipe(final String name, final int price, final Integer coffee, final Integer milk,
			final Integer sugar, final Integer chocolate) {
		final Recipe recipe = new Recipe();
		recipe.setName(name);
		recipe.setPrice(price);
		final Ingredient coffeeIngr = new Ingredient("Coffee", coffee);
		final Ingredient milkIngr = new Ingredient("Milk", milk);
		final Ingredient sugarIngr = new Ingredient("Sugar", sugar);
		final Ingredient chocolateIngr = new Ingredient("Chocolate", chocolate);

		recipe.addIngredient(coffeeIngr);
		recipe.addIngredient(milkIngr);
		recipe.addIngredient(sugarIngr);
		recipe.addIngredient(chocolateIngr);

		return recipe;
	}

}
