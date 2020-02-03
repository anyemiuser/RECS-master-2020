package com.anyemi.recska.model;

public class SbiPayPendingModel {


    /**
     * apiResp : {"pspRefNo":"04310818000161","upiTransRefNo":2963513071,"npciTransId":"SBI8C173C80F43A4DEFACE7073A8CACF07C","custRefNo":"824312582490","amount":"250.00","txnAuthDate":"2018-08-31 12:01:10 PM","status":"S","statusDesc":"Collect request initiated successfully.","addInfo":{"addInfo1":"161"},"payerVPA":"9642444934@ybl","payeeVPA":"paystar@sbi","txn_type":"COLLECT","ref_url":"https://www.sbi.co.in","errCode":"UP00","txn_note":"Collect from 9642444934@ybl"}
     */

    private ApiRespBean apiResp;

    public ApiRespBean getApiResp() {
        return apiResp;
    }

    public void setApiResp(ApiRespBean apiResp) {
        this.apiResp = apiResp;
    }

    public static class ApiRespBean {
        /**
         * pspRefNo : 04310818000161
         * upiTransRefNo : 2963513071
         * npciTransId : SBI8C173C80F43A4DEFACE7073A8CACF07C
         * custRefNo : 824312582490
         * amount : 250.00
         * txnAuthDate : 2018-08-31 12:01:10 PM
         * status : S
         * statusDesc : Collect request initiated successfully.
         * addInfo : {"addInfo1":"161"}
         * payerVPA : 9642444934@ybl
         * payeeVPA : paystar@sbi
         * txn_type : COLLECT
         * ref_url : https://www.sbi.co.in
         * errCode : UP00
         * txn_note : Collect from 9642444934@ybl
         */

        private String pspRefNo;
        private long upiTransRefNo;
        private String npciTransId;
        private String custRefNo;
        private String amount;
        private String txnAuthDate;
        private String status;
        private String statusDesc;
        private AddInfoBean addInfo;
        private String payerVPA;
        private String payeeVPA;
        private String txn_type;
        private String ref_url;
        private String errCode;
        private String txn_note;

        public String getPspRefNo() {
            return pspRefNo;
        }

        public void setPspRefNo(String pspRefNo) {
            this.pspRefNo = pspRefNo;
        }

        public long getUpiTransRefNo() {
            return upiTransRefNo;
        }

        public void setUpiTransRefNo(long upiTransRefNo) {
            this.upiTransRefNo = upiTransRefNo;
        }

        public String getNpciTransId() {
            return npciTransId;
        }

        public void setNpciTransId(String npciTransId) {
            this.npciTransId = npciTransId;
        }

        public String getCustRefNo() {
            return custRefNo;
        }

        public void setCustRefNo(String custRefNo) {
            this.custRefNo = custRefNo;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTxnAuthDate() {
            return txnAuthDate;
        }

        public void setTxnAuthDate(String txnAuthDate) {
            this.txnAuthDate = txnAuthDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatusDesc() {
            return statusDesc;
        }

        public void setStatusDesc(String statusDesc) {
            this.statusDesc = statusDesc;
        }

        public AddInfoBean getAddInfo() {
            return addInfo;
        }

        public void setAddInfo(AddInfoBean addInfo) {
            this.addInfo = addInfo;
        }

        public String getPayerVPA() {
            return payerVPA;
        }

        public void setPayerVPA(String payerVPA) {
            this.payerVPA = payerVPA;
        }

        public String getPayeeVPA() {
            return payeeVPA;
        }

        public void setPayeeVPA(String payeeVPA) {
            this.payeeVPA = payeeVPA;
        }

        public String getTxn_type() {
            return txn_type;
        }

        public void setTxn_type(String txn_type) {
            this.txn_type = txn_type;
        }

        public String getRef_url() {
            return ref_url;
        }

        public void setRef_url(String ref_url) {
            this.ref_url = ref_url;
        }

        public String getErrCode() {
            return errCode;
        }

        public void setErrCode(String errCode) {
            this.errCode = errCode;
        }

        public String getTxn_note() {
            return txn_note;
        }

        public void setTxn_note(String txn_note) {
            this.txn_note = txn_note;
        }

        public static class AddInfoBean {
            /**
             * addInfo1 : 161
             */

            private String addInfo1;

            public String getAddInfo1() {
                return addInfo1;
            }

            public void setAddInfo1(String addInfo1) {
                this.addInfo1 = addInfo1;
            }
        }
    }
}
