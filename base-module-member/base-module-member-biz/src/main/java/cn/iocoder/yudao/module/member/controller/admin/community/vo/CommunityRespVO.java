package cn.iocoder.yudao.module.member.controller.admin.community.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 论坛文章 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CommunityRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9858")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9893")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "用户昵称")
    @ExcelProperty("用户昵称")
    private String nickname;

    @Schema(description = "用户手机号")
    @ExcelProperty("用户手机号")
    private String mobile;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "品牌ID", example = "32648")
    @ExcelProperty("品牌ID")
    private Long brandId;

    @Schema(description = "品牌")
    @ExcelProperty("品牌")
    private String brand;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("标题")
    private String title;

    @Schema(description = "内容")
    @ExcelProperty("内容")
    private String content;

    @Schema(description = "封面图", example = "https://www.iocoder.cn")
    @ExcelProperty("封面图")
    private String picUrl;

    @Schema(description = "附件", example = "https://www.iocoder.cn")
    @ExcelProperty("附件")
    private String fileUrl;

    @Schema(description = "是否热门")
    @ExcelProperty(value = "是否热门", converter = DictConvert.class)
    @DictFormat("system_yes_no") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer isHot;

    @Schema(description = "是否精选")
    @ExcelProperty(value = "是否精选", converter = DictConvert.class)
    @DictFormat("system_yes_no") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer isChoice;

    @Schema(description = "收藏数", example = "9874")
    @ExcelProperty("收藏数")
    private Long collectCount;

    @Schema(description = "点赞数", example = "7718")
    @ExcelProperty("点赞数")
    private Long likeCount;

    @Schema(description = "评论数", example = "26224")
    @ExcelProperty("评论数")
    private Long reviewCount;

    @Schema(description = "状态", example = "1")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("ext_community_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

    @Schema(description = "审核时间")
    @ExcelProperty("审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "审核时间格式化")
    private String auditTimeFormat;

    @Schema(description = "拒绝原因", example = "不喜欢")
    @ExcelProperty("拒绝原因")
    private String reason;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建时间格式化")
    private String createTimeFormat;

    /**********前端扩展字段***********/
    @Schema(description = "是否点赞过")
    private Boolean isLike;

    @Schema(description = "是否收藏过")
    private Boolean isCollect;

}