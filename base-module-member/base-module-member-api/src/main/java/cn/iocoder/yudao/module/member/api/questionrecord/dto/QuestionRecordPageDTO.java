package cn.iocoder.yudao.module.member.api.questionrecord.dto;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import lombok.Data;

@Data
public class QuestionRecordPageDTO extends PageParam {
    /**
     * 用户ID
     */
    private Long userId;

}
