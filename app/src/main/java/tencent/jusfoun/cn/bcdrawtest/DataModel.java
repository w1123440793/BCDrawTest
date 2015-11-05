package tencent.jusfoun.cn.bcdrawtest;

import java.util.ArrayList;

/**
 * Author  JUSFOUN
 * CreateDate 2015/10/22.
 * Description
 */
public class DataModel {

    private String name;
    private ArrayList<ItemOneModel> itemOneList;
    private ArrayList<ItemTwoModel> itemTowList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ItemOneModel> getItemOneList() {
        return itemOneList;
    }

    public void setItemOneList(ArrayList<ItemOneModel> itemOneList) {
        this.itemOneList = itemOneList;
    }

    public ArrayList<ItemTwoModel> getItemTowList() {
        return itemTowList;
    }

    public void setItemTowList(ArrayList<ItemTwoModel> itemTowList) {
        this.itemTowList = itemTowList;
    }
}
