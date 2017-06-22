package com.aaramon.aaramonjio.supplier;

public class CityListModel {
    public String getCityName() {
        return CityName;
    }
    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public int getCityId() {
        return CityId;
    }
    public void setCityId(int cityId) {
        CityId = cityId;
    }

    private String CityName;
    private int CityId;
}
