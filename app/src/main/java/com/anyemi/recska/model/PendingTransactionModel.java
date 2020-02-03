package com.anyemi.recska.model;

import java.util.List;

public class PendingTransactionModel {


    /**
     * status : sucess
     * transactions : [{"id":"6","payment_id":"226","payment_response":"{\"apiResp\":{\"pspRefNo\":\"04030918000226\",\"upiTransRefNo\":3026421755,\"npciTransId\":\"SBIDA7E95BDF80A48E4AD1169D969FA9EA2\",\"custRefNo\":\"824622682117\",\"amount\":\"5.00\",\"txnAuthDate\":\"2018-09-03 10:09:58 PM\",\"status\":\"P\",\"statusDesc\":\"Collect Approval waiting from Payer\",\"addInfo\":{\"addInfo1\":\"226\",\"addInfo2\":\"1028981946\"},\"payerVPA\":\"9642444934@ybl\",\"payeeVPA\":\"paystar@sbi\",\"responseMsg\":\"\",\"reason_desc\":\"\",\"txn_type\":\"COLLECT\",\"ref_url\":\"https://www.sbi.co.in\",\"errCode\":\"UPTP\"}}","payment_request":"{\"bankname\":\"\",\"checkdate\":\"\",\"checkno\":0,\"emi_ids\":\"662\",\"mobile_number\":\"9645456464\",\"payment_type\":\"anna_canteen\",\"remarks\":\"ANNA_CANTEEN\",\"rr_number\":\"\",\"total_amount\":\"5\",\"upi_id\":\"9642444934@ybl\",\"user_id\":662}"},{"id":"5","payment_id":"225","payment_response":null,"payment_request":"{\"bankname\":\"\",\"checkdate\":\"\",\"checkno\":0,\"emi_ids\":\"662\",\"mobile_number\":\"9642444934\",\"payment_type\":\"anna_canteen\",\"remarks\":\"ANNA_CANTEEN\",\"rr_number\":\"824621597326\",\"serviceCharge\":\"0\",\"total_amount\":\"5\",\"trsno\":\"824621597326\",\"upi_id\":\"9912208840@upi\",\"user_id\":662}"},{"id":"4","payment_id":"0","payment_response":"{\"apiResp\":{\"pspRefNo\":\"04030918000224\",\"upiTransRefNo\":3025637952,\"npciTransId\":\"SBI6A6CD74504D044468CC80B0D9D124112\",\"custRefNo\":\"824621622205\",\"amount\":\"5.00\",\"txnAuthDate\":\"2018-09-03 09:19:27 PM\",\"status\":\"X\",\"statusDesc\":\"Collect request is expired\",\"addInfo\":{\"addInfo1\":\"224\",\"addInfo2\":\"1028675260\"},\"payerVPA\":\"9642444934@ybl\",\"payeeVPA\":\"paystar@sbi\",\"responseMsg\":\"Collect Expired\",\"reason_desc\":\"Collect Expired\",\"txn_type\":\"COLLECT\",\"ref_url\":\"https://www.sbi.co.in\",\"errCode\":\"U69\"}}","payment_request":"{\"bankname\":\"\",\"checkdate\":\"\",\"checkno\":0,\"emi_ids\":\"662\",\"mobile_number\":\"9938282828\",\"payment_type\":\"anna_canteen\",\"remarks\":\"ANNA_CANTEEN\",\"rr_number\":\"\",\"total_amount\":\"5\",\"upi_id\":\"9642444934@ybl\",\"user_id\":662}"},{"id":"3","payment_id":"0","payment_response":"{\"apiResp\":{\"pspRefNo\":\"04030918000223\",\"upiTransRefNo\":3025551484,\"npciTransId\":\"SBI2758233CBD5443ACB62EA37FCBAACC05\",\"custRefNo\":\"824621615725\",\"amount\":\"5.00\",\"txnAuthDate\":\"2018-09-03 09:14:33 PM\",\"responseCode\":\"00\",\"approvalNumber\":\"220228\",\"status\":\"S\",\"statusDesc\":\"Payment Successful\",\"addInfo\":{\"addInfo1\":\"223\",\"addInfo2\":\"1028642222\"},\"payerVPA\":\"9912208840@upi\",\"payeeVPA\":\"paystar@sbi\",\"responseMsg\":\"\",\"reason_desc\":\"\",\"txn_type\":\"COLLECT\",\"ref_url\":\"https://www.sbi.co.in\",\"errCode\":\"UP00\",\"payerRespCode\":\"00\"}}","payment_request":"{\"bankname\":\"\",\"checkdate\":\"\",\"checkno\":0,\"emi_ids\":\"662\",\"mobile_number\":\"9653828282\",\"payment_type\":\"anna_canteen\",\"remarks\":\"ANNA_CANTEEN\",\"rr_number\":\"\",\"total_amount\":\"5\",\"upi_id\":\"9912208840@upi\",\"user_id\":662}"},{"id":"2","payment_id":"0","payment_response":"{\"apiResp\":{\"pspRefNo\":\"04030918000222\",\"upiTransRefNo\":3025320227,\"npciTransId\":\"SBI4D099C57965746F38EE4304DB00D7F7D\",\"custRefNo\":\"824621597326\",\"amount\":\"5.00\",\"txnAuthDate\":\"2018-09-03 09:01:36 PM\",\"responseCode\":\"00\",\"approvalNumber\":\"730772\",\"status\":\"S\",\"statusDesc\":\"Payment Successful\",\"addInfo\":{\"addInfo1\":\"222\",\"addInfo2\":\"1028549474\"},\"payerVPA\":\"9912208840@upi\",\"payeeVPA\":\"paystar@sbi\",\"responseMsg\":\"\",\"reason_desc\":\"\",\"txn_type\":\"COLLECT\",\"ref_url\":\"https://www.sbi.co.in\",\"errCode\":\"UP00\",\"payerRespCode\":\"00\"}}","payment_request":"{\"bankname\":\"\",\"checkdate\":\"\",\"checkno\":0,\"emi_ids\":\"662\",\"mobile_number\":\"9642444934\",\"payment_type\":\"anna_canteen\",\"remarks\":\"ANNA_CANTEEN\",\"rr_number\":\"\",\"total_amount\":\"5\",\"upi_id\":\"9912208840@upi\",\"user_id\":662}"},{"id":"1","payment_id":"0","payment_response":"{\"apiResp\":{\"pspRefNo\":\"04030918000221\",\"upiTransRefNo\":3025279611,\"npciTransId\":\"SBI88A06239FFB6479D8146C7BBE96E002A\",\"custRefNo\":\"824620593918\",\"amount\":\"5.00\",\"txnAuthDate\":\"2018-09-03 08:59:29 PM\",\"responseCode\":\"ZA\",\"status\":\"R\",\"statusDesc\":\"Collect Request rejected by customer\",\"addInfo\":{\"addInfo1\":\"221\",\"addInfo2\":\"1028533674\"},\"payerVPA\":\"9642444934@ybl\",\"payeeVPA\":\"paystar@sbi\",\"responseMsg\":\"\",\"reason_desc\":\"\",\"txn_type\":\"COLLECT\",\"ref_url\":\"https://www.sbi.co.in\",\"errCode\":\"U19\",\"payerRespCode\":\"ZA\"}}","payment_request":"{\"bankname\":\"\",\"checkdate\":\"\",\"checkno\":0,\"emi_ids\":\"662\",\"mobile_number\":\"9642444934\",\"payment_type\":\"anna_canteen\",\"remarks\":\"ANNA_CANTEEN\",\"rr_number\":\"\",\"total_amount\":\"5\",\"upi_id\":\"9642444934@ybl\",\"user_id\":662}"}]
     */

