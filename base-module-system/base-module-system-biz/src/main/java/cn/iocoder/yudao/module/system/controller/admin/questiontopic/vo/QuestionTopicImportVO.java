package cn.iocoder.yudao.module.system.controller.admin.questiontopic.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ExcelIgnoreUnannotated
@Accessors(chain = false)
public class QuestionTopicImportVO {

    @ExcelProperty("题目编号")
    private String code;

    @ExcelProperty("题目名称")
    private String name;

    @ExcelProperty(value = "题目类型", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.SYSTEM_QUESTION_TOPIC_TYPE)
    private Integer type;

    @ExcelProperty("排序号")
    private Integer orderNo;

    @ExcelProperty("选项值1")
    private String optValue1;

    @ExcelProperty("选项内容1")
    private String optContent1;

    @ExcelProperty("选项分值1")
    private Integer optScore1;

    @ExcelProperty("选项值2")
    private String optValue2;

    @ExcelProperty("选项内容2")
    private String optContent2;

    @ExcelProperty("选项分值2")
    private Integer optScore2;

    @ExcelProperty("选项值3")
    private String optValue3;

    @ExcelProperty("选项内容3")
    private String optContent3;

    @ExcelProperty("选项分值3")
    private Integer optScore3;

    @ExcelProperty("选项值4")
    private String optValue4;

    @ExcelProperty("选项内容4")
    private String optContent4;

    @ExcelProperty("选项分值4")
    private Integer optScore4;

    @ExcelProperty("选项值5")
    private String optValue5;

    @ExcelProperty("选项内容5")
    private String optContent5;

    @ExcelProperty("选项分值5")
    private Integer optScore5;

    @ExcelProperty("选项值6")
    private String optValue6;

    @ExcelProperty("选项内容6")
    private String optContent6;

    @ExcelProperty("选项分值6")
    private Integer optScore6;

    @ExcelProperty("选项值7")
    private String optValue7;

    @ExcelProperty("选项内容7")
    private String optContent7;

    @ExcelProperty("选项分值7")
    private Integer optScore7;

    @ExcelProperty("选项值8")
    private String optValue8;

    @ExcelProperty("选项内容8")
    private String optContent8;

    @ExcelProperty("选项分值8")
    private Integer optScore8;



}