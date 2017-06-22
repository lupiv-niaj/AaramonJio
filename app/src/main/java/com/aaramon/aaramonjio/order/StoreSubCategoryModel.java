package com.aaramon.aaramonjio.order;

/**
 * Created by DELL STORE on 22-May-17.
 */

class StoreSubCategoryModel {
    int category_id;

    StoreSubCategoryModel(int id, String name) {
        this.category_id = id;
        this.category_name = name;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    String category_name;

}
