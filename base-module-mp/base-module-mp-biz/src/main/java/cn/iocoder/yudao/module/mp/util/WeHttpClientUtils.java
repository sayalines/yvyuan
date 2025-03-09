package cn.iocoder.yudao.module.mp.util;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

/**
 * 测试httpclient 4.0 1. 重新设计了HttpClient 4.0 API架构，彻底从内部解决了所有 HttpClient 3.x 已知的架构缺陷代码。 2. HttpClient 4.0 提供了更简洁，更灵活，更明确的API。 3. HttpClient 4.0 引入了很多模块化的结构。 4.
 * HttpClient 4.0性能方面得到了不小的提升，包括更少的内存使用，通过使用HttpCore模块更高效完成HTTP传输。 5. 通过使用 协议拦截器(protocol interceptors), HttpClient 4.0实现了 交叉HTTP（cross-cutting HTTP protocol）
 * 协议 6. HttpClient 4.0增强了对连接的管理，更好的处理持久化连接，同时HttpClient 4.0还支持连接状态 7. HttpClient 4.0增加了插件式（可插拔的）的 重定向（redirect） 和 验证（authentication）处理。 8. HttpClient
 * 4.0支持通过代理发送请求，或者通过一组代理发送请求。 9. 更灵活的SSL context 自定义功能在HttpClient 4.0中得以实现。 10. HttpClient 4.0减少了在省城HTTP请求 和 解析HTTP响应 过程中的垃圾信息。 11. HttpClient团队鼓励所有的项目升级成
 * HttpClient 4.0
 *
 * 
 */
public class WeHttpClientUtils {
	private static Logger logger = LoggerFactory.getLogger(WeHttpClientUtils.class);
	/* 发送 get请求 用HTTPclient 发送请求*/
	public static String get(String URL) {
		long responseLength = 0; // 响应长度
		String responseContent = null; // 响应内容
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(URL); // 创建org.apache.http.client.methods.HttpGet
		try {
			HttpResponse response = httpClient.execute(httpGet); // 执行GET请求
			HttpEntity entity = response.getEntity(); // 获取响应实体
			if (null != entity) {
				responseLength = entity.getContentLength();
				responseContent = EntityUtils.toString(entity,"UTF-8");
				EntityUtils.consume(entity); // Consume response content
			}
			System.out.println("请求地址: " + httpGet.getURI());
			System.out.println("响应状态: " + response.getStatusLine());
			System.out.println("响应长度: " + responseLength);
			System.out.println("响应内容: " + responseContent);
		} catch (ClientProtocolException e) {
			logger.error(e.getMessage());
		} catch (ParseException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源
		}
		logger.info(responseContent);
		return responseContent;
	}

	
	/* 发送 post请求 用HTTPclient 发送请求*/
	public static byte[] post(String URL, String json) {
		byte[] data = null;
		InputStream inputStream = null;
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost
		HttpPost httppost = new HttpPost(URL);
		httppost.addHeader("Content-type", "application/json; charset=utf-8");
		httppost.setHeader("Accept", "application/json");
		try {
			StringEntity s = new StringEntity(json, Charset.forName("UTF-8")); 
			s.setContentEncoding("UTF-8");
			s.setContentType("image/jpg");
			httppost.setEntity(s);
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				// 获取相应实体
				HttpEntity entity = response.getEntity();
				String contentType = entity.getContentType().getValue().toLowerCase();
				System.out.println("Content-Type:"+contentType);
				if (contentType.contains("json")){
					logger.error(EntityUtils.toString(entity,"UTF-8"));
					return null;
				}else{
					if (entity != null) {
						inputStream = entity.getContent();
						data = readInputStream(inputStream);
					}
					return data;
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
 
    /**  将流 保存为数据数组
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		// 使用一个输入流从buffer里把数据读取出来
		while ((len = inStream.read(buffer)) != -1) {
			// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		// 关闭输入流
		inStream.close();
		// 把outStream里的数据写入内存
		return outStream.toByteArray();
	}
	
	/**
     * 将二进制转换成文件保存
     * @param instreams 二进制流
     * @param destPath 图片的保存路径
     * @param realPath 图片的名称
     * @return
     *      1：保存正常
     *      0：保存失败
     */
    public static String saveToImgByInputStream(InputStream instreams,String destPath,String realPath){
    	  int stateInt = 0;
        if(instreams != null){
        		stateInt = 1;
            try {
               File destFile = new File(realPath);
               File destDir = destFile.getParentFile();
				   if (destDir != null) {
	    				destDir.mkdirs();
	    			}
               FileOutputStream fos=new FileOutputStream(destFile);
               byte[] b = new byte[1024];
               int nRead = 0;
               while ((nRead = instreams.read(b)) != -1) {
	                fos.write(b, 0, nRead);
	            		}
	            fos.flush();
	            fos.close();
            } catch (Exception e) {
                stateInt = 0;
                e.printStackTrace();
            } finally {
            }
        }
      if (stateInt==1) {
        	return destPath;
      }else {
        	return "";
        }
    }
}
