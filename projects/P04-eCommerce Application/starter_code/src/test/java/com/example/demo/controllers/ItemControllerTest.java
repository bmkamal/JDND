package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ItemControllerTest {
    public Item testItem;
    public ItemRepository itemRepo = mock(ItemRepository.class);
    public ItemController itemController;

    @Before
    public void setup(){
        itemController = new ItemController();
        com.example.demo.TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
        testItem = new Item();
        testItem.setId(1L);
        testItem.setName("testItem");
        testItem.setDescription("testDescription");
        testItem.setPrice(new BigDecimal(3));
    }

    @Test
    public void test_get_item_by_Id() {
        when(itemRepo.findById(1L)).thenReturn(java.util.Optional.ofNullable(testItem));
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("testItem", response.getBody().getName());
    }
    @Test
    public void test_get_items_by_name() {
        List<Item> testItemsList = new ArrayList<>();
        testItemsList.add(testItem);
        when(itemRepo.findByName("testItem")).thenReturn(testItemsList);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("testItem");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("testItem", response.getBody().get(0).getName());

        

    }
}
