# my-template

Java Web 项目模板，基于 Spring MVC + MyBatis 架构。

## 技术栈

### 核心框架
- **Spring Framework** 4.1.7.RELEASE
- **Spring MVC** - Web 层
- **MyBatis** 3.2.8 - 持久层框架
- **Spring Data Redis** 1.6.0.RELEASE - Redis 支持
- **Spring RabbitMQ** 1.6.0.RELEASE - 消息队列支持

### 中间件
- **MySQL** - 关系型数据库
- **MongoDB** - 文档数据库
- **Redis** - 缓存
- **RabbitMQ** - 消息队列

### 主要依赖
- **Lombok** - 简化代码
- **Jackson/FastJSON/Gson** - JSON 处理
- **Apache Commons** - 工具类库
- **Google Guava** - 工具类库
- **Apache POI** - Office 文档处理
- **Jsoup** - HTML 解析
- **Thumbnailator** - 图片处理
- **Pinyin4j/HanLP** - 中文处理

### 构建配置
- **JDK** 21
- **Maven** 构建
- **WAR** 打包

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/           # Java 源代码
│   ├── resources/
│   │   ├── application.properties    # 应用配置
│   │   ├── applicationContext.xml    # Spring 主配置
│   │   ├── spring-mvc.xml            # Spring MVC 配置
│   │   └── logback.xml               # 日志配置
│   └── webapp/            # Web 资源
└── test/
    └── java/              # 测试代码
```

## 快速开始

### 环境要求
- JDK 21+
- Maven 3.x

### 构建项目
```bash
mvn clean package
```

### 运行项目
```bash
# 使用 Jetty 运行
mvn jetty:run
# 访问 http://localhost:8787/
```

### 打包部署
```bash
mvn clean package
# 生成的 WAR 包：target/junyan.war
```

## 开发工具

### IDE 配置
```bash
# 生成 Eclipse 配置
mvn eclipse:eclipse
```

### 代码质量
```bash
# 生成 Javadoc
mvn javadoc:javadoc

# 代码覆盖率
mvn cobertura:cobertura
```

## 配置说明

### 数据库配置
编辑 `src/main/resources/application.properties` 配置数据库连接。

### Redis 配置
编辑 `src/main/resources/applicationContext.xml` 配置 Redis 连接。

### RabbitMQ 配置
编辑 `src/main/resources/applicationContext.xml` 配置 RabbitMQ 连接。

## 测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=MyTest
```

## 许可证

MIT License
