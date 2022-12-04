package vn.edu.stu.doan.model;

public class Category {
    private int cId;
    private String cName;

    public Category() {
        this.cName="";
    }

    public Category(int cid, String cname) {
        this.cId = cid;
        this.cName = cname;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cid) {
        this.cId = cid;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cname) {
        this.cName = cname;
    }

    @Override
    public String toString() {
        return cId + " - " + cName ;
    }
}
