# my-template

一个长期维护的 Java 学习与实验仓库。项目最初是传统的 Spring MVC Web 模板，当前主要开发内容是一个 **3×3 魔方 CFOP 求解器**；仓库中还保留了设计模式、算法、Java 基础和实用工具等示例代码。

## 当前主要功能：CFOP 魔方求解器

`com.temp.cube` 提供了完整的魔方建模、打乱、转动、求解和结果验证能力。

- 生成随机 3×3 魔方打乱公式，也支持固定 seed 复现
- 解析 `R`、`U'`、`F2` 等标准转动记法
- 按 CFOP 的四个阶段求解：Cross、F2L、OLL、PLL
- 使用 IDA*、启发式距离表和公式匹配完成各阶段搜索
- 内置 57 个 OLL 和 21 个 PLL 标准公式
- 输出分阶段解法、步数统计以及最终还原验证结果

演示入口：`src/main/java/com/temp/cube/Main.java`

核心类：

```text
com.temp.cube
├── model/                  # 魔方与面的数据模型
├── generator/              # 随机打乱生成与公式解析
├── turn/                   # 转动动作和标准记法
├── solver/                 # CFOP 四阶段对外求解器
│   └── engine/             # 搜索、状态表示、距离表和阶段实现
├── constants/Algorithms    # OLL/PLL 公式表
└── result/                 # 求解结果与格式化输出
```

## 其他内容

这个仓库同时也是个人 Java 代码试验场，包含：

- Spring MVC、Spring JDBC、MyBatis 和 JSP Web 示例
- 常见设计模式示例
- LeetCode、排序和其他算法练习
- Java IO、NIO、并发、反射与 Guice 示例
- Excel/Word、HTTP、HTML 解析、图片和中文处理工具
- MySQL、MongoDB、Redis、RabbitMQ 等相关依赖和试验代码

这些模块之间大多相互独立，不应将整个仓库视作一个完整的生产业务应用。

## 技术栈

- JDK 21
- Maven 3.x
- JUnit 4、Mockito
- Spring Framework 5.3.39（保留 `javax.servlet` 命名空间）
- Spring MVC、Spring JDBC
- MyBatis 3.5.16
- Jetty 9.4
- Maven WAR 打包

项目还引入了 Redis、RabbitMQ、MySQL、MongoDB、Apache POI、Jsoup、Guava 等依赖，主要供历史示例和工具代码使用；运行魔方求解器不需要这些外部服务。

## 环境要求

- JDK 21+
- Maven 3.x

确认环境：

```bash
java -version
mvn -version
```

## 快速开始

### 运行魔方求解演示

```bash
mvn -DskipTests compile
java -cp target/classes com.temp.cube.Main
```

程序会生成一条随机打乱，依次输出 Cross、F2L、OLL、PLL 解法，并验证执行“打乱 + 解法”后魔方是否已经还原。

### 运行测试

```bash
# 全部测试
mvn test

# CFOP 主流程测试
mvn -Dtest=CFOPSolverTest test

# OLL/PLL 公式覆盖测试
mvn -Dtest=OllCoverageTest,PllCoverageTest test
```

部分历史测试属于演示程序，可能依赖本地环境或外部资源；开发魔方模块时可以优先运行 `com.temp.cube` 下的测试。

### 构建 WAR

```bash
mvn clean package
```

生成文件：`target/junyan.war`

## 运行 Web 示例

Web 部分是传统的 Spring MVC + JSP 示例，提供 `/test/get` 等简单接口。运行前请先检查并替换 `src/main/resources/application.properties` 中的数据库配置，不要直接使用仓库内的示例连接信息。

```bash
mvn jetty:run
```

默认访问地址：<http://localhost:8787/>

Web 相关配置：

```text
src/main/resources/application.properties   # 数据库配置
src/main/resources/applicationContext.xml   # Spring 与数据源配置
src/main/resources/spring-mvc.xml            # Spring MVC 配置
src/main/webapp/WEB-INF/web.xml              # Servlet 配置
```

## 项目结构

```text
src/
├── main/
│   ├── java/com/temp/
│   │   ├── cube/            # 当前主要开发的 CFOP 魔方求解器
│   │   ├── designPatterns/  # 设计模式示例
│   │   ├── leetcode/        # 算法练习
│   │   ├── rest/            # Spring MVC 示例接口
│   │   ├── service/         # Web 示例服务层
│   │   └── tool/            # 独立工具和实验代码
│   ├── resources/           # Spring、日志和应用配置
│   └── webapp/              # JSP 与 Web 资源
└── test/java/               # 魔方、Java 基础和框架测试
```

## 说明

- 工程使用 `javax.servlet`，因此 Jetty 固定在兼容该命名空间的 9.4 系列。
- 若升级到 Spring 6 或 Jetty 11+，需要先将相关代码迁移到 `jakarta.*`。
- 新功能应尽量避免继续依赖历史示例中的固定路径、远程服务或本机配置。

## License

[MIT License](LICENSE)
