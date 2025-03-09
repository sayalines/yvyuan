package cn.iocoder.yudao.module.promotion.dal.dataobject.banner;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.promotion.enums.banner.BannerPositionEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * banner DO
 *
 * @author xia
 */
@TableName("promotion_banner")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerDO extends BaseDO {

    /**
     * 编号
     */
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 跳转链接
     */
    private String url;
    /**
     * 图片尺寸
     */
    private String picSize;
    /**
     * 图片链接
     */
    private String picUrl;
    /**
     * WAP图片链接(兼容移动端)
     * */
    private String wapPicUrl;
    /**
     * 视频地址
     * */
    private String videoUrl;
    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态 {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * 定位 {@link BannerPositionEnum}
     */
    private Integer position;

    /**
     * 备注
     */
    private String memo;

    /**
     * 点击次数
     */
    private Integer browseCount;

    /**
     * 备用属性01
     */
    private String attr01;

    /**
     * 备用属性02
     */
    private String attr02;

    /**
     * 备用属性03
     */
    private String attr03;

    /**
     * 备用属性04
     */
    private String attr04;

    /**
     * 备用属性05
     */
    private String attr05;

}
