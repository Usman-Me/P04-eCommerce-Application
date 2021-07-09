package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.junit.Assert;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void init(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void CreateUserTest() throws Exception{
        when(encoder.encode("TestPassword")).thenReturn("thisIsHashed");
        CreateUserRequest UserRequest = new CreateUserRequest();
        UserRequest.setUsername("Test");
        UserRequest.setPassword("TestPassword");
        UserRequest.setConfirmPassword("TestPassword");

        final ResponseEntity<User> response =  userController.createUser(UserRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        Assert.assertNotNull(user);
        Assert.assertEquals(0, user.getId());
        Assert.assertEquals("Test", user.getUsername());
    }
    @Test
    public void FindUserByIdTest()
    {

    User user = new User();
    user.setUsername("NewTestUser");
    user.setId(1L);
    user.setPassword("TestPassword");
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    ResponseEntity<User> response = userController.findById(1L);

    Assert.assertNotNull(response);
    Assert.assertEquals(200, response.getStatusCodeValue());

    User CurrentUser = response.getBody();
    Assert.assertNotNull(CurrentUser);
    Assert.assertEquals(1L, CurrentUser.getId());

    Assert.assertEquals("NewTestUser", CurrentUser.getUsername());
    Assert.assertEquals("TestPassword", CurrentUser.getPassword());
    }

    @Test
    public void FindUserByUserNameTest()
    {
        User user = new User();
        user.setUsername("NewTestUser");
        user.setId(1L);
        user.setPassword("TestPassword");

        when(userRepository.findByUsername("NewTestUser")).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName("NewTestUser");

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        User CurrentUser = response.getBody();
        Assert.assertNotNull(CurrentUser);
        Assert.assertEquals(1L, CurrentUser.getId());
        Assert.assertEquals("NewTestUser", CurrentUser.getUsername());
        Assert.assertEquals("TestPassword", CurrentUser.getPassword());

    }





}
