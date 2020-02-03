package com.anyemi.recska.model;

/**
 * Created by SuryaTejaChalla on 27-12-2017.
 */

public class EztapPaymentResponseModel {


    /**
     * error :
     * status : success
     * result : {"txn":{"txnId":"171227095425698E020069170","txnDate":"1514368472000","amount":"20","amountOriginal":"20","amountCashBack":"0","amountAdditional":"0","currencyCode":"INR","paymentMode":"CARD","authCode":"D65954","deviceSerial":"6A685422","mid":"112112121236789","tid":"11314300","emiId":"","signReqd":"false","status":"AUTHORIZED","txnType":"CHARGE","settlementStatus":"PENDING","postingDate":"1514368472000","rrNumber":"RRCD084068E2C7","invoiceNumber":"11","pgInvoiceNumber":"11","batchNumber":"4"},"merchant":{"merchantName":"Any EMI","merchantCode":"ANY_EMI_62641947"},"customer":{"email":"","mobileNo":"9642444934","name":"VALUED CUSTOMER          /"},"receipt":{"receiptDate":"2017-12-27T15:24:32+0530","receiptUrl":"http://d.eze.cc/r/o/z6wq9NJn"},"cardDetails":{"maskedCardNo":"4280-90XX-XXXX-2838","cardBrand":"VISA","cardType":"DEBIT","cardIssuer":"NONE"},"references":{"reference1":"123456789"}}
     */

    private String error;
    private String status;
    private ResultBean result;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * txn : {"txnId":"171227095425698E020069170","txnDate":"1514368472000","amount":"20","amountOriginal":"20","amountCashBack":"0","amountAdditional":"0","currencyCode":"INR","paymentMode":"CARD","authCode":"D65954","deviceSerial":"6A685422","mid":"112112121236789","tid":"11314300","emiId":"","signReqd":"false","status":"AUTHORIZED","txnType":"CHARGE","settlementStatus":"PENDING","postingDate":"1514368472000","rrNumber":"RRCD084068E2C7","invoiceNumber":"11","pgInvoiceNumber":"11","batchNumber":"4"}
         * merchant : {"merchantName":"Any EMI","merchantCode":"ANY_EMI_62641947"}
         * customer : {"email":"","mobileNo":"9642444934","name":"VALUED CUSTOMER          /"}
         * receipt : {"receiptDate":"2017-12-27T15:24:32+0530","receiptUrl":"http://d.eze.cc/r/o/z6wq9NJn"}
         * cardDetails : {"maskedCardNo":"4280-90XX-XXXX-2838","cardBrand":"VISA","cardType":"DEBIT","cardIssuer":"NONE"}
         * references : {"reference1":"123456789"}
         */

        private TxnBean txn;
        private MerchantBean merchant;
        private CustomerBean customer;
        private ReceiptBean receipt;
        private CardDetailsBean cardDetails;
        private ReferencesBean references;

        public TxnBean getTxn() {
            return txn;
        }

        public void setTxn(TxnBean txn) {
            this.txn = txn;
        }

        public MerchantBean getMerchant() {
            return merchant;
        }

        public void setMerchant(MerchantBean merchant) {
            this.merchant = merchant;
        }

        public CustomerBean getCustomer() {
            return customer;
        }

        public void setCustomer(CustomerBean customer) {
            this.customer = customer;
        }

        public ReceiptBean getReceipt() {
            return receipt;
        }

        public void setReceipt(ReceiptBean receipt) {
            this.receipt = receipt;
        }

        public CardDetailsBean getCardDetails() {
            return cardDetails;
        }

        public void setCardDetails(CardDetailsBean cardDetails) {
            this.cardDetails = cardDetails;
        }

        public ReferencesBean getReferences() {
            return references;
        }

        public void setReferences(ReferencesBean references) {
            this.references = references;
        }

        public static class TxnBean {
            /**
             * txnId : 171227095425698E020069170
             * txnDate : 1514368472000
             * amount : 20
             * amountOriginal : 20
             * amountCashBack : 0
             * amountAdditional : 0
             * currencyCode : INR
             * paymentMode : CARD
             * authCode : D65954
             * deviceSerial : 6A685422
             * mid : 112112121236789
             * tid : 11314300
             * emiId :
             * signReqd : false
             * status : AUTHORIZED
             * txnType : CHARGE
             * settlementStatus : PENDING
             * postingDate : 1514368472000
             * rrNumber : RRCD084068E2C7
             * invoiceNumber : 11
             * pgInvoiceNumber : 11
             * batchNumber : 4
             */

            private String txnId;
            private String txnDate;
            private String amount;
            private String amountOriginal;
            private String amountCashBack;
            private String amountAdditional;
            private String currencyCode;
            private String paymentMode;
            private String authCode;
            private String deviceSerial;
            private String mid;
            private String tid;
            private String emiId;
            private String signReqd;
            private String status;
            private String txnType;
            private String settlementStatus;
            private String postingDate;
            private String rrNumber;
            private String invoiceNumber;
            private String pgInvoiceNumber;
            private String batchNumber;

            public String getTxnId() {
                return txnId;
            }

            public void setTxnId(String txnId) {
                this.txnId = txnId;
            }

            public String getTxnDate() {
                return txnDate;
            }

