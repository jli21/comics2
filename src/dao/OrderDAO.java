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

public class OrderDAO {

    /**
     * Retrieves a Product from the database.
     * 
     * @param productId the productId of the product to retrieve
     * @return the product retrieved from the database
     * @throws SQLException 
     */
    
    public static Order getOrder(long orderId) throws SQLException {
        Order order = null;

        Connection conn = DAO.getConnection();
            
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM orders WHERE orderId = ?");
  
        pstmt.setLong(1, orderId);

        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            order = new Order();
            
            order.setOrderId(rs.getLong(1));
            order.setOrderDate(rs.getLong(2));
            order.setStatus(rs.getString(3));
            order.setTotal(rs.getDouble(4));
            order.setCustomer((Customer)(rs.getRef(5)).getObject());
            
        }
                 
        rs.close();
        pstmt.close();
        conn.close();
        
        return order;
    }
    
    /**
     * Retrieves all Products from the database.
     * 
     * @return a list of products retrieved from the database
     * @throws SQLException
     */
    
    public static List<Order> getOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        Connection conn = DAO.getConnection();
        
        Statement stmt = conn.createStatement();
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM orders");
                
        while (rs.next()) {
            Order order = new Order();
            order.setOrderId(rs.getLong(1));
            order.setOrderDate(rs.getLong(2));
            order.setStatus(rs.getString(3));
            order.setTotal(rs.getDouble(4));
            order.setCustomer(CustomerDAO.getCustomer(rs.getLong(5)));
            
            orders.add(order);
        }
        
        rs.close();
        stmt.close();
        conn.close();
        
        return orders;
    }
    
    public static List<Order> getOrders(long customerId) throws SQLException {
    	List<Order> orders = new ArrayList<>();
        Connection conn = DAO.getConnection();
        
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM orders WHERE customerId = ?");
        
        
        pstmt.setLong(1, customerId);
        
        ResultSet rs = pstmt.executeQuery();
                
        while (rs.next()) {
            Order order = new Order();
            order.setOrderId(rs.getLong(1));
            order.setOrderDate(rs.getLong(2));
            order.setStatus(rs.getString(3));
            order.setTotal(rs.getDouble(4));
            order.setCustomer(CustomerDAO.getCustomer(rs.getLong(5)));
            
            orders.add(order);
        }
        
        rs.close();
        pstmt.close();
        conn.close();
        
        return orders;
    }
    
    /**
     * Inserts a Product into the database.
     * 
     * @param product the product to insert into the database
     * @throws SQLException
     */
        
    public static void insertOrder(Order order) throws SQLException {        
        Connection conn = DAO.getConnection();        
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO orders (" +
            "   orderdate, " +
            "   status, " +
            "   total, " +
            "   customerid " +
            ") VALUES (?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        
        pstmt.setLong(1, order.getOrderDate());
        pstmt.setString(2, order.getStatus());
        pstmt.setDouble(3, order.getTotal());
        pstmt.setLong(4, order.getCustomer().getCustomerId());
        
        pstmt.executeUpdate();
        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                order.setOrderId(generatedKeys.getLong(1));
            }
            else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }
        }
        pstmt.close();
        conn.close();
    }
    
    /**
     * Updates an existing Product in the database
     * 
     * @param product the new product used to update the old one
     * @throws SQLException
     */
    
    public static void updateOrder(Order order) throws SQLException {
        Connection conn = DAO.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "UPDATE orders SET " +
            "   orderDate = ?, " +
            "   status = ?, " +
            "   total = ?, " +
            "   customerid = ?, " +
            "WHERE id = ?"
        );
                
        pstmt.setLong(1, order.getOrderDate());
        pstmt.setString(2, order.getStatus());
        pstmt.setDouble(3, order.getTotal());
        pstmt.setLong(4, order.getCustomer().getCustomerId());
        pstmt.setLong(5,  order.getOrderId());
        
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
    
    public static void deleteOrder(Order order) throws SQLException {
        Connection conn = DAO.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM orders WHERE id = ?");

        pstmt.setLong(1, order.getOrderId());
        
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }
}


