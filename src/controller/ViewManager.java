package controller;

import java.awt.CardLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import GUI.MidtownComics;
import model.Order;
import model.OrderItem;
import model.Product;
import views.CartView;
import views.InventoryView;
import views.OrderView;
import views.ProductView;

public class ViewManager {

    private static ViewManager manager;

    private Container views;
    private List<Product> inventory;
    private Order order;

    /*
     * A private constructor, implementing the singleton pattern.
     * 
     * @param views
     */

    private ViewManager(Container views) {
        this.views = views;

        this.populate();
    }
    
    /**
     * Returns an instance of the ViewManager class, creating one if necessary.
     * 
     * @param views the parent container
     * @return an instance of the ViewManager class
     */

    public static ViewManager getInstance(Container views) {
        if (manager == null) {
            manager = new ViewManager(views);
        }

        return manager;
    }

    /**
     * Switches to the specified named view.
     * 
     * @param view the view to switch to
     */

    public void switchTo(String view) {
        ((CardLayout) views.getLayout()).show(views, view);
    }
    
    /**
     * Attaches a product to the product view.
     * 
     * @param product the product to attach
     */
    
    public void attachProduct(Product product) {
        ((ProductView) views.getComponent(MidtownComics.ProductViewIndex)).setProduct(product);
    }
    
    /**
     * Detaches a product from the product view.
     */
    
    public void detachProduct() {
        ((ProductView) views.getComponent(MidtownComics.ProductViewIndex)).setProduct(null);
    }
    
    /**
     * Adds a new Product to inventory.
     * 
     * @param product the new product
     */

    public void addProductToInventory(Product product) {
        inventory.add(product);

        detachProduct();
        refreshInventoryList();
        switchTo(MidtownComics.InventoryView);
    }

    /**
     * Modifies an existing Product in inventory.
     * 
     * @param product the modified product
     */

    public void modifyProductInInventory(Product product) {
        inventory.set(findProductInInventory(product), product);
        
        detachProduct();
        refreshInventoryList();
        switchTo(MidtownComics.InventoryView);
    }

    /**
     * Removes an existing Product from inventory.
     * 
     * @param product the product to be removed
     */

    public void removeProductFromInventory(Product product) {
        inventory.remove(product);

        detachProduct();
        refreshInventoryList();
        switchTo(MidtownComics.InventoryView);
    }

    /**
     * Adds an OrderItem to an Order, creating the Order first if necessary.
     * 
     * @param item the item to be added
     */

    public void addItemToOrder(OrderItem item) {
        if (order == null) {
            order = new Order();
        }

        order.addItem(item);
        refreshCart();
        updateOrderTotal();
        manager.switchTo(MidtownComics.CartView);
    }

    /**
     * Modify the quantity of an OrderItem in an Order.
     * 
     * @param product  the product to be modified
     * @param quantity the new quantity
     */

    public void modifyItemQuantityInOrder(Product product, int quantity) {
        int index = findItemInOrder(product);
        order.getItems().get(index).setQuantity(quantity);        

        refreshCart();
        updateOrderTotal();
        switchTo(MidtownComics.InventoryView);
        switchTo(MidtownComics.CartView);
    }

    /**
     * Remove an OrderItem from an existing Order.
     * 
     * @param product the product to be removed
     */

    public void removeItemFromOrder(Product product) {
        int index = findItemInOrder(product);
        order.getItems().remove(index);

        refreshCart();
        updateOrderTotal();
        switchTo(MidtownComics.InventoryView);
        
        if (order.getItems().size() > 0) {
            switchTo(MidtownComics.CartView);
        }
    }
    
    /**
     * Returns the OrderItem quantity by Product.
     * 
     * @param product the product whose quantity we need
     * @return the order item quantity
     */
    
    public int getOrderItemQuantity(Product product) {
        int index = findItemInOrder(product);
        
        return index != -1 ? order.getItems().get(index).getQuantity() : 0;
    }
    
    /**
     * Returns the subtotal for this Product.
     * 
     * @param product the product whose subtotal we need
     * @return the subtotal
     */
    
