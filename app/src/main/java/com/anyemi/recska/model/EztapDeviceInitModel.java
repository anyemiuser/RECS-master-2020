package com.anyemi.recska.model;

/**
 * Created by SuryaTejaChalla on 27-12-2017.
 */

public class EztapDeviceInitModel {


    /**
     * error :
     * status : success
     * result : {"message":"Device instantiated!"}
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
         * message : Device instantiated!
         */

        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
