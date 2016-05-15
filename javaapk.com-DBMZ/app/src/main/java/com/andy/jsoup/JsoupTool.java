package com.andy.jsoup;

import com.andy.model.ImageInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Andy on 2015/6/12
 */
public class JsoupTool {
    private static JsoupTool instance = null;

    private JsoupTool() {
        trustEveryone();
    }

    public static JsoupTool getInstance() {
        if (instance == null) {
            synchronized (JsoupTool.class) {
                instance = new JsoupTool();
            }
        }

        return instance;
    }

    public static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ImageInfo> getAllImages(String pageUrl) {
        try {
            Document doc = Jsoup.connect(pageUrl)
                    .timeout(10000)
                    .post();
            String title = doc.title();
            System.out.println(title);

            List<ImageInfo> imgList = new ArrayList<ImageInfo>();
            ImageInfo imageInfo;

            //Element singerListDiv = doc.getElementsByAttributeValue("class", "main_width").first();
            Elements links = doc.getElementsByAttributeValue("class","nrl_item");

            for (Element link: links) {
                imageInfo = new ImageInfo();

                Element e1=link.getElementsByAttributeValue("class", "list_img").first();
                imageInfo.setImgTitle(e1.attr("title"));
                imageInfo.setImgLink("http://www.pm25.com" + e1.attr("href"));

                Element e2=e1.getElementsByTag("Img").first();
                imageInfo.setImgUrl("http://www.pm25.com" +e2.attr("src"));

                imageInfo.setImgContent(link.getElementsByTag("dd").text());
                imgList.add(imageInfo);
            }

            /*Elements urls = doc.select("img[src$=.jpg]");
            for (Element url : urls) {
                imageInfo = new ImageInfo();
                imageInfo.setImgTitle(url.attr("title"));
                imageInfo.setImgUrl("http://www.pm25.com"+url.attr("src"));
                imgList.add(imageInfo);
            }*/

            return imgList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
