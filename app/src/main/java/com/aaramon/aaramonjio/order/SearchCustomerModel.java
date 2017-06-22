package com.aaramon.aaramonjio.order;

public class SearchCustomerModel { //implements Parcelable{
    private String shopper_id;
    private String shopper_name;
    boolean isChecked;
    private String shopper_mobile;
    private String shopper_address;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    private String pincode;
    public int getState_id() {
        return state_id;
    }

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }

    private int state_id;

    public String getShopper_email() {
        return shopper_email;
    }

    public void setShopper_email(String shopper_email) {
        this.shopper_email = shopper_email;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public String getGSTIN() {
        return GSTIN;
    }

    public void setGSTIN(String GSTIN) {
        this.GSTIN = GSTIN;
    }

    public String getPAN() {
        return PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    private String shopper_email;
    private String BusinessName;
    private String GSTIN;
    private String PAN;


    public String getShopper_id() {
        return shopper_id;
    }

    public void setShopper_id(String shopper_id) {
        this.shopper_id = shopper_id;
    }

    public String getShopper_name() {
        return shopper_name;
    }

    public void setShopper_name(String shopper_name) {
        this.shopper_name = shopper_name;
    }

    public String getShopper_mobile() {
        return shopper_mobile;
    }

    public void setShopper_mobile(String shopper_mobile) {
        this.shopper_mobile = shopper_mobile;
    }

    public String getShopper_address() {
        return shopper_address;
    }

    public void setShopper_address(String shopper_address) {
        this.shopper_address = shopper_address;
    }


    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(shopper_address);
//        dest.writeString(shopper_id);
//        dest.writeString(shopper_mobile);
//        dest.writeString(shopper_name);
//        dest.writeString(name);
//    }
}