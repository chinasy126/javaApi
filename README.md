
# adminAPI

项目配置文件
```bash
application.properties
```

修改数据库连接
```bash
application.properties 文件内
# 数据库配置信息 修改对应的数据库名即可
spring.datasource.url=jdbc:mysql://localhost:3306/mydb?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&serverTimezone=Asia/Shanghai
# 用户库用户名
spring.datasource.username=root
# 数据库密码
spring.datasource.password=root
```
文件上传OSS 配置文件
```bash
application-aliyun-oss.properties
# 配置文件
# 地域节点   https://oss-cn-beijing.aliyuncs.com
aliyun.endPoint=https://oss-cn-beijing.aliyuncs.com
# Bucket 域名 / 自定义域名
aliyun.urlPrefix=https://***.oss-cn-beijing.aliyuncs.com/
# accessKey Id
aliyun.accessKeyId=****
# accessKey Secret
aliyun.accessKeySecret=****
# 仓库名
aliyun.bucketName=***
```

**拦截配置文件**
```bash
WebConfig.java
```

图片路径上传配置文件
```bash
HttpConverterConfig.java
```
swagger访问地址
```bash
http://localhost:8088/swagger-ui.html#/
```

项目数据库
```bash
目录中的mydb.sql文件
```