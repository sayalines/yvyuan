package cn.iocoder.yudao.server.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ApiBannerDO {

    /**
     * 广告编号
     */
    @TableId
    private Long id;

    /**
     * 广告标题
     */
    private String title;

    /**
     * 广告备注
     */
    private String memo;

    /**
     * 广告外链
     */
    private String url;

    /**
     * 广告图片
     */
    private String picUrl;

    /**
     * 广告位置
     */
    private Integer position;

    /**
     * 备用字段1
     */
    private String attr01;

    /**
     * 备用字段2
     */
    private String attr02;
}
