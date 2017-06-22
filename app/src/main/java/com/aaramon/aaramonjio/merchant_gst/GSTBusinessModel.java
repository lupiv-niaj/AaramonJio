package com.aaramon.aaramonjio.merchant_gst;

/**
 * Created by DELL STORE on 17-May-17.
 */

class GSTBusinessModel {
    int GSTBusinessCategoryId;
    String GSTBusinessCategoryName;

    public boolean getselected() {
        return isselected;
    }

    public void setIsselected(boolean isselected) {
        this.isselected = isselected;
    }

    boolean isselected;
    public int getGSTBusinessCategoryId() {
        return GSTBusinessCategoryId;
    }

    public void setGSTBusinessCategoryId(int GSTBusinessCategoryId) {
        this.GSTBusinessCategoryId = GSTBusinessCategoryId;
    }

    public String getGSTBusinessCategoryName() {
        return GSTBusinessCategoryName;
    }

    public void setGSTBusinessCategoryName(String GSTBusinessCategoryName) {
        this.GSTBusinessCategoryName = GSTBusinessCategoryName;
    }
}
