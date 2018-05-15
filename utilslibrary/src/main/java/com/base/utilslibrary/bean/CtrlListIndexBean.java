package com.base.utilslibrary.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/14.
 */

public class CtrlListIndexBean {

    public List<PublicListBean> publicList;

    public static class PublicListBean {
        /**
         * db_dt_id : 73
         * db_name : 门锁
         * dd_db_sbID : 10000
         * dd_macaddr : 38FFD2053058
         * dd_ui_id : 5
         * di_status : 0
         * list : [{"db_dt_id":73,"dd_db_sbID":10000,"dd_macaddr":"38FFD2053058","dd_ui_id":5,"di_clusterID":1,"di_clustername":"门锁1","di_id":138,"di_status":0}]
         */

        public int db_dt_id;
        public String db_name;
        public String dd_db_sbID;
        public String dd_macaddr;
        public int dd_ui_id;
        public int di_status;
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
            public int dd_db_sbID;
            public String dd_macaddr;
            public int dd_ui_id;
            public int di_clusterID;
            public String di_clustername;
            public int di_id;
            public int di_status;
        }
    }


}
