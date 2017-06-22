package com.aaramon.aaramonjio.supplier;

import java.util.ArrayList;

/**
 * Created by Aaramshop on 5/20/2017.
 */

public class PurchaseEntryModel
{
    private Long PurchaseEntryId;
    private String BillNumber;
    private String BillDate;
    private Long MerchantStoreId;
    private String PaymentMode;
    private Byte[] BillImage;
    private String BillImageName;

    public String getBillImageName() {
        return BillImageName;
    }

    public void setBillImageName(String billImageName) {
        BillImageName = billImageName;
    }

    private String PurchaseDate;
    private Double TotalAmount;
    private Double Tax;
    private Double AmountPaid;
    private int Status;
    private String PaymentTerm;

    private PurchaseEntryDetailModel PurchaseEntryDetailModel = new PurchaseEntryDetailModel();

    private SupplierModel SupplierModel = new SupplierModel();
    private ArrayList<PurchaseEntryDetailModel> ProductEntryDetailList = new ArrayList<PurchaseEntryDetailModel>();

    public Long getPurchaseEntryId() {
        return PurchaseEntryId;
    }

    public void setPurchaseEntryId(Long purchaseEntryId) {
        PurchaseEntryId = purchaseEntryId;
    }

    public String getBillNumber() {
        return BillNumber;
    }

    public void setBillNumber(String billNumber) {
        BillNumber = billNumber;
    }

    public Long getMerchantStoreId() {
        return MerchantStoreId;
    }

    public void setMerchantStoreId(Long merchantStoreId) {
        MerchantStoreId = merchantStoreId;
    }

    public void setBillDate(String billDate) {
        BillDate = billDate;
    }

    public String getPurchaseDate() {
        return PurchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        PurchaseDate = purchaseDate;
    }

    public Double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        TotalAmount = totalAmount;
    }

    public Double getTax() {
        return Tax;
    }

    public void setTax(Double tax) {
        Tax = tax;
    }

    public String getPaymentTerm() {
        return PaymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        PaymentTerm = paymentTerm;
    }

    public Double getAmountPaid() {
        return AmountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        AmountPaid = amountPaid;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public SupplierModel getSupplierModel() {
        return SupplierModel;
    }

    public void setSupplierModel(SupplierModel supplierModel) {
        SupplierModel = supplierModel;   }

    public String getBillDate() {
        return BillDate;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public Byte[] getBillImage() {
        return BillImage;
    }

    public PurchaseEntryDetailModel getPurchaseEntryDetailModel() {
        return PurchaseEntryDetailModel;
    }

    public void setPurchaseEntryDetailModel(PurchaseEntryDetailModel purchaseEntryDetailModel) {
        PurchaseEntryDetailModel = purchaseEntryDetailModel;
    }

    public ArrayList<PurchaseEntryDetailModel> getProductEntryDetailList() {
        return ProductEntryDetailList;
    }

    public void setProductEntryDetailList(ArrayList<PurchaseEntryDetailModel> productEntryDetailList) {
        ProductEntryDetailList = productEntryDetailList;
    }

    public void setBillImage(Byte[] billImage) {
        BillImage = billImage;
    }


}
