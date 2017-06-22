package com.aaramon.aaramonjio.order;

/**
 * Created by dineshsolanki on 23/05/17.
 */

public class MainDashboardModel {
    private int Icon;
    private String Text;
    private String TextDetail;
    private String PendinOrderCount;

    public void setIcon(int IconName) {
        this.Icon = IconName;
    }

    public void setText(String TextValue) {
        this.Text = TextValue;
    }

    public void setTextDetail(String TextDetail) {
        this.TextDetail = TextDetail;
    }

    public void setPendingOrderCount(String PendinOrderCount) {
        this.PendinOrderCount = PendinOrderCount;
    }

    public int getIcon() {
        return Icon;
    }

    public String getText() {
        return Text;
    }

    public String getTextDetail() {
        return TextDetail;
    }

    public String getPendinOrderCount() {
        return PendinOrderCount;
    }
}
