package com.anyemi.recska.model;

import java.util.List;

public class LoginModel {


    private String id;
    private String user_name;
    private String user_email;
    private String user_id;
    private String user_phone_number;
    private String mpos_username;
    private String mpos_password;
    private String mpos_id;
    private String wallet_amount;
    private String group_id;
    private String Service;
    private String status;
    private String version_id;

    public String getAEP_MID() {
        return AEP_MID;
    }

    public void setAEP_MID(String AEP_MID) {
        this.AEP_MID = AEP_MID;
    }

    private String AEP_MID;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;
    private List<UpilistBean> upilist;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_phone_number() {
        return user_phone_number;
    }

    public void setUser_phone_number(String user_phone_number) {
        this.user_phone_number = user_phone_number;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getMpos_username() {
        return mpos_username;
    }

    public void setMpos_username(String mpos_username) {
        this.mpos_username = mpos_username;
    }

    public String getMpos_password() {
        return mpos_password;
    }

    public void setMpos_password(String mpos_password) {
        this.mpos_password = mpos_password;
    }

    public String getMpos_id() {
        return mpos_id;
    }

    public void setMpos_id(String mpos_id) {
        this.mpos_id = mpos_id;
    }

    public String getWallet_amount() {
        return wallet_amount;
    }

    public void setWallet_amount(String wallet_amount) {
        this.wallet_amount = wallet_amount;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getService() {
        return Service;
    }

    public void setService(String Service) {
        this.Service = Service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion_id() {
        return version_id;
    }

    public void setVersion_id(String version_id) {
        this.version_id = version_id;
    }

    public List<UpilistBean> getUpilist() {
        return upilist;
    }

    public void setUpilist(List<UpilistBean> upilist) {
        this.upilist = upilist;
    }

    public static class UpilistBean {
        /**
         * upi_id : 9642444934@ybl
         * status : 1
         * primary_upi : 0
         */

        private String upi_id;
        private String status;
        private String primary_upi;

        public String getUpi_id() {
            return upi_id;
        }

        public void setUpi_id(String upi_id) {
            this.upi_id = upi_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPrimary_upi() {
            return primary_upi;
        }

        public void setPrimary_upi(String primary_upi) {
            this.primary_upi = primary_upi;
        }
    }
}
