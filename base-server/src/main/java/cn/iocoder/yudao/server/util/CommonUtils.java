package cn.iocoder.yudao.server.util;

import cn.iocoder.yudao.module.product.api.property.dto.ProductPropertyValueDetailRespDTO;
import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CommonUtils {

    /**
     * 格式化价格
     * @param price
     * @return
     */
    public static BigDecimal formatPrice(Integer price){
        BigDecimal result = BigDecimal.ZERO;
        if (price!=null){
            result = new BigDecimal(price).divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
        }
        return result;
    }

    /**
     * 格式化价格
     * @param price
     * @return
     */
    public static BigDecimal formatPrice(Long price){
        BigDecimal result = BigDecimal.ZERO;
        if (price!=null){
            result = new BigDecimal(price).divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
        }
        return result;
    }

    /**
     * 格式化整数
     * @param data
     * @return
     */
    public static Integer formatInteger(Integer data){
        Integer result = 0;
        if (data!=null){
            result =data;
        }
        return result;
    }

    /**
     * 格式化整数
     * @param data
     * @return
     */
    public static Long formatLongValue(Long data){
        Long result = null;
        if (data!=null){
            result =data;
        }
        return result;
    }

    /**
     * 格式化整数
     * @param data
     * @return
     */
    public static Long formatLongValue(Long data,Long defaultValue){
        Long result = defaultValue;
        if (data!=null){
            result =data;
        }
        return result;
    }

    /**
     * 格式化浮点数
     * @param data
     * @return
     */
    public static BigDecimal formatBigDecimal(String data){
        BigDecimal result = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(data)){
            result =new BigDecimal(data);
        }
        return result;
    }

    /**
     * 格式化整数
     * @param data
     * @return
     */
    public static String formatLong(Long data){
        String result = "";
        if (data!=null){
            result =String.valueOf(data);
        }
        return result;
    }

    public static Boolean isLong(String content){
        Boolean result = false;
        if (StringUtils.isNotEmpty(content)){
            try{
                Long.parseLong(content);
                result = true;
            }catch (Exception e){

            }
        }
        return result;
    }

    /**
     * 格式化整数
     * @param data
     * @return
     */
    public static String formatString(String data){
        String result = "";
        if (StringUtils.isNotEmpty(data)){
            result =data;
        }
        return result;
    }

    /**
     * 格式化时间
     * @param dateTime
     * @return
     */
    public static String formatLocalDateTime(LocalDateTime dateTime,String formatter){
        String result = "";
        if (dateTime!=null){
            if (StringUtils.isEmpty(formatter)){
                formatter = "yyyy-MM-dd HH:mm:ss";
            }
            result = dateTime.format(DateTimeFormatter.ofPattern(formatter));
        }
        return result;
    }

    public static LocalDateTime addLocalDateTimeByDay(LocalDateTime dateTime,Integer day){
        if (dateTime!=null){
            if (day>0){
                return dateTime.plus(day, ChronoUnit.DAYS);
            }else{
                return dateTime.minus(day*-1, ChronoUnit.DAYS);
            }
        }
        return null;
    }

    public static LocalDateTime addLocalDateTimeByHours(LocalDateTime dateTime,Integer hours){
        if (dateTime!=null){
            if (hours>0){
                return dateTime.plus(hours, ChronoUnit.HOURS);
            }else{
                return dateTime.minus(hours*-1, ChronoUnit.HOURS);
            }
        }
        return null;
    }

    /**
     * 获取今天时间范围
     * @return
     */
    public static LocalDateTime[] getTodayTimeRange(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        LocalDateTime startTime = toFormatLocalDateTime(sdf.format(currentDate)+" 00:00:00","yyyy-MM-dd HH:mm:ss");
        LocalDateTime endTime = toFormatLocalDateTime(sdf.format(currentDate)+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        return new LocalDateTime[]{startTime,endTime};
    }

    /**
     * 获取今天开始时间
     * @return
     */
    public static LocalDateTime getTodayBeginTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        return toFormatLocalDateTime(sdf.format(currentDate)+" 00:00:00","yyyy-MM-dd HH:mm:ss");
    }
    /**
     * 获取今天结束时间
     * @return
     */
    public static LocalDateTime getTodayEndTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        return toFormatLocalDateTime(sdf.format(currentDate)+" 23:59:59","yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化时间
     * @param dateTime
     * @return
     */
    public static String formatDateTime(Date dateTime, String formatter){
        String result = "";
        if (dateTime!=null){
            if (StringUtils.isEmpty(formatter)){
                formatter = "yyyy-MM-dd HH:mm:ss";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(formatter);
            result = sdf.format(dateTime);
        }
        return result;
    }


    /**
     * 获取过期时间
     * @return
     */
    public static String getExpireDate(Integer timeout){
        String result = "";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,timeout);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        result = sdf.format(calendar.getTime());
        return result;
    }

    /**
     * 获取到期时间
     * @return
     */
    public static String formatExpireDate(String datetime){
        String result = "";
        if (StringUtils.isNotEmpty(datetime)){
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                Date oldDate = sdf.parse(datetime);
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                result = sdf2.format(oldDate);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 判断日期是否有效
     * @return
     */
    public static Boolean isValidDate(String datetime){
        Boolean result = false;
        if (StringUtils.isNotEmpty(datetime)){
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                Date oldDate = sdf.parse(datetime);
                Date currentDate = new Date();
                if (currentDate.compareTo(oldDate)<0){
                    result =true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 格式化时间
     * @param dateTime
     * @return
     */
    public static LocalDateTime toFormatLocalDateTime(String dateTime,String formatter){
        LocalDateTime result = null;
        if (StringUtils.isNotEmpty(dateTime)){
            if (StringUtils.isEmpty(formatter)){
                formatter = "yyyy-MM-dd HH:mm:ss";
            }
            if (formatter.equalsIgnoreCase("yyyy-MM-dd")){
                dateTime = dateTime+" 00:00:00";
                formatter="yyyy-MM-dd HH:mm:ss";
            }
            DateTimeFormatter df = DateTimeFormatter.ofPattern(formatter);
            result = LocalDateTime.parse(dateTime,df);
        }
        return result;
    }

    /**
     * 格式化时间T
     * @param dateTime
     * @return
     */
    public static String toFormatLocalDateTimeT(String dateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
        return localDateTime.format(formatter);
    }

    /**
     * 格式化性别
     * @param sex
     * @return
     */
    public static String formatSex(Integer sex){
        String result = "未知";
        if(sex!=null){
            if (sex.compareTo(1)==0){
                result = "男";
            }else if (sex.compareTo(2)==0) {
                result = "女";
            }
        }
        return result;
    }

    /**
     * 脱敏手机号
     * @param mobile
     * @return
     */
    public static String formatMobile(String mobile){
        String result = "";
        if (StringUtils.isNotEmpty(mobile)){
            if (mobile.length()==11){
                result = "*******"+mobile.substring(7);
            }else{
                result = "***********";
            }
        }
        return result;
    }

    /**
     * 脱敏身份证
     * @param idCard
     * @return
     */
    public static String formIdCard(String idCard){
        if (idCard == null || idCard.length() < 8){
            return "********";
        }
        return idCard.substring(0, 4) + "********" + idCard.substring(idCard.length()-4);
    }

    /**
     * 格式化性别
     * @param sex
     * @return
     */
    public static Integer toFormatSex(String sex){
        Integer result = 3;
        if(StringUtils.isNotEmpty(sex)){
            if (sex.equalsIgnoreCase("男")){
                result = 1;
            }else if (sex.equalsIgnoreCase("女")) {
                result = 2;
            }
        }
        return result;
    }

    public static String getSkuName(List<ProductSkuDO.Property> properties){
        String result ="";
        if (properties!=null && properties.size()>0){
            ProductSkuDO.Property property = properties.get(0);
            result = property.getValueName();
        }
        return result;
    }

    public static String getSkuName2(List<TradeOrderItemDO.Property> properties){
        String result ="";
        if (properties!=null && properties.size()>0){
            TradeOrderItemDO.Property property = properties.get(0);
            result = property.getValueName();
        }
        return result;
    }

    public static String getSkuName3(List<ProductPropertyValueDetailRespDTO> properties){
        String result ="";
        if (properties!=null && properties.size()>0){
            ProductPropertyValueDetailRespDTO property = properties.get(0);
            result = property.getValueName();
        }
        return result;
    }

    public static String encrypt(String plainText) {
        byte[] bytes = plainText.getBytes();
        return Base64.getEncoder().withoutPadding().encodeToString(bytes);
    }

    public static String decrypt(String encryptedText) {
        byte[] bytes = encryptedText.getBytes();
        byte[] decryptedBytes = Base64.getDecoder().decode(bytes);
        return new String(decryptedBytes);
    }

    public static String getMobileModel(String content) {
        String result = "其他";
        if (StringUtils.isNotEmpty(content)){
            if (content.contains("Android")){
                result = "安卓";
            }else if (content.contains("iPhone")){
                result = "苹果";
            }else if (content.contains("Windows")){
                result = "Windows";
            }
        }
        return result;
    }

    public static String decryptEnCode(String text) {
        String result = "";
        if (StringUtils.isNotEmpty(text)){
            try{
                result = URLDecoder.decode(text,"UTF-8");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }
}
