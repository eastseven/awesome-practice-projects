# 爬虫示例

抓取[全国公共资源网](http://deal.ggzy.gov.cn/ds/deal/dealList.jsp)数据

启动命令

```shell
mvn clean spring-boot:run
```

打包

```shell

mvn clean package -Dmaven.skip.test=true

```

部署

```shell
java -jar target/*.jar
```