package BRM;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

class BRMFrame {
    Connection con;
    PreparedStatement ps;
    JFrame frame;
    JTabbedPane tabbedPane;

    //Insert Panel
    JPanel insertPanel;
    JLabel bookID;
    JLabel bookTitle;
    JLabel bookPrice;
    JLabel bookWriter;
    JLabel bookPublisher;
    JLabel bookDetails;
    JTextField bookIDTextField;
    JTextField bookTitleTextField;
    JTextField bookPriceTextField;
    JTextField bookWriterTextField;
    JTextField bookPublisherTextField;
    JButton formSubmitButton;
    JLabel insertPanelResultMessageLabel;

    //View Panel
    JPanel viewPanel;
    JButton updateButton;
    JButton deleteButton;
    DefaultTableModel tableModel;
    JTable table;
    JScrollPane scrollPane;
    String[] columnNames = {"Book ID", "Book Title", "Book Price", "Book Writer", "Book Publisher"};
    JLabel updateResultMessageLabel;
    JLabel deleteResultMessageLabel;


    public BRMFrame() {
        getConnectionFromMySQl();
        initComponents();
    }

    public void getConnectionFromMySQl() {
        String url = "jdbc:mysql://localhost:3306/db1";
        String user = "root";
        String password = "******";
        try {
            con = DriverManager.getConnection(url, user, password);
            if (con != null) {
                System.out.println("Connection Established");
            } else System.out.println("Connection not established");
        } catch (SQLException exc) {
            System.out.println(exc.getMessage());
        }
    }

