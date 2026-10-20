# 🏥 医院预约挂号系统 (Hospital Registration Platform)

> **研发与归档时间**：2023 年 11 月  
> **所属阶段**：01-萌芽与基础课设 (JavaWeb 实训与工程化立项实战)  
> **架构类型**：经典 JavaWeb (JSP + Servlet + JavaBean + 原生 JDBC)  
> **项目角色**：第一组项目负责人 / 核心开发 吴琦  

---

## 📖 项目简介
本项目是大学卓越工程师实训阶段作为小组负责人主导开发的**端到端医院在线预约挂号调度系统**。  
系统旨在解决传统医院窗口排队拥挤、就诊号源信息不透明等痛点，包含就诊患者**线上预约挂号门户**与院方**排班运营管理端**，实现了按科室查医生、按日期预约号源、取消预约、健康资讯宣教等完整业务链路。

---

## 🛠️ 技术栈与架构选型

* **开发语言**：Java (JDK 8 / 1.8)
* **表现层技术**：JSP (JavaServer Pages), JSTL, EL 表达式, HTML5, CSS3, JavaScript
* **控制层技术**：原生 Servlet (规范化 MVC 分层架构)
* **模型层技术**：JavaBean (POJO), DAO 数据访问对象模式
* **持久层与数据库**：原生 JDBC + MySQL (数据库名 `registered`)
* **Web 容器**：Apache Tomcat 8.5 / 9.0
* **辅助依赖**：`mysql-connector-java-5.0.8-bin.jar`, `taglibs-standard`, `jackson`

---

## 🌟 核心功能板块

### 1. 患者端挂号门户 (JSP 前台)
* **系统首页 (`index.jsp`)**：医院导航、名医推荐、就医指南、健康资讯轮播。
* **找医院与科室 (`hospital.jsp`, `hospitalSynopsis.jsp`)**：院区概况、重点特色专科、科室医生分布。
* **名医排班查询 (`doctor.jsp`)**：医生职称（主任/副主任医师）、擅长病症、周排班日历。
* **在线挂号预约 (`booking.jsp`)**：选择就诊时间段（上午/下午）、填报就诊人信息、生成预约单号。
* **个人中心 (`my.jsp`)**：查看我的预约历史、就诊状态追踪、支持就诊前在线退号取消。
* **患者认证 (`login1.jsp`, `login2.jsp`)**：支持用户注册、手机号密码登录。

### 2. 院方管理后台 (`adminMy.jsp`)
* **排班号源管理**：科室出诊医生调配、每日限号名额配置。
* **挂号履约核销**：患者到院取号状态确认、违约爽约记录。
* **科室与资讯维护**：健康宣教资讯发布、科室介绍更新。

---

## 🗄️ 数据库脚本与一手立项文档

### 1. 数据库脚本
* 脚本位于：[`sql/registered.sql`](sql/registered.sql)
* 默认数据库名：`registered`
* 核心数据表包含：医院信息表、科室表 (`Department`)、医生排班表 (`Doctor`)、患者用户表 (`User`)、挂号预约订单表 (`Orders`) 等。

### 2. 完整工程化实训文档
* 本次归档完整保留了项目规划与设计的官方文档：
  * [`docs/第一组-预约挂号网-预约挂号网项目立项及开发规划说明书-吴琦.doc`](docs/第一组-预约挂号网-预约挂号网项目立项及开发规划说明书-吴琦.doc)
  * [`docs/第一组-挂号通需求规格说明书-吴琦.doc`](docs/第一组-挂号通需求规格说明书-吴琦.doc)
  * [`docs/第一组-挂号通项目-表设计-吴琦.xlsm`](docs/第一组-挂号通项目-表设计-吴琦.xlsm)
  * [`docs/第一组-挂号通项目开发计划表-吴琦.xls`](docs/第一组-挂号通项目开发计划表-吴琦.xls)

---

## 🚀 本地运行与部署指南

### 1. 导入数据库
1. 启动本地 MySQL 服务；
2. 新建数据库 `registered`；
3. 执行导入 [`sql/registered.sql`](sql/registered.sql)；
4. 如有需要，可在 `src/src/db/DBHelper.java` 中调整数据库连接账号与密码（默认 `root` / `123456`）。

### 2. 部署到 Tomcat
1. 在 Eclipse for Java EE 或 IntelliJ IDEA 中导入本工程；
2. 配置项目 Facets 为 Dynamic Web Module；
3. 关联 Apache Tomcat 8.5+ 服务器；
4. 部署应用并启动 Tomcat；
5. 浏览器访问：`http://localhost:8080/registration/index.jsp`。

---

## 📂 仓库工程结构
```text
├── docs/                 # 团队立项书、需求规格说明书、表结构设计与甘特计划表
├── sql/                  # 数据库表结构与预置医生排班数据 (registered.sql)
├── src/                  # 系统完整源码
│   ├── src/              # 后端 Java 源码 (DAO、DBHelper、Servlet 控制器、POJO)
│   └── WebContent/       # JSP 视图文件、静态资源 (css/、img/) 与 WEB-INF 配置
└── README.md             # 本说明文档
```

---

## 💡 成长复盘与体会
这是我大学时期第一次作为项目负责人（组长）带领团队完成的完整软件工程项目：
1. **工程化流程意识**：不仅要写代码，还要完成从立项规划、甘特图排期、需求规格说明到数据库表结构设计的全流程文档交付；
2. **MVC 设计模式落地**：通过 Servlet 承接 HTTP 请求，调度 DAO 操作数据库，将数据封装入 JavaBean 并由 JSP 渲染展示，真正理解了分层解耦的精髓；
3. 为后续深入学习 Spring、Spring MVC、MyBatis 以及分布式高并发系统打下了极牢固的底层基础。
