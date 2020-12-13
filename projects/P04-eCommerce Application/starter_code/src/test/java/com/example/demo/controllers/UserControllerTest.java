package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;

    private final UserRepository userRepo = mock(UserRepository.class);

    private final CartRepository cartRepo = mock(CartRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }
    @Test
    public void createUserHappyPath() {
        when(encoder.encode("testPassword")).thenReturn("hashedPass");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testuser");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");
        final ResponseEntity<User> response = userController.createUser(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("testuser", u.getUsername());
        assertEquals("hashedPass", u.getPassword());
    }
    @Test
    public void test_find_by_id(){
        when(encoder.encode("testPassword")).thenReturn("hashedPass");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testuser");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");
        final ResponseEntity<User> response = userController.createUser(request);
        User user = response.getBody();
        when(userRepo.findById(0L)).thenReturn(java.util.Optional.ofNullable(user));

        final ResponseEntity<User> userResponseEntity = userController.findById(0L);

        User u = userResponseEntity.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("testuser", u.getUsername());
        assertEquals("hashedPass", u.getPassword());
    }
    @Test
    public void test_find_by_user_name() {
        when(encoder.encode("testPassword")).thenReturn("hashedPass");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testuser");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");
        final ResponseEntity<User> response = userController.createUser(request);
        User user = response.getBody();
        when(userRepo.findByUsername("testuser")).thenReturn(user);

        final ResponseEntity<User> userResponseEntity = userController.findByUserName("testuser");

        User u = userResponseEntity.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("testuser", u.getUsername());
        assertEquals("hashedPass", u.getPassword());
    }

}
