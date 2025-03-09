package cn.iocoder.yudao.server.vo;

import lombok.Data;

@Data
public class ApiKeywordContentDO {
    /**
     * 关键字
     */
    private String name;
    /**
     * 开始位置
     */
    private String startPosition;
    /**
     * 结束位置
     */
    private String endPosition;
    /**
     * 链接地址
     */
    private String url;
}
