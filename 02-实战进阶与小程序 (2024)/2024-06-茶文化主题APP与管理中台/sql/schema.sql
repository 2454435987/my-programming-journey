-- ============================================================
-- 茶文化主题 App 数据库 schema（MySQL/MariaDB 8 语法兼容 10.11）
-- 数据库名：tea_culture
-- ============================================================
DROP DATABASE IF EXISTS tea_culture;
CREATE DATABASE tea_culture DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tea_culture;

-- ---------- 1. 系统管理员 ----------
CREATE TABLE sys_admin (
  admin_id      BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '管理员ID',
  username      VARCHAR(50)  NOT NULL COMMENT '登录账号',
  password      VARCHAR(100) NOT NULL COMMENT '密码(BCrypt)',
  real_name     VARCHAR(50)  DEFAULT '' COMMENT '姓名',
  avatar        VARCHAR(255) DEFAULT '' COMMENT '头像',
  email         VARCHAR(100) DEFAULT '' COMMENT '邮箱',
  phone         VARCHAR(20)  DEFAULT '' COMMENT '手机号',
  status        TINYINT      DEFAULT 1 COMMENT '状态 1启用 0禁用',
  last_login    DATETIME     DEFAULT NULL COMMENT '最后登录时间',
  created_at    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY uk_username (username)
) ENGINE=InnoDB COMMENT='系统管理员表';

-- ---------- 2. 角色 ----------
CREATE TABLE sys_role (
  role_id     BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
  role_code   VARCHAR(50)  NOT NULL COMMENT '角色编码',
  role_name   VARCHAR(50)  NOT NULL COMMENT '角色名称',
  description VARCHAR(200) DEFAULT '' COMMENT '描述',
  status      TINYINT      DEFAULT 1 COMMENT '状态 1启用 0禁用',
  created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY uk_code (role_code)
) ENGINE=InnoDB COMMENT='角色表';

-- ---------- 3. 管理员-角色 ----------
CREATE TABLE sys_admin_role (
  id        BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  admin_id  BIGINT NOT NULL COMMENT '管理员ID',
  role_id   BIGINT NOT NULL COMMENT '角色ID',
  KEY idx_admin (admin_id),
  KEY idx_role (role_id)
) ENGINE=InnoDB COMMENT='管理员角色关联表';

