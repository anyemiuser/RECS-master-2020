package com.anyemi.recska.model;

import java.util.ArrayList;
import java.util.List;

public class VerifyOtpModel {

    /**
     * id : 858
     * status : success
     * msg : Otp Verified Sucessfully
     * name : mmmm
     * email : tax_type
     * username : 8008621100
     * user_vpas : [{"ext_nm":"@sbi","type":"Bank","upi_nme":"State Bank of India","vpa":"8008621100@sbi"},{"ext_nm":"@hdfcbank","type":"Bank","upi_nme":"HDFC Bank","vpa":"8008621100@hdfcbank"},{"ext_nm":"@hdfcbank","type":"Bank","upi_nme":"HDFC Bank","vpa":"8008621100@hdfcbank"},{"ext_nm":"@idfcbank","type":"Bank","upi_nme":" IDFC Bank","vpa":"8008621100@idfcbank"},{"ext_nm":"@kmbl","type":"Bank","upi_nme":"Kotak Mahendra","vpa":"8008621100@kmbl"},{"ext_nm":"@upi","type":"Wallet","upi_nme":"BHIM","vpa":"8008621100@upi"}]
     */

    private String id;
    private String status;
    private String msg;
    private String name;
    private String email;
    private String username;
    private ArrayList<UserVpasBean> user_vpas;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<UserVpasBean> getUser_vpas() {
        return user_vpas;
    }

    public void setUser_vpas(ArrayList<UserVpasBean> user_vpas) {
        this.user_vpas = user_vpas;
    }

    public static class UserVpasBean {
        /**
         * ext_nm : @sbi
         * type : Bank
         * upi_nme : State Bank of India
         * vpa : 8008621100@sbi
         */

        private String ext_nm;
        private String type;
        private String upi_nme;
        private String vpa;
        private String icon;

        public String getExt_nm() {
            return ext_nm;
        }

        public void setExt_nm(String ext_nm) {
            this.ext_nm = ext_nm;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUpi_nme() {
            return upi_nme;
        }

        public void setUpi_nme(String upi_nme) {
            this.upi_nme = upi_nme;
        }

        public String getVpa() {
            return vpa;
        }

        public void setVpa(String vpa) {
            this.vpa = vpa;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
