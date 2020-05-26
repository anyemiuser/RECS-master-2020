package com.anyemi.recska.activities;

public class UpdateBillModel {

    /**
     * status : Success
     * msg : SUCCESS
     * change_emi_id : 325355
     * orderid : RECS-260520-000037
     */

    private String status;
    private String msg;
    private int change_emi_id;
    private String orderid;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getChange_emi_id() {
        return change_emi_id;
    }

    public void setChange_emi_id(int change_emi_id) {
        this.change_emi_id = change_emi_id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }
}
