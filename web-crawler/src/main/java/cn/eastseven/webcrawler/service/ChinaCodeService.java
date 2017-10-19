package cn.eastseven.webcrawler.service;

import cn.eastseven.webcrawler.model.ChinaCode;
import cn.eastseven.webcrawler.repository.ChinaCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class ChinaCodeService {

    @Autowired
    ChinaCodeRepository repository;

    final int time = 60 * 60 * 1000;
    final String prefix = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/";

    public void start() throws Exception {
        String url = prefix + "index.html";

        Elements provinces = Jsoup.connect(url).timeout(time).get().body().select("table.provincetable tr.provincetr td a");
        for (Element province : provinces) {
            String provinceCode = province.attr("href").replace(".html", "");
            String provinceName = province.text();
            String provinceLink = prefix + province.attr("href");

            new Thread(() -> {
                try {
                    fetchCities(provinceCode, provinceName, provinceLink);
                } catch (Exception e) {
                    log.error("", e);
                }
            }).run();
        }
    }

    private void fetchCities(String provinceCode, String provinceName, String provinceLink) throws Exception {

        Elements cities = Jsoup.connect(provinceLink).timeout(time).get().body().select("table.citytable tr.citytr");
        for (Element city : cities) {
            String cityName = city.select("td:nth-child(2)").text();
            String cityHref = city.select("td:nth-child(2) a").attr("href");
            String cityLink = prefix + cityHref;

            new Thread(() -> {
                try {
                    fetchCountries(provinceCode, provinceName, cityName, cityLink);
                } catch (Exception e) {
                    log.error("", e);
                }
            }).run();
        }
    }

    private void fetchCountries(String provinceCode, String provinceName, String cityName, String cityLink) {
        Elements counties = null;
        try {
            counties = Jsoup.connect(cityLink).timeout(time).get().body().select("table.countytable tr.countytr");
        } catch (IOException e) {
            log.error("", e);
            log.error("county jsoup connect >>> {},{},{},{}", provinceCode, provinceName, cityName, cityLink);
        }
        for (Element county : counties) {
            String countyName = county.select("td").get(1).text();
            //String countyLink = null;
            if (county.select("td a").hasAttr("href")) {
                String countyLink = prefix + provinceCode + '/' + county.select("a").attr("href");
                Elements towns = null;
                try {
                    towns = Jsoup.connect(countyLink).timeout(time).get().body().select("table.towntable tr.towntr");
                    for (Element town : towns) {
                        String townName = town.select("td").get(1).text();

                        ChinaCode code = saveChinaCode(provinceName, cityName, countyName, townName);
                        if (code == null) continue;
                    }
                } catch (IOException e) {
                    log.error("", e);
                    log.error("town jsoup connect >>> {}", countyLink);
                }
            }
        }
    }

    private ChinaCode saveChinaCode(String provinceName, String cityName, String countyName, String townName) {
        String id = DigestUtils.md5Hex(provinceName + cityName + countyName + townName);
        if (repository.exists(id)) {
            log.debug("exists {}-{}-{}-{}", townName, countyName, cityName, provinceName);
            return null;
        }
        ChinaCode code = new ChinaCode();
        code.setId(id);
        code.setProvince(provinceName);
        code.setCity(cityName);
        code.setCountry(countyName);
        code.setTown(townName);
        repository.save(code);
        log.debug("{}-{}-{}-{}", townName, countyName, cityName, provinceName);
        return code;
    }
}
