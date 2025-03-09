package cn.iocoder.yudao.server.vo;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;


/**
 * 文章分类
 */
@Data
public class ApiArticleCategoryDO {

    /**
     * 文章分类编号
     */
    @TableId
    private Long id;
    /**
     * 文章分类名称
     */
    private String name;
    /**
     * 图标地址
     */
    private String picUrl;
    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;


}
