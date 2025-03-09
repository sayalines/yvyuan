package cn.iocoder.yudao.server.vo;

import lombok.Data;

@Data
public class ApiMemberDO {
    /**
     * 用户ID
     */
    private Long id;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 性别
     */
    private String sex;
    /**
     * 邮箱
     * */
    private String email;
}