-- ---------- 4. 菜单/权限 ----------
CREATE TABLE sys_menu (
  menu_id     BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '菜单ID',
  parent_id   BIGINT       DEFAULT 0 COMMENT '父菜单ID',
  menu_name   VARCHAR(50)  NOT NULL COMMENT '菜单名称',
  path        VARCHAR(100) DEFAULT '' COMMENT '路由路径',
  perms       VARCHAR(100) DEFAULT '' COMMENT '权限标识',
  menu_type   TINYINT      DEFAULT 2 COMMENT '类型 1目录 2菜单 3按钮',
  sort_order  INT          DEFAULT 0 COMMENT '排序',
  icon        VARCHAR(50)  DEFAULT '' COMMENT '图标',
  visible     TINYINT      DEFAULT 1 COMMENT '是否可见',
  created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB COMMENT='菜单权限表';

-- ---------- 5. 角色-菜单 ----------
CREATE TABLE sys_role_menu (
  id      BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  role_id BIGINT NOT NULL COMMENT '角色ID',
  menu_id BIGINT NOT NULL COMMENT '菜单ID',
  KEY idx_role (role_id),
  KEY idx_menu (menu_id)
) ENGINE=InnoDB COMMENT='角色菜单关联表';

-- ---------- 6. App 用户 ----------
CREATE TABLE app_user (
  user_id     BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  username    VARCHAR(50)  NOT NULL COMMENT '用户名',
  password    VARCHAR(100) NOT NULL COMMENT '密码(BCrypt)',
  nickname    VARCHAR(50)  DEFAULT '' COMMENT '昵称',
  avatar      VARCHAR(255) DEFAULT '' COMMENT '头像',
  gender      TINYINT      DEFAULT 0 COMMENT '性别 0未知 1男 2女',
  phone       VARCHAR(20)  DEFAULT '' COMMENT '手机号',
  email       VARCHAR(100) DEFAULT '' COMMENT '邮箱',
  points      INT          DEFAULT 0 COMMENT '积分',
  level       TINYINT      DEFAULT 1 COMMENT '会员等级',
  status      TINYINT      DEFAULT 1 COMMENT '状态 1正常 0禁用',
  signature   VARCHAR(200) DEFAULT '' COMMENT '个性签名',
  created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  updated_at  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY uk_username (username)
) ENGINE=InnoDB COMMENT='App用户表';

-- ---------- 7. 资讯/知识分类 ----------
CREATE TABLE tea_category (
  category_id   BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
  category_name VARCHAR(50)  NOT NULL COMMENT '分类名称',
  category_type TINYINT      NOT NULL COMMENT '类型 1知识 2商品 3教程',
  description   VARCHAR(200) DEFAULT '' COMMENT '描述',
  icon          VARCHAR(255) DEFAULT '' COMMENT '图标',
  sort_order    INT          DEFAULT 0 COMMENT '排序',
  status        TINYINT      DEFAULT 1 COMMENT '状态',
  created_at    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB COMMENT='分类表(知识/商品/教程共用)';

-- ---------- 8. 茶文化知识 ----------
CREATE TABLE tea_knowledge (
  knowledge_id  BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '知识ID',
  category_id   BIGINT       DEFAULT 0 COMMENT '分类ID',
  title         VARCHAR(100) NOT NULL COMMENT '标题',
  summary       VARCHAR(500) DEFAULT '' COMMENT '摘要',
  content       TEXT         COMMENT '正文',
  cover_image   VARCHAR(255) DEFAULT '' COMMENT '封面图',
  author        VARCHAR(50)  DEFAULT '' COMMENT '作者',
  source        VARCHAR(100) DEFAULT '' COMMENT '来源',
  view_count    INT          DEFAULT 0 COMMENT '浏览量',
  like_count    INT          DEFAULT 0 COMMENT '点赞量',
  is_top        TINYINT      DEFAULT 0 COMMENT '是否置顶',
  status        TINYINT      DEFAULT 1 COMMENT '状态 1发布 0草稿',
  created_at    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  updated_at    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  KEY idx_category (category_id),
  KEY idx_status (status)
) ENGINE=InnoDB COMMENT='茶文化知识表';

-- ---------- 9. 商品分类冗余到 tea_category，此处为茶品商品 ----------
CREATE TABLE tea_product (
  product_id    BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
  category_id   BIGINT         DEFAULT 0 COMMENT '分类ID',
  product_name  VARCHAR(100)   NOT NULL COMMENT '商品名称',
  subtitle      VARCHAR(200)   DEFAULT '' COMMENT '副标题',
  cover_image   VARCHAR(255)   DEFAULT '' COMMENT '主图',
  images        TEXT           COMMENT '图集,逗号分隔',
  price         DECIMAL(10,2)  DEFAULT 0 COMMENT '售价',
  original_price DECIMAL(10,2) DEFAULT 0 COMMENT '原价',
  stock         INT            DEFAULT 0 COMMENT '库存',
  sales         INT            DEFAULT 0 COMMENT '销量',
  unit          VARCHAR(20)    DEFAULT '盒' COMMENT '单位',
  origin        VARCHAR(50)    DEFAULT '' COMMENT '产地',
  description   TEXT           COMMENT '商品详情',
  status        TINYINT        DEFAULT 1 COMMENT '状态 1上架 0下架',
  created_at    DATETIME       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at    DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  KEY idx_category (category_id),
  KEY idx_status (status)
) ENGINE=InnoDB COMMENT='茶品商品表';

-- ---------- 10. 茶艺教程 ----------
CREATE TABLE tea_course (
  course_id    BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '教程ID',
  category_id  BIGINT       DEFAULT 0 COMMENT '分类ID',
  title        VARCHAR(100) NOT NULL COMMENT '教程标题',
  summary      VARCHAR(500) DEFAULT '' COMMENT '简介',
  content      TEXT         COMMENT '图文内容',
  cover_image  VARCHAR(255) DEFAULT '' COMMENT '封面图',
  video_url    VARCHAR(255) DEFAULT '' COMMENT '视频地址',
  level        TINYINT      DEFAULT 1 COMMENT '难度 1入门 2进阶 3高级',
  duration     INT          DEFAULT 0 COMMENT '时长(分钟)',
  view_count   INT          DEFAULT 0 COMMENT '浏览量',
  like_count   INT          DEFAULT 0 COMMENT '点赞量',
  is_free      TINYINT      DEFAULT 1 COMMENT '是否免费',
  price        DECIMAL(10,2) DEFAULT 0 COMMENT '价格',
  status       TINYINT      DEFAULT 1 COMMENT '状态 1发布 0下架',
  created_at   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  KEY idx_category (category_id)
) ENGINE=InnoDB COMMENT='茶艺教程表';

-- ---------- 11. 社区帖子 ----------
CREATE TABLE community_post (
  post_id     BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '帖子ID',
  user_id     BIGINT       NOT NULL COMMENT '发帖用户ID',
  title       VARCHAR(100) DEFAULT '' COMMENT '标题',
  content     TEXT         COMMENT '正文',
  images      TEXT         COMMENT '图片,逗号分隔',
  topic       VARCHAR(50)  DEFAULT '' COMMENT '话题标签',
  view_count  INT          DEFAULT 0 COMMENT '浏览量',
  like_count  INT          DEFAULT 0 COMMENT '点赞量',
  comment_count INT        DEFAULT 0 COMMENT '评论数',
  is_top      TINYINT      DEFAULT 0 COMMENT '是否置顶',
  is_essence  TINYINT      DEFAULT 0 COMMENT '是否精华',
  status      TINYINT      DEFAULT 1 COMMENT '状态 1可见 0隐藏',
  created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  updated_at  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  KEY idx_user (user_id),
  KEY idx_status (status)
) ENGINE=InnoDB COMMENT='社区帖子表';

-- ---------- 12. 帖子评论 ----------
CREATE TABLE post_comment (
  comment_id  BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
  post_id     BIGINT       NOT NULL COMMENT '帖子ID',
  user_id     BIGINT       NOT NULL COMMENT '评论用户ID',
  parent_id   BIGINT       DEFAULT 0 COMMENT '父评论ID',
  content     VARCHAR(500) NOT NULL COMMENT '评论内容',
  like_count  INT          DEFAULT 0 COMMENT '点赞数',
  status      TINYINT      DEFAULT 1 COMMENT '状态 1可见 0隐藏',
  created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  KEY idx_post (post_id),
  KEY idx_user (user_id)
) ENGINE=InnoDB COMMENT='帖子评论表';

-- ---------- 13. 茶事活动 ----------
CREATE TABLE tea_activity (
  activity_id    BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '活动ID',
  title          VARCHAR(100) NOT NULL COMMENT '活动标题',
  summary        VARCHAR(500) DEFAULT '' COMMENT '活动简介',
  content        TEXT         COMMENT '活动详情',
  cover_image    VARCHAR(255) DEFAULT '' COMMENT '封面图',
  activity_type  VARCHAR(30)  DEFAULT '线下' COMMENT '活动类型',
  location       VARCHAR(200) DEFAULT '' COMMENT '活动地点',
  start_time     DATETIME     DEFAULT NULL COMMENT '开始时间',
  end_time       DATETIME     DEFAULT NULL COMMENT '结束时间',
  enroll_deadline DATETIME    DEFAULT NULL COMMENT '报名截止',
  max_enroll     INT          DEFAULT 0 COMMENT '最大报名人数',
  enrolled_count INT          DEFAULT 0 COMMENT '已报名人数',
  price          DECIMAL(10,2) DEFAULT 0 COMMENT '费用 0免费',
  status         TINYINT      DEFAULT 1 COMMENT '状态 1报名中 2进行中 3已结束 0取消',
  created_at     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  KEY idx_status (status)
) ENGINE=InnoDB COMMENT='茶事活动表';

-- ---------- 14. 活动报名 ----------
CREATE TABLE activity_enroll (
  enroll_id   BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报名ID',
  activity_id BIGINT       NOT NULL COMMENT '活动ID',
  user_id     BIGINT       NOT NULL COMMENT '用户ID',
  real_name   VARCHAR(50)  DEFAULT '' COMMENT '姓名',
  phone       VARCHAR(20)  DEFAULT '' COMMENT '手机号',
  remark      VARCHAR(200) DEFAULT '' COMMENT '备注',
  status      TINYINT      DEFAULT 1 COMMENT '状态 1已报名 2已取消 3已签到',
  created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
  UNIQUE KEY uk_act_user (activity_id, user_id),
  KEY idx_user (user_id)
) ENGINE=InnoDB COMMENT='活动报名表';

-- ---------- 15. 收货地址 ----------
CREATE TABLE user_address (
  address_id  BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '地址ID',
  user_id     BIGINT       NOT NULL COMMENT '用户ID',
  receiver    VARCHAR(50)  NOT NULL COMMENT '收货人',
  phone       VARCHAR(20)  NOT NULL COMMENT '手机号',
  province    VARCHAR(50)  DEFAULT '' COMMENT '省',
  city        VARCHAR(50)  DEFAULT '' COMMENT '市',
  district    VARCHAR(50)  DEFAULT '' COMMENT '区',
  detail      VARCHAR(200) NOT NULL COMMENT '详细地址',
  is_default  TINYINT      DEFAULT 0 COMMENT '是否默认',
  created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  KEY idx_user (user_id)
) ENGINE=InnoDB COMMENT='收货地址表';

-- ---------- 16. 购物车 ----------
CREATE TABLE cart_item (
  cart_id     BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '购物车ID',
  user_id     BIGINT NOT NULL COMMENT '用户ID',
  product_id  BIGINT NOT NULL COMMENT '商品ID',
  quantity    INT    DEFAULT 1 COMMENT '数量',
  checked     TINYINT DEFAULT 1 COMMENT '是否选中',
  created_at  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  KEY idx_user (user_id)
) ENGINE=InnoDB COMMENT='购物车表';

-- ---------- 17. 订单 ----------
CREATE TABLE orders (
  order_id      BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
  order_no      VARCHAR(32)  NOT NULL COMMENT '订单编号',
  user_id       BIGINT       NOT NULL COMMENT '用户ID',
  total_amount  DECIMAL(10,2) DEFAULT 0 COMMENT '订单总额',
  pay_amount    DECIMAL(10,2) DEFAULT 0 COMMENT '实付金额',
  freight       DECIMAL(10,2) DEFAULT 0 COMMENT '运费',
  status        TINYINT      DEFAULT 0 COMMENT '状态 0待付款 1待发货 2待收货 3已完成 4已取消 5已退款',
  pay_type      TINYINT      DEFAULT 1 COMMENT '支付方式 1微信 2支付宝 3余额',
  pay_time      DATETIME     DEFAULT NULL COMMENT '支付时间',
  ship_time     DATETIME     DEFAULT NULL COMMENT '发货时间',
  receive_time  DATETIME     DEFAULT NULL COMMENT '收货时间',
  receiver      VARCHAR(50)  DEFAULT '' COMMENT '收货人',
  receiver_phone VARCHAR(20) DEFAULT '' COMMENT '收货电话',
  receiver_address VARCHAR(300) DEFAULT '' COMMENT '收货地址',
  remark        VARCHAR(200) DEFAULT '' COMMENT '买家备注',
  created_at    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  updated_at    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY uk_order_no (order_no),
  KEY idx_user (user_id),
  KEY idx_status (status)
) ENGINE=InnoDB COMMENT='订单表';

-- ---------- 18. 订单明细 ----------
CREATE TABLE order_item (
  item_id      BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
  order_id     BIGINT       NOT NULL COMMENT '订单ID',
  order_no     VARCHAR(32)  DEFAULT '' COMMENT '订单编号',
  product_id   BIGINT       NOT NULL COMMENT '商品ID',
  product_name VARCHAR(100) DEFAULT '' COMMENT '商品名称(快照)',
  product_image VARCHAR(255) DEFAULT '' COMMENT '商品图(快照)',
  price        DECIMAL(10,2) DEFAULT 0 COMMENT '成交单价',
  quantity     INT          DEFAULT 1 COMMENT '数量',
  KEY idx_order (order_id)
) ENGINE=InnoDB COMMENT='订单明细表';

-- ---------- 19. 消息通知 ----------
CREATE TABLE message_notice (
  message_id   BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID',
  user_id      BIGINT       DEFAULT 0 COMMENT '接收用户ID 0为系统广播',
  title        VARCHAR(100) NOT NULL COMMENT '消息标题',
  content      VARCHAR(500) DEFAULT '' COMMENT '消息内容',
  msg_type     TINYINT      DEFAULT 1 COMMENT '类型 1系统 2订单 3活动 4互动',
  is_read      TINYINT      DEFAULT 0 COMMENT '是否已读',
  created_at   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  KEY idx_user (user_id)
) ENGINE=InnoDB COMMENT='消息通知表';

-- ---------- 20. 轮播图/运营位 ----------
CREATE TABLE banner (
  banner_id   BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'BannerID',
  title       VARCHAR(100) NOT NULL COMMENT '标题',
  image_url   VARCHAR(255) NOT NULL COMMENT '图片地址',
  link_url    VARCHAR(255) DEFAULT '' COMMENT '跳转链接',
  position    VARCHAR(30)  DEFAULT 'home' COMMENT '位置 home/activity/mall',
  sort_order  INT          DEFAULT 0 COMMENT '排序',
  status      TINYINT      DEFAULT 1 COMMENT '状态 1启用 0停用',
  start_time  DATETIME     DEFAULT NULL COMMENT '生效时间',
  end_time    DATETIME     DEFAULT NULL COMMENT '失效时间',
  created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB COMMENT='轮播运营位表';

-- ---------- 21. 用户收藏 ----------
CREATE TABLE user_favorite (
  favorite_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
  user_id     BIGINT      NOT NULL COMMENT '用户ID',
  target_id   BIGINT      NOT NULL COMMENT '目标ID',
  target_type TINYINT     DEFAULT 1 COMMENT '类型 1知识 2商品 3教程 4帖子 5活动',
  created_at  DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  UNIQUE KEY uk_user_target (user_id, target_id, target_type)
) ENGINE=InnoDB COMMENT='用户收藏表';

-- ---------- 22. 操作日志 ----------
CREATE TABLE sys_operation_log (
  log_id      BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
  admin_id    BIGINT       DEFAULT 0 COMMENT '操作人ID',
  admin_name  VARCHAR(50)  DEFAULT '' COMMENT '操作人',
  operation   VARCHAR(100) DEFAULT '' COMMENT '操作内容',
  method      VARCHAR(200) DEFAULT '' COMMENT '请求方法',
  params      VARCHAR(1000) DEFAULT '' COMMENT '请求参数',
  ip          VARCHAR(50)  DEFAULT '' COMMENT 'IP',
  status      TINYINT      DEFAULT 1 COMMENT '状态 1成功 0失败',
  created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间'
) ENGINE=InnoDB COMMENT='操作日志表';

-- ---------- 23. 知识/帖子点赞 ----------
CREATE TABLE content_like (
  like_id     BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '点赞ID',
  user_id     BIGINT  NOT NULL COMMENT '用户ID',
  target_id   BIGINT  NOT NULL COMMENT '目标ID',
  target_type TINYINT DEFAULT 1 COMMENT '类型 1知识 2教程 3帖子',
  created_at  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  UNIQUE KEY uk_user_target (user_id, target_id, target_type)
) ENGINE=InnoDB COMMENT='内容点赞表';
