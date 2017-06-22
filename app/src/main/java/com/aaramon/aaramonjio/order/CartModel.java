package com.aaramon.aaramonjio.order;

/**
 * Created by DELL STORE on 17-May-17.
 */

class CartModel {
    String cart_name;
    String cart_id;
    String items;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    boolean check;

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String amount;
    String date;

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }


    CartModel(String cart_name, String cart_id, String items,
              String amount,
              String date, boolean check) {
        this.cart_name = cart_name;
        this.cart_id = cart_id;
        this.date = date;
        this.items = items;
        this.amount = amount;
        this.check = check;
    }


    public String getCart_name() {
        return cart_name;
    }

    public void setCart_name(String cart_name) {
        this.cart_name = cart_name;
    }


}
