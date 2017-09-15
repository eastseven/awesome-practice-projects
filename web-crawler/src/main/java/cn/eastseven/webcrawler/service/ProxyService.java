package cn.eastseven.webcrawler.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.selector.Html;

import java.net.URL;

@Slf4j
@Service
public class ProxyService {

    @Value("${app.proxy.pool.url}") String GET_PROXY_URL;

    public Proxy get() {
        try {
            String html = Jsoup.parse(new URL(GET_PROXY_URL), 10000).body().html();
            log.debug("{}", html);

            String host = html.split(":")[0];
            int port = Integer.valueOf(html.split(":")[1]);

            return new Proxy(host, port);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    public Proxy getValidProxy() {
        HttpClientDownloader downloader = new HttpClientDownloader();
        while (true) {
            Proxy proxy = get();
            downloader.setProxyProvider(SimpleProxyProvider.from(proxy));
            try {
                Html html = downloader.download("https://www.baidu.com/", "UTF-8");
                log.debug("{}", html);
                return proxy;
            } catch (Exception e) {
                log.warn("无效代理 {}", proxy);
            }
        }
        //return null;
    }
}
