package com.anyemi.recska.model;

public class PaymentIdModel {


    /**
     * status : success
     * msg : SUCCESS
     * payment_id : 30607
     */

    private String status;
    private String msg;
    private int payment_id;

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

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }
}
