package com.anyemi.recska.instamojo;

import com.anyemi.recska.model.PaymentRequestModel;
import com.google.gson.annotations.SerializedName;

public class GetOrderIDRequest {

    @SerializedName("env")
    private String env;

    @SerializedName("buyer_name")
    private String buyerName;

    @SerializedName("buyer_email")
    private String buyerEmail;

    @SerializedName("buyer_phone")
    private String buyerPhone;

    @SerializedName("amount")
    private String amount;

    @SerializedName("description")
    private String description;

    @SerializedName("type")
    private String type;

    @SerializedName("PaymentRequestModel")
    private PaymentRequestModel paymentRequestModel;

    public PaymentRequestModel getPaymentRequestModel() {
        return paymentRequestModel;
    }

    public void setPaymentRequestModel(PaymentRequestModel paymentRequestModel) {
        this.paymentRequestModel = paymentRequestModel;
    }



    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
