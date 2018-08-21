package top.vchao.live.pro.bean;

import top.vchao.live.utils.LogUtils;

/**
 * @ Create_time: 2018/8/20 on 15:06.
 * @ description：
 * @ author: vchao  blog: http://blog.csdn.net/zheng_weichao
 */
public class Translation {
//  http://serve.qhdyzb.cn/update
    /**
     * code : 1
     * message : Successful
     * data : {"id":1,"pid":0,"version_code":"1.9.4","description":"1. 企业专区登陆逸掌帮啦，想享受到更低价的优惠和更优质的服务吗？快加入企业用户吧 #2. 优化了订单下单流程 #3. 修复了已知的BUG","url":"http://app.mbs001.com/public/static/app/20180811/339a660f5da34d18ed6aaf3d012fde8d.apk","update_time":"2018-08-11 10:28:50"}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * pid : 0
         * version_code : 1.9.4
         * description : 1. 企业专区登陆逸掌帮啦，想享受到更低价的优惠和更优质的服务吗？快加入企业用户吧 #2. 优化了订单下单流程 #3. 修复了已知的BUG
         * url : http://app.mbs001.com/public/static/app/20180811/339a660f5da34d18ed6aaf3d012fde8d.apk
         * update_time : 2018-08-11 10:28:50
         */

        private int id;
        private int pid;
        private String version_code;
        private String description;
        private String url;
        private String update_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getVersion_code() {
            return version_code;
        }

        public void setVersion_code(String version_code) {
            this.version_code = version_code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }
    }
}
