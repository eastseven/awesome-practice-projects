package cn.eastseven;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static java.lang.System.out;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpMethodGetVsPostApplicationTests {

	@Test
	public void contextLoads() {

		int x = 1;
		int y = x;
		x++;

		StringBuilder s = new StringBuilder("test1");
		StringBuilder t = s;
		s.append("tweet ");

		if (s != t) {
			out.println("impossible, they are equal");
		}

		if (x != y) {
			out.println("why is x not equal to y ?");
		}
	}

}
