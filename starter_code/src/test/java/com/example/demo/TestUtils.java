package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject){

        boolean wasPrivate = false;

        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            if (!f.isAccessible()){
                f.setAccessible(true);
                wasPrivate = true;
            }
            f.set(target, toInject);

            if(wasPrivate){
                f.setAccessible(false);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /*
    * Neuen User anlegen
    * */
    public static User createUser()
    {
        User NewUser = new User();
        NewUser.setId(1L);
        NewUser.setUsername("TestUserHarald");
        NewUser.setPassword("TestPassword");
        NewUser.setCart(createCart(NewUser));
        return NewUser;
    }


    /*
     * Neuen Warenkorb anlegen
     * */
    public static Cart createCart(User user)
    {
        Cart NewCart = new Cart();
        NewCart.setId(1L);
        List<Item> items = createItems();
        NewCart.setItems(createItems());
        NewCart.setTotal(items.stream().map(item -> item.getPrice()).reduce(BigDecimal::add).get());
        NewCart.setUser(user);
        return NewCart;
    }


    /*
     * Neuen Warenkorb Auftrag anlegen
     * */
    public static List<UserOrder> createOrders()
    {
        List<UserOrder> orders = new ArrayList<>();
        IntStream.range(0,2).forEach(i -> {
            UserOrder NewOrder = new UserOrder();
            Cart NewCart = createCart(createUser());
            NewOrder.setItems(NewCart.getItems());
            NewOrder.setTotal(NewCart.getTotal());
            NewOrder.setUser(createUser());
            NewOrder.setId(Long.valueOf(i));
            orders.add(NewOrder);
        });
        return orders;
    }

    /*
     * Neues Produkt anlegen
     * */
    public static Item createItem(long id)
    {
        Item NewItem = new Item();
        NewItem.setId(id);
        NewItem.setPrice(BigDecimal.valueOf(id * 1.2));
        NewItem.setName("Item: " + NewItem.getId());
        NewItem.setDescription("This is an Test Item!");
        return NewItem;
    }
    /*
     * Liste mit Items erstellen
     * */
    public static List<Item> createItems()
    {
        List<Item> items = new ArrayList<>();
        for (int i = 1; i <= 3; i++)
        {
            items.add(createItem(i));
        }
        return items;
    }
}
