package com.aaramon.aaramonjio.supplier;

/**
 * Created by dell on 5/25/2017.
 */

class StateListModel {
    public String getStateName() {
        return StateName;
    }
    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public int getStateId() {
        return StateId;
    }
    public void setStateId(int stateId) {
        StateId = stateId;
    }

    private String StateName;
    private int StateId;
}
