package cn.eastseven.hadoop;

import cn.eastseven.hadoop.model.UrlMetadata;
import cn.eastseven.hadoop.repository.UrlMetadataRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SpringHadoopDemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringHadoopDemoApplication.class, args);
	}

	@Autowired
	UrlMetadataRepository urlMetadataRepository;

	@Override
	public void run(String... strings) throws Exception {
		try {
			List<UrlMetadata> all = Lists.newArrayList(urlMetadataRepository.findAll());
			all.forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
