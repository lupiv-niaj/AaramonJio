package com.aaramon.aaramonjio.supplier;

class CompanyListModel {
    public String getCompanyName() {
        return CompanyName;
    }
    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public int getCompanyId() {
        return CompanyId;
    }
    public void setCompanyId(int companyId) {
        CompanyId = companyId;
    }

    private String CompanyName;
    private int CompanyId;
}
