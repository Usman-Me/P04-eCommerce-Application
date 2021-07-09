package com.example.demo.controllers;


import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.junit.Assert;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.example.demo.TestUtils.*;
import static com.example.demo.TestUtils.createOrders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {
    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    /*
    * Vor allem Test Methoden laufen lassen
     */
    @Before
    public void init()
    {
        User user = createUser();


        when(userRepository.findByUsername("TestUserHarald")).thenReturn(user);
        when(orderRepository.findByUser(any())).thenReturn(createOrders());
    }

    /*
     * Order versenden Test
     */
    @Test
    public void submitOrderTest()
    {

        ResponseEntity<UserOrder> response = orderController.submit("TestUserHarald");
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        UserOrder order = response.getBody();
        Assert.assertEquals(createItems(), order.getItems());
        Assert.assertEquals(createUser().getId(), order.getUser().getId());
        verify(orderRepository, times(1)).save(order);

    }

    /*
     * Order bekommen Tests
     */
    @Test
    public void getOrdersTest()
    {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("TestUserHarald");
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> orders = response.getBody();
        Assert.assertEquals(createOrders().size(), orders.size());

    }

    /*
     * Ungültiges Order Senden Test
     */
    @Test
    public void submitInvalidOrderTest()
    {
        ResponseEntity<UserOrder> response = orderController.submit("InvalidUser");
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
        Assert.assertNull( response.getBody());
        verify(userRepository, times(1)).findByUsername("InvalidUser");
    }

    /*
     * Von Ungültigen Benutzer Order bekommen Test
     */
    @Test
    public void getOrdersForInvalidUserTest()
    {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("InvalidUser");
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
        Assert.assertNull( response.getBody());
        verify(userRepository, times(1)).findByUsername("InvalidUser");
    }



}
