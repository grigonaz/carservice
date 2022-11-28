package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.CartDao;
import cz.cvut.kbss.ear.carservice.dao.ProductDao;
import cz.cvut.kbss.ear.carservice.exceptions.EmptyCartException;
import cz.cvut.kbss.ear.carservice.exceptions.InsufficientAmountException;
import cz.cvut.kbss.ear.carservice.exceptions.ItemNotExistException;
import cz.cvut.kbss.ear.carservice.exceptions.NotFoundException;
import cz.cvut.kbss.ear.carservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class CartService {

    private final CartDao cartDao;
    private final ProductDao productDao;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public CartService(CartDao cartDao, ProductDao productDao, UserService userService, ProductService productService) {
        this.cartDao = cartDao;
        this.productDao = productDao;
        this.userService = userService;
        this.productService = productService;
    }

    @Transactional
    public void addItem(Cart cart, Item item) {
        Objects.requireNonNull(item);
        Objects.requireNonNull(cart);
        if(productService.findById(item.getProduct().getId()) == null){
            throw new NotFoundException("Product with id" + item.getProduct().getId() + " doesn't exist");
        }
        Product product = productService.findById(item.getProduct().getId());
        item.setProduct(product);
        updateProductAmountOnItemCreation(item);
        CartItem ci = (CartItem) item;
        cart.addItem(ci);
        cartDao.update(cart);
    }

    private void updateProductAmountOnItemCreation(Item item) {
        final Product product = item.getProduct();

        if (product.getAmount() < item.getAmount()) {
            throw new InsufficientAmountException(
                    "The amount of product " + product + " is insufficient to create cart item.");
        }

        product.setAmount(product.getAmount() - item.getAmount());
        productDao.update(product);
    }

    @Transactional
    public void removeItem(Cart cart, Item item) {
        Objects.requireNonNull(item);
        Objects.requireNonNull(cart);
        CartItem ci = (CartItem) item;
        removeItem(cart,ci);
        cartDao.update(cart);
    }

    @Transactional
    public void removeItem(Cart cart,CartItem item){
        if (cart.getItems() == null) {
            throw new EmptyCartException("Attempt to remove item without items in it");
        }

        CartItem op = cart.getItems().stream().filter(exIt -> exIt.getId().equals(item.getId())).findFirst().orElse(null);

        if (op != null) {
            int itemAmount = op.getAmount();
            op.getProduct().setAmount(itemAmount + op.getProduct().getAmount());
            productDao.update(op.getProduct());
            op.setAmount(op.getAmount() - item.getAmount());

            if (op.getAmount() <= 0) {
                cart.getItems().remove(op);
            }
        } else {
            throw new ItemNotExistException("Attempt to remove item, which is not in the cart");
        }
    }

    @Transactional
    public void removeAll(Cart cart) {
        Objects.requireNonNull(cart);
        for(CartItem c : cart.getItems()){
            int itemAmount = c.getAmount();
            c.getProduct().setAmount(itemAmount + c.getProduct().getAmount());
            productDao.update(c.getProduct());
        }
        cart.setItems(new ArrayList<>());
        cartDao.update(cart);
    }

    @Transactional
    public void moveItemsFromOneCartToAnother(Cart from, Cart to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        ArrayList<CartItem> itemsToMove = new ArrayList<>(from.getItems());
        to.setItems(itemsToMove);
        from.getItems().clear();
        cartDao.update(from);
        cartDao.update(to);
    }

    @Transactional
    public Cart getCartByUserId(Integer id){
        User user = userService.findUser(id);
        Cart cart =  user.getCart();
        return cart;
    }
}
