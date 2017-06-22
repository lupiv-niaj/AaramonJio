package com.aaramon.aaramonjio.supplier;

/**
 * Created by Aaramshop on 5/16/2017.
 */

public class SupplierBillModel
{
    private int PurchaseEntryId;
    private String Month;
    private int Day;
    private String BillNo;
    private String PurchaseType;
    private long ItemsCount;
    private Double BillAmount;

    public SupplierBillModel(){}

    public SupplierBillModel(String month, int day, String billNo, String purchaseType, long itemsCount, Double billAmount) {
        Month = month;
        Day = day;
        BillNo = billNo;
        PurchaseType = purchaseType;
        ItemsCount = itemsCount;
        BillAmount = billAmount;
    }

    public String getMonth() {
        return Month;
    }

    public int getDay() {
        return Day;
    }

    public String getBillNo() {
        return BillNo;
    }

    public String getPurchaseType() {
        return PurchaseType;
    }

    public long getItemsCount() {
        return ItemsCount;
    }

    public Double getBillAmount() {
        return BillAmount;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public void setDay(int day) {
        Day = day;
    }

    public void setBillNo(String billNo) {
        BillNo = billNo;
    }

    public void setPurchaseType(String purchaseType) {
        PurchaseType = purchaseType;
    }

    public void setItemsCount(long itemsCount) {
        ItemsCount = itemsCount;
    }

    public void setBillAmount(Double billAmount) {
        BillAmount = billAmount;
    }

    public int getPurchaseEntryId() {
        return PurchaseEntryId;
    }

    public void setPurchaseEntryId(int purchaseEntryId) {
        PurchaseEntryId = purchaseEntryId;
    }
}