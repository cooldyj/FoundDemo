package com.chinayszc.mobile.module.home.model;

import com.google.gson.annotations.SerializedName;

/**
 * 轮播图Model
 * Created by Jerry on 2017/4/18.
 */

public class GalleryModel {

    /**
     * img_path : http://my.image.ezhi.com/201704/58ec7df8ba12d.png
     * img_thumb_path : http://my.image.ezhi.com/201704/58ec7df8ba12d_415_178.png
     * info_url : /product/product.htm?id=1
     */

    private String img_path;
    private String img_thumb_path;
    private String info_url;
    private int id;
    /**
     * obj : {"id":0,"type":"web","url":"http://m.azhongzhi.com"}
     */

    private ObjBean obj;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getImg_thumb_path() {
        return img_thumb_path;
    }

    public void setImg_thumb_path(String img_thumb_path) {
        this.img_thumb_path = img_thumb_path;
    }

    public String getInfo_url() {
        return info_url;
    }

    public void setInfo_url(String info_url) {
        this.info_url = info_url;
    }

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * id : 0
         * type : web
         * url : http://m.azhongzhi.com
         */

        private int id;
        private String type;
        private String url;

        public int getId() {
            return id;
        }

        public void setId(int idX) {
            this.id = idX;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
