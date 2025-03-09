package cn.iocoder.yudao.server.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ApiAnswerDO {
    /**
     * ID
     */
    private Long id;
    /**
     * 序号
     */
    private Integer indexNo;
    /**
     *题目名称
     */
    private String name;
    /**
     *答案
     */
    private String answer;
    /**
     *答案详细
     */
    private String answerDesc;
    /**
     *分值
     */
    private Integer score;
}
