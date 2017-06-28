package com.rest.mail;

import com.model.entity.MailItem;

import java.util.ArrayList;

/**
 * Created by Julia on 22.06.2017.
 */
class MailListResponse {
    private ArrayList<MailItem> data;

    public ArrayList<MailItem> getData() {
        return data;
    }

    public void setData(ArrayList<MailItem> data) {
        this.data = data;
    }
}
