package cn.iocoder.yudao.module.promotion.controller.admin.banner.vo;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.promotion.enums.banner.BannerPositionEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Banner Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 * @author xia
 */
@Data
public class BannerBaseVO {

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "标题不能为空")
    private String title;

    @Schema(description = "图片尺寸")
    private String picSize;

    @Schema(description = "跳转链接")
    private String url;

    @Schema(description = "图片地址")
    private String picUrl;

    @Schema(description = "WAP图片地址")
    private String wapPicUrl;

    @Schema(description = "视频地址")
    private String videoUrl;

    @Schema(description = "position", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "position 不能为空")
    private Integer position;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "状态不能为空")
    @InEnum(CommonStatusEnum.class)
    private Integer status;

    @Schema(description = "备注")
    private String memo;

    @Schema(description = "备用属性01")
    private String attr01;

    @Schema(description = "备用属性02")
    private String attr02;

    @Schema(description = "备用属性03")
    private String attr03;

    @Schema(description = "备用属性04")
    private String attr04;

    @Schema(description = "备用属性05")
    private String attr05;

}
