package com.aaramon.aaramonjio.supplier;

/**
 * Created by Aaramshop on 5/20/2017.
 */

public class ProductModel
{
    private long ProductId;
    private String ProductName;
    private String ImageUrl;
    private long ServerProductId=0;
    private TaxScheduleModel TaxScheduleModel = new TaxScheduleModel();

    public long getProductId() {
        return ProductId;
    }

    public void setProductId(long productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public long getServerProductId() {
        return ServerProductId;
    }

    public void setServerProductId(long serverProductId) {
        ServerProductId = serverProductId;
    }

    public TaxScheduleModel getTaxScheduleModel() {
        return TaxScheduleModel;
    }

    public void setTaxScheduleModel(TaxScheduleModel taxScheduleModel) {
        TaxScheduleModel = taxScheduleModel;
    }
}
