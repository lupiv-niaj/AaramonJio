package com.aaramon.aaramonjio.supplier;

import java.util.ArrayList;

/**
 * Created by Aaramshop on 5/20/2017.
 */

public class PurchaseEntryBillDetailModel
{
    private long PurchaseEntryDetailId;
    private long PurchaseId;
    private Double PurchaseRate;
    private Double MRP;
    private Double SP;
    private int Qty;
    private Double TaxPercentage;
    private int Status;
    String TotalItems;
    String OrderAmount;

    public String getTotalItems() {
        return TotalItems;
    }

    public void setTotalItems(String totalItems) {
        TotalItems = totalItems;
    }

    public String getOrderAmount() {
        return OrderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        OrderAmount = orderAmount;
    }

    public String getBillDate() {
        return BillDate;
    }

    public void setBillDate(String billDate) {
        BillDate = billDate;
    }

    public String getBillNo() {
        return BillNo;
    }

    public void setBillNo(String billNo) {
        BillNo = billNo;
    }

    public String getPurchaseType() {
        return PurchaseType;
    }

    public void setPurchaseType(String purchaseType) {
        PurchaseType = purchaseType;
    }

    String BillDate;
    String BillNo;
    String PurchaseType;

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    private String ProductImage;
    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    private String ProductName;
    private ProductBatchModel ProductBatchModel = new ProductBatchModel();

    private ArrayList<PurchaseEntryFreeProductModel> FreeProductList = new ArrayList<PurchaseEntryFreeProductModel>();

    public long getPurchaseEntryDetailId() {
        return PurchaseEntryDetailId;
    }

    public void setPurchaseEntryDetailId(long purchaseEntryDetailId) {
        PurchaseEntryDetailId = purchaseEntryDetailId;
    }

    public long getPurchaseId() {
        return PurchaseId;
    }

    public void setPurchaseId(long purchaseId) {
        PurchaseId = purchaseId;
    }


    public Double getPurchaseRate() {
        return PurchaseRate;
    }

    public void setPurchaseRate(Double purchaseRate) {
        PurchaseRate = purchaseRate;
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

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public Double getTaxPercentage() {
        return TaxPercentage;
    }

    public void setTaxPercentage(Double taxPercentage) {
        TaxPercentage = taxPercentage;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public ArrayList<PurchaseEntryFreeProductModel> getFreeProductList() {
        return FreeProductList;
    }

    public void setFreeProductList(ArrayList<PurchaseEntryFreeProductModel> freeProductList) {
        FreeProductList = freeProductList;
    }

    public ProductBatchModel getProductBatchModel() {
        return ProductBatchModel;
    }

    public void setProductBatchModel(ProductBatchModel productBatchModel) {
        ProductBatchModel = productBatchModel;
    }
}
