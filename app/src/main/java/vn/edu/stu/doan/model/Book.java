package vn.edu.stu.doan.model;

public class Book {

    private  int bID;
    private  String bName;
    private  Category bCategory;
    private  String bImage;
    private  String bAuthor;
    private  int bPrice;

    public Book() {
        this.bName = "";
        this.bCategory = null;
        this.bImage = "";
        this.bAuthor = " ";
        this.bPrice = 0;
    }

    public Book(int id,String name, Category category,String image) {
        this.bID=id;
        this.bName=name;
        this.bCategory=category;
        this.bImage=image;
    }

    public Book(int bid, String bname, Category bcategory, String bimage, String bauthor, int bprice) {
        this.bID = bid;
        this.bName = bname;
        this.bCategory = bcategory;
        this.bImage = bimage;
        this.bAuthor = bauthor;
        this.bPrice = bprice;
    }

    public int getbID() { return bID; }

    public void setbID(int bID) { this.bID = bID; }

    public String getbName() { return bName; }

    public void setbName(String bName) { this.bName = bName; }

    public Category  getbCategory() { return bCategory; }

    public void setbCategory(Category bCategory) { this.bCategory = bCategory; }

    public String getbImage() { return bImage; }

    public void setbImage(String bImage) { this.bImage = bImage; }

    public String getbAuthor() { return bAuthor; }

    public void setbAuthor(String bAuthor) { this.bAuthor = bAuthor; }

    public int getbPrice() { return bPrice; }

    public void setbPrice(int bPrice) { this.bPrice = bPrice; }

    @Override
    public String toString() { return bID + " - " + bName + " - " + bCategory;}
}
