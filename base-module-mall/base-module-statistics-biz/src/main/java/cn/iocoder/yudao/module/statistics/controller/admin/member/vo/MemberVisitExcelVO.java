package cn.iocoder.yudao.module.statistics.controller.admin.member.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class MemberVisitExcelVO {
    @ExcelProperty(value = "访客ID")
    private Long userId;

    @ExcelProperty(value = "访客IP")
    private String userIp;

    @ExcelProperty(value = "手机号")
    private String mobile;

    @ExcelProperty(value = "微信头像")
    private String avatar;

    @ExcelProperty(value = "微信昵称")
    private String nickname;

    @ExcelProperty(value = "性别")
    private String sex;

    @ExcelProperty(value = "年龄")
    private Integer age;

    @ExcelProperty(value = "手机型号")
    private String mobileModel;

    @ExcelProperty(value = "邮箱")
    private String email;
}
