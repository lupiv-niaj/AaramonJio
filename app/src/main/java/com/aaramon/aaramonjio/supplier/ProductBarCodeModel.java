package com.aaramon.aaramonjio.supplier;

/**
 * Created by Aaramshop on 5/25/2017.
 */

public class ProductBarCodeModel
{
    private long ProductBarCodeId;
    private String EanCode;

    public long getProductBarCodeId() {
        return ProductBarCodeId;
    }

    public void setProductBarCodeId(long productBarCodeId) {
        ProductBarCodeId = productBarCodeId;
    }

    public String getEanCode() {
        return EanCode;
    }

    public void setEanCode(String eanCode) {
        EanCode = eanCode;
    }
}
