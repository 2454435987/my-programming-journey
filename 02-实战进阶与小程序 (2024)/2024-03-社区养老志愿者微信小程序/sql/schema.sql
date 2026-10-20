-- =====================================================================
-- 社区养老志愿者微信小程序 —— 数据库建表脚本
-- 数据库: community_elder_care (MySQL 8 / MariaDB 10.11, utf8mb4)
-- 引擎: InnoDB
-- =====================================================================
DROP DATABASE IF EXISTS community_elder_care;
CREATE DATABASE community_elder_care DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE community_elder_care;

-- ---------------------------------------------------------------------
-- 1. 系统用户表 sys_user: 记录微信授权登录后的账号主体
-- ---------------------------------------------------------------------
CREATE TABLE sys_user (
    id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    openid        VARCHAR(64)  NOT NULL COMMENT '微信小程序OpenID',
    nickname      VARCHAR(64)  DEFAULT '' COMMENT '微信昵称',
    avatar        VARCHAR(255) DEFAULT '' COMMENT '头像地址',
    role          VARCHAR(16)  NOT NULL COMMENT '角色: elder老人 / volunteer志愿者 / admin管理员',
    real_name     VARCHAR(32)  DEFAULT '' COMMENT '真实姓名',
    phone         VARCHAR(20)  DEFAULT '' COMMENT '手机号',
    id_card       VARCHAR(20)  DEFAULT '' COMMENT '身份证号',
    status        TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0禁用 1正常 2待审核',
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_openid (openid),
    KEY idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ---------------------------------------------------------------------
-- 2. 老人档案表 elder_info: 社区老人基础信息与健康备注(管理员审核录入)
-- ---------------------------------------------------------------------
CREATE TABLE elder_info (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '档案ID',
    user_id          BIGINT       NOT NULL COMMENT '关联用户ID',
    elder_name       VARCHAR(32)  NOT NULL COMMENT '老人姓名',
    gender           VARCHAR(4)   DEFAULT '男' COMMENT '性别',
    phone            VARCHAR(20)  DEFAULT '' COMMENT '联系电话',
    birth_date       DATE         DEFAULT NULL COMMENT '出生日期',
    community        VARCHAR(64)  DEFAULT '' COMMENT '所属社区',
    address          VARCHAR(128) DEFAULT '' COMMENT '居住地址',
    health_note      VARCHAR(255) DEFAULT '' COMMENT '健康备注',
    emergency_contact VARCHAR(32) DEFAULT '' COMMENT '紧急联系人',
    emergency_phone  VARCHAR(20)  DEFAULT '' COMMENT '紧急联系人电话',
    lng              DECIMAL(10,6) DEFAULT 0 COMMENT '经度(紧急求助就近匹配)',
    lat              DECIMAL(10,6) DEFAULT 0 COMMENT '纬度',
    bind_status      TINYINT      NOT NULL DEFAULT 0 COMMENT '绑定状态: 0未绑定 1已绑定 2审核中',
    audit_status     TINYINT      NOT NULL DEFAULT 0 COMMENT '审核状态: 0待审核 1通过 2驳回',
    audit_remark     VARCHAR(128) DEFAULT '' COMMENT '审核备注',
    audit_by         BIGINT       DEFAULT NULL COMMENT '审核管理员ID',
    audit_time       DATETIME     DEFAULT NULL COMMENT '审核时间',
    create_time      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_user (user_id),
    KEY idx_audit (audit_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='老人档案表';

-- ---------------------------------------------------------------------
-- 3. 志愿者档案表 volunteer_info: 实名认证、技能与可服务时段
-- ---------------------------------------------------------------------
CREATE TABLE volunteer_info (
    id             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '志愿者ID',
    user_id        BIGINT       NOT NULL COMMENT '关联用户ID',
    vol_name       VARCHAR(32)  NOT NULL COMMENT '志愿者姓名',
    gender         VARCHAR(4)   DEFAULT '男' COMMENT '性别',
    phone          VARCHAR(20)  DEFAULT '' COMMENT '联系电话',
    id_card        VARCHAR(20)  DEFAULT '' COMMENT '身份证号(实名认证)',
    skills         VARCHAR(128) DEFAULT '' COMMENT '技能标签,逗号分隔',
    serve_period   VARCHAR(64)  DEFAULT '' COMMENT '可服务时段',
    service_area   VARCHAR(64)  DEFAULT '' COMMENT '可服务区域',
    lng            DECIMAL(10,6) DEFAULT 0 COMMENT '经度',
    lat            DECIMAL(10,6) DEFAULT 0 COMMENT '纬度',
    points         INT          NOT NULL DEFAULT 0 COMMENT '志愿积分',
    total_minutes  INT          NOT NULL DEFAULT 0 COMMENT '累计服务时长(分钟)',
    audit_status   TINYINT      NOT NULL DEFAULT 0 COMMENT '审核状态: 0待审核 1通过 2驳回',
    audit_remark   VARCHAR(128) DEFAULT '' COMMENT '审核备注',
    audit_by       BIGINT       DEFAULT NULL COMMENT '审核管理员ID',
    audit_time     DATETIME     DEFAULT NULL COMMENT '审核时间',
    create_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    PRIMARY KEY (id),
    KEY idx_user (user_id),
    KEY idx_audit (audit_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='志愿者档案表';

-- ---------------------------------------------------------------------
-- 4. 服务需求表 service_demand: 老人发布的养老服务需求
-- ---------------------------------------------------------------------
CREATE TABLE service_demand (
    id             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '需求ID',
    demand_no      VARCHAR(32)  NOT NULL COMMENT '需求编号',
    elder_id       BIGINT       NOT NULL COMMENT '发布老人档案ID',
    category       VARCHAR(16)  NOT NULL COMMENT '服务类别: 上门陪伴/助餐/助洁/代购',
    title          VARCHAR(64)  NOT NULL COMMENT '需求标题',
    description    VARCHAR(512) DEFAULT '' COMMENT '需求描述',
    service_time   DATETIME     NOT NULL COMMENT '期望服务时间',
    address        VARCHAR(128) DEFAULT '' COMMENT '服务地址',
    lng            DECIMAL(10,6) DEFAULT 0 COMMENT '经度',
    lat            DECIMAL(10,6) DEFAULT 0 COMMENT '纬度',
    status         VARCHAR(16)  NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING待接单/SERVING服务中/DONE已完成/CANCELLED已取消',
    volunteer_id   BIGINT       DEFAULT NULL COMMENT '接单志愿者ID',
    accept_time    DATETIME     DEFAULT NULL COMMENT '接单时间',
    start_time     DATETIME     DEFAULT NULL COMMENT '服务开始时间',
    finish_time    DATETIME     DEFAULT NULL COMMENT '服务完成时间',
    audit_status   TINYINT      NOT NULL DEFAULT 0 COMMENT '平台审核: 0待审核 1通过 2驳回',
    audit_remark   VARCHAR(128) DEFAULT '' COMMENT '审核备注',
    create_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_demand_no (demand_no),
    KEY idx_elder (elder_id),
    KEY idx_status (status),
    KEY idx_volunteer (volunteer_id),
    KEY idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='养老服务需求表';

-- ---------------------------------------------------------------------
-- 5. 志愿任务表 volunteer_task: 管理员发布的社区志愿任务
-- ---------------------------------------------------------------------
CREATE TABLE volunteer_task (
    id             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    task_no        VARCHAR(32)  NOT NULL COMMENT '任务编号',
    publisher_id   BIGINT       NOT NULL COMMENT '发布管理员ID',
    title          VARCHAR(64)  NOT NULL COMMENT '任务标题',
    category       VARCHAR(16)  NOT NULL COMMENT '任务类别',
    description    VARCHAR(512) DEFAULT '' COMMENT '任务描述',
    require_skills VARCHAR(128) DEFAULT '' COMMENT '所需技能',
    service_time   DATETIME     NOT NULL COMMENT '服务时间',
    address        VARCHAR(128) DEFAULT '' COMMENT '服务地点',
    required_count INT          NOT NULL DEFAULT 1 COMMENT '需要人数',
    joined_count   INT          NOT NULL DEFAULT 0 COMMENT '已报名人数',
    status         VARCHAR(16)  NOT NULL DEFAULT 'OPEN' COMMENT '状态: OPEN招募中/MATCHED已分配/DONE已完成/CLOSED已关闭',
    create_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_task_no (task_no),
    KEY idx_status (status),
    KEY idx_publisher (publisher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='志愿任务表';

-- ---------------------------------------------------------------------
-- 6. 任务报名表 task_signup: 志愿者报名志愿任务
-- ---------------------------------------------------------------------
CREATE TABLE task_signup (
    id            BIGINT      NOT NULL AUTO_INCREMENT COMMENT '报名ID',
    task_id       BIGINT      NOT NULL COMMENT '任务ID',
    volunteer_id  BIGINT      NOT NULL COMMENT '志愿者ID',
    status        VARCHAR(16) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING待处理/ACCEPTED已通过/REJECTED未通过',
    remark        VARCHAR(128) DEFAULT '' COMMENT '报名备注',
    create_time   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
    PRIMARY KEY (id),
    KEY idx_task (task_id),
    KEY idx_volunteer (volunteer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务报名表';

-- ---------------------------------------------------------------------
-- 7. 服务日志表 service_log: 志愿者签到签退与服务日志
-- ---------------------------------------------------------------------
CREATE TABLE service_log (
    id              BIGINT   NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    demand_id       BIGINT   NOT NULL COMMENT '服务需求ID',
    volunteer_id    BIGINT   NOT NULL COMMENT '志愿者ID',
    check_in_time   DATETIME DEFAULT NULL COMMENT '签到时间',
    check_in_lng    DECIMAL(10,6) DEFAULT 0 COMMENT '签到经度',
    check_in_lat    DECIMAL(10,6) DEFAULT 0 COMMENT '签到纬度',
    check_out_time  DATETIME DEFAULT NULL COMMENT '签退时间',
    check_out_lng   DECIMAL(10,6) DEFAULT 0 COMMENT '签退经度',
    check_out_lat   DECIMAL(10,6) DEFAULT 0 COMMENT '签退纬度',
    duration_minutes INT     NOT NULL DEFAULT 0 COMMENT '服务时长(分钟)',
    log_content     VARCHAR(512) DEFAULT '' COMMENT '服务日志内容',
    status          VARCHAR(16) NOT NULL DEFAULT 'IN' COMMENT '状态: IN服务中/OUT已签退',
    create_time     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_demand (demand_id),
    KEY idx_volunteer (volunteer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务签到日志表';

-- ---------------------------------------------------------------------
-- 8. 服务评价表 service_eval: 老人对志愿者服务的评价
-- ---------------------------------------------------------------------
CREATE TABLE service_eval (
    id           BIGINT      NOT NULL AUTO_INCREMENT COMMENT '评价ID',
    demand_id    BIGINT      NOT NULL COMMENT '服务需求ID',
    elder_id     BIGINT      NOT NULL COMMENT '评价老人档案ID',
    volunteer_id BIGINT      NOT NULL COMMENT '被评志愿者ID',
    score        TINYINT     NOT NULL DEFAULT 5 COMMENT '评分1-5星',
    content      VARCHAR(255) DEFAULT '' COMMENT '评价内容',
    create_time  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
    PRIMARY KEY (id),
    KEY idx_demand (demand_id),
    KEY idx_volunteer (volunteer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务评价表';

-- ---------------------------------------------------------------------
-- 9. 积分记录表 points_record: 志愿积分流水
-- ---------------------------------------------------------------------
CREATE TABLE points_record (
    id           BIGINT      NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    volunteer_id BIGINT      NOT NULL COMMENT '志愿者ID',
    change_val   INT         NOT NULL COMMENT '积分变动值(正增负减)',
    type         VARCHAR(16) NOT NULL COMMENT '类型: EARN服务奖励/EXCHANGE兑换/DEDUCT扣减',
    reason       VARCHAR(128) DEFAULT '' COMMENT '变动原因',
    related_id   BIGINT      DEFAULT NULL COMMENT '关联业务ID',
    create_time  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_volunteer (volunteer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='志愿积分流水表';

-- ---------------------------------------------------------------------
-- 10. 紧急求助表 emergency_help: 一键紧急呼叫记录
-- ---------------------------------------------------------------------
CREATE TABLE emergency_help (
    id              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '求助ID',
    elder_id        BIGINT       NOT NULL COMMENT '老人档案ID',
    help_type       VARCHAR(16)  DEFAULT 'SOS' COMMENT '求助类型: SOS一键求助/FALL跌倒/MEDICAL医疗/OTHER其他',
    description     VARCHAR(255) DEFAULT '' COMMENT '情况描述',
    address         VARCHAR(128) DEFAULT '' COMMENT '事发地址',
    lng             DECIMAL(10,6) DEFAULT 0 COMMENT '经度',
    lat             DECIMAL(10,6) DEFAULT 0 COMMENT '纬度',
    status          VARCHAR(16)  NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING待处理/HANDLING处理中/RESOLVED已解决',
    notify_admin_id BIGINT       DEFAULT NULL COMMENT '接报管理员ID',
    nearby_vol_ids  VARCHAR(128) DEFAULT '' COMMENT '就近志愿者ID列表',
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '求助时间',
    resolve_time    DATETIME     DEFAULT NULL COMMENT '解决时间',
    resolve_note    VARCHAR(255) DEFAULT '' COMMENT '处理说明',
    PRIMARY KEY (id),
    KEY idx_elder (elder_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='紧急求助表';

-- ---------------------------------------------------------------------
-- 11. 消息通知表 message: 站内消息推送
-- ---------------------------------------------------------------------
CREATE TABLE message (
    id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    sender_id     BIGINT       DEFAULT NULL COMMENT '发送者用户ID',
    receiver_id   BIGINT       NOT NULL COMMENT '接收者用户ID',
    receiver_role VARCHAR(16)  DEFAULT '' COMMENT '接收者角色',
    msg_type      VARCHAR(24)  NOT NULL COMMENT '消息类型: ACCEPT接单提醒/TIME_CHANGE时间变更/EMERGENCY紧急求助/SYSTEM系统通知/PUSH推送',
    title         VARCHAR(64)  NOT NULL COMMENT '消息标题',
    content       VARCHAR(255) NOT NULL COMMENT '消息内容',
    related_id    BIGINT       DEFAULT NULL COMMENT '关联业务ID',
    is_read       TINYINT      NOT NULL DEFAULT 0 COMMENT '是否已读: 0未读 1已读',
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    PRIMARY KEY (id),
    KEY idx_receiver (receiver_id),
    KEY idx_read (is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表';

-- ---------------------------------------------------------------------
-- 12. 操作日志表 audit_log: 全过程监管留痕
-- ---------------------------------------------------------------------
CREATE TABLE audit_log (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    operator_id BIGINT       NOT NULL COMMENT '操作人用户ID',
    operator_role VARCHAR(16) NOT NULL COMMENT '操作人角色',
    action      VARCHAR(32)  NOT NULL COMMENT '操作动作',
    target_type VARCHAR(32)  DEFAULT '' COMMENT '操作对象类型',
    target_id   BIGINT       DEFAULT NULL COMMENT '操作对象ID',
    detail      VARCHAR(255) DEFAULT '' COMMENT '操作详情',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (id),
    KEY idx_operator (operator_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统操作日志表';
