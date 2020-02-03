package com.anyemi.recska.model;

import java.util.List;

public class VpaListModel {

    private String status;
    private String msg;
    private List<UpilistBean> upilist;

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

    public List<UpilistBean> getUpilist() {
        return upilist;
    }

    public void setUpilist(List<UpilistBean> upilist) {
        this.upilist = upilist;
    }

    public static class UpilistBean {
        /**
         * upi_id : 9912208840@upi
         * status : 1
         * primary_upi : 0
         */

        private String upi_id;
        private String status;
        private String primary_upi;
        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        private String icon;


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
