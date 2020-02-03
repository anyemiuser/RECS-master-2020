package com.anyemi.recska.model;

import java.util.List;

public class AreaModel {


    private List<AREAMASTERBean> AREA_MASTER;

    public List<AREAMASTERBean> getAREA_MASTER() {
        return AREA_MASTER;
    }

    public void setAREA_MASTER(List<AREAMASTERBean> AREA_MASTER) {
        this.AREA_MASTER = AREA_MASTER;
    }

    public static class AREAMASTERBean {
        /**
         * AREACODE : 10115
         * AREANAME : VENKUPALEM (S)
         * EROCD : 1
         */

        private String AREACODE;
        private String AREANAME;
        private String EROCD;

        public String getAREACODE() {
            return AREACODE;
        }

        public void setAREACODE(String AREACODE) {
            this.AREACODE = AREACODE;
        }

        public String getAREANAME() {
            return AREANAME;
        }

        public void setAREANAME(String AREANAME) {
            this.AREANAME = AREANAME;
        }

        public String getEROCD() {
            return EROCD;
        }

        public void setEROCD(String EROCD) {
            this.EROCD = EROCD;
        }
    }
}
