package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Customer;
import model.Order;
import model.OrderItem;
import model.Product;

public class OrderItemDAO {
	
	public int orderId;

    /**
     * Retrieves a Product from the database.
     * 
     * @param productId the productId of the product to retrieve
     * @return the product retrieved from the database
     * @throws SQLException 
     */
    
    public static OrderItem getOrderItem(long itemId) throws SQLException {
        OrderItem orderItem = null;
        
        Connection conn = DAO.getConnection();

        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM orderitems WHERE orderId = ?");
        

        pstmt.setLong(1, itemId);

        
        ResultSet rs = pstmt.executeQuery();
        
            
        if (rs.next()) {
            orderItem = new OrderItem((Product) (rs.getRef(4)).getObject());
            orderItem.setItemId(rs.getLong(1));
            orderItem.setQuantity(rs.getInt(2));
        }

                
        rs.close();
        pstmt.close();
        conn.close();
        
        return orderItem;
    }
    
    /**
     * Retrieves all Products from the database.
     * 
     * @return a list of products retrieved from the database
     * @throws SQLException
     */
    
    public static List<OrderItem> getOrderItems(long itemId, List<Product> products) throws SQLException {
        List<OrderItem> orderItems = new ArrayList<>();
        Connection conn = DAO.getConnection();
        
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM orderitems WHERE orderid = ?");
        
        pstmt.setLong(1,  itemId);
        
        ResultSet rs = pstmt.executeQuery();
            
        while (rs.next()) {
            pstmt.setLong(1,  rs.getLong(1));
            for (int i = 0; i < products.size(); i++) {
            	if (rs.getLong(1) == products.get(i).getProductId()) {
            		OrderItem orderItem = new OrderItem(products.get(i));
            		
            		orderItem.setItemId(rs.getLong(1));
            		orderItem.setQuantity(rs.getInt(2));
            		
            		orderItems.add(orderItem);
            	}
            }
        }
        
        rs.close();
        pstmt.close();
        conn.close();
        
        return orderItems;
    }
    
    /**
     * Inserts a Product into the database.
     * 
     * @param product the product to insert into the database
     * @throws SQLException
     */
        
    public static void insertOrderItem(OrderItem orderItem, Order order) throws SQLException {        
        Connection conn = DAO.getConnection();        
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO orderitems (" +
            "   quantity, " +
            "   orderid, " +
            "   productid " +
            ") VALUES (?, ?, ?)"
        );
        
        pstmt.setInt(1, orderItem.getQuantity());
        pstmt.setLong(2, order.getOrderId());
        pstmt.setLong(3, orderItem.getProduct().getProductId());
        
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }
    
    /**
     * Updates an existing Product in the database
     * 
     * @param product the new product used to update the old one
     * @throws SQLException
     */
    
    public static void updateOrder(OrderItem orderItem, Order order) throws SQLException {
        Connection conn = DAO.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "UPDATE orders SET " +
            "   quantity = ?, " +
            "   orderid = ?, " +
            "   productid = ?, " +
            "WHERE id = ?"
        );
                
        pstmt.setInt(1, orderItem.getQuantity());
        pstmt.setLong(2, order.getOrderId());
        pstmt.setLong(3, orderItem.getProduct().getProductId());
        
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }
    
    /**
     * Deletes an existing Product from the database.
     * 
     * @param product the product to delete
     * @throws SQLException
     */
    
    public static void deleteOrderItem(OrderItem orderItem) throws SQLException {
        Connection conn = DAO.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM orderItems WHERE id = ?");
        
        pstmt.setLong(1, orderItem.getItemId());
        
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }
}


