package com.p8.common.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * @author : WX.Y
 * date : 2021/2/24 15:52
 * description : 发票
 */
public class InvoiceSection extends SectionEntity<Invoice> {
    private String month;
    private String total;

    public InvoiceSection(boolean isHeader, String month, String total) {
        super(isHeader, month);
        this.total = total;
        this.month = month;
    }

    public InvoiceSection(Invoice invoice) {
        super(invoice);
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

