package cn.iocoder.yudao.server.util;

import java.util.Map;

/**
 * 微信消息模板
 */
public class WxMessageTemplate {
    /**
     * 模板消息id
     */
    private String template_id;

    /**
     * 跳转页面
     */
    private String page;
    /**
     * 用户openId
     */
    private String touser;

    /**
     * 详细内容
     */
    private Map<String,TemplateData> data;

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public Map<String, TemplateData> getData() {
        return data;
    }

    public void setData(Map<String, TemplateData> data) {
        this.data = data;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
