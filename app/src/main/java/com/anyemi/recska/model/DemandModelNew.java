package com.anyemi.recska.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SuryaTejaChalla on 13-02-2018.
 */

public class DemandModelNew {

    /**
     * status : Success
     * msg : Successfully send
     * loan_details : {"id":"121","date_time":"2018-04-09 13:49:50","financer":"1","loan_number":"90701 09933","total_loan_amount":"363","emi_amount":"363","roi":"6","tenture":"0","emi_start_date":"2018-03-20 00:00:00","customer_name":"COMMISSIONER G.V.M.C   78 B3","phone":"9876541230","email":null,"loan_id":null,"status":"open","remarks":null,"address":"AGANAMPUDISTREET LIGHTS","adhaar":"123456789012"}
     * emi : [{"id":"324","date_time":"2018-04-17 00:00:00","financer":"1","customer":"COMMISSIONER G.V.M.C   78 B3","loan_number":"90701 09933         ","emi_amount":"-10086","due_date":"2018-04-20 00:00:00","over_due":"0","p_status":"no","loan_id":"121","payment_to":null,"payment_type":null,"remarks":null,"paid_amount":"0","paid_over_due":"0","adjamt":"855","newarrears":"855","reconnection_fee":"855"}]
     * pay_array : {"C":"Cash","CQ":"Check","CC":"Credit Card","BQ":"Barath QR","DC":"Debit Card","NB":"Net Banking","Paytm":"Paytm","PhonePe":"PhonePe","PayU":"PayUMoney","*99#":"*99#","BHIM":"BHIM","AADHAR":"Aadhar Pay","UPI":"SBI UPI","Jio":"Jio Money","Airtel":"Airtel Money","M-Pesa":"M-Pesa","FC":"Free Charge","PQ":"Paytm QR","SBIUPI":"SBI UPI"}
     * tax_array : [{"id":"1","transaction_amount":"2","from":"1","to":"1000","credit_service_tax_":"1.75","gst_debit":"0","gst_credit":"18","debit_service_tax_":"0","extra_tax":"0"},{"id":"2","transaction_amount":"5","from":"1000.01","to":"2000","credit_service_tax_":"1.75","gst_debit":"0","gst_credit":"18","debit_service_tax_":"0","extra_tax":"0"},{"id":"3","transaction_amount":"10","from":"2000.01","to":"2500","credit_service_tax_":"1.75","gst_debit":"0","gst_credit":"18","debit_service_tax_":"0","extra_tax":"0"},{"id":"4","transaction_amount":"25","from":"2500.01","to":"5000","credit_service_tax_":"1.75","gst_debit":"18","gst_credit":"18","debit_service_tax_":"1","extra_tax":"0"},{"id":"5","transaction_amount":"25","from":"5000.01","to":"9999999999999","credit_service_tax_":"1.75","gst_debit":"18","gst_credit":"18","debit_service_tax_":"1","extra_tax":"0.15"}]
     */

    private String status;
    private String msg;
    private LoanDetailsBean loan_details;
    private PayArrayBean pay_array;
    private List<EmiBean> emi;
    private List<TaxArrayBean> tax_array;

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

    public LoanDetailsBean getLoan_details() {
        return loan_details;
    }

    public void setLoan_details(LoanDetailsBean loan_details) {
        this.loan_details = loan_details;
    }

    public PayArrayBean getPay_array() {
        return pay_array;
    }

    public void setPay_array(PayArrayBean pay_array) {
        this.pay_array = pay_array;
    }

    public List<EmiBean> getEmi() {
        return emi;
    }

    public void setEmi(List<EmiBean> emi) {
        this.emi = emi;
    }

    public List<TaxArrayBean> getTax_array() {
        return tax_array;
    }

    public void setTax_array(List<TaxArrayBean> tax_array) {
        this.tax_array = tax_array;
    }

