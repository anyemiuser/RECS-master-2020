package com.anyemi.recska.model;

import java.util.List;

/**
 * Created by SuryaTejaChalla on 26-12-2017.
 */

public class PaymentRequestModelDummy {


    /**
     * emi : [{"id":"2519"},{"id":"2520"}]
     * payment_type : check
     * total_amount : 2000
     * payment_extra_fields : {"checkno":111,"bankname":"ICICI","branch":"Vizaq","checkdate":"yy-mm-dd","rr_number":"","mobile_number":984889556,"upi_id":""}
     * user_id : 614
     */

    private String payment_type;
    private int total_amount;
    private PaymentExtraFieldsBean payment_extra_fields;
    private int user_id;
    private List<EmiBean> emi;

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public PaymentExtraFieldsBean getPayment_extra_fields() {
        return payment_extra_fields;
    }

    public void setPayment_extra_fields(PaymentExtraFieldsBean payment_extra_fields) {
        this.payment_extra_fields = payment_extra_fields;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public List<EmiBean> getEmi() {
        return emi;
    }

    public void setEmi(List<EmiBean> emi) {
        this.emi = emi;
    }

    public static class PaymentExtraFieldsBean {
        /**
         * checkno : 111
         * bankname : ICICI
         * branch : Vizaq
         * checkdate : yy-mm-dd
         * rr_number :
         * mobile_number : 984889556
         * upi_id :
         */

        private int checkno;
        private String bankname;
        private String branch;
        private String checkdate;
        private String rr_number;
        private int mobile_number;
        private String upi_id;

        public int getCheckno() {
            return checkno;
        }

        public void setCheckno(int checkno) {
            this.checkno = checkno;
        }

        public String getBankname() {
            return bankname;
        }

        public void setBankname(String bankname) {
            this.bankname = bankname;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }

        public String getCheckdate() {
            return checkdate;
        }

        public void setCheckdate(String checkdate) {
            this.checkdate = checkdate;
        }

        public String getRr_number() {
            return rr_number;
        }

        public void setRr_number(String rr_number) {
            this.rr_number = rr_number;
        }

        public int getMobile_number() {
            return mobile_number;
        }

        public void setMobile_number(int mobile_number) {
            this.mobile_number = mobile_number;
        }

        public String getUpi_id() {
            return upi_id;
        }

        public void setUpi_id(String upi_id) {
            this.upi_id = upi_id;
        }
    }

    public static class EmiBean {
        /**
         * id : 2519
         */

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
