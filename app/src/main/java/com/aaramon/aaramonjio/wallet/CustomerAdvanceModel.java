package com.aaramon.aaramonjio.wallet;

public class CustomerAdvanceModel {

    private String shopper_id;
    private String shopper_name;
    private String shopper_image;
    private String order_id;
    private String order_date;

    private String outstanding_amount;
    private int type;


    public String get_shopper_name() {
        return shopper_name;
    }

    public void set_shopper_name(String shopper_name) {
        this.shopper_name = shopper_name;
    }

    public String get_shopper_id() {
        return shopper_id;
    }

    public void set_shopper_id(String shopper_id) {
        this.shopper_id = shopper_id;
    }

    public String get_shopper_image() {
        return shopper_image;
    }

    public void set_shopper_image(String shopper_image) {
        this.shopper_image = shopper_image;
    }

    public String get_order_date() {
        return order_date;
    }

    public void set_order_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String get_outstanding_amount() {
        return outstanding_amount;
    }

    public void set_outstanding_amount(String outstanding_amount) {
        this.outstanding_amount = outstanding_amount;
    }

    public int get_type() {
        return type;
    }

    public void set_type(int type) {
        this.type = type;
    }


}
