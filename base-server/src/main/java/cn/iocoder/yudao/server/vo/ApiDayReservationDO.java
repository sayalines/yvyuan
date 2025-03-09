package cn.iocoder.yudao.server.vo;

import lombok.Data;

import java.util.List;

@Data
public class ApiDayReservationDO {
    /**
     * 预约日期
     */
    private String date;
    /**
     * 状态
     */
    private String status;
    /**
     * 时间段列表
     */
    private List<Item> items;

    @Data
    public static class Item {
        /**
         * 时间段
         */
        String time;
        /**
         * 可预约人数
         */
        Integer count;
        /**
         * 已预约人数
         */
        Integer useCount;
        /**
         * 剩余人数
         */
        Integer remainCount;
        /**
         * 状态
         */
        String status;
        /**
         * 备注
         */
        String remarks;
    }

}
