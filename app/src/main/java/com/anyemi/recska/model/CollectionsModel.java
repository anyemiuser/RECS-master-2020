package com.anyemi.recska.model;

import java.util.List;

/**
 * Created by SuryaTejaChalla on 08-12-2017.
 */

public class CollectionsModel {


    private List<CollectionsBean> collections;

    public List<CollectionsBean> getCollections() {
        return collections;
    }

    public void setCollections(List<CollectionsBean> collections) {
        this.collections = collections;
    }

    public static class CollectionsBean {
        /**
         * id : 2
         * date_time : 2018-04-17 19:51:17
         * financer_name : RECS
         * loan_number : 90703 00133
         * payment_type : C
         * txn_payment_id : AE-EB-170418-000002
         * customer_name : THE COMMISSIONER
         * emi_amount : 16018
         * total_amount : 16067.027
         * duedates : 2018-04-20
         * total_emi : 16018
         * total_overdue : 0
         * due_date : 2018-04-20
         * adjamt : 0
         * newarrears : 9946
         * last_paid_date :
         * last_paid_amt :
         * billamt : 5972
         * reconnection_fee : 100.00
         * agent_name : A KANAKA RATNAM
         * section : 16018
         * ero : HVC
         * circle : 604
         * bank_charges : 0
         * dname : OPERATOR1
         * pay_ref : Cash-98512452-172893-
         * service_charge : 49.027
         */

        private String id;
        private String date_time;
        private String financer_name;
        private String loan_number;
        private String payment_type;
        private String txn_payment_id;
        private String customer_name;
        private String emi_amount;
        private String total_amount;
        private String duedates;
        private String total_emi;
        private String total_overdue;
        private String due_date;
        private String adjamt;
        private String newarrears;
        private String last_paid_date;
        private String last_paid_amt;
        private String billamt;
        private String reconnection_fee;
        private String agent_name;
        private String section;
        private String ero;
        private String circle;
        private String bank_charges;
        private String dname;
        private String pay_ref;
        private String service_charge;
        private String surcharge_fee;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        private String icon;

        public String getSurcharge_fee() {
            return surcharge_fee;
        }

        public void setSurcharge_fee(String surcharge_fee) {
            this.surcharge_fee = surcharge_fee;
        }



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

        public String getFinancer_name() {
            return financer_name;
        }

        public void setFinancer_name(String financer_name) {
            this.financer_name = financer_name;
        }

        public String getLoan_number() {
            return loan_number;
        }

        public void setLoan_number(String loan_number) {
            this.loan_number = loan_number;
        }

        public String getPayment_type() {
            return payment_type;
        }

        public void setPayment_type(String payment_type) {
            this.payment_type = payment_type;
        }

        public String getTxn_payment_id() {
            return txn_payment_id;
        }

        public void setTxn_payment_id(String txn_payment_id) {
            this.txn_payment_id = txn_payment_id;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public void setCustomer_name(String customer_name) {
            this.customer_name = customer_name;
        }

        public String getEmi_amount() {
            return emi_amount;
        }

        public void setEmi_amount(String emi_amount) {
            this.emi_amount = emi_amount;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getDuedates() {
            return duedates;
        }

        public void setDuedates(String duedates) {
            this.duedates = duedates;
        }

        public String getTotal_emi() {
            return total_emi;
        }

        public void setTotal_emi(String total_emi) {
            this.total_emi = total_emi;
        }

        public String getTotal_overdue() {
            return total_overdue;
        }

        public void setTotal_overdue(String total_overdue) {
            this.total_overdue = total_overdue;
        }

        public String getDue_date() {
            return due_date;
        }

        public void setDue_date(String due_date) {
            this.due_date = due_date;
        }

        public String getAdjamt() {
            return adjamt;
        }

        public void setAdjamt(String adjamt) {
            this.adjamt = adjamt;
        }

        public String getNewarrears() {
            return newarrears;
        }

        public void setNewarrears(String newarrears) {
            this.newarrears = newarrears;
        }

        public String getLast_paid_date() {
            return last_paid_date;
        }

        public void setLast_paid_date(String last_paid_date) {
            this.last_paid_date = last_paid_date;
        }

        public String getLast_paid_amt() {
            return last_paid_amt;
        }

        public void setLast_paid_amt(String last_paid_amt) {
            this.last_paid_amt = last_paid_amt;
        }

        public String getBillamt() {
            return billamt;
        }

        public void setBillamt(String billamt) {
            this.billamt = billamt;
        }

        public String getReconnection_fee() {
            return reconnection_fee;
        }

        public void setReconnection_fee(String reconnection_fee) {
            this.reconnection_fee = reconnection_fee;
        }

        public String getAgent_name() {
            return agent_name;
        }

        public void setAgent_name(String agent_name) {
            this.agent_name = agent_name;
        }

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }

        public String getEro() {
            return ero;
        }

        public void setEro(String ero) {
            this.ero = ero;
        }

        public String getCircle() {
            return circle;
        }

        public void setCircle(String circle) {
            this.circle = circle;
        }

        public String getBank_charges() {
            return bank_charges;
        }

        public void setBank_charges(String bank_charges) {
            this.bank_charges = bank_charges;
        }

        public String getDname() {
            return dname;
        }

        public void setDname(String dname) {
            this.dname = dname;
        }

        public String getPay_ref() {
            return pay_ref;
        }

        public void setPay_ref(String pay_ref) {
            this.pay_ref = pay_ref;
        }

        public String getService_charge() {
            return service_charge;
        }

        public void setService_charge(String service_charge) {
            this.service_charge = service_charge;
        }
    }
}
