package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private Cart testCart;
    private Item testItem;
    private User testUser;
    private UserOrder testOrder;

    private CartController cartController;
    private UserRepository userRepo = mock(UserRepository.class);;
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        com.example.demo.TestUtils.injectObjects(cartController, "userRepository", userRepo);
        com.example.demo.TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
        com.example.demo.TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        testItem = new Item();
        testItem.setId(1L);
        testItem.setName("testItem");
        testItem.setDescription("testDescription");
        testItem.setPrice(new BigDecimal(3));

        testCart = new Cart();
        testCart.setId(1L);
        testCart.setItems(new ArrayList<Item>());
        testCart.getItems().add(testItem);
        testCart.setTotal(new BigDecimal(3));
        testCart.setUser(testUser);

        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setId(1L);
        testUser.setCart(testCart);

    }
    @Test
    public void test_add_to_cart(){
        when(userRepo.findByUsername("testUser")).thenReturn(testUser);
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(testItem));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testUser");
        request.setItemId(1L);
        request.setQuantity(1);

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertEquals(200,response.getStatusCodeValue());
        Cart testCartResp = response.getBody();
        assertNotNull(testCartResp);
        assertEquals(new BigDecimal("6"),testCartResp.getTotal());
    }

    @Test
    public void test_remove_from_cart() {
        when(userRepo.findByUsername("testUser")).thenReturn(testUser);
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(testItem));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testUser");
        request.setItemId(1L);
        request.setQuantity(0);

        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertEquals(200,response.getStatusCodeValue());

    }
}
