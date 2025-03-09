package cn.iocoder.yudao.module.mp.account;

public interface AccountApi {
    /**
     * 获取小程序有效Token
     * @return
     */
    String getAccessToken(Boolean isRefresh);

    /**
     * 生成短链接
     * @return
     */
    String createShortLink(String pageUrl,String pageTitle,Boolean isPermanent);
    /**
     * 生成二维码
     * @return
     */
    String createQrCode(String pageUrl, Integer width);
}
