/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hotel.ui;

import hotel.classes.Order;
import hotel.databaseOperation.BookingDb; // Ensure this import is correct and the class exists in the specified package
import hotel.databaseOperation.FoodDb;
import hotel.databaseoperation.ItemDb;
import hotel.databaseoperation.FoodDb;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
import net.proteanit.sql.DbUtils; // Ensure rs2xml.jar is added to the project's classpath
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;


/**
 *
 * @author Faysal
 */
public class OrderPanel extends javax.swing.JDialog  {

    /**
     * Creates new form OrderPanel
     */
    
    ArrayList<String> bookingList = new ArrayList<>();
     BookingDb  db = new BookingDb(); 
    transient ResultSet result;
    FoodDb foodDb = new FoodDb();
    ItemDb itemDb = new ItemDb();
    public OrderPanel(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.getContentPane().setBackground(new Color(241,241,242));
        searchHelper();
        populateFoodTable();
        populateItemTable();
        AutoCompleteDecorator.decorate(comboBooking);
    }
    
    public void searchHelper()
    {
         final DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(new Vector<>(bookingList));
        comboBooking.setModel((DefaultComboBoxModel<String>) model);
        
        
        JTextComponent editor = (JTextComponent) comboBooking.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent evt)
            {
               
                if(evt.getKeyChar() == KeyEvent.VK_ENTER)
                {
                    String details = (String) comboBooking.getSelectedItem();
                   
                    if(!details.contains(","))
                    {
                        JOptionPane.showMessageDialog(null, "no booking found, try adding a new booking");
                    }
                    else
                    {
                        int bookinId = Integer.parseInt(details.substring(details.lastIndexOf(",")+1));
                        tfBookingId.setText(bookinId+"");
                      
                        
                    }
                    
                }
                
                
              
                 String value = "";
                try {
                    value = comboBooking.getEditor().getItem().toString();
                    

                } catch (Exception ex) {
                }
                if (value.length() >= 2) {

                
                    bookingComboFill(db.bookingsReadyForOrder(value));
                    db.flushAll();
                }

            }
        });
    }
    
    public void bookingComboFill(ResultSet result)
    {
        bookingList.clear();
        try {
            
            while (result.next()) {
                bookingList.add(result.getString("booking_room") + ", " + result.getString("name") + "," + result.getString("booking_id"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "bookingCombo fill error");
        }

    }
    
     private void populateFoodTable() {
        result = foodDb.getFoods();
        tableFood.setModel(DbUtils.resultSetToTableModel(result));
        foodDb.flushAll();
    }
     
     private void populateItemTable()
     {
        result = itemDb.getItems();
        tableItem.setModel(DbUtils.resultSetToTableModel(result));
        itemDb.flushAll();
     }


    @SuppressWarnings("unchecked")
   
    private void initComponents() {

        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        tableFood = new javax.swing.JTable();
        javax.swing.JScrollPane jScrollPane2 = new javax.swing.JScrollPane();
        tableItem = new javax.swing.JTable();
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        tfFoodItem = new javax.swing.JTextField();
        tfQuantity = new javax.swing.JTextField();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        tfPrice = new javax.swing.JTextField();
        tfTotal = new javax.swing.JTextField();
        javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        tfBookingId = new javax.swing.JTextField();
        javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        comboBooking = new javax.swing.JComboBox<>();
        javax.swing.JButton jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tableFood.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableFood.addMouseListener(new java.awt.event.MouseAdapter() {
                   
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        tableFoodMouseClicked();
                    }
                });
        jScrollPane1.setViewportView(tableFood);

        tableItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableItemMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableItem);

        jPanel1.setBackground(new java.awt.Color(230, 231, 232));

        tfQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfQuantityKeyReleased(evt);
            }
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tf_quantityKeyTyped(evt);
            }
        });

        jLabel1.setText("Item/Food");

        jLabel2.setText("Quantity");

        tfPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tf_priceKeyTyped(evt);
            }
        });

        jLabel3.setText("Total");

        tfBookingId.setBackground(new java.awt.Color(204, 255, 0));

        jLabel4.setText("Price");

        comboBooking.setEditable(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SaveButton.png"))); 
        jButton1.addActionListener(this::jButton1ActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(comboBooking, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(tfBookingId)
                                .addComponent(tfFoodItem)
                                .addComponent(tfQuantity)
                                .addComponent(tfTotal)
                                .addComponent(tfPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(114, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(comboBooking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tfBookingId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfFoodItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tfPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tfTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(91, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(71, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(66, Short.MAX_VALUE))
        );

        pack();
    }

    private void tableFoodMouseClicked() {
        int row = tableFood.getSelectedRow();
        displayToTextField(row);
    }

    private void tfQuantityKeyReleased(java.awt.event.KeyEvent evt) {
       int price = Integer.parseInt(tfPrice.getText());
        try{
           int quantity = Integer.parseInt(tfQuantity.getText());
            tfTotal.setText(quantity*price+"");
       } catch (NumberFormatException | NullPointerException ex) {
           
       }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        db.insertOrder(new Order(
                Integer.parseInt(tfBookingId.getText()),
                tfFoodItem.getText(),
                Integer.parseInt(tfPrice.getText()),
                Integer.parseInt(tfQuantity.getText()),
                Integer.parseInt(tfTotal.getText())
                
        ));
    }

    private void tableItemMouseClicked(java.awt.event.MouseEvent evt) {
        int row = tableItem.getSelectedRow();
        displayToTextFieldFromItem(row);
    }

    private void tf_priceKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        
        if(!(Character.isDigit(c) || c== KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE ))
        {
            evt.consume();
        }
    }

    private void tf_quantityKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        
        if(!(Character.isDigit(c) || c== KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE ))
        {
            evt.consume();
        }
    }
    
     private void displayToTextField(int row) {
        tfFoodItem.setText(tableFood.getModel().getValueAt(row, 1)+"");
        tfPrice.setText(tableFood.getModel().getValueAt(row, 2)+"");
       
    }
     private void displayToTextFieldFromItem(int row) {
        tfFoodItem.setText(tableItem.getModel().getValueAt(row, 1)+"");
        tfPrice.setText(tableItem.getModel().getValueAt(row, 2)+"");
       
    }
  
    public static void main(String[] args) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OrderPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
       

        java.awt.EventQueue.invokeLater(() -> {
            OrderPanel dialog = new OrderPanel(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    private javax.swing.JComboBox<String> comboBooking;
    
    private javax.swing.JTable tableFood;
    private javax.swing.JTable tableItem;
    private javax.swing.JTextField tfBookingId;
    private javax.swing.JTextField tfFoodItem;
    private javax.swing.JTextField tfPrice;
    private javax.swing.JTextField tfQuantity;
    private javax.swing.JTextField tfTotal;
    

   
}
