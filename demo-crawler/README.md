项目名：[通用爬虫]，git仓库地址：[https://github.com/eastseven/awesome-practice-projects/tree/master/demo-crawler] 。本项目第一负责人为 `[owner]`。


## 1. 如何运行

### 1.1 开发环境配置

> java版本 `jdk8`
> maven

### 1.2 开发过程

#### 1.2.1 命令

```sh
# 开发
mvn clean spring-boot:run -U

# 发布
mvn clean package -Dmaven.test.skip=true
```

### 1.3 发布

打包后，会在target目录下生成jar文件，将jar文件拷贝至目标服务器上，执行命令```java -jar -Dspring.profiles.active=prod xxx.jar```即可

### 1.4 错误告警及监控

日志输出请在*application-prod.yml*中配置，具体配置请查阅[spring-boot](https://docs.spring.io/spring-boot/docs/2.0.1.RELEASE/reference/htmlsingle/#boot-features-logging)文档


## 2. 使用介绍

### 2.1 接口

入口地址为 `http://localhost:8080`，目前有接口:

1. 获取抓取数据列表，带分页 ```GET http://localhost:8080/data```
2. 启动爬虫 ```GET http://localhost:8080/spider```
3. 添加爬虫配置 ```POST http://localhost:8080/spider```

具体接口使用见[API Doc](http://localhost:8080/swagger-ui.html)                                         |