    public void initComponents() {
        //Insert Panel
        bookDetails = new JLabel();
        bookDetails.setText("Enter Book Details to insert");
        bookID = new JLabel();
        bookID.setText("Book ID:");
        bookTitle = new JLabel();
        bookTitle.setText("Book Title:");
        bookPrice = new JLabel();
        bookPrice.setText("Book Price:");
        bookWriter = new JLabel();
        bookWriter.setText("Book Writer:");
        bookPublisher = new JLabel();
        bookPublisher.setText("Book Publisher:");
        formSubmitButton = new JButton();
        bookIDTextField = new JTextField();
        bookTitleTextField = new JTextField();
        bookPriceTextField = new JTextField();
        bookWriterTextField = new JTextField();
        bookPublisherTextField = new JTextField();
        formSubmitButton.setText("Save");
        insertPanelResultMessageLabel = new JLabel();
        insertPanelResultMessageLabel = new JLabel();
        insertPanelResultMessageLabel.setText("Inserted Successfully!");
        insertPanelResultMessageLabel.setFont(new Font("Font.SERIF", Font.ITALIC, 15));
        insertPanelResultMessageLabel.setForeground(Color.green);
        insertPanelResultMessageLabel.setVisible(false);

        bookDetails.setBounds(100, 60, 200, 30);
        bookID.setBounds(100, 120, 150, 30);
        bookIDTextField.setBounds(300, 120, 150, 20);
        bookTitle.setBounds(100, 180, 150, 30);
        bookTitleTextField.setBounds(300, 180, 150, 20);
        bookPrice.setBounds(100, 240, 150, 30);
        bookPriceTextField.setBounds(300, 240, 150, 20);
        bookWriter.setBounds(100, 300, 150, 30);
        bookWriterTextField.setBounds(300, 300, 150, 20);
        bookPublisher.setBounds(100, 360, 150, 30);
        bookPublisherTextField.setBounds(300, 360, 150, 20);
        formSubmitButton.setBounds(200, 420, 100, 30);
        insertPanelResultMessageLabel.setBounds(300, 470, 200, 30);

        insertPanel=new JPanel();
        insertPanel.setLayout(null);
        insertPanel.add(bookDetails);
        insertPanel.add(bookID);
        insertPanel.add(bookIDTextField);
        insertPanel.add(bookTitle);
        insertPanel.add(bookTitleTextField);
        insertPanel.add(bookPrice);
        insertPanel.add(bookPriceTextField);
        insertPanel.add(bookWriter);
        insertPanel.add(bookWriterTextField);
        insertPanel.add(bookPublisher);
        insertPanel.add(bookPublisherTextField);
        insertPanel.add(formSubmitButton);
        insertPanel.add(insertPanelResultMessageLabel);

        //View Panel
        updateButton = new JButton();
        deleteButton = new JButton();
        updateButton.setText("Update");
        deleteButton.setText("Delete");

        tableModel = new DefaultTableModel();
        tableModel.setColumnCount(5);
        tableModel.setColumnIdentifiers(columnNames);
        table=new JTable();
        table.setModel(tableModel);
        ArrayList<BRM.Book> bookList = fetchBookRecords();
        setDataOnTable(bookList);
        scrollPane=new JScrollPane(table);

        updateResultMessageLabel = new JLabel();
        updateResultMessageLabel.setText("Updated Successfully!");
        updateResultMessageLabel.setFont(new Font("Font.SERIF", Font.ITALIC, 15));
        updateResultMessageLabel.setForeground(Color.green);
        updateResultMessageLabel.setVisible(false);
        deleteResultMessageLabel = new JLabel();
        deleteResultMessageLabel.setText("Deleted Successfully!");
        deleteResultMessageLabel.setFont(new Font("Font.SERIF", Font.ITALIC, 15));
        deleteResultMessageLabel.setForeground(Color.green);
        deleteResultMessageLabel.setVisible(false);

        viewPanel=new JPanel();
        viewPanel.add(updateButton);
        viewPanel.add(deleteButton);
        viewPanel.add(scrollPane);
        viewPanel.add(updateResultMessageLabel);
        viewPanel.add(deleteResultMessageLabel);

        tabbedPane=new JTabbedPane();
        tabbedPane.add(insertPanel);
        tabbedPane.add(viewPanel);
        frame = new JFrame();
        frame.add(tabbedPane);
        frame.setSize(600, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Book Record Management");


        formSubmitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Insertion into database table
                try {
                    String sqlStatement = "Insert into book (bookid,title,price,author,publisher) values (?,?,?,?,?)";
                    ps = con.prepareStatement(sqlStatement);
                    ps.setInt(1, Integer.parseInt(bookIDTextField.getText()));
                    ps.setString(2, bookTitleTextField.getText());
                    ps.setDouble(3, Double.parseDouble(bookPriceTextField.getText()));
                    ps.setString(4, bookWriterTextField.getText());
                    ps.setString(5, bookPublisherTextField.getText());
                    ps.execute();
                    updateJTable();
                    bookIDTextField.setText("");
                    bookTitleTextField.setText("");
                    bookPriceTextField.setText("");
                    bookWriterTextField.setText("");
                    bookPublisherTextField.setText("");
                    insertPanelResultMessageLabel.setVisible(true);
                    System.out.println("Inserted");
                } catch (SQLException exc) {
                    System.out.println(exc.getMessage());
                }
            }
        });

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int index=tabbedPane.getSelectedIndex();
                if (index==0){
                    updateResultMessageLabel.setVisible(false);
                    deleteResultMessageLabel.setVisible(false);
                }
                if(index==1){
                    insertPanelResultMessageLabel.setVisible(false);
                }
            }
        });



        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Book> updatedBookList=getJTableData();
                String sqlString="update book set title=?, price=?, author=?, publisher=? where bookid=?";
                try{
                    ps=con.prepareStatement(sqlString);
                    for(int i=0;i<updatedBookList.size();i++){
                        ps.setString(1,updatedBookList.get(i).getBookTitle());
                        ps.setDouble(2,updatedBookList.get(i).getBookPrice());
                        ps.setString(3,updatedBookList.get(i).getBookWriter());
                        ps.setString(4,updatedBookList.get(i).getBookPublisher());
                        ps.setInt(5,updatedBookList.get(i).getBookID());
                        ps.execute();
                    }
                    updateResultMessageLabel.setVisible(true);
                    deleteResultMessageLabel.setVisible(false);
                    System.out.println("Updated");
                } catch (SQLException ex) {
                    System.out.println("Exception: "+ex.getMessage());
                }

            }
        });


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowNo = table.getSelectedRow();
                if (rowNo != -1) {
                    String sqlStatement = "DELETE FROM book WHERE bookid = ?";
                    try (PreparedStatement ps = con.prepareStatement(sqlStatement)) {
                        ps.setInt(1, Integer.parseInt(table.getValueAt(rowNo, 0).toString()));
                        ps.execute();
                        updateJTable();
                        deleteResultMessageLabel.setVisible(true);
                        updateResultMessageLabel.setVisible(false);
                        System.out.println("Deleted");
                    } catch (SQLException ex) {
                        System.out.println("Exception: " + ex.getMessage());
                    }
                } else {
                    System.out.println("No row selected.");
                }
            }
        });
    }

    ArrayList<BRM.Book> fetchBookRecords() {
        ArrayList<BRM.Book> bookList = new ArrayList<>();
        String sqlStatement = "select * from book";
        try {
            ps = con.prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BRM.Book book;
                book = new BRM.Book();
                book.setBookID(rs.getInt(1));
                book.setBookTitle(rs.getString(2));
                book.setBookPrice(rs.getDouble(3));
                book.setBookWriter(rs.getString(4));
                book.setBookPublisher(rs.getString(5));
                bookList.add(book);
            }
        } catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            return bookList;
        }
    }

    public void setDataOnTable(ArrayList<BRM.Book> bookList) {
        Object[][] obj = new Object[bookList.size()][5];
        for (int i = 0; i < bookList.size(); i++) {
            obj[i][0] = bookList.get(i).getBookID();
            obj[i][1] = bookList.get(i).getBookTitle();
            obj[i][2] = bookList.get(i).getBookPrice();
            obj[i][3] = bookList.get(i).getBookWriter();
            obj[i][4] = bookList.get(i).getBookPublisher();
        }
        tableModel.setRowCount(bookList.size());
        for (int i = 0; i < bookList.size(); i++) {
            tableModel.setValueAt(obj[i][0], i, 0);
            tableModel.setValueAt(obj[i][1], i, 1);
            tableModel.setValueAt(obj[i][2], i, 2);
            tableModel.setValueAt(obj[i][3], i, 3);
            tableModel.setValueAt(obj[i][4], i, 4);
        }
    }
    public void updateJTable(){
        ArrayList<BRM.Book> bookList=fetchBookRecords();
        setDataOnTable(bookList);
    }

    public ArrayList<Book> getJTableData(){
        ArrayList<Book> updatedBookList=new ArrayList<Book>();
        for (int i=0;i<table.getRowCount();i++){
            Book book=new Book();
            book.setBookID(Integer.parseInt(table.getValueAt(i,0).toString()));
            book.setBookTitle(table.getValueAt(i,1).toString());
            book.setBookPrice(Double.parseDouble(table.getValueAt(i,2).toString()));
            book.setBookWriter(table.getValueAt(i,3).toString());
            book.setBookPublisher(table.getValueAt(i,4).toString());
            updatedBookList.add(book);
        }
        return updatedBookList;
    }
}

