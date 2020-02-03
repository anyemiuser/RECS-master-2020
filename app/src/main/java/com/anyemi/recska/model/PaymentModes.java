package com.anyemi.recska.model;

import java.util.List;

public class PaymentModes {


    private List<PaymentModesBean> payment_modes;

    public List<PaymentModesBean> getPayment_modes() {
        return payment_modes;
    }

    public void setPayment_modes(List<PaymentModesBean> payment_modes) {
        this.payment_modes = payment_modes;
    }

    public static class PaymentModesBean {
        /**
         * id : 1
         * financier_id : 1
         * payment_mode : UPI
         * paymentmde_icon : https://anyemi.com/PaySTAR/images/sbiupi.jpeg
         * paymentcode_code : SBIUPI
         * is_public : 1
         * status : 1
         */

        private String id;
        private String financier_id;
        private String payment_mode;
        private String paymentmde_icon;
        private String paymentcode_code;
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
            return payment_mode;
        }

        public void setPayment_mode(String payment_mode) {
            this.payment_mode = payment_mode;
        }

        public String getPaymentmde_icon() {
            return paymentmde_icon;
        }

        public void setPaymentmde_icon(String paymentmde_icon) {
            this.paymentmde_icon = paymentmde_icon;
        }

        public String getPaymentcode_code() {
            return paymentcode_code;
        }

        public void setPaymentcode_code(String paymentcode_code) {
            this.paymentcode_code = paymentcode_code;
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
