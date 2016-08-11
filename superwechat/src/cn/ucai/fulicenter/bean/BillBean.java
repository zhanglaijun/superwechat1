package cn.ucai.fulicenter.bean;

/**
 * Created by Administrator on 2016/8/11.
 */
public class BillBean {
    private String orderName;
    private String orderPhone;
    private String orderProvince;
    private String orderSteet;

    public BillBean(String orderName, String orderPhone, String orderProvince, String orderSteet) {
        this.orderName = orderName;
        this.orderPhone = orderPhone;
        this.orderProvince = orderProvince;
        this.orderSteet = orderSteet;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
    }

    public String getOrderProvince() {
        return orderProvince;
    }

    public void setOrderProvince(String orderProvince) {
        this.orderProvince = orderProvince;
    }

    public String getOrderSteet() {
        return orderSteet;
    }

    public void setOrderSteet(String orderSteet) {
        this.orderSteet = orderSteet;
    }

    @Override
    public String toString() {
        return "BillBean{" +
                "orderName='" + orderName + '\'' +
                ", orderPhone='" + orderPhone + '\'' +
                ", orderProvince='" + orderProvince + '\'' +
                ", orderSteet='" + orderSteet + '\'' +
                '}';
    }
}
