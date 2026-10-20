# 🎮 基于 Spring Cloud 的分布式游戏商城系统 (Game Business System)

> **归档时间**：2025 年 6 月  
> **所属阶段**：03-企业级架构与微服务实战  
> **作者**：吴琦  

---

## 📖 项目简介
本系统是一套基于 **Spring Cloud + Spring Boot + Spring Cloud Alibaba Nacos + MyBatis-Plus** 架构开发的分布式游戏数字商城系统。  
系统涵盖面向普通游戏玩家的**数字商城前台**（浏览游戏外设/数字周边、加入购物车、收藏、下单、地址管理）以及面向运营人员的**后台管理中台**（商品上架、分类管理、订单核销、系统字典、角色权限），实现了微服务架构下的高内聚低耦合拆分。

---

## 🛠️ 技术选型与架构设计

### 1. 技术栈
* **核心框架**：Spring Boot 2.x, Spring Cloud, Spring Cloud Alibaba
* **注册与配置中心**：Alibaba Nacos (127.0.0.1:8848)
* **服务通信**：OpenFeign 声明式 REST 客户端远程调用
* **持久层框架**：MyBatis-Plus + MySQL 8.0
* **前端展示**：Thymeleaf 模板引擎 + LayuiAdmin / Bootstrap + jQuery
* **构建工具**：Maven 聚合多模块构建

### 2. 微服务模块划分
```text
shop-parent (父工程，统一依赖版本管理)
├── common-api        # 通用公共模块：实体类(POJO)、DTO、通用响应结果Result、工具类与 Feign 契约
├── shop-service      # 核心业务微服务 (Port: 9008)
│   ├── 商品服务 (ProductController): 商品列表、详情、库存增删改查
│   ├── 购物车服务 (CartController): 购物车加购、数量变更、合并结算
│   ├── 订单服务 (OrderController): 创建订单、状态流转、订单详情与历史
│   ├── 收藏服务 (CollectController): 用户心愿单与收藏夹
│   ├── 用户与权限 (RoleController, SystemConfigController): 系统用户、角色分配
│   └── 地址服务 (LocationController): 收货地址管理
└── shop-web          # 前端交互与网关聚合服务 (Port: 8008)
    ├── Thymeleaf 渲染与模板路由
    ├── LayuiAdmin 后台管理视图
    └── 远程调用 shop-service 聚合数据返回前台页面
```

---

## 🌟 核心业务功能

### 1. 玩家端核心链路
* **商城大厅**：热门游戏周边、限时特惠、分类筛选与关键字检索。
* **商品详情**：多图轮播展示、规格参数、玩家评论区。
* **购物车与结算**：动态计算运费与总金额、多项选择合并下单。
* **订单全生命周期**：待支付、已支付、已发货、已完成流转。
* **个人中心**：收货地址增删改查、商品收藏夹、历史订单记录。

### 2. 管理端运营体系
* **商品管理**：周边与游戏分类管理、新品录入、库存预警与上下架。
* **订单追踪**：全局订单监控、物流单号录入、发货与退单核销。
* **角色与权限**：管理员角色分配、系统核心配置字典维护。

---

## 🗄️ 数据库与默认配置

### 1. 数据库脚本
* 脚本位于：[`sql/gbs12.sql`](sql/gbs12.sql)
* 默认数据库名：`gbs12`
* 数据库驱动：`com.mysql.cj.jdbc.Driver`
* 默认本地连接密码：`123456`（可在 `application.yml` 按需修改）

---

## 🚀 本地开发与启动指南

### 前置环境要求
1. JDK 1.8+
2. Maven 3.6+
3. MySQL 5.7 或 8.0
4. Nacos Server 2.x（启动单机模式：`startup.cmd -m standalone`）

### 启动步骤
1. **启动 Nacos**：确保 Nacos 服务端在 `127.0.0.1:8848` 正常运行。
2. **导入数据库**：在本地 MySQL 中执行 [`sql/gbs12.sql`](sql/gbs12.sql)。
3. **启动核心业务服务**：运行 `shop-service` 模块的启动类 `ShopServiceApplication9008.java`。
4. **启动前端聚合服务**：运行 `shop-web` 模块的主启动类。
5. **访问系统**：浏览器打开 `http://localhost:8008` 即可进入商城前台与管理后台。

---

## 📂 项目工程结构说明
```text
├── docs/                 # 答辩 PPT 与需求分析设计 Word 说明文档
│   ├── 基于SpringCloud的游戏商城 - 课程设计答辩.pptx
│   └── 基于SpringCloud的游戏商城需求分析文档.docx
├── sql/                  # 数据库表结构与预置商品数据
│   └── gbs12.sql
├── src/                  # 微服务聚合工程源码
│   ├── common-api/       # 通用公共模型模块
│   ├── shop-service/     # 核心服务提供者微服务 (9008)
│   ├── shop-web/         # 前端与管理端聚合微服务 (8008)
│   └── pom.xml           # 聚合根依赖管理
└── README.md             # 本说明文档
```

---

## 💡 成长心得与架构复盘
这个项目标志着从早期的单体应用开发正式跃升至**分布式微服务架构体系**：
1. **架构解耦**：体会到了将大型业务拆分为 `common-api`、`shop-service`、`shop-web` 后的职责边界划分；
2. **服务治理**：掌握了利用 Nacos 进行微服务实例的自动注册、心跳健康检查与发现机制；
3. **微服务调用链**：深入理解了 REST 客户端在分布式系统中的通信方式与异常降级容错机制。