    public static class LoanDetailsBean {
        /**
         * id : 121
         * date_time : 2018-04-09 13:49:50
         * financer : 1
         * loan_number : 90701 09933
         * total_loan_amount : 363
         * emi_amount : 363
         * roi : 6
         * tenture : 0
         * emi_start_date : 2018-03-20 00:00:00
         * customer_name : COMMISSIONER G.V.M.C   78 B3
         * phone : 9876541230
         * email : null
         * loan_id : null
         * status : open
         * remarks : null
         * address : AGANAMPUDISTREET LIGHTS
         * adhaar : 123456789012
         */

        private String id;
        private String date_time;
        private String financer;
        private String loan_number;
        private String total_loan_amount;
        private String emi_amount;
        private String roi;
        private String tenture;
        private String emi_start_date;
        private String customer_name;
        private String phone;
        private Object email;
        private Object loan_id;
        private String status;
        private Object remarks;
        private String address;
        private String adhaar;

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

        public String getTotal_loan_amount() {
            return total_loan_amount;
        }

        public void setTotal_loan_amount(String total_loan_amount) {
            this.total_loan_amount = total_loan_amount;
        }

        public String getEmi_amount() {
            return emi_amount;
        }

        public void setEmi_amount(String emi_amount) {
            this.emi_amount = emi_amount;
        }

        public String getRoi() {
            return roi;
        }

        public void setRoi(String roi) {
            this.roi = roi;
        }

        public String getTenture() {
            return tenture;
        }

        public void setTenture(String tenture) {
            this.tenture = tenture;
        }

        public String getEmi_start_date() {
            return emi_start_date;
        }

