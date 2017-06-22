package com.aaramon.aaramonjio.supplier;

import java.util.ArrayList;

/**
 * Created by Aaramshop on 5/20/2017.
 */

public class PurchaseEntryDetailModel
{
    private long PurchaseEntryDetailId;
    private long PurchaseId;
    private Double PurchaseRate;
    private Double NewPurchaseRate;
    private Double MRP;
    private Double SP;
    private int Qty;
    private Double TaxPercentage;
    private int Status;
    private double sgst;
    private double cgst;
    private double igst;
    private double cess;
    private double total_amt;

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    private String BatchNo;


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

    public double getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(double total_amt) {
        this.total_amt = total_amt;
    }

    public double getTax_amt() {
        return tax_amt;
    }

    public void setTax_amt(double tax_amt) {
        this.tax_amt = tax_amt;
    }

    private double tax_amt;
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


    public Double getNewPurchaseRate() {
        return NewPurchaseRate;
    }

    public void setNewPurchaseRate(Double NewpurchaseRate) {
        NewPurchaseRate = NewpurchaseRate;
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
