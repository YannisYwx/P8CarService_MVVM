package com.p8.common.bean;

import java.util.List;

/**
 * @author : WX.Y
 * date : 2020/9/25 14:28
 * description :
 */
public class Machines {

    private int total;
    private List<Machine> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Machine> getList() {
        return list;
    }

    public void setList(List<Machine> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Machines{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
