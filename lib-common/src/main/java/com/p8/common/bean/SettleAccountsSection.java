package com.p8.common.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * @author : WX.Y
 * date : 2021/2/24 17:30
 * description :
 */
public class SettleAccountsSection extends SectionEntity<SettleAccounts> {
    private String total;

    public SettleAccountsSection(boolean isHeader, String header, String total) {
        super(isHeader, header);
        this.total = total;
    }

    public SettleAccountsSection(SettleAccounts settleAccounts) {
        super(settleAccounts);
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

