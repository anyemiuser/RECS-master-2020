package com.anyemi.recska.model;

/**
 * Created by SuryaTejaChalla on 02-03-2018.
 */

public class CheckValidResponseModel {


    /**
     * requestInfo : {"pspId":"89","pspRefNo":"952996"}
     * payeeType : {"virtualAddress":"anyemi5688@sbi","name":"FIFA WORLD CUP U17"}
     * status : VE
     * statusDesc : VPA is available for transaction
     * errCode : UPVA
     * service_list_id : 55
     */

    private RequestInfoBean requestInfo;
    private PayeeTypeBean payeeType;
    private String status;
    private String statusDesc;
    private String errCode;
    private int service_list_id;

    public RequestInfoBean getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfoBean requestInfo) {
        this.requestInfo = requestInfo;
    }

    public PayeeTypeBean getPayeeType() {
        return payeeType;
    }

    public void setPayeeType(PayeeTypeBean payeeType) {
        this.payeeType = payeeType;
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

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public int getService_list_id() {
        return service_list_id;
    }

    public void setService_list_id(int service_list_id) {
        this.service_list_id = service_list_id;
    }

    public static class RequestInfoBean {
        /**
         * pspId : 89
         * pspRefNo : 952996
         */

        private String pspId;
        private String pspRefNo;

        public String getPspId() {
            return pspId;
        }

        public void setPspId(String pspId) {
            this.pspId = pspId;
        }

        public String getPspRefNo() {
            return pspRefNo;
        }

        public void setPspRefNo(String pspRefNo) {
            this.pspRefNo = pspRefNo;
        }
    }

    public static class PayeeTypeBean {
        /**
         * virtualAddress : anyemi5688@sbi
         * name : FIFA WORLD CUP U17
         */

        private String virtualAddress;
        private String name;

        public String getVirtualAddress() {
            return virtualAddress;
        }

        public void setVirtualAddress(String virtualAddress) {
            this.virtualAddress = virtualAddress;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
