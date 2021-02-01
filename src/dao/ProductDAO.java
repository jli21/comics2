package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Product;

public class ProductDAO {

    /**
     * Retrieves a Product from the database.
     * 
     * @param productId the productId of the product to retrieve
     * @return the product retrieved from the database
     * @throws SQLException 
     */
    
    public static Product getProduct(long productId) throws SQLException {
        Product product = null;

        Connection conn = DAO.getConnection();
        
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM products WHERE productId = ?");
        
     
        pstmt.setLong(1, productId);
        
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            product = new Product();
            
            product.setProductId(rs.getLong(1));
            product.setTitle(rs.getString(2));
            product.setAuthor(rs.getString(3));
            product.setReleaseDate(rs.getLong(4));
            product.setIssue(rs.getInt(5));
            product.setUnitPrice(rs.getDouble(6));
            product.setCopies(rs.getInt(7));
        }
        
           
        rs.close();
        pstmt.close();
        conn.close();
        
        return product;
    }
    
    /**
     * Retrieves all Products from the database.
     * 
     * @return a list of products retrieved from the database
     * @throws SQLException
     */
    
    public static List<Product> getProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection conn = DAO.getConnection();

        Statement stmt = conn.createStatement();
 
        ResultSet rs = stmt.executeQuery("SELECT * FROM products");
      
        while (rs.next()) {
            Product product = new Product();
            
            product.setProductId(rs.getLong(1));
            product.setTitle(rs.getString(2));
            product.setAuthor(rs.getString(3));
            product.setReleaseDate(rs.getLong(4));
            product.setIssue(rs.getInt(5));
            product.setUnitPrice(rs.getDouble(6));
            product.setCopies(rs.getInt(7));
                        
            products.add(product);
        }
        
        rs.close();
        stmt.close();
        conn.close();
        
        return products;
    }
    
    /**
     * Inserts a Product into the database.
     * 
     * @param product the product to insert into the database
     * @throws SQLException
     */
        
    public static void insertProduct(Product product) throws SQLException {        
        Connection conn = DAO.getConnection();        
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO products (" +
            "   title, " +
            "   author, " +
            "   releasedate, " +
            "   issue, " +
            "   unitprice, " +
            "   copies " +
            ") VALUES (?, ?, ?, ?, ?, ?)"
        );
        
        pstmt.setString(1, product.getTitle());
        pstmt.setString(2, product.getAuthor());
        pstmt.setLong(3, product.getReleaseDate());
        pstmt.setInt(4, product.getIssue());
        pstmt.setDouble(5, product.getUnitPrice());
        pstmt.setInt(6, product.getCopies());
        
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
    
    public static void updateProduct(Product product) throws SQLException {
        Connection conn = DAO.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "UPDATE products SET " +
            "   title = ?, " +
            "   author = ?, " +
            "   releasedate = ?, " +
            "   issue = ?, " +
            "   unitprice = ?, " +
            "   copies = ? " +
            "WHERE id = ?"
        );
                
        pstmt.setString(1, product.getTitle());
        pstmt.setString(2, product.getAuthor());
        pstmt.setLong(3, product.getReleaseDate());
        pstmt.setInt(4, product.getIssue());
        pstmt.setDouble(5, product.getUnitPrice());
        pstmt.setInt(6, product.getCopies());
        pstmt.setLong(7, product.getProductId());
        
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
    
    public static void deleteProduct(Product product) throws SQLException {
        Connection conn = DAO.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM products WHERE id = ?");

        pstmt.setLong(1, product.getProductId());
        
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }
}

