package cn.iocoder.yudao.server.vo;

import lombok.Data;

@Data
public class ApiMemberSignDO {
    /**
     *
     */
    private Long id;
    /**
     * 签到第 x 天
     */
    private Integer day;
    /**
     * 奖励积分
     */
    private Integer point;
    /**
     * 奖励经验
     */
    private Integer experience;
    /**
     * 是否已签到
     */
    private Boolean isSign;
}
