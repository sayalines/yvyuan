package cn.iocoder.yudao.module.member.enums;

public enum MemberQuestionStatus {
    TO_AUDIT(1, "待解决"),
    AUDIT_SUCCESS(2, "已解决"),
    AUDIT_FAIL(3, "无法解决");

    private final int code;
    private final String description;

    MemberQuestionStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
