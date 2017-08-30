package cn.eastseven.web.crawler;

import cn.eastseven.web.crawler.model.JingDong;
import cn.eastseven.web.crawler.model.QiXin;
import cn.eastseven.web.crawler.pipeline.HDFSPipeline;
import cn.eastseven.web.crawler.pipeline.JingDongPipeline;
import cn.eastseven.web.crawler.pipeline.MongoPipeline;
import cn.eastseven.web.crawler.processor.GongGongZiYuanPageProcessor;
import cn.eastseven.web.crawler.processor.JingDongPageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;

@Slf4j
@SpringBootApplication
public class WebCrawlerWithHdfsApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(WebCrawlerWithHdfsApplication.class, args);
	}

	@Autowired
	GongGongZiYuanPageProcessor gongGongZiYuanPageProcessor;

	@Autowired
	HDFSPipeline hdfsPipeline;

	@Autowired
	MongoPipeline mongoPipeline;

	@Autowired
	JingDongPageProcessor jdPageProcessor;

	@Autowired
	JingDongPipeline jdPipeline;

	@Override
	public void run(String... strings) throws Exception {
		/*
		Spider.create(gongGongZiYuanPageProcessor)
				.addPipeline(mongoPipeline)
				.addPipeline(hdfsPipeline)
				.addUrl("http://deal.ggzy.gov.cn/ds/deal/dealList.jsp")
				.run();
		*/
		//Spider.create(jdPageProcessor).addUrl("https://www.jd.com/").run();

		//OOSpider.create(Site.me().setSleepTime(1000), jdPipeline, JingDong.class).addUrl("https://www.jd.com/newWare.html").run();

		//OOSpider.create(Site.me().setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36")
		//		.setSleepTime(1000) , new ConsolePageModelPipeline(), QiXin.class).addUrl("http://www.qixin.com/").run();
	}
}