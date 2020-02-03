package com.anyemi.recska.model;

import java.util.List;

/**
 * Created by SuryaTejaChalla on 12-12-2017.
 */

public class DemandModel {


    /**
     * loan_details : {"id":"1378","date_time":"2017-12-04 23:27:52","financer":"1","loan_number":"1137002482","total_loan_amount":null,"emi_amount":null,"roi":null,"tenture":null,"emi_start_date":null,"customer_name":"Pintakota Lovareaju","phone":null,"email":null,"loan_id":null,"status":"open","remarks":"1540","area":"D.No : No, Kothapalem, Ward 1, Yelamanchili, Kothapalem,Ward : ","tax_type":"HT","ulbcode":"1137"}
     * emi : [{"id":"2519","date_time":"2017-12-04 23:27:52","financer":"1","customer":"Pintakota Lovareaju","loan_number":"1137002482","emi_amount":"245","due_date":"2018-01-01 00:00:00","over_due":"30","p_status":"no","loan_id":"1378","payment_to":null,"payment_type":null,"remarks":null,"paid_amount":"0","paid_over_due":"0","tax_type":"HT","request_id":"15868","noc_id":"","noc_amount":"","demand_number":"YLM-20171204_23:27_2158","demand_date":"2017-12-04 23:27:52"},{"id":"2518","date_time":"2017-12-04 23:27:52","financer":"1","customer":"Pintakota Lovareaju","loan_number":"1137002482","emi_amount":"245","due_date":"2018-02-01 00:00:00","over_due":"0","p_status":"no","loan_id":"1378","payment_to":null,"payment_type":null,"remarks":null,"paid_amount":"0","paid_over_due":"0","tax_type":"HT","request_id":"15868","noc_id":"","noc_amount":"","demand_number":"YLM-20171204_23:27_2158","demand_date":"2017-12-04 23:27:52"}]
     * tax_array : {"HT":"Property Tax","TAP":"Water Tax","BSB":"Semi Bulk"}
     */

    private LoanDetailsBean loan_details;
    private TaxArrayBean tax_array;
    private List<EmiBean> emi;

    public LoanDetailsBean getLoan_details() {
        return loan_details;
    }

    public void setLoan_details(LoanDetailsBean loan_details) {
        this.loan_details = loan_details;
    }

    public TaxArrayBean getTax_array() {
        return tax_array;
    }

    public void setTax_array(TaxArrayBean tax_array) {
        this.tax_array = tax_array;
    }

    public List<EmiBean> getEmi() {
        return emi;
    }

    public void setEmi(List<EmiBean> emi) {
        this.emi = emi;
    }

    public static class LoanDetailsBean {
        /**
         * id : 1378
         * date_time : 2017-12-04 23:27:52
         * financer : 1
         * loan_number : 1137002482
         * total_loan_amount : null
         * emi_amount : null
         * roi : null
         * tenture : null
         * emi_start_date : null
         * customer_name : Pintakota Lovareaju
         * phone : null
         * email : null
         * loan_id : null
         * status : open
         * remarks : 1540
         * area : D.No : No, Kothapalem, Ward 1, Yelamanchili, Kothapalem,Ward :
         * tax_type : HT
         * ulbcode : 1137
         */

        private String id;
        private String date_time;
        private String financer;
        private String loan_number;
        private Object total_loan_amount;
        private Object emi_amount;
        private Object roi;
        private Object tenture;
        private Object emi_start_date;
        private String customer_name;
        private String phone;
        private Object email;
        private Object loan_id;
        private String status;
        private String remarks;
        private String area;
        private String tax_type;
        private String ulbcode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDate_time() {
            return date_time;
        }

        public void setDate_time(String date_time) {
            this.date_time = date_time;
        }

        public String getFinancer() {
            return financer;
        }

        public void setFinancer(String financer) {
            this.financer = financer;
        }

        public String getLoan_number() {
            return loan_number;
        }

        public void setLoan_number(String loan_number) {
            this.loan_number = loan_number;
        }

        public Object getTotal_loan_amount() {
            return total_loan_amount;
        }

        public void setTotal_loan_amount(Object total_loan_amount) {
            this.total_loan_amount = total_loan_amount;
        }

        public Object getEmi_amount() {
            return emi_amount;
        }

        public void setEmi_amount(Object emi_amount) {
            this.emi_amount = emi_amount;
        }

        public Object getRoi() {
            return roi;
        }

        public void setRoi(Object roi) {
            this.roi = roi;
        }

        public Object getTenture() {
            return tenture;
        }

        public void setTenture(Object tenture) {
            this.tenture = tenture;
        }

        public Object getEmi_start_date() {
            return emi_start_date;
        }

        public void setEmi_start_date(Object emi_start_date) {
            this.emi_start_date = emi_start_date;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public void setCustomer_name(String customer_name) {
            this.customer_name = customer_name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public Object getLoan_id() {
            return loan_id;
        }

        public void setLoan_id(Object loan_id) {
            this.loan_id = loan_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getTax_type() {
            return tax_type;
        }

        public void setTax_type(String tax_type) {
            this.tax_type = tax_type;
        }

        public String getUlbcode() {
            return ulbcode;
        }

        public void setUlbcode(String ulbcode) {
            this.ulbcode = ulbcode;
        }
    }

    public static class TaxArrayBean {
        /**
         * HT : Property Tax
         * TAP : Water Tax
         * BSB : Semi Bulk
         */

        private String HT;
        private String TAP;
        private String BSB;

        public String getHT() {
            return HT;
        }

        public void setHT(String HT) {
            this.HT = HT;
        }

        public String getTAP() {
            return TAP;
        }

        public void setTAP(String TAP) {
            this.TAP = TAP;
        }

        public String getBSB() {
            return BSB;
        }

        public void setBSB(String BSB) {
            this.BSB = BSB;
        }
    }

    public static class EmiBean {
        /**
         * id : 2519
         * date_time : 2017-12-04 23:27:52
         * financer : 1
         * customer : Pintakota Lovareaju
         * loan_number : 1137002482
         * emi_amount : 245
         * due_date : 2018-01-01 00:00:00
         * over_due : 30
         * p_status : no
         * loan_id : 1378
         * payment_to : null
         * payment_type : null
         * remarks : null
         * paid_amount : 0
         * paid_over_due : 0
         * tax_type : HT
         * request_id : 15868
         * noc_id :
         * noc_amount :
         * demand_number : YLM-20171204_23:27_2158
         * demand_date : 2017-12-04 23:27:52
         */

        private String id;
        private String date_time;
        private String financer;
        private String customer;
        private String loan_number;
        private String emi_amount;
        private String due_date;
        private String over_due;
        private String p_status;
        private String loan_id;
        private Object payment_to;
        private Object payment_type;
        private Object remarks;
        private String paid_amount;
        private String paid_over_due;
        private String tax_type;
        private String request_id;
        private String noc_id;
        private String noc_amount;
        private String demand_number;
        private String demand_date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDate_time() {
            return date_time;
        }

        public void setDate_time(String date_time) {
            this.date_time = date_time;
        }

        public String getFinancer() {
            return financer;
        }

        public void setFinancer(String financer) {
            this.financer = financer;
        }

        public String getCustomer() {
            return customer;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
        }

        public String getLoan_number() {
            return loan_number;
        }

        public void setLoan_number(String loan_number) {
            this.loan_number = loan_number;
        }

        public String getEmi_amount() {
            return emi_amount;
        }

        public void setEmi_amount(String emi_amount) {
            this.emi_amount = emi_amount;
        }

        public String getDue_date() {
            return due_date;
        }

        public void setDue_date(String due_date) {
            this.due_date = due_date;
        }

        public String getOver_due() {
            return over_due;
        }

        public void setOver_due(String over_due) {
            this.over_due = over_due;
        }

        public String getP_status() {
            return p_status;
        }

        public void setP_status(String p_status) {
            this.p_status = p_status;
        }

        public String getLoan_id() {
            return loan_id;
        }

        public void setLoan_id(String loan_id) {
            this.loan_id = loan_id;
        }

        public Object getPayment_to() {
            return payment_to;
        }

        public void setPayment_to(Object payment_to) {
            this.payment_to = payment_to;
        }

        public Object getPayment_type() {
            return payment_type;
        }

        public void setPayment_type(Object payment_type) {
            this.payment_type = payment_type;
        }

        public Object getRemarks() {
            return remarks;
        }

        public void setRemarks(Object remarks) {
            this.remarks = remarks;
        }

        public String getPaid_amount() {
            return paid_amount;
        }

        public void setPaid_amount(String paid_amount) {
            this.paid_amount = paid_amount;
        }

        public String getPaid_over_due() {
            return paid_over_due;
        }

        public void setPaid_over_due(String paid_over_due) {
            this.paid_over_due = paid_over_due;
        }

        public String getTax_type() {
            return tax_type;
        }

        public void setTax_type(String tax_type) {
            this.tax_type = tax_type;
        }

        public String getRequest_id() {
            return request_id;
        }

        public void setRequest_id(String request_id) {
            this.request_id = request_id;
        }

        public String getNoc_id() {
            return noc_id;
        }

        public void setNoc_id(String noc_id) {
            this.noc_id = noc_id;
        }

        public String getNoc_amount() {
            return noc_amount;
        }

        public void setNoc_amount(String noc_amount) {
            this.noc_amount = noc_amount;
        }

        public String getDemand_number() {
            return demand_number;
        }

        public void setDemand_number(String demand_number) {
            this.demand_number = demand_number;
        }

        public String getDemand_date() {
            return demand_date;
        }

        public void setDemand_date(String demand_date) {
            this.demand_date = demand_date;
        }
    }
}
