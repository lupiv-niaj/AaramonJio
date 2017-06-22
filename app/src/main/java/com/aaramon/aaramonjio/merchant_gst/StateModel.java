package com.aaramon.aaramonjio.merchant_gst;

/**
 * Created by DELL STORE on 17-May-17.
 */

public class StateModel {
    int StateId;
    String StateName;
    int StateGSTCode;

    public int getStateId() {
        return StateId;
    }

    public void setStateId(int stateId) {
        StateId = stateId;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public int getStateGSTCode() {
        return StateGSTCode;
    }

    public void setStateGSTCode(int stateGSTCode) {
        StateGSTCode = stateGSTCode;
    }

    public boolean getSelected() {
        return IsSelected;
    }

    public void setSelected(boolean selected) {
        IsSelected = selected;
    }

    boolean IsSelected;
}
