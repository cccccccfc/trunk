package com.base.project.bean;

import java.util.List;

/**
 * Created by MI on 2017/9/27 0027.
 */

public class PushcmdBean {


    /**
     * db_sbID : 10000
     * payload : [{"ctrl":{"sw":0,"learnmode":1,"cmd":10,"position":0,"functions":0,"times":5,"handle":6},"clusterID":1}]
     */

    private int db_sbID;
    private List<PayloadBean> payload;

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    private  int retcode ;
    public int getDb_sbID() {
        return db_sbID;
    }

    public void setDb_sbID(int db_sbID) {
        this.db_sbID = db_sbID;
    }

    public List<PayloadBean> getPayload() {
        return payload;
    }

    public void setPayload(List<PayloadBean> payload) {
        this.payload = payload;
    }

    public static class PayloadBean {
        /**
         * ctrl : {"sw":0,"learnmode":1,"cmd":10,"position":0,"functions":0,"times":5,"handle":6}
         * clusterID : 1
         */

        private CtrlBean ctrl;
        private int clusterID;

        public CtrlBean getCtrl() {
            return ctrl;
        }

        public void setCtrl(CtrlBean ctrl) {
            this.ctrl = ctrl;
        }

        public int getClusterID() {
            return clusterID;
        }

        public void setClusterID(int clusterID) {
            this.clusterID = clusterID;
        }

        public static class CtrlBean {
            /**
             * sw : 0
             * learnmode : 1
             * cmd : 10
             * position : 0
             * functions : 0
             * times : 5
             * handle : 6
             */

            private int sw;
            private int learnmode;
            private int cmd;
            private int position;
            private int functions;
            private int times;
            private int handle;

            public int getDelayS() {
                return delayS;
            }

            public void setDelayS(int delayS) {
                this.delayS = delayS;
            }

            private int delayS; //延时

            public int getLocation() {
                return location;
            }

            public void setLocation(int location) {
                this.location = location;
            }

            private int location;

            public int getSw() {
                return sw;
            }

            public void setSw(int sw) {
                this.sw = sw;
            }

            public int getLearnmode() {
                return learnmode;
            }

            public void setLearnmode(int learnmode) {
                this.learnmode = learnmode;
            }

            public int getCmd() {
                return cmd;
            }

            public void setCmd(int cmd) {
                this.cmd = cmd;
            }

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public int getFunctions() {
                return functions;
            }

            public void setFunctions(int functions) {
                this.functions = functions;
            }

            public int getTimes() {
                return times;
            }

            public void setTimes(int times) {
                this.times = times;
            }

            public int getHandle() {
                return handle;
            }

            public void setHandle(int handle) {
                this.handle = handle;
            }
        }
    }
}
