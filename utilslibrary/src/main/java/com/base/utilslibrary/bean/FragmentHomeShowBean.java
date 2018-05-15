package com.base.utilslibrary.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/3.
 */

public class FragmentHomeShowBean {

    public boolean isChangestate() {
        return changestate;
    }

    public void setChangestate(boolean changestate) {
        this.changestate = changestate;
    }
    public boolean changestate = true;

    /**
     * dd_macaddr : 38FFD2053058
     * dd_zjname : 灯
     * di_id : 16
     * di_db_sbID : 10010
     * di_clusterID : 8
     * di_clustername : 灯8
     * di_status : 1
     * db_dt_id : 5
     */

    public String dd_macaddr;
    public String dd_zjname;
    public int di_id;

    public int getDd_ui_id() {
        return dd_ui_id;
    }

    public void setDd_ui_id(int dd_ui_id) {
        this.dd_ui_id = dd_ui_id;
    }

    public int dd_ui_id;
    public String getDd_macaddr() {
        return dd_macaddr;
    }

    public void setDd_macaddr(String dd_macaddr) {
        this.dd_macaddr = dd_macaddr;
    }

    public String getDd_zjname() {
        return dd_zjname;
    }

    public void setDd_zjname(String dd_zjname) {
        this.dd_zjname = dd_zjname;
    }

    public int getDi_id() {
        return di_id;
    }

    public void setDi_id(int di_id) {
        this.di_id = di_id;
    }

    public int getDi_db_sbID() {
        return di_db_sbID;
    }

    public void setDi_db_sbID(int di_db_sbID) {
        this.di_db_sbID = di_db_sbID;
    }

    public int getDi_clusterID() {
        return di_clusterID;
    }

    public void setDi_clusterID(int di_clusterID) {
        this.di_clusterID = di_clusterID;
    }

    public String getDi_clustername() {
        return di_clustername;
    }

    public void setDi_clustername(String di_clustername) {
        this.di_clustername = di_clustername;
    }

    public int getDb_dt_id() {
        return db_dt_id;
    }

    public void setDb_dt_id(int db_dt_id) {
        this.db_dt_id = db_dt_id;
    }

    public int di_db_sbID;
    public int di_clusterID;
    public String di_clustername;
    public int di_status;
    public int db_dt_id;



    public int getDi_status() {
        return di_status;
    }

    public void setDi_status(int di_status) {
        this.di_status = di_status;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ListBean> list;

    public static class ListBean {
        /**
         * db_dt_id : 73
         * dd_db_sbID : 10000
         * dd_macaddr : 38FFD2053058
         * dd_ui_id : 5
         * di_clusterID : 1
         * di_clustername : 门锁1
         * di_id : 138
         * di_status : 0
         */

        public int db_dt_id;

        public int getDb_dt_id() {
            return db_dt_id;
        }

        public void setDb_dt_id(int db_dt_id) {
            this.db_dt_id = db_dt_id;
        }

        public int getDd_db_sbID() {
            return dd_db_sbID;
        }

        public void setDd_db_sbID(int dd_db_sbID) {
            this.dd_db_sbID = dd_db_sbID;
        }

        public String getDd_macaddr() {
            return dd_macaddr;
        }

        public void setDd_macaddr(String dd_macaddr) {
            this.dd_macaddr = dd_macaddr;
        }

        public int getDd_ui_id() {
            return dd_ui_id;
        }

        public void setDd_ui_id(int dd_ui_id) {
            this.dd_ui_id = dd_ui_id;
        }

        public int getDi_clusterID() {
            return di_clusterID;
        }

        public void setDi_clusterID(int di_clusterID) {
            this.di_clusterID = di_clusterID;
        }

        public String getDi_clustername() {
            return di_clustername;
        }

        public void setDi_clustername(String di_clustername) {
            this.di_clustername = di_clustername;
        }

        public int getDi_id() {
            return di_id;
        }

        public void setDi_id(int di_id) {
            this.di_id = di_id;
        }

        public int getDi_status() {
            return di_status;
        }
        public boolean isChangestate() {
            return changestate;
        }

        public void setChangestate(boolean changestate) {
            this.changestate = changestate;
        }
        public boolean changestate = true;

        public void setDi_status(int di_status) {
            this.di_status = di_status;
        }
        public String getDi_sw() {
            return di_sw;
        }

        public void setDi_sw(String di_sw) {
            this.di_sw = di_sw;
        }

        public String di_sw = "0";

        public int dd_db_sbID;
        public String dd_macaddr;
        public int dd_ui_id;
        public int di_clusterID;
        public String di_clustername;
        public int di_id;
        public int di_status;
    }
}
