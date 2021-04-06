package com.p8.common.bean;

import java.util.List;

/**
 * @author : WX.Y
 * date : 2020/11/13 15:17
 * description :
 */
public class Landlords {
    private int total;
    private List<Landlord> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Landlord> getList() {
        return list;
    }

    public void setList(List<Landlord> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Landlords{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}

