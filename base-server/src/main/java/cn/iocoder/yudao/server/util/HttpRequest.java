package cn.iocoder.yudao.server.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private String host = "";
    private Map<String, String> header;
    private CloseableHttpResponse response;

    public HttpRequest() {

    }

    public HttpRequest(String host) {
        this.host = host;
    }

    public HttpRequest(String host, Map<String, String> header) {
        this.host = host;
        this.header = header;
    }

    public CloseableHttpResponse getResponse() {
        return response;
    }

    public Object getRequest(String path, Map<String, String> params) {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            try {
                HttpGet get = new HttpGet();
                if (header != null) {
                    for (Map.Entry<String, String> entry : header.entrySet()) {
                        get.setHeader(entry.getKey(), entry.getValue());
                    }
                }
                String urlParams = "";
                if (params != null) {
                    int i = 0;
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        if (i == 0) {
                            urlParams = "?" + entry.getKey() + "=" + entry.getValue();
                        } else {
                            urlParams = urlParams + "&" + entry.getKey() + "=" + entry.getValue();
                        }
                        i++;
                    }
                }
                get.setURI(new URI(host + path + urlParams));
                response = client.execute(get);
                try {
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        HttpEntity entity = response.getEntity();
                        Header header = response.getFirstHeader("Content-disposition");
                        if (header != null && header.getValue().contains("attachment")) {
                            return EntityUtils.toByteArray(entity);
                        } else {
                            return EntityUtils.toString(entity, "UTF-8");
                        }
                    }
                } finally {
                    response.close();
                }
            } finally {
                client.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getData(String path, Map<String, String> params) {
        Object object = getRequest(path, params);
        if (object != null) {
            return (String) object;
        } else {
            return null;
        }
    }

    public byte[] getBytes(String path, Map<String, String> params) {
        Object object = getRequest(path, params);
        if (object != null) {
            return (byte[]) object;
        } else {
            return null;
        }
    }

    public String postData(String path, Map<String, String> params, String body) {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            try {
                HttpPost post = new HttpPost();
                if (header != null) {
                    for (Map.Entry<String, String> entry : header.entrySet()) {
                        post.setHeader(entry.getKey(), entry.getValue());
                    }
                }
                String urlParams = "";
                if (params != null) {
                    int i = 0;
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        if (i == 0) {
                            urlParams = "?" + entry.getKey() + "=" + entry.getValue();
                        } else {
                            urlParams = urlParams + "&" + entry.getKey() + "=" + entry.getValue();
                        }
                        i++;
                    }
                }
                post.setURI(new URI(host + path + urlParams));
                String isoString = new String(body.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
                StringEntity stringEntity = new StringEntity(isoString);
                post.setEntity(stringEntity);
                response = client.execute(post);
                try {
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            return EntityUtils.toString(entity, "UTF-8");
                        } else {
                            return null;
                        }
                    }
                } finally {
                    response.close();
                }
            } finally {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String postData(String path, Map<String, String> params, Map<String, String> body) {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            try {
                HttpPost post = new HttpPost();
                if (header != null) {
                    for (Map.Entry<String, String> entry : header.entrySet()) {
                        post.setHeader(entry.getKey(), entry.getValue());
                    }
                }
                String urlParams = "";
                if (params != null) {
                    int i = 0;
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        if (i == 0) {
                            urlParams = "?" + entry.getKey() + "=" + entry.getValue();
                        } else {
                            urlParams = urlParams + "&" + entry.getKey() + "=" + entry.getValue();
                        }
                        i++;
                    }
                }
                post.setURI(new URI(host + path + urlParams));
                if (body != null) {
                    List<NameValuePair> envelopedParam = new ArrayList<>();
                    for (Map.Entry<String, String> entry : body.entrySet()) {
                        envelopedParam.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                        //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    }

                    UrlEncodedFormEntity pEntity = new UrlEncodedFormEntity(envelopedParam, "UTF-8");
                    post.setEntity(pEntity);
                }
                response = client.execute(post);
                try {
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            return EntityUtils.toString(entity, "UTF-8");
                        } else {
                            return null;
                        }
                    }
                } finally {
                    response.close();
                }
            } finally {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String postData(String path, Map<String, String> params) {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            try {
                HttpPost post = new HttpPost(host + path);
                if (header != null) {
                    for (Map.Entry<String, String> entry : header.entrySet()) {
                        post.setHeader(entry.getKey(), entry.getValue());
                    }
                }
                //post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                if (params != null) {
                    List<NameValuePair> envelopedParam = new ArrayList<>();
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        envelopedParam.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                        //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    }
                    UrlEncodedFormEntity pEntity = new UrlEncodedFormEntity(envelopedParam, "UTF-8");
                    post.setEntity(pEntity);
                }
                response = client.execute(post);
                try {
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            return EntityUtils.toString(entity, "UTF-8");
                        } else {
                            return null;
                        }
                    }
                } finally {
                    response.close();
                }
            } finally {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String postData(String path, String body) {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            try {
                HttpPost post = new HttpPost(host + path);
                if (header != null) {
                    for (Map.Entry<String, String> entry : header.entrySet()) {
                        post.setHeader(entry.getKey(), entry.getValue());
                    }
                }
                if (body != null) {
                    StringEntity entity = new StringEntity(body, "UTF-8");
                    post.setEntity(entity);
                }
                response = client.execute(post);
                try {
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            return EntityUtils.toString(entity, "UTF-8");
                        } else {
                            return null;
                        }
                    }
                } finally {
                    response.close();
                }
            } finally {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String postData(String path, String body,String contentType) {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            try {
                HttpPost post = new HttpPost(host + path);
                if (header != null) {
                    for (Map.Entry<String, String> entry : header.entrySet()) {
                        post.setHeader(entry.getKey(), entry.getValue());
                    }
                }
                if (body != null) {
                    StringEntity entity = new StringEntity(body, "UTF-8");
                    entity.setContentType(contentType);
                    post.setEntity(entity);
                }
                response = client.execute(post);
                try {
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            return EntityUtils.toString(entity, "UTF-8");
                        } else {
                            return null;
                        }
                    }
                } finally {
                    response.close();
                }
            } finally {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getImage(String url) {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            try {
                HttpGet get = new HttpGet();
                get.setURI(new URI(url));
                response = client.execute(get);
                try {
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            InputStream in = entity.getContent();
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            byte[] buffer = new byte[4096];
                            int n = 0;
                            while (-1 != (n = in.read(buffer))) {
                                out.write(buffer, 0, n);
                            }
                            in.close();
                            out.close();
                            return out.toByteArray();
                        } else {
                            return null;
                        }
                    }
                } finally {
                    response.close();
                }
            } finally {
                client.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
