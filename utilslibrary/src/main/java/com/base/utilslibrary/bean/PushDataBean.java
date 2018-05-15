package com.base.utilslibrary.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public class PushDataBean {


    public int getDb_sbID() {
        return db_sbID;
    }

    public void setDb_sbID(int db_sbID) {
        this.db_sbID = db_sbID;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public List<PayloadBean> getPayload() {
        return payload;
    }

    public void setPayload(List<PayloadBean> payload) {
        this.payload = payload;
    }

    /**
     * db_sbID : 0
     * passwd : 52399399
     * payload : [{"data":{"db_sbID":0,"clusterID":0,"packetID":132,"cmd":19},"clusterID":1}]
     */

    public int db_sbID;
    public String passwd;
    public List<PayloadBean> payload;

    public static class PayloadBean {
        /**
         * data : {"db_sbID":0,"clusterID":0,"packetID":132,"cmd":19}
         * clusterID : 1
         */

        public DataBean data;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public int getClusterID() {
            return clusterID;
        }

        public void setClusterID(int clusterID) {
            this.clusterID = clusterID;
        }

        public int clusterID;

        public static class DataBean {
            public int getDb_sbID() {
                return db_sbID;
            }

            public void setDb_sbID(int db_sbID) {
                this.db_sbID = db_sbID;
            }

            public int getClusterID() {
                return clusterID;
            }

            public void setClusterID(int clusterID) {
                this.clusterID = clusterID;
            }

            public int getPacketID() {
                return packetID;
            }

            public void setPacketID(int packetID) {
                this.packetID = packetID;
            }

            public int getCmd() {
                return cmd;
            }

            public void setCmd(int cmd) {
                this.cmd = cmd;
            }

            /**
             * db_sbID : 0
             * clusterID : 0
             * packetID : 132
             * cmd : 19
             */


            public int db_sbID;
            public int clusterID;
            public int packetID;
            public int cmd;
        }
    }
}
