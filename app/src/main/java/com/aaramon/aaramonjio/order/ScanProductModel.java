package com.aaramon.aaramonjio.order;

/**
 * Created by DELL STORE on 17-May-17.
 */

class ScanProductModel {
    String product_name;
    double product_price;
    double offer_price;
    String product_image;
    int qty;
    String product_id;
    double sgst;
    double cgst;
    double igst;

    String batch_no;

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    String batch_id;
    public double getSgst() {
        return sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public double getCgst() {
        return cgst;
    }

    public void setCgst(double cgst) {
        this.cgst = cgst;
    }

    public double getIgst() {
        return igst;
    }

    public void setIgst(double igst) {
        this.igst = igst;
    }

    public double getCess() {
        return cess;
    }

    public void setCess(double cess) {
        this.cess = cess;
    }

    double cess;


    public double getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(double tax_amount) {
        this.tax_amount = tax_amount;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    double tax_amount;
    double total_amount;

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    String cart_id;

    public String getCart_item_id() {
        return cart_item_id;
    }

    public void setCart_item_id(String cart_item_id) {
        this.cart_item_id = cart_item_id;
    }

    String cart_item_id;


    ScanProductModel(String product_name, double product_price, double offer_price, String product_image, int qty, String product_id, String cart_id, String cart_item_id, double total_amount, double tax_amount, double cgst, double igst, double sgst, double cess,String batch_id,String batch_no) {
        this.cart_item_id = cart_item_id;
        this.product_image = product_image;
        this.product_name = product_name;
        this.offer_price = offer_price;
        this.product_price = product_price;
        this.qty = qty;
        this.product_id = product_id;
        this.cart_id = cart_id;
        this.total_amount = total_amount;
        this.tax_amount = tax_amount;
        this.igst = igst;
        this.sgst = sgst;
        this.cgst = cgst;
        this.cess = cess;
        this.batch_id=batch_id;
        this.batch_no=batch_no;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }


    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public double getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(double offer_price) {
        this.offer_price = offer_price;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }


}
