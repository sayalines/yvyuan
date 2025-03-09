package cn.iocoder.yudao.server.vo;

import lombok.Data;

import java.util.List;


/**
 * 文章分组管理
 */
@Data
public class ApiArticleGroupDO {
    /**
     *分组
     */
    String group;
    /**
     *文章列表
     */
    List<ApiArticleDO> list;
}
