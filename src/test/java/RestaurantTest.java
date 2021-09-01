
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RestaurantTest {
    public Restaurant restaurant;
    //common method for new restaurant object
    public Restaurant createRestaurant(){
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        Restaurant restaurantObject = new Restaurant("Amelie's cafe","Chennai",openingTime,closingTime);
        restaurantObject.addToMenu("Sweet corn soup",119);
        restaurantObject.addToMenu("Vegetable lasagne", 269);

        return restaurantObject;
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //-------FOR THE 2 TESTS BELOW, YOU MAY USE THE CONCEPT OF MOCKING, IF YOU RUN INTO ANY TROUBLE
    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time(){
        //verify restaurant open based on current time
        restaurant = createRestaurant();
        //create spy restaurant for mocking current time
        LocalTime mockedTime = LocalTime.parse("13:00:00");
        Restaurant spyRestaurant = Mockito.spy(restaurant);
        Mockito.when(spyRestaurant.getCurrentTime()).thenReturn(mockedTime);

        //Assert restaurant open with mocked time
        assertTrue(spyRestaurant.isRestaurantOpen());

    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time(){
        //verify restaurant closed based on current time
        restaurant = createRestaurant();

        //create spy restaurant for mocking current time
        LocalTime mockedTime = LocalTime.parse("06:00:00");
        Restaurant spyRestaurant = Mockito.spy(restaurant);
        Mockito.when(spyRestaurant.getCurrentTime()).thenReturn(mockedTime);

        //Assert restaurant close with mocked time
        assertFalse(spyRestaurant.isRestaurantOpen());

    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<OPEN/CLOSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1(){
        restaurant = createRestaurant();

        int initialMenuSize = restaurant.getMenu().size();
        restaurant.addToMenu("Sizzling brownie",319);
        assertEquals(initialMenuSize+1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1() throws itemNotFoundException {
        restaurant = createRestaurant();

        int initialMenuSize = restaurant.getMenu().size();
        restaurant.removeFromMenu("Vegetable lasagne");
        assertEquals(initialMenuSize-1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {
        restaurant = createRestaurant();

        assertThrows(itemNotFoundException.class,
                ()->restaurant.removeFromMenu("French fries"));
    }
    //<<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    @Test
    public void selecting_items_from_menu_should_return_the_order_cost()
    {
        //Create the restaurant and add all items
        restaurant = createRestaurant();
        restaurant.addToMenu("Sizzling brownie",319);
        restaurant.addToMenu("Garlic bread",111);

        //select few items from the menu
        ArrayList<String> orderNames = new ArrayList<>();
        orderNames.add("Sweet corn soup");
        orderNames.add("Garlic bread");

        //verify the order cost
        int orderTotal = restaurant.calculateOrderCost(orderNames);
        assertEquals(230,orderTotal);
    }

    @Test
    public void removing_items_from_order_should_return_the_updated_order_cost()
    {
        //Create the restaurant and add all items
        restaurant = createRestaurant();
        restaurant.addToMenu("Sizzling brownie",319);
        restaurant.addToMenu("Garlic bread",111);

        //select few items from the menu
        ArrayList<String> orderNames = new ArrayList<>();
        orderNames.add("Sweet corn soup");
        orderNames.add("Vegetable lasagne");
        orderNames.add("Garlic bread");

        //verify the order cost after removing few items
        int preOrderTotal = restaurant.calculateOrderCost(orderNames);
        orderNames.remove("Sweet corn soup");
        assertNotEquals(preOrderTotal,restaurant.calculateOrderCost(orderNames));

    }
}