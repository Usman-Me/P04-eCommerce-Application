package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.junit.Assert;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.demo.TestUtils.*;
import static com.example.demo.TestUtils.createOrders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    public static final String ROUND_WIDGET = "Round Widget";

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void init()
    {
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(createItem(1)));
        Mockito.when(itemRepository.findAll()).thenReturn(createItems());
     //   Mockito.when(itemRepository.findByName("Item")).thenReturn(Arrays.asList(createItem(1), createItem(2)));
    }

    @Test
    public void getItemsTest()
    {
        ResponseEntity<List<Item>> response = itemController.getItems();
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        Assert.assertEquals(createItems(), items);
        verify(itemRepository , times(1)).findAll();
    }

    @Test
    public void getItemByIdTest()
    {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        Assert.assertNotNull(response);
        Assert. assertEquals(200, response.getStatusCodeValue());
        Item item = response.getBody();
        Assert.assertEquals(createItem(1L), item);
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    public void getItemByIdInvalidTest()
    {
        ResponseEntity<Item> response = itemController.getItemById(10L);
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
        Assert.assertNull(response.getBody());
        verify(itemRepository, times(1)).findById(10L);
    }

    @Test
    public void getItemsByNameTest()
    {
        Item item0 = getItem0();
        List<Item> items = new ArrayList<>(2);
        items.add(item0);
        when(itemRepository.findByName(ROUND_WIDGET)).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName(ROUND_WIDGET);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> retrievedItems = response.getBody();
        assertNotNull(retrievedItems);
        assertEquals(1, retrievedItems.size());
        assertEquals(item0, retrievedItems.get(0));

     /*   ResponseEntity<List<Item>> response = itemController.getItemsByName("item");
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        List<Item> items = Arrays.asList(createItem(1), createItem(2),createItem(3));
        Assert.assertEquals(createItems(), items);
        verify(itemRepository , times(1)).findByName("item"); */
    }

    @Test
    public void getItemByInvalidNameTest()
    {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Invalid Name");
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
        Assert.assertNull(response.getBody());
        verify(itemRepository , times(1)).findByName("Invalid Name");
    }

    public static Item getItem0() {
        Item item = new Item();
        item.setId(0L);
        item.setName(ROUND_WIDGET);
        item.setPrice(new BigDecimal("2.99"));
        item.setDescription("A widget that is round");
        return item;
    }

    public static Item getItem1() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Square Widget");
        item.setPrice(new BigDecimal("1.99"));
        item.setDescription("A widget that is square");
        return item;
    }

}
