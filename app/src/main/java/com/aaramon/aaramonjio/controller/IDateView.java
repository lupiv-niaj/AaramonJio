package com.aaramon.aaramonjio.controller;

import java.util.ArrayList;

public interface IDateView {

    public void getData();
    public ArrayList<String> getNextData(int i);
    public ArrayList<String> getPreviousData(int i);
}
