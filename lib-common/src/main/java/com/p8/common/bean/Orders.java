package com.p8.common.bean;

import java.util.List;

/**
 * @author : WX.Y
 * date : 2020/11/13 15:17
 * description : 订单列表
 */
public class Orders {
    private int total;
    private List<Order> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Order> getList() {
        return list;
    }

    public void setList(List<Order> list) {
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

