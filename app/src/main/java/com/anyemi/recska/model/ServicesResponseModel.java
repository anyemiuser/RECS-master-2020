package com.anyemi.recska.model;

import java.util.List;

/**
 * Created by SuryaTejaChalla on 08-12-2017.
 */

public class ServicesResponseModel {


    private List<FinancerBean> financer;
    private List<TaxArrayBean> tax_array;



    public List<FinancerBean> getFinancer() {
        return financer;
    }

    public void setFinancer(List<FinancerBean> financer) {
        this.financer = financer;
    }

    public List<TaxArrayBean> getTax_array() {
        return tax_array;
    }

    public void setTax_array(List<TaxArrayBean> tax_array) {
        this.tax_array = tax_array;
    }

    public static class FinancerBean {
        /**
         * id : 5
         * icon : https://anyemi.com/PaySTAR/images/annacanteen1.png
         * block : 0
         * is_public : 1
         * tpe : ANNA CANTEEN
         * category : 9
         * payment_mode : [{"id":"9","financier_id":"2","Payment_mode":"UPI","paymentmode_icon":"https://anyemi.com/PaySTAR/images/sbiupi.jpeg","paymentmode_code":"SBIUPI","is_public":"1","status":"1"},{"id":"10","financier_id":"2","Payment_mode":"CREDIT/DEBIT CARD","paymentmode_icon":"https://anyemi.com/PaySTAR/images/c-card.png","paymentmode_code":"DC","is_public":"1","status":"1"},{"id":"11","financier_id":"2","Payment_mode":"CREDIT CARD","paymentmode_icon":"https://anyemi.com/PaySTAR/images/c-card.png","paymentmode_code":"CC","is_public":"1","status":"0"},{"id":"12","financier_id":"2","Payment_mode":"ANYEMI WALLET","paymentmode_icon":"https://anyemi.com/PaySTAR/images/anyEMI%20Wallet.png","paymentmode_code":"AW","is_public":"1","status":"1"},{"id":"13","financier_id":"2","Payment_mode":"AADHAR PAY","paymentmode_icon":"https://anyemi.com/PaySTAR/images/Aadharhdpi.png","paymentmode_code":"AADHAR","is_public":"1","status":"1"},{"id":"14","financier_id":"2","Payment_mode":"PAYTM PG","paymentmode_icon":"https://anyemi.com/PaySTAR/images/wallethdpi.png","paymentmode_code":"Paytm","is_public":"1","status":"1"},{"id":"15","financier_id":"2","Payment_mode":"CHEQUE","paymentmode_icon":"https://anyemi.com/PaySTAR/images/cheque.png","paymentmode_code":"CQ","is_public":"1","status":"1"}]
         */

        private String id;
        private String icon;
        private String block;
        private String is_public;
        private String tpe;
        private String category;
        private List<PaymentModeBean> payment_mode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getBlock() {
            return block;
        }

        public void setBlock(String block) {
            this.block = block;
        }

        public String getIs_public() {
            return is_public;
        }

        public void setIs_public(String is_public) {
            this.is_public = is_public;
        }

        public String getTpe() {
            return tpe;
        }

        public void setTpe(String tpe) {
            this.tpe = tpe;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public List<PaymentModeBean> getPayment_mode() {
            return payment_mode;
        }

        public void setPayment_mode(List<PaymentModeBean> payment_mode) {
            this.payment_mode = payment_mode;
        }

        public static class PaymentModeBean {
            /**
             * id : 9
             * financier_id : 2
             * Payment_mode : UPI
             * paymentmode_icon : https://anyemi.com/PaySTAR/images/sbiupi.jpeg
             * paymentmode_code : SBIUPI
             * is_public : 1
             * status : 1
             */

            private String id;
            private String financier_id;
            private String Payment_mode;
            private String paymentmode_icon;
            private String paymentmode_code;
            private String is_public;
            private String status;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getFinancier_id() {
                return financier_id;
            }

            public void setFinancier_id(String financier_id) {
                this.financier_id = financier_id;
            }

            public String getPayment_mode() {
                return Payment_mode;
            }

            public void setPayment_mode(String Payment_mode) {
                this.Payment_mode = Payment_mode;
            }

            public String getPaymentmode_icon() {
                return paymentmode_icon;
            }

            public void setPaymentmode_icon(String paymentmode_icon) {
                this.paymentmode_icon = paymentmode_icon;
            }

            public String getPaymentmode_code() {
                return paymentmode_code;
            }

            public void setPaymentmode_code(String paymentmode_code) {
                this.paymentmode_code = paymentmode_code;
            }

            public String getIs_public() {
                return is_public;
            }

            public void setIs_public(String is_public) {
                this.is_public = is_public;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }

    public static class TaxArrayBean {
        /**
         * id : 53
         * Biller_id : Broadband Postpaid
         * transaction_amount : 0
         * from : 0
         * to : 1000
         * credit_service_tax_ : 1.75
         * gst_debit : 18
         * gst_credit : 18
         * debit_service_tax_ : 0
         * extra_tax : 5
         */

        private String id;
        private String Biller_id;
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

        public String getBiller_id() {
            return Biller_id;
        }

        public void setBiller_id(String Biller_id) {
            this.Biller_id = Biller_id;
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
