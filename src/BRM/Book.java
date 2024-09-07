package BRM;

public class Book {
    private int bookID;
    private String bookTitle;
    private double bookPrice;
    private String bookWriter;
    private String bookPublisher;

    public int getBookID(){
        return bookID;
    }

    public String getBookTitle(){
        return bookTitle;
    }

    public double getBookPrice(){
        return bookPrice;
    }

    public String getBookWriter(){
        return bookWriter;
    }

    public String getBookPublisher(){
        return bookPublisher;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setBookPrice(double bookPrice) {
        this.bookPrice = bookPrice;
    }

    public void setBookWriter(String bookWriter) {
        this.bookWriter = bookWriter;
    }

    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }
}
