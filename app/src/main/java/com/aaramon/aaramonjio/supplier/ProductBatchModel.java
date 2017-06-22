package com.aaramon.aaramonjio.supplier;

/**
 * Created by Aaramshop on 5/20/2017.
 */

public class ProductBatchModel
{
    private long ProductBatchId;
    private String BatchNo;
    private String Expiry;
    private Double Stock;
    private Double PurchasePrice;
    private Double MRP;
    private Double SP;
    private int Status;
    private ProductBarCodeModel ProductBarCodeModel = new ProductBarCodeModel();
    private ProductModel ProductModel = new ProductModel();

    public static final int ADDBATCH = 1;
    public static final int SUBTRACTBATCH = 2;
    public static final int UPDATEBATCH = 3;


    public long getProductBatchId() {
        return ProductBatchId;
    }

    public void setProductBatchId(long productBatchId) {
        ProductBatchId = productBatchId;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    public String getExpiry() {
        return Expiry;
    }

    public void setExpiry(String expiry) {
        Expiry = expiry;
    }

    public ProductBarCodeModel getProductBarCodeModel() {
        return ProductBarCodeModel;
    }

    public void setProductBarCodeModel(ProductBarCodeModel productBarCodeModel) {
        ProductBarCodeModel = productBarCodeModel;
    }

    public Double getStock() {
        return Stock;
    }

    public void setStock(Double stock) {
        Stock = stock;
    }

    public Double getPurchasePrice() {
        return PurchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        PurchasePrice = purchasePrice;
    }

    public Double getMRP() {
        return MRP;
    }

    public void setMRP(Double MRP) {
        this.MRP = MRP;
    }

    public Double getSP() {
        return SP;
    }

    public void setSP(Double SP) {
        this.SP = SP;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public ProductModel getProductModel() {
        return ProductModel;
    }

    public void setProductModel(ProductModel productModel) {
        ProductModel = productModel;
    }
}