            public void setTxnDate(String txnDate) {
                this.txnDate = txnDate;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getAmountOriginal() {
                return amountOriginal;
            }

            public void setAmountOriginal(String amountOriginal) {
                this.amountOriginal = amountOriginal;
            }

            public String getAmountCashBack() {
                return amountCashBack;
            }

            public void setAmountCashBack(String amountCashBack) {
                this.amountCashBack = amountCashBack;
            }

            public String getAmountAdditional() {
                return amountAdditional;
            }

            public void setAmountAdditional(String amountAdditional) {
                this.amountAdditional = amountAdditional;
            }

            public String getCurrencyCode() {
                return currencyCode;
            }

            public void setCurrencyCode(String currencyCode) {
                this.currencyCode = currencyCode;
            }

            public String getPaymentMode() {
                return paymentMode;
            }

            public void setPaymentMode(String paymentMode) {
                this.paymentMode = paymentMode;
            }

            public String getAuthCode() {
                return authCode;
            }

            public void setAuthCode(String authCode) {
                this.authCode = authCode;
            }

            public String getDeviceSerial() {
                return deviceSerial;
            }

            public void setDeviceSerial(String deviceSerial) {
                this.deviceSerial = deviceSerial;
            }

            public String getMid() {
                return mid;
            }

            public void setMid(String mid) {
                this.mid = mid;
            }

            public String getTid() {
                return tid;
            }

            public void setTid(String tid) {
                this.tid = tid;
            }

            public String getEmiId() {
                return emiId;
            }

            public void setEmiId(String emiId) {
                this.emiId = emiId;
            }

            public String getSignReqd() {
                return signReqd;
            }

            public void setSignReqd(String signReqd) {
                this.signReqd = signReqd;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getTxnType() {
                return txnType;
            }

            public void setTxnType(String txnType) {
                this.txnType = txnType;
            }

            public String getSettlementStatus() {
                return settlementStatus;
            }

            public void setSettlementStatus(String settlementStatus) {
                this.settlementStatus = settlementStatus;
            }

            public String getPostingDate() {
                return postingDate;
            }

            public void setPostingDate(String postingDate) {
                this.postingDate = postingDate;
            }

            public String getRrNumber() {
                return rrNumber;
            }

            public void setRrNumber(String rrNumber) {
                this.rrNumber = rrNumber;
            }

            public String getInvoiceNumber() {
                return invoiceNumber;
            }

            public void setInvoiceNumber(String invoiceNumber) {
                this.invoiceNumber = invoiceNumber;
            }

            public String getPgInvoiceNumber() {
                return pgInvoiceNumber;
            }

            public void setPgInvoiceNumber(String pgInvoiceNumber) {
                this.pgInvoiceNumber = pgInvoiceNumber;
            }

            public String getBatchNumber() {
                return batchNumber;
            }

            public void setBatchNumber(String batchNumber) {
                this.batchNumber = batchNumber;
            }
        }

        public static class MerchantBean {
            /**
             * merchantName : Any EMI
             * merchantCode : ANY_EMI_62641947
             */

            private String merchantName;
            private String merchantCode;

            public String getMerchantName() {
                return merchantName;
            }

            public void setMerchantName(String merchantName) {
                this.merchantName = merchantName;
            }

            public String getMerchantCode() {
                return merchantCode;
            }

            public void setMerchantCode(String merchantCode) {
                this.merchantCode = merchantCode;
            }
        }

        public static class CustomerBean {
            /**
             * email :
             * mobileNo : 9642444934
             * name : VALUED CUSTOMER          /
             */

            private String email;
            private String mobileNo;
            private String name;

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getMobileNo() {
                return mobileNo;
            }

            public void setMobileNo(String mobileNo) {
                this.mobileNo = mobileNo;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class ReceiptBean {
            /**
             * receiptDate : 2017-12-27T15:24:32+0530
             * receiptUrl : http://d.eze.cc/r/o/z6wq9NJn
             */

            private String receiptDate;
            private String receiptUrl;

            public String getReceiptDate() {
                return receiptDate;
            }

            public void setReceiptDate(String receiptDate) {
                this.receiptDate = receiptDate;
            }

            public String getReceiptUrl() {
                return receiptUrl;
            }

            public void setReceiptUrl(String receiptUrl) {
                this.receiptUrl = receiptUrl;
            }
        }

        public static class CardDetailsBean {
            /**
             * maskedCardNo : 4280-90XX-XXXX-2838
             * cardBrand : VISA
             * cardType : DEBIT
             * cardIssuer : NONE
             */

            private String maskedCardNo;
            private String cardBrand;
            private String cardType;
            private String cardIssuer;

            public String getMaskedCardNo() {
                return maskedCardNo;
            }

            public void setMaskedCardNo(String maskedCardNo) {
                this.maskedCardNo = maskedCardNo;
            }

            public String getCardBrand() {
                return cardBrand;
            }

            public void setCardBrand(String cardBrand) {
                this.cardBrand = cardBrand;
            }

            public String getCardType() {
                return cardType;
            }

            public void setCardType(String cardType) {
                this.cardType = cardType;
            }

            public String getCardIssuer() {
                return cardIssuer;
            }

            public void setCardIssuer(String cardIssuer) {
                this.cardIssuer = cardIssuer;
            }
        }

        public static class ReferencesBean {
            /**
             * reference1 : 123456789
             */

            private String reference1;

            public String getReference1() {
                return reference1;
            }

            public void setReference1(String reference1) {
                this.reference1 = reference1;
            }
        }
    }
}
