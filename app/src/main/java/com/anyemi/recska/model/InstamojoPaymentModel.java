package com.anyemi.recska.model;

import java.util.List;

public class InstamojoPaymentModel {


    /**
     * status : Success
     * msg : Successfully sent
     * instamojo_payment_modes : [{"name":"NET BANKING","icon":"https://dev.anyemi.com/PaySTAR/images/Internethdpi.png","is_status":"1","key":"NETBANKING"},{"name":"CREDIT CARD","icon":"https://dev.anyemi.com/PaySTAR/images/c-card.png","is_status":"1","key":"CREDITCARD"},{"name":"DEBIT CARD","icon":"https://dev.anyemi.com/PaySTAR/images/c-card.png","is_status":"1","key":"DEBITCARD"},{"name":"WALLETS","icon":"https://dev.anyemi.com/PaySTAR/images/wallethdpi.png","is_status":"1","key":"WALLET"},{"name":"UPI","icon":"https://dev.anyemi.com/PaySTAR/images/bhim.jpg","is_status":"1","key":"UPI"},{"name":"NEFT","icon":"https://dev.anyemi.com/PaySTAR/images/bhim.jpg","is_status":"0","key":"NEFT"}]
     * inst_service_tax : [{"Payment_type":"DEBITCARD","extra_tax":"0","from":"0","to":"2000","gst_perc":"0","extra_tax_perc":"0"},{"Payment_type":"DEBITCARD","extra_tax":"0","from":"2000.01","to":"999999999999999999","gst_perc":"18","extra_tax_perc":"1.00"},{"Payment_type":"CREDITCARD","extra_tax":"0","from":"0","to":"9999999999999","gst_perc":"18","extra_tax_perc":"1.20"},{"Payment_type":"NETBANKING","extra_tax":"10","from":"0","to":"9999999999999999","gst_perc":"18","extra_tax_perc":"0"},{"Payment_type":"NEFT","extra_tax":"5","from":"0","to":"99999999999999999999","gst_perc":"18","extra_tax_perc":"0"},{"Payment_type":"UPI","extra_tax":"0","from":"0","to":"2000","gst_perc":"0","extra_tax_perc":"0"},{"Payment_type":"WALLET","extra_tax":"0","from":"0","to":"999999999999999999","gst_perc":"18","extra_tax_perc":"1.90"},{"Payment_type":"UPI","extra_tax":"0","from":"2000.01","to":"999999999999","gst_perc":"18","extra_tax_perc":"0.65"}]
     */

    private String status;
    private String msg;
    private List<InstamojoPaymentModesBean> instamojo_payment_modes;
    private List<InstServiceTaxBean> inst_service_tax;

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

    public List<InstamojoPaymentModesBean> getInstamojo_payment_modes() {
        return instamojo_payment_modes;
    }

    public void setInstamojo_payment_modes(List<InstamojoPaymentModesBean> instamojo_payment_modes) {
        this.instamojo_payment_modes = instamojo_payment_modes;
    }

    public List<InstServiceTaxBean> getInst_service_tax() {
        return inst_service_tax;
    }

    public void setInst_service_tax(List<InstServiceTaxBean> inst_service_tax) {
        this.inst_service_tax = inst_service_tax;
    }

    public static class InstamojoPaymentModesBean {
        /**
         * name : NET BANKING
         * icon : https://dev.anyemi.com/PaySTAR/images/Internethdpi.png
         * is_status : 1
         * key : NETBANKING
         */

        private String name;
        private String icon;
        private String is_status;
        private String key;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIs_status() {
            return is_status;
        }

        public void setIs_status(String is_status) {
            this.is_status = is_status;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    public static class InstServiceTaxBean {
        /**
         * Payment_type : DEBITCARD
         * extra_tax : 0
         * from : 0
         * to : 2000
         * gst_perc : 0
         * extra_tax_perc : 0
         */

        private String Payment_type;
        private String extra_tax;
        private String from;
        private String to;
        private String gst_perc;
        private String extra_tax_perc;

        public String getPayment_type() {
            return Payment_type;
        }

        public void setPayment_type(String Payment_type) {
            this.Payment_type = Payment_type;
        }

        public String getExtra_tax() {
            return extra_tax;
        }

        public void setExtra_tax(String extra_tax) {
            this.extra_tax = extra_tax;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getGst_perc() {
            return gst_perc;
        }

        public void setGst_perc(String gst_perc) {
            this.gst_perc = gst_perc;
        }

        public String getExtra_tax_perc() {
            return extra_tax_perc;
        }

        public void setExtra_tax_perc(String extra_tax_perc) {
            this.extra_tax_perc = extra_tax_perc;
        }
    }
}
