package com.anyemi.recska.model;

/**
 * Created by SuryaTejaChalla on 02-03-2018.
 */

public class CheackValidVpaModel {

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getUpi_id() {
        return upi_id;
    }

    public void setUpi_id(String upi_id) {
        this.upi_id = upi_id;
    }

    private String payment_type;
    private String upi_id;

    }
