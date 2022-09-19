# 人工智能-icu

## 技术栈

- Spring-Boot
- Sa-Token
- Mybatis-Plus
- MySQL
- Redis

## 启动

首先，你需要一个application.yml,将其中的内容替换成自己的

```yaml
spring:
  servlet:
    multipart:
      enabled: true
      max-file-size: 2MB   #单个文件的最大上限
      max-request-size: 3MB #单个请求的文件总大小上限
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/rgzn?serverTimezone=Asia/Shanghai
    username: root
    password: 1234
    type: com.alibaba.druid.pool.DruidDataSource
  jackson:
    deserialization.UNWRAP_ROOT_VALUE: true
  cache:
    type: redis
    cache-names: user
    redis:
      time-to-live: 86400
      cache-null-values: false


  redis:
    host: localhost  #修改端口
    port: 6379
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
        max-wait: 1000ms
  tengxun:
    secretId: 
    secretKey: 
    region: 
    url: 
    bucketName: 
    fileType:
      - ".jpg"
      - ".png"
      - ".jpeg"
      - ".webp"
      - ".gif"


mybatis-plus:
  configuration:
     log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  mapper-locations: mapper/*.xml


jwt:
  secret: 
  sessionTime: 86400

mail:
  # 配置 SMTP 服务器地址
  host: smtp.163.com
  # 发送者邮箱
  username: 
  # 配置密码，注意不是真正的密码，而是刚刚申请到的授权码
  password:
  # 端口号163默认为 465 994
  port: 465
  # 默认的邮件编码为UTF-8


logging:
  file:
    max-size: 10MB
    name: /root/blog/logs/logger.log
  level:
    com.fischer: info
    org.springframework: info

# Sa-Token配置
sa-token:
  # token 名称 (同时也是cookie名称)
  token-name: Authorization
  # token 有效期，单位s 默认30天, -1代表永不过期
  timeout: 2592000
  # token 临时有效期 (指定时间内无操作就视为token过期) 单位: 秒
  activity-timeout: -1
  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)
  is-concurrent: true
  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)
  is-share: false
  # token风格
  token-style: uuid
  # 是否输出操作日志
  is-log: false
```

