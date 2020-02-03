package com.anyemi.recska.model;

import java.util.List;

public class ReportsModel {

    /**
     * PaymentWiseTranscitionsDetails : [{"TotalTranscitions":"5","payment_type":null,"Totalamount":"12165"},{"TotalTranscitions":"1","payment_type":"Airtel","Totalamount":"505"},{"TotalTranscitions":"22","payment_type":"C","Totalamount":"105591.99"},{"TotalTranscitions":"2","payment_type":"Jio","Totalamount":"650"},{"TotalTranscitions":"4894","payment_type":"Paytm","Totalamount":"6946263.429999998"},{"TotalTranscitions":"367","payment_type":"SBIUPI","Totalamount":"303640.06999999995"}]
     * total_records : 6
     */

    private int total_records;
    private List<PaymentWiseTranscitionsDetailsBean> PaymentWiseTranscitionsDetails;

    public int getTotal_records() {
        return total_records;
    }

    public void setTotal_records(int total_records) {
        this.total_records = total_records;
    }

    public List<PaymentWiseTranscitionsDetailsBean> getPaymentWiseTranscitionsDetails() {
        return PaymentWiseTranscitionsDetails;
    }

    public void setPaymentWiseTranscitionsDetails(List<PaymentWiseTranscitionsDetailsBean> PaymentWiseTranscitionsDetails) {
        this.PaymentWiseTranscitionsDetails = PaymentWiseTranscitionsDetails;
    }

    public static class PaymentWiseTranscitionsDetailsBean {
        /**
         * TotalTranscitions : 5
         * payment_type : null
         * Totalamount : 12165
         */

        private String TotalTranscitions;
        private Object payment_type;
        private String Totalamount;

        public String getTotalTranscitions() {
            return TotalTranscitions;
        }

        public void setTotalTranscitions(String TotalTranscitions) {
            this.TotalTranscitions = TotalTranscitions;
        }

        public Object getPayment_type() {
            return payment_type;
        }

        public void setPayment_type(Object payment_type) {
            this.payment_type = payment_type;
        }

        public String getTotalamount() {
            return Totalamount;
        }

        public void setTotalamount(String Totalamount) {
            this.Totalamount = Totalamount;
        }
    }
}
