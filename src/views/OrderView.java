
package views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import GUI.MidtownComics;
import controller.ViewManager;
import dao.OrderDAO;
import model.Order;
import views.CustomerForm;

@SuppressWarnings("serial")
public class OrderView extends JPanel implements ActionListener {
    
    private ViewManager manager;
    private JScrollPane scroll;
    private OrderForm orderForm;
    private Order order;
    private JButton cancel;
    private JButton remove;
    private JButton save;
    
    /**
     * Creates an instance of the InventoryView class.
     * 
     * @param manager the controller
     */
    
    public OrderView(ViewManager manager) {
        super(new BorderLayout());
        
        this.manager = manager;
        this.orderForm = new OrderForm();
        this.init();
    }
    
    public void setOrder(Order order) {
    	this.order = order;
    	
    	remove.setEnabled(true);
    	orderForm.updateFields(order);
    }
    
    /**
     * Refreshes the inventory list.
     */
    
    public void refreshOrderView() {
        this.remove(scroll);
        
        initOrderForm();
    }
    

    
    private void init() {        
        initHeader();
        initOrderForm();
        initFooter();
    }
    

    private void initHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel label = new JLabel("Midtown Comics");
        label.setFont(new Font("DialogInput", Font.BOLD, 21));
        label.setBorder(new EmptyBorder(15, 15, 10, 0));
                
        panel.add(label, BorderLayout.WEST);
        this.add(panel, BorderLayout.NORTH);
    }
    

    
    	private void initOrderForm() {
            this.add(new JScrollPane(orderForm), BorderLayout.CENTER);
    }
    

    private void initFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 15, 15, 15));
        cancel = new JButton("cancel");
        cancel.putClientProperty("id", -1L);
        cancel.addActionListener(this);
        
        remove = new JButton("remove");
        remove.putClientProperty("id", -1L);
        remove.addActionListener(this);
        
        save = new JButton("save");
        save.putClientProperty("id",  -1L);
        save.addActionListener(this);
        
        panel.add(cancel, BorderLayout.WEST);
        panel.add(remove, BorderLayout.CENTER);
        panel.add(save, BorderLayout.EAST);
        this.add(panel, BorderLayout.SOUTH);
    }
    
  
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        
        if (source.equals(cancel)) {
        	manager.detachOrder();
            manager.switchTo(MidtownComics.OrderListView);
        } else if (source.equals(remove)) {
        	manager.removeOrderFromOrderList(order);
        	manager.switchTo(MidtownComics.OrderListView);
        } else if (source.equals(save)) {
        	try {
				manager.modifyOrderInOrderList(orderForm.getOrderFromFields());
			} catch (NumberFormatException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }
}
