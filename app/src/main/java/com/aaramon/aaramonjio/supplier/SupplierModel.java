package com.aaramon.aaramonjio.supplier;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Aaramshop on 5/17/2017.
 */

public class SupplierModel
{
    int SupplierId;
    String SupplierCode;
    String SuppplierName;
    String SupplierAddress;
    String SupplierMobile;
    Double SupplierOutstanding;

    ArrayList<SupplierBillModel> SupplierBillList = new ArrayList<SupplierBillModel>();
    Context context;

    public int getSupplierId() {
        return SupplierId;
    }

    public void setSupplierId(int supplierId) {
        SupplierId = supplierId;
    }

    public String getSuppplierName() {
        return SuppplierName;
    }

    public void setSuppplierName(String suppplierName) {
        SuppplierName = suppplierName;
    }

    public String getSupplierAddress() {
        return SupplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        SupplierAddress = supplierAddress;
    }

    public String getSupplierMobile() {
        return SupplierMobile;
    }

    public void setSupplierMobile(String supplierMobile) {
        SupplierMobile = supplierMobile;
    }

    public Double getSupplierOutstanding() {
        return SupplierOutstanding;
    }

    public void setSupplierOutstanding(Double supplierOutstanding) {
        SupplierOutstanding = supplierOutstanding;
    }

    public ArrayList<SupplierBillModel> getSupplierBillList() {
        return SupplierBillList;
    }

    public void setSupplierBillList(ArrayList<SupplierBillModel> supplierBillList) {
        SupplierBillList = supplierBillList;
    }

    public String getSupplierCode() {
        return SupplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        SupplierCode = supplierCode;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }




}
