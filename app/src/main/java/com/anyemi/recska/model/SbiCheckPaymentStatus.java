package com.anyemi.recska.model;

public class SbiCheckPaymentStatus {


    /**
     * apiResp : {"pspRefNo":"346015","upiTransRefNo":2103211294,"npciTransId":"SBI22B63C913F9949CAB325BC0AE1F3FC45","custRefNo":"817210364808","amount":"73.00","txnAuthDate":"2018-06-21 10:08:56 AM","status":"P","statusDesc":"Collect Approval waiting from Payer","addInfo":{"addInfo2":"700102549"},"payerVPA":"9642444934@ybl","payeeVPA":"anyemi@sbi","responseMsg":"","reason_desc":"","txn_type":"COLLECT","ref_url":"https://www.sbi.co.in","errCode":"UPTP"}
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
         * pspRefNo : 346015
         * upiTransRefNo : 2103211294
         * npciTransId : SBI22B63C913F9949CAB325BC0AE1F3FC45
         * custRefNo : 817210364808
         * amount : 73.00
         * txnAuthDate : 2018-06-21 10:08:56 AM
         * status : P
         * statusDesc : Collect Approval waiting from Payer
         * addInfo : {"addInfo2":"700102549"}
         * payerVPA : 9642444934@ybl
         * payeeVPA : anyemi@sbi
         * responseMsg :
         * reason_desc :
         * txn_type : COLLECT
         * ref_url : https://www.sbi.co.in
         * errCode : UPTP
         */

        private String pspRefNo;
        private int upiTransRefNo;
        private String npciTransId;
        private String custRefNo;
        private String amount;
        private String txnAuthDate;
        private String status;
        private String statusDesc;
        private AddInfoBean addInfo;
        private String payerVPA;
        private String payeeVPA;
        private String responseMsg;
        private String reason_desc;
        private String txn_type;
        private String ref_url;
        private String errCode;

        public String getPspRefNo() {
            return pspRefNo;
        }

        public void setPspRefNo(String pspRefNo) {
            this.pspRefNo = pspRefNo;
        }

        public int getUpiTransRefNo() {
            return upiTransRefNo;
        }

        public void setUpiTransRefNo(int upiTransRefNo) {
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

        public String getResponseMsg() {
            return responseMsg;
        }

        public void setResponseMsg(String responseMsg) {
            this.responseMsg = responseMsg;
        }

        public String getReason_desc() {
            return reason_desc;
        }

        public void setReason_desc(String reason_desc) {
            this.reason_desc = reason_desc;
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

        public static class AddInfoBean {
            /**
             * addInfo2 : 700102549
             */

            private String addInfo2;

            public String getAddInfo2() {
                return addInfo2;
            }

            public void setAddInfo2(String addInfo2) {
                this.addInfo2 = addInfo2;
            }
        }
    }
}