        public void setEmi_start_date(String emi_start_date) {
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

        public Object getRemarks() {
            return remarks;
        }

        public void setRemarks(Object remarks) {
            this.remarks = remarks;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAdhaar() {
            return adhaar;
        }

        public void setAdhaar(String adhaar) {
            this.adhaar = adhaar;
        }
    }

    public static class PayArrayBean {
        /**
         * C : Cash
         * CQ : Check
         * CC : Credit Card
         * BQ : Barath QR
         * DC : Debit Card
         * NB : Net Banking
         * Paytm : Paytm
         * PhonePe : PhonePe
         * PayU : PayUMoney
         * *99# : *99#
         * BHIM : BHIM
         * AADHAR : Aadhar Pay
         * UPI : SBI UPI
         * Jio : Jio Money
         * Airtel : Airtel Money
         * M-Pesa : M-Pesa
         * FC : Free Charge
         * PQ : Paytm QR
         * SBIUPI : SBI UPI
         */

        private String C;
        private String CQ;
        private String CC;
        private String BQ;
        private String DC;
        private String NB;
        private String Paytm;
        private String PhonePe;
        private String PayU;
        @SerializedName("*99#")
        private String _$99212; // FIXME check this code
        private String BHIM;
        private String AADHAR;
        private String UPI;
        private String Jio;
        private String Airtel;
        @SerializedName("M-Pesa")
        private String MPesa;
        private String FC;
        private String PQ;
        private String SBIUPI;

        public String getC() {
            return C;
        }

        public void setC(String C) {
            this.C = C;
        }

        public String getCQ() {
            return CQ;
        }

        public void setCQ(String CQ) {
            this.CQ = CQ;
        }

        public String getCC() {
            return CC;
        }

        public void setCC(String CC) {
            this.CC = CC;
        }

        public String getBQ() {
            return BQ;
        }

        public void setBQ(String BQ) {
            this.BQ = BQ;
        }

        public String getDC() {
            return DC;
        }

        public void setDC(String DC) {
            this.DC = DC;
        }

        public String getNB() {
            return NB;
        }

        public void setNB(String NB) {
            this.NB = NB;
        }

        public String getPaytm() {
            return Paytm;
        }

        public void setPaytm(String Paytm) {
            this.Paytm = Paytm;
        }

        public String getPhonePe() {
            return PhonePe;
        }

        public void setPhonePe(String PhonePe) {
            this.PhonePe = PhonePe;
        }

        public String getPayU() {
            return PayU;
        }

        public void setPayU(String PayU) {
            this.PayU = PayU;
        }

        public String get_$99212() {
            return _$99212;
        }

        public void set_$99212(String _$99212) {
            this._$99212 = _$99212;
        }

        public String getBHIM() {
            return BHIM;
        }

        public void setBHIM(String BHIM) {
            this.BHIM = BHIM;
        }

        public String getAADHAR() {
            return AADHAR;
        }

        public void setAADHAR(String AADHAR) {
            this.AADHAR = AADHAR;
        }

        public String getUPI() {
            return UPI;
        }

        public void setUPI(String UPI) {
            this.UPI = UPI;
        }

        public String getJio() {
            return Jio;
        }

        public void setJio(String Jio) {
            this.Jio = Jio;
        }

        public String getAirtel() {
            return Airtel;
        }

        public void setAirtel(String Airtel) {
            this.Airtel = Airtel;
        }

        public String getMPesa() {
            return MPesa;
        }

        public void setMPesa(String MPesa) {
            this.MPesa = MPesa;
        }

        public String getFC() {
            return FC;
        }

        public void setFC(String FC) {
            this.FC = FC;
        }

        public String getPQ() {
            return PQ;
        }

        public void setPQ(String PQ) {
            this.PQ = PQ;
        }

        public String getSBIUPI() {
            return SBIUPI;
        }

        public void setSBIUPI(String SBIUPI) {
            this.SBIUPI = SBIUPI;
        }
    }

    public static class EmiBean {
        /**
         * id : 324
         * date_time : 2018-04-17 00:00:00
         * financer : 1
         * customer : COMMISSIONER G.V.M.C   78 B3
         * loan_number : 90701 09933
         * emi_amount : -10086
         * due_date : 2018-04-20 00:00:00
         * over_due : 0
         * p_status : no
         * loan_id : 121
         * payment_to : null
         * payment_type : null
         * remarks : null
         * paid_amount : 0
         * paid_over_due : 0
         * adjamt : 855
         * newarrears : 855
         * reconnection_fee : 855
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
        private String adjamt;
        private String newarrears;
        private String reconnection_fee;
        private String last_paid_date;
        private String last_paid_amt;
        private String surcharge_fee;

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

        public String getSurcharge_fee() {
            return surcharge_fee;
        }

        public void setSurcharge_fee(String surcharge_fee) {
            this.surcharge_fee = surcharge_fee;
        }



        public String getBillamt() {
            return billamt;
        }

        public void setBillamt(String billamt) {
            this.billamt = billamt;
        }

        private String billamt;

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

        public String getReconnection_fee() {
            return reconnection_fee;
        }

        public void setReconnection_fee(String reconnection_fee) {
            this.reconnection_fee = reconnection_fee;
        }
    }

    public static class TaxArrayBean {
        /**
         * id : 1
         * transaction_amount : 2
         * from : 1
         * to : 1000
         * credit_service_tax_ : 1.75
         * gst_debit : 0
         * gst_credit : 18
         * debit_service_tax_ : 0
         * extra_tax : 0
         */

        private String id;
        private String transaction_amount;
        private String from;
        private String to;
        private String credit_service_tax_;
        private String gst_debit;
        private String gst_credit;
        private String debit_service_tax_;
        private String extra_tax;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTransaction_amount() {
            return transaction_amount;
        }

        public void setTransaction_amount(String transaction_amount) {
            this.transaction_amount = transaction_amount;
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

        public String getCredit_service_tax_() {
            return credit_service_tax_;
        }

        public void setCredit_service_tax_(String credit_service_tax_) {
            this.credit_service_tax_ = credit_service_tax_;
        }

        public String getGst_debit() {
            return gst_debit;
        }

        public void setGst_debit(String gst_debit) {
            this.gst_debit = gst_debit;
        }

        public String getGst_credit() {
            return gst_credit;
        }

        public void setGst_credit(String gst_credit) {
            this.gst_credit = gst_credit;
        }

        public String getDebit_service_tax_() {
            return debit_service_tax_;
        }

        public void setDebit_service_tax_(String debit_service_tax_) {
            this.debit_service_tax_ = debit_service_tax_;
        }

        public String getExtra_tax() {
            return extra_tax;
        }

        public void setExtra_tax(String extra_tax) {
            this.extra_tax = extra_tax;
        }
    }
}
