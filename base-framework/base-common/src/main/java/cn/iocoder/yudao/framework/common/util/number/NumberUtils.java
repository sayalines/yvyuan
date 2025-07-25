package cn.iocoder.yudao.framework.common.util.number;

import cn.hutool.core.util.StrUtil;

/**
 * 数字的工具类，补全 {@link cn.hutool.core.util.NumberUtil} 的功能
 *
 * @author 商城管理系统
 */
public class NumberUtils {

    public static Long parseLong(String str) {
        return StrUtil.isNotEmpty(str) ? Long.valueOf(str) : null;
    }

    /**
     * 通过经纬度获取地球上两点之间的距离
     *
     * 参考 <<a href="https://gitee.com/dromara/hutool/blob/1caabb586b1f95aec66a21d039c5695df5e0f4c1/hutool-core/src/main/java/cn/hutool/core/util/DistanceUtil.java">DistanceUtil</a>> 实现，目前它已经被 hutool 删除
     *
     * @param lat1 经度1
     * @param lng1 纬度1
     * @param lat2 经度2
     * @param lng2 纬度2
     * @return 距离，单位：千米
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = lat1 * Math.PI / 180.0;
        double radLat2 = lat2 * Math.PI / 180.0;
        double a = radLat1 - radLat2;
        double b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        distance = distance * 6378.137;
        distance = Math.round(distance * 10000d) / 10000d;
        return distance;
    }

}
