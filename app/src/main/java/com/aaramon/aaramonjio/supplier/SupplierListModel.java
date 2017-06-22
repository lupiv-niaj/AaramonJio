package com.aaramon.aaramonjio.supplier;

/**
 * Created by dell on 5/16/2017.
 */

public class SupplierListModel {
    public String getSupplierCode() {
        return SupplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        SupplierCode = supplierCode;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public String getSupplierImage() {
        return SupplierImage;
    }

    public void setSupplierImage(String supplierImage) {
        SupplierImage = supplierImage;
    }

    public String getSupplierCompany() {
        return SupplierCompany;
    }

    public void setSupplierCompany(String supplierCompany) {
        SupplierCompany = supplierCompany;
    }

    public String getSupplierMobile() {
        return SupplierMobile;
    }

    public void setSupplierMobile(String supplierMobile) {
        SupplierMobile = supplierMobile;
    }

    public int getSupplierId() {
        return SupplierId;
    }

    public void setSupplierId(int supplierId) {
        SupplierId = supplierId;
    }

    private String SupplierCode,SupplierName,SupplierImage,SupplierCompany,SupplierMobile;
    private int SupplierId;

    public int getGSTREgister() {
        return GSTREgister;
    }

    public void setGSTREgister(int GSTREgister) {
        this.GSTREgister = GSTREgister;
    }

    private int GSTREgister;



    String GSTNNo;
    String GSTCategoryId;
    String StateId;
    String CityId;
    String StateCode;
    String StateName;
    String CityName;



    public String getGSTNNo() {
        return GSTNNo;
    }

    public void setGSTNNo(String GSTNNo) {
        this.GSTNNo = GSTNNo;
    }

    public String getGSTCategoryId() {
        return GSTCategoryId;
    }

    public void setGSTCategoryId(String GSTCategoryId) {
        this.GSTCategoryId = GSTCategoryId;
    }

    public String getStateId() {
        return StateId;
    }

    public void setStateId(String stateId) {
        StateId = stateId;
    }

    public String getCityId() {
        return CityId;
    }

    public void setCityId(String cityId) {
        CityId = cityId;
    }

    public String getStateCode() {
        return StateCode;
    }

    public void setStateCode(String stateCode) {
        StateCode = stateCode;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }


}
