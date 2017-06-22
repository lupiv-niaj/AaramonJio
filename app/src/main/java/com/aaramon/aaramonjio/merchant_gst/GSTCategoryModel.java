package com.aaramon.aaramonjio.merchant_gst;

/**
 * Created by DELL STORE on 17-May-17.
 */

public class GSTCategoryModel {
    int GSTDealerCategoryId;
    String GSTDealerCategoryName;

    public boolean getselected() {
        return isselected;
    }

    public void setIsselected(boolean isselected) {
        this.isselected = isselected;
    }

    boolean isselected;
    public int getGSTDealerCategoryId() {
        return GSTDealerCategoryId;
    }

    public void setGSTDealerCategoryId(int GSTDealerCategoryId) {
        this.GSTDealerCategoryId = GSTDealerCategoryId;
    }

    public String getGSTDealerCategoryName() {
        return GSTDealerCategoryName;
    }

    public void setGSTDealerCategoryName(String GSTDealerCategoryName) {
        this.GSTDealerCategoryName = GSTDealerCategoryName;
    }


}