    private String status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;
    private List<TransactionsBean> transactions;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TransactionsBean> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionsBean> transactions) {
        this.transactions = transactions;
    }

    public static class TransactionsBean {
        /**
         * id : 6
         * payment_id : 226
         * payment_response : {"apiResp":{"pspRefNo":"04030918000226","upiTransRefNo":3026421755,"npciTransId":"SBIDA7E95BDF80A48E4AD1169D969FA9EA2","custRefNo":"824622682117","amount":"5.00","txnAuthDate":"2018-09-03 10:09:58 PM","status":"P","statusDesc":"Collect Approval waiting from Payer","addInfo":{"addInfo1":"226","addInfo2":"1028981946"},"payerVPA":"9642444934@ybl","payeeVPA":"paystar@sbi","responseMsg":"","reason_desc":"","txn_type":"COLLECT","ref_url":"https://www.sbi.co.in","errCode":"UPTP"}}
         * payment_request : {"bankname":"","checkdate":"","checkno":0,"emi_ids":"662","mobile_number":"9645456464","payment_type":"anna_canteen","remarks":"ANNA_CANTEEN","rr_number":"","total_amount":"5","upi_id":"9642444934@ybl","user_id":662}
         */

        private String id;
        private String payment_id;
        private String payment_response;
        private String payment_request;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPayment_id() {
            return payment_id;
        }

        public void setPayment_id(String payment_id) {
            this.payment_id = payment_id;
        }

        public String getPayment_response() {
            return payment_response;
        }

        public void setPayment_response(String payment_response) {
            this.payment_response = payment_response;
        }

        public String getPayment_request() {
            return payment_request;
        }

        public void setPayment_request(String payment_request) {
            this.payment_request = payment_request;
        }
    }
}
