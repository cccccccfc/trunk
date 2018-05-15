package com.base.project.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/22.
 */

public class SetpushDate {

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ListBean> list;

    public static class ListBean {
        /**
         * me_di_clusterID : 1
         * me_di_db_sbID : 10015
         * me_mh_type : 6
         * mg_id : 7032
         * me_time : 10
         */

        public int me_di_clusterID;
        public int me_di_db_sbID;

        public int getMe_di_clusterID() {
            return me_di_clusterID;
        }

        public void setMe_di_clusterID(int me_di_clusterID) {
            this.me_di_clusterID = me_di_clusterID;
        }

        public int getMe_di_db_sbID() {
            return me_di_db_sbID;
        }

        public void setMe_di_db_sbID(int me_di_db_sbID) {
            this.me_di_db_sbID = me_di_db_sbID;
        }

        public int getMe_mh_type() {
            return me_mh_type;
        }

        public void setMe_mh_type(int me_mh_type) {
            this.me_mh_type = me_mh_type;
        }

        public int getMg_id() {
            return mg_id;
        }

        public void setMg_id(int mg_id) {
            this.mg_id = mg_id;
        }

        public int getMe_time() {
            return me_time;
        }

        public void setMe_time(int me_time) {
            this.me_time = me_time;
        }

        public int me_mh_type;
        public int mg_id;
        public int me_time;
    }
}