    public double getSubtotal(Product product) {
        int index = findItemInOrder(product);
        
        return index != -1 ? order.getItems().get(index).getPrice() : 0;
    }
    
    /**
     * Returns whether or not the Product already exists in the Order.
     * 
     * @param product the product
     * @return whether or not it exists in the order
     */
    
    public boolean productExistsInOrder(Product product) {
        if (order == null) {
            return false;
        }
        
        return findItemInOrder(product) != -1;
    }
    
    /**
     * Submits an order, which will modify the product in inventory.
     */
    
    public void submitOrder() {
        for (int i = 0; i < order.getItems().size(); i++) {
            OrderItem item = order.getItems().get(i);
            Product product = item.getProduct();
            int quantity = item.getQuantity();
            
            int index = findProductInInventory(product);
            if (index != -1) {
                int copies = product.getCopies();
                product.setCopies(copies - quantity);
                
                modifyProductInInventory(product);
            }
        }
        
        clearOrder();
    }

    /**
     * Returns the list of products in inventory.
     *
     * @return inventory
     */

    public List<Product> getInventory() {
        return inventory;
    }
    
    /**
     * Returns the Order.
     *
     * @return order
     */
    
    public Order getOrder() {
        return order;
    }

    /*
     * Populates a set of products in inventory. Ordinarily, this sort of thing
     * would be handled by a database.
     */

    private void populate() {
        if (inventory == null) {
            inventory = new ArrayList<>();
        }

        inventory.add(new Product("The Amazing Spider-Man", "Stan Lee", 19630301L, 1, 19.99, 3));
        inventory.add(new Product("The Amazing Spider-Man", "Stan Lee", 19630510L, 2, 19.99, 7));
        inventory.add(new Product("The Amazing Spider-Man", "Stan Lee", 19630710L, 3, 19.99, 9));
        inventory.add(new Product("The Amazing Spider-Man", "Stan Lee", 19630910L, 4, 19.99, 12));
        inventory.add(new Product("The Amazing Spider-Man", "Stan Lee", 19631010L, 5, 19.99, 3));
        inventory.add(new Product("The Amazing Spider-Man", "Stan Lee", 19631110L, 6, 19.99, 7));
        inventory.add(new Product("The Amazing Spider-Man", "Stan Lee", 19631210L, 7, 19.99, 9));
        inventory.add(new Product("The Amazing Spider-Man", "Stan Lee", 19640110L, 8, 19.99, 12));
        inventory.add(new Product("The Amazing Spider-Man", "Stan Lee", 19640210L, 9, 19.99, 3));
        inventory.add(new Product("The Amazing Spider-Man", "Stan Lee", 19640310L, 10, 19.99, 7));
    }
    
    /*
     * Refreshes the inventory view when the list of products changes.
     */
    
    private void refreshInventoryList() {
        ((InventoryView) views.getComponent(MidtownComics.InventoryViewIndex)).refreshInventoryList();
    }
    
    /*
     * Refreshes the cart view when an item changes.
     */
    
    private void refreshCart() {
        ((CartView) views.getComponent(MidtownComics.CartViewIndex)).refreshCart();
    }
    
    /*
     * Refreshes the order view when the order total changes.
     */
    
    private void updateOrderTotal() {
        ((OrderView) views.getComponent(MidtownComics.OrderViewIndex)).updateOrderTotal(order.getTotal());
    }
    
    /*
     * Refreshes the cart view when an order is submitted.
     */
    
    private void clearOrder() {
        ((OrderView) views.getComponent(MidtownComics.OrderViewIndex)).clearOrder();
    }
    
    /*
     * Finds a product in inventory.
     *
     * @param product the product we're looking for
     * @return the index of the product if found
     */

    private int findProductInInventory(Product product) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getProductId() == product.getProductId()) {
                return i;
            }
        }

        return -1;
    }
    
    /*
     * Finds an item in an order.
     *
     * @param product the item we're looking for
     * @return the index of the item if found
     */

    private int findItemInOrder(Product p) {
        for (int i = 0; i < order.getItems().size(); i++) {
            if (order.getItems().get(i).getProduct().getProductId() == p.getProductId()) {
                return i;
            }
        }

        return -1;
    }
}
