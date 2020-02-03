package com.anyemi.recska.model;

public class PayuResponseModel {


    /**
     * id : 8826188199
     * mode : CC
     * status : failure
     * unmappedstatus : failed
     * key : WqVkGJ
     * txnid : 1564393389979
     * transaction_fee : 365.00
     * amount : 365.00
     * cardCategory : domestic
     * discount : 0.00
     * addedon : 2019-07-29 15:13:52
     * productinfo : Bill Payment
     * firstname : Jaya Lakshmi
     * email : jaya@anyemi.com
     * phone : 9441177811
     * udf1 : udf1
     * udf2 : udf2
     * udf3 : udf3
     * udf4 : udf4
     * udf5 : udf5
     * hash : ec7678adf1dc772393d5decd21fc412fb3147c209107634b6b82e3529103e62a687363d814a04bb03094b052b4a2f38cada9f312e09cc5e44e566f68481a6640
     * field8 : Not Enrolled Failure
     * field9 : Not Enrolled Failure
     * payment_source : payu
     * PG_TYPE : HDFCPG
     * ibibo_code : CC
     * error_code : E1302
     * Error_Message : Bank failed to authenticate the customer due to 3D Secure Enrollment decline
     * name_on_card : test
     * card_no : 401200XXXXXX1112
     * is_seamless : 1
     * surl : https://payuresponse.firebaseapp.com/success
     * furl : https://payuresponse.firebaseapp.com/failure
     */

    private long id;
    private String mode;
    private String status;
    private String unmappedstatus;
    private String key;
    private String txnid;
    private String transaction_fee;
    private String amount;
    private String cardCategory;
    private String discount;
    private String addedon;
    private String productinfo;
    private String firstname;
    private String email;
    private String phone;
    private String udf1;
    private String udf2;
    private String udf3;
    private String udf4;
    private String udf5;
    private String hash;
    private String field8;
    private String field9;
    private String payment_source;
    private String PG_TYPE;
    private String ibibo_code;
    private String error_code;
    private String Error_Message;
    private String name_on_card;
    private String card_no;
    private int is_seamless;
    private String surl;
    private String furl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnmappedstatus() {
        return unmappedstatus;
    }

    public void setUnmappedstatus(String unmappedstatus) {
        this.unmappedstatus = unmappedstatus;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getTransaction_fee() {
        return transaction_fee;
    }

    public void setTransaction_fee(String transaction_fee) {
        this.transaction_fee = transaction_fee;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(String cardCategory) {
        this.cardCategory = cardCategory;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAddedon() {
        return addedon;
    }

    public void setAddedon(String addedon) {
        this.addedon = addedon;
    }

    public String getProductinfo() {
        return productinfo;
    }

    public void setProductinfo(String productinfo) {
        this.productinfo = productinfo;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUdf1() {
        return udf1;
    }

    public void setUdf1(String udf1) {
        this.udf1 = udf1;
    }

    public String getUdf2() {
        return udf2;
    }

    public void setUdf2(String udf2) {
        this.udf2 = udf2;
    }

    public String getUdf3() {
        return udf3;
    }

    public void setUdf3(String udf3) {
        this.udf3 = udf3;
    }

    public String getUdf4() {
        return udf4;
    }

    public void setUdf4(String udf4) {
        this.udf4 = udf4;
    }

    public String getUdf5() {
        return udf5;
    }

    public void setUdf5(String udf5) {
        this.udf5 = udf5;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getField8() {
        return field8;
    }

    public void setField8(String field8) {
        this.field8 = field8;
    }

    public String getField9() {
        return field9;
    }

    public void setField9(String field9) {
        this.field9 = field9;
    }

    public String getPayment_source() {
        return payment_source;
    }

    public void setPayment_source(String payment_source) {
        this.payment_source = payment_source;
    }

    public String getPG_TYPE() {
        return PG_TYPE;
    }

    public void setPG_TYPE(String PG_TYPE) {
        this.PG_TYPE = PG_TYPE;
    }

    public String getIbibo_code() {
        return ibibo_code;
    }

    public void setIbibo_code(String ibibo_code) {
        this.ibibo_code = ibibo_code;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_Message() {
        return Error_Message;
    }

    public void setError_Message(String Error_Message) {
        this.Error_Message = Error_Message;
    }

    public String getName_on_card() {
        return name_on_card;
    }

    public void setName_on_card(String name_on_card) {
        this.name_on_card = name_on_card;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public int getIs_seamless() {
        return is_seamless;
    }

    public void setIs_seamless(int is_seamless) {
        this.is_seamless = is_seamless;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }

    public String getFurl() {
        return furl;
    }

    public void setFurl(String furl) {
        this.furl = furl;
    }
}
