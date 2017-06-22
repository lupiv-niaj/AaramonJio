package com.aaramon.aaramonjio.order;

/**
 * Created by DELL STORE on 17-May-17.
 */

public class PaymentModel {
    String name;
    int id;
    private Boolean isselected;
    private double pay_amount;
    private String image;
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public PaymentModel(String name, int id, Boolean isselected,
                        double pay_amount, String image) {
        this.name = name;
        this.id = id;
        this.isselected = isselected;
        this.pay_amount = pay_amount;
        this.image=image;
    }

    public double get_payamt() {
        return this.pay_amount;
    }

    public void set_payamt(double pay_amount) {
        this.pay_amount = pay_amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void set_id(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int get_id() {
        return this.id;
    }

    public Boolean get_selected() {
        return isselected;
    }

    public void set_selected(Boolean isselected) {
        this.isselected = isselected;
    }

}
