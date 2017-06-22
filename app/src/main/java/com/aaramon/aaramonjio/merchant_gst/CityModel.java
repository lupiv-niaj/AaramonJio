package com.aaramon.aaramonjio.merchant_gst;

/**
 * Created by DELL STORE on 17-May-17.
 */

public class CityModel {
    int cityid;
    String cityName;

    public int getCityid() {
        return cityid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public boolean isSelected() {
        return IsSelected;
    }

    public void setSelected(boolean selected) {
        IsSelected = selected;
    }

    boolean IsSelected;
}
