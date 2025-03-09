package cn.iocoder.yudao.module.member.controller.admin.feedback.vo;

import io.swagger.v3.oas.annotations.media.Schema;

public class MemberFeedbackAuditReqVO {

    @Schema(description = "问题编号",example = "1")
    private Long id;

    @Schema(description = "解决结果",example = "已解决")
    private String result;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
