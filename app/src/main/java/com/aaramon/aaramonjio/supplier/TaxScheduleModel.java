package com.aaramon.aaramonjio.supplier;

/**
 * Created by Aaramshop on 5/22/2017.
 */

public class TaxScheduleModel
{
    private int TaxScheduleID;
    private String TaxScheduleName;
    private Double TaxPercentage;
    private int IsActive;
    public static final int ACTIVETAXES = 1;
    public static final int DEACTIVETAXES = 2;
    public static final int ALLTAXES = 3;


    public int getTaxScheduleID() {
        return TaxScheduleID;
    }

    public void setTaxScheduleID(int taxScheduleID) {
        TaxScheduleID = taxScheduleID;
    }

    public String getTaxScheduleName() {
        return TaxScheduleName;
    }

    public void setTaxScheduleName(String taxScheduleName) {
        TaxScheduleName = taxScheduleName;
    }

    public Double getTaxPercentage() {
        return TaxPercentage;
    }

    public int getIsActive() {
        return IsActive;
    }

    public void setIsActive(int isActive) {
        IsActive = isActive;
    }

    public void setTaxPercentage(Double taxPercentage) {
        TaxPercentage = taxPercentage;
    }

    // Business Method
    public String getTaxScheduleDetail()
    {
        return getTaxScheduleName() + " - " + getTaxPercentage() + "%";

    }
}
