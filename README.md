# 一、环境配置

* JDK 1.8
* IntelliJ IDEA 2018.1.4
* Gradle 4.8.1
* Spring Boot 2.0


# 二、数据库的表设计

详见 sql 包下的 init.sql


# 三、Mybatis-Generator

运行 Generator 类加载配置文件 mybatis-generator.xml ，在包 dao、model 下自动生成对应的类，并生成对应的 mapper 映射文件 


# 四、数据源

使用开源的 Druid 数据源监控。配置参考 DruidConfiguration


# 五、日志

使用 logback 框架记录日志。配置参考 logback.xml


# 六、JSP视图解析

因为要使用 Spring Boot 内置 Tomcat 容器，需添加相关依赖项：
```
    // Tomcat-Jasper
    compile('org.apache.tomcat.embed:tomcat-embed-jasper:9.0.6')
    // JSP
    compile('org.apache.tomcat:tomcat-jsp-api:9.0.7')
    compile('javax.servlet:javax.servlet-api:3.1.0')
    compile('javax.servlet:jstl:1.2')
```


# 七、Lombok

开发利器，通过注解 @Getter @Setter @Slf4j 简化代码量


# 八、Redis

引入 Redis 作为权限关系的缓存，详见 RedisConfiguration
