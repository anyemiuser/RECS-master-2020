package com.anyemi.recska.model;

/**
 * Created by SuryaTejaChalla on 12-02-2018.
 */

public class RegisterModel {
    /**
     * status : sucess
     * msg : Successfully Registered
     * otp : 7389
     * mobile_number : 9866977638
     */

    private String status;
    private String msg;
    private int otp;
    private String mobile_number;

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

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }
}
