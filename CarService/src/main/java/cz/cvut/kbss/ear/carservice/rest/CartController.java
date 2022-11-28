package cz.cvut.kbss.ear.carservice.rest;

import cz.cvut.kbss.ear.carservice.exceptions.NotFoundException;
import cz.cvut.kbss.ear.carservice.model.*;
import cz.cvut.kbss.ear.carservice.service.CartService;
import cz.cvut.kbss.ear.carservice.service.ItemService;
import cz.cvut.kbss.ear.carservice.service.ProductService;
import cz.cvut.kbss.ear.carservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;
    private final ItemService itemService;

    @Autowired
    public CartController(CartService cartService, UserService userService, ProductService productService, ItemService itemService) {
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
        this.itemService = itemService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Cart getCart() {
        User user = userService.getCurrentUser();
        return user.getCart();
    }

    @PutMapping(value = "/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addItem(@RequestBody CartItem item) {
        Product product = productService.findById(item.getProduct().getId());
        if (product == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Product with id " + item.getProduct().getId() + " is not found" );
        }
        item.setProduct(product);
        User user = userService.getCurrentUser();
        Cart cart = user.getCart();
        cartService.addItem(cart, item);
    }

    @DeleteMapping(value = "/items/{id}")
    public void removeItem(@PathVariable Integer id) {
        Item item = itemService.findById(id);
        if (item == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Item with id " + id + " is not found" );
        }
        User user = userService.getCurrentUser();
        Cart cart = user.getCart();
        cartService.removeItem(cart, item);
    }

    @DeleteMapping(value = "/items/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public void clearCart() {
        User user = userService.getCurrentUser();
        Cart cart = user.getCart();
        cartService.removeAll(cart);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/move/{id}")
    public void moveItems(@PathVariable Integer id) {
        Cart to = cartService.getCartByUserId(id);
        if (to == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User with id " +id + " is not found" );
        }
        User admin = userService.getCurrentUser();
        cartService.moveItemsFromOneCartToAnother(admin.getCart(), to);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Cart getCartById(@PathVariable Integer id) {
        User user = userService.findUser(id);
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User with id " +id + " is not found" );
        }
        return user.getCart();
    }
}
