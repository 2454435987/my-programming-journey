-- ============================================================
-- 茶文化主题 App 演示数据（系统预置）
-- 密码统一为 BCrypt 加密的 admin123 / 123456
-- admin123 -> $2a$10$korxyNRxE1.MvvuZtEP.le9f6cRKm.CqMe/tRgQ1DgEkh5AJyjTRC
-- 123456   -> $2a$10$8on4Ots/ha7wRVFjdiXNT.uywgpnotwPfx5Q3dRdzfFLxQdnRqwoq
-- ============================================================
USE tea_culture;

-- 管理员
INSERT INTO sys_admin (admin_id, username, password, real_name, email, phone, status) VALUES
(1, 'admin',  '$2a$10$korxyNRxE1.MvvuZtEP.le9f6cRKm.CqMe/tRgQ1DgEkh5AJyjTRC', '超级管理员', 'admin@tea.cn', '13800000001', 1),
(2, 'editor', '$2a$10$korxyNRxE1.MvvuZtEP.le9f6cRKm.CqMe/tRgQ1DgEkh5AJyjTRC', '内容编辑',   'editor@tea.cn','13800000002', 1);

-- 角色
INSERT INTO sys_role (role_id, role_code, role_name, description) VALUES
(1, 'SUPER_ADMIN', '超级管理员', '拥有全部权限'),
(2, 'CONTENT_ADMIN','内容管理员', '内容/社区/活动管理'),
(3, 'OPERATOR',    '运营专员',   '运营与统计查看');

INSERT INTO sys_admin_role (admin_id, role_id) VALUES (1,1),(2,2);

-- 菜单
INSERT INTO sys_menu (menu_id, parent_id, menu_name, path, perms, menu_type, sort_order, icon) VALUES
(1, 0, '系统管理', '/system', 'system', 1, 90, 'Setting'),
(2, 1, '管理员管理', '/system/admins', 'system:admin:list', 2, 1, 'User'),
(3, 1, '角色管理',   '/system/roles',  'system:role:list', 2, 2, 'Lock'),
(4, 1, '菜单管理',   '/system/menus',  'system:menu:list', 2, 3, 'Menu'),
(5, 1, '操作日志',   '/system/logs',   'system:log:list',  2, 4, 'Document'),
(10, 0, '用户管理', '/users',    'user:list',  2, 10, 'UserFilled'),
(20, 0, '内容管理', '/content',  'content',    1, 20, 'Document'),
(21,20, '知识管理', '/content/knowledge', 'content:knowledge:list', 2, 1, 'Reading'),
(22,20, '教程管理', '/content/courses',   'content:course:list',   2, 2, 'VideoCamera'),
(23,20, '分类管理', '/content/categories','content:category:list', 2, 3, 'Grid'),
(30, 0, '商品与订单', '/mall',   'mall',       1, 30, 'ShoppingCart'),
(31,30, '商品管理', '/mall/products', 'mall:product:list', 2, 1, 'Goods'),
(32,30, '订单管理', '/mall/orders',   'mall:order:list',   2, 2, 'List'),
(40, 0, '社区管理', '/community',  'community',  1, 40, 'ChatDotRound'),
(41,40, '帖子管理', '/community/posts',    'community:post:list', 2, 1, 'ChatLineSquare'),
(42,40, '评论管理', '/community/comments', 'community:comment:list',2,2, 'ChatLineRound'),
(50, 0, '活动管理', '/activities', 'activity:list', 2, 50, 'Flag'),
(60, 0, '运营管理', '/operation',  'operation', 1, 60, 'Operation'),
(61,60, 'Banner管理', '/operation/banners', 'operation:banner:list', 2, 1, 'Picture'),
(62,60, '消息通知',   '/operation/messages', 'operation:message:list', 2, 2, 'Bell'),
(70, 0, '数据统计', '/dashboard', 'stats:view', 2, 70, 'DataAnalysis');

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu;
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 2, menu_id FROM sys_menu WHERE menu_id IN (10,20,21,22,23,40,41,42,50);
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 3, menu_id FROM sys_menu WHERE menu_id IN (60,61,62,70);

-- App 用户
INSERT INTO app_user (user_id, username, password, nickname, gender, phone, email, points, level, signature) VALUES
(1, 'tealover',   '$2a$10$8on4Ots/ha7wRVFjdiXNT.uywgpnotwPfx5Q3dRdzfFLxQdnRqwoq', '清风茶客', 1, '13900000001', 'tealover@qq.com',  1280, 3, '一盏清茶，半日浮生'),
(2, 'chayu01',    '$2a$10$8on4Ots/ha7wRVFjdiXNT.uywgpnotwPfx5Q3dRdzfFLxQdnRqwoq', '雨前龙井', 2, '13900000002', 'chayu01@qq.com',    860, 2, '龙井四绝，色绿香郁'),
(3, 'puer_fan',   '$2a$10$8on4Ots/ha7wRVFjdiXNT.uywgpnotwPfx5Q3dRdzfFLxQdnRqwoq', '普洱老王', 1, '13900000003', 'puer@qq.com',      2350, 4, '越陈越香'),
(4, 'teagirl',    '$2a$10$8on4Ots/ha7wRVFjdiXNT.uywgpnotwPfx5Q3dRdzfFLxQdnRqwoq', '茶小满',   2, '13900000004', 'girl@tea.cn',       320, 1, '白茶清欢无别事'),
(5, 'wuyi_rocks', '$2a$10$8on4Ots/ha7wRVFjdiXNT.uywgpnotwPfx5Q3dRdzfFLxQdnRqwoq', '岩骨花香', 1, '13900000005', 'wuyi@tea.cn',      1560, 3, '岩茶爱好者'),
(6, 'matcha_new', '$2a$10$8on4Ots/ha7wRVFjdiXNT.uywgpnotwPfx5Q3dRdzfFLxQdnRqwoq', '抹茶萌新', 2, '13900000006', 'matcha@tea.cn',      60, 1, '刚入茶坑');

-- 分类
INSERT INTO tea_category (category_id, category_name, category_type, description, sort_order) VALUES
(1, '绿茶', 2, '不发酵茶，清汤绿叶', 1),
(2, '红茶', 2, '全发酵茶，红汤红叶', 2),
(3, '乌龙茶', 2, '半发酵茶，岩骨花香', 3),
(4, '白茶', 2, '微发酵茶，越陈越香', 4),
(5, '黑茶', 2, '后发酵茶，醇厚顺滑', 5),
(6, '花茶', 2, '再加工茶，花香怡人', 6),
(7, '茶史文化', 1, '茶的起源与历史演变', 1),
(8, '茶道礼仪', 1, '泡茶品茶的礼仪规范', 2),
(9, '茶与健康', 1, '茶叶成分与养生功效', 3),
(10, '名茶鉴赏', 1, '中国十大名茶品鉴', 4),
(11, '冲泡技巧', 3, '不同茶类冲泡方法', 1),
(12, '茶艺表演', 3, '茶艺程式与表演技巧', 2),
(13, '茶席设计', 3, '茶席布置与美学', 3);

-- 茶文化知识
INSERT INTO tea_knowledge (knowledge_id, category_id, title, summary, content, cover_image, author, source, view_count, like_count, is_top) VALUES
(1, 7, '中国茶文化的起源与历史演变', '从神农尝百草到陆羽《茶经》，梳理中国茶文化五千年发展脉络。',
 '<p>中国是茶的故乡，茶文化源远流长。相传神农尝百草，日遇七十二毒，得荼而解之。</p><p>唐代陆羽所著《茶经》是世界上第一部茶学专著，系统总结了茶的起源、制作、器具与煮饮方法，奠定了中国茶道的基础。</p><p>宋代点茶盛行，斗茶之风风靡朝野；明清时期散茶冲泡成为主流，紫砂壶艺术应运而生。</p><p>如今，茶已成为中国文化的标志性符号之一，茶文化被列入国家级非物质文化遗产名录。</p>',
 '/static/images/tea_history.jpg', '茶文化研究院', '《茶经》', 3256, 286, 1),
(2, 8, '茶道六君子与茶席礼仪', '认识茶道器具，掌握泡茶品茶过程中的礼仪规范。',
 '<p>茶道六君子指的是茶筒、茶匙、茶漏、茶针、茶夹、茶则六种茶具配件。</p><p>泡茶时需注意：净手、温杯、投茶、注水、出汤、奉茶等步骤的礼仪细节。</p><p>品茶讲究"一看二闻三品味"，观汤色、闻香气、品滋味，方能体会茶之真味。</p>',
 '/static/images/tea_ceremony.jpg', '清风茶客', '茶道笔记', 2180, 198, 1),
(3, 9, '茶叶中的营养成分与健康功效', '解析茶多酚、咖啡碱、氨基酸等成分对人体的益处。',
 '<p>茶叶中含有茶多酚、咖啡碱、氨基酸、维生素、矿物质等多种有益成分。</p><p>茶多酚具有抗氧化、防辐射、降血脂等功效；茶氨酸有助于舒缓压力、提高注意力。</p><p>饮茶需适量，空腹、睡前不宜饮浓茶，胃寒者宜饮红茶、熟普等温性茶。</p>',
 '/static/images/tea_health.jpg', '营养与健康', '中国茶叶学会', 4120, 356, 0),
(4, 10, '西湖龙井：色绿香郁味甘形美', '十大名茶之首西湖龙井的产地、工艺与品鉴要点。',
 '<p>西湖龙井产于浙江杭州西湖龙井村周围群山，以"色绿、香郁、味甘、形美"四绝著称。</p><p>传统龙井采摘讲究"早、嫩、勤"，特级龙井以一芽一叶初展为标准。</p><p>冲泡龙井宜用玻璃杯，水温80-85℃，采用中投法，可欣赏茶叶在水中舒展的姿态。</p>',
 '/static/images/longjing.jpg', '名茶志', '中国茶叶博物馆', 5680, 462, 0),
(5, 10, '武夷岩茶：岩骨花香的奥秘', '从山场到工艺，解读武夷岩茶独特的岩韵。',
 '<p>武夷岩茶产于福建武夷山，属乌龙茶类，以大红袍、肉桂、水仙最为著名。</p><p>"岩骨花香"是岩茶最显著的品质特征，源于武夷山独特的丹霞地貌与微域气候。</p><p>岩茶工艺复杂，经做青、炒青、揉捻、烘焙等多道工序，形成独特的岩韵。</p>',
 '/static/images/wuyi_rock.jpg', '岩骨花香', '武夷茶经', 3890, 312, 0),
(6, 7, '宋代点茶与斗茶文化', '穿越回宋朝，体验风靡一时的点茶与斗茶之风。',
 '<p>宋代是中国茶文化的鼎盛时期，点茶、斗茶、分茶等茶事活动盛行。</p><p>点茶是将茶饼碾末置于盏中，注汤击拂，使茶汤表面形成细腻的沫饽。</p><p>斗茶比试点茶技艺的高低，以汤花咬盏持久者为胜，宋徽宗赵佶《大观茶论》有详细记载。</p>',
 '/static/images/song_tea.jpg', '茶史研究', '大观茶论', 1860, 145, 0),
(7, 9, '四季饮茶指南：春花夏绿秋青冬红', '根据季节变化选择合适的茶类，喝出健康。',
 '<p>春季宜饮花茶，如茉莉花茶，可散发冬季郁积之寒气，促进阳气生发。</p><p>夏季宜饮绿茶，清热解暑、生津止渴；秋季宜饮乌龙茶，润肤润燥。</p><p>冬季宜饮红茶、熟普洱，温补养胃，增强人体对冬季气候的适应能力。</p>',
 '/static/images/four_seasons.jpg', '养生茶话', '中医茶疗', 2950, 267, 0),
(8, 8, '工夫茶艺十八道工序详解', '潮汕工夫茶完整程式，从备器到品茗一步步教学。',
 '<p>潮汕工夫茶被誉为"中国茶道的活化石"，讲究茶叶、用水、火候、冲泡技巧。</p><p>十八道工序包括：焚香静气、叶嘉酬宾、孟臣沐霖、乌龙入宫、悬壶高冲、春风拂面等。</p><p>每一道工序都蕴含着中国传统文化的哲学思想与审美情趣。</p>',
 '/static/images/gongfu_tea.jpg', '工夫茶传习所', '潮汕茶文化', 2440, 210, 0);

-- 商品
INSERT INTO tea_product (product_id, category_id, product_name, subtitle, cover_image, price, original_price, stock, sales, unit, origin, description, status) VALUES
(1, 1, '明前西湖龙井特级 250g', '2024新茶 明前采摘 色绿香郁', '/static/images/p_longjing.jpg', 368.00, 498.00, 320, 1286, '罐', '浙江杭州', '精选明前一芽一叶初展鲜叶，传统辉锅工艺，豆花香明显，滋味鲜爽甘醇。', 1),
(2, 2, '正山小种红茶礼盒 200g', '桐木关高山 烟熏松香 蜜甜', '/static/images/p_zhengshan.jpg', 268.00, 328.00, 450, 856, '盒', '福建武夷山', '产自桐木关自然保护区，传统烟熏工艺，汤色橙红透亮，桂圆干香显。', 1),
(3, 3, '武夷肉桂岩茶 125g', '岩骨花香 肉桂香浓 回甘持久', '/static/images/p_rougui.jpg', 458.00, 568.00, 180, 542, '盒', '福建武夷山', '核心产区肉桂，中足火焙制，桂皮香辛锐，岩韵明显，回甘迅速。', 1),
(4, 4, '福鼎白毫银针 100g', '头采芽头 毫香蜜韵 2023年', '/static/images/p_yinzhen.jpg', 520.00, 680.00, 150, 368, '罐', '福建福鼎', '头采单芽，日光萎凋，毫香蜜韵，汤色杏黄明亮，清甜爽口。', 1),
(5, 5, '云南古树熟普洱茶饼 357g', '勐海古树 五年陈化 醇厚顺滑', '/static/images/p_puer.jpg', 298.00, 388.00, 260, 724, '饼', '云南勐海', '古树原料发酵，五年陈化，汤色红浓透亮，陈香纯正，滋味醇厚。', 1),
(6, 6, '苏州茉莉花茶 150g', '七窨一提 花香鲜灵 鲜爽回甘', '/static/images/p_moli.jpg', 128.00, 168.00, 600, 1560, '袋', '江苏苏州', '选用苏州鲜茉莉花七次窨制，花香鲜灵持久，茶味鲜爽回甘。', 1),
(7, 1, '安吉白茶 2024新茶 250g', '氨基酸含量高 鲜爽如鸡汤', '/static/images/p_anji.jpg', 258.00, 318.00, 380, 642, '罐', '浙江安吉', '白叶一号品种，高氨基酸含量，滋味鲜爽，叶底玉白成朵。', 1),
(8, 3, '安溪铁观音清香型 250g', '观音韵 兰花香 七泡有余香', '/static/images/p_tieguanyin.jpg', 188.00, 238.00, 520, 986, '盒', '福建安溪', '传统正味铁观音，兰花香馥郁，观音韵显，七泡余香不绝。', 1);

-- 教程
INSERT INTO tea_course (course_id, category_id, title, summary, content, cover_image, level, duration, view_count, like_count, is_free, price, status) VALUES
(1, 11, '玻璃杯冲泡绿茶三法', '上投法、中投法、下投法详解，轻松泡好一杯绿茶。',
 '<p>绿茶冲泡三法各有妙用：上投法适合碧螺春等细嫩茶，先注水后投茶；中投法适合龙井，先注部分水再投茶注满；下投法适合条索较松的绿茶。</p><p>水温控制在80-85℃，避免烫熟茶叶，影响鲜爽度。</p>',
 '/static/images/c_glass.jpg', 1, 15, 3260, 298, 1, 0, 1),
(2, 11, '紫砂壶泡乌龙茶技巧', '选壶、温壶、投茶量与出汤时间的黄金比例。',
 '<p>泡乌龙茶宜选110-150ml小品紫砂壶，投茶量为壶容积的1/3-1/2。</p><p>先温壶温杯，沸水冲泡，第一泡润茶10秒倒掉，之后每泡递增5秒出汤。</p>',
 '/static/images/c_zisha.jpg', 2, 22, 2180, 186, 0, 29.90, 1),
(3, 12, '潮汕工夫茶十八道完整演示', '国家级非遗传承人亲授，从备器到品茗全程演示。',
 '<p>本课程完整演示潮汕工夫茶十八道工序，包含每一个动作的要领讲解与常见误区纠正。</p><p>配备多机位特写镜头，可反复观摩学习。</p>',
 '/static/images/c_gongfu.jpg', 3, 45, 1860, 245, 0, 49.90, 1),
(4, 13, '茶席设计入门：色彩与器物搭配', '学会茶席布置，营造雅致的品茶空间。',
 '<p>茶席设计讲究主题立意、色彩搭配、器物选择与空间层次。</p><p>常用配色有：自然原木色系、青瓷素雅色系、暖色花艺色系等。</p>',
 '/static/images/c_xixi.jpg', 2, 18, 1420, 128, 1, 0, 1),
(5, 11, '盖碗冲泡白茶完整教程', '盖碗是白茶的最佳搭档，掌握水温与出汤节奏。',
 '<p>白茶冲泡首选白瓷盖碗，不吸香不夺味，能真实呈现白茶的毫香蜜韵。</p><p>新白茶水温90℃左右，老白茶可用沸水冲泡或煮饮。</p>',
 '/static/images/c_gaiwan.jpg', 1, 12, 2640, 226, 1, 0, 1),
(6, 12, '茶艺表演基础：手法与仪态训练', '茶艺表演中的手势、身姿与眼神训练方法。',
 '<p>茶艺表演要求动作优美流畅、仪态端庄大方，手法包括凤凰三点头、高山流水、回旋斟水等。</p><p>配合呼吸与节奏，使整个表演如行云流水般自然。</p>',
 '/static/images/c_perform.jpg', 2, 28, 980, 86, 0, 39.90, 1);

-- 帖子
INSERT INTO community_post (post_id, user_id, title, content, images, topic, view_count, like_count, comment_count, is_top, is_essence) VALUES
(1, 1, '今日茶席：雨后窗边的一杯龙井', '雨过天晴，阳光透过窗帘洒在茶席上。取明前龙井一泡，玻璃杯中茶叶缓缓舒展，豆花香扑鼻。这一刻，时光都慢了下来。',
 '/static/images/post1.jpg', '茶席分享', 1286, 186, 24, 1, 1),
(2, 3, '我的普洱茶仓储经验分享', '作为一个喝了十年普洱的老茶客，分享几点仓储心得：一要注意温湿度控制，温度25℃左右、湿度65%为宜；二要避免异味污染；三要定期检查茶饼状态。',
 '', '普洱仓储', 856, 124, 18, 0, 1),
(3, 5, '武夷山寻茶记：探访牛栏坑肉桂', '上个月专程去了武夷山，实地探访了牛栏坑肉桂的产地。丹霞地貌、坑涧微域气候，果然名不虚传。分享一些照片和感悟。',
 '/static/images/post2.jpg,static/images/post3.jpg', '武夷岩茶', 2340, 312, 42, 0, 1),
(4, 2, '新手入门：如何选购第一套茶具？', '刚入坑的朋友们常常困惑茶具怎么选。我的建议是：先从白瓷盖碗+公道杯+品茗杯三件套开始，总预算200元以内就够用，等有感觉了再逐步升级。',
 '', '新手入门', 1560, 198, 36, 0, 0),
(5, 4, '今日份茶点：桂花糕配白牡丹', '自制桂花糕配上一泡福鼎白牡丹，甜而不腻，花香茶香交织。附上桂花糕的做法，简单易学。',
 '/static/images/post4.jpg,static/images/post5.jpg', '茶点搭配', 986, 156, 28, 0, 0),
(6, 6, '第一次喝到岩骨花香是什么体验', '今天在茶友家第一次喝到正岩肉桂，入口辛锐的桂皮香，随后是持久的回甘，终于理解什么叫"岩骨花香"了！',
 '', '岩茶体验', 742, 88, 15, 0, 0);

INSERT INTO post_comment (post_id, user_id, content, like_count) VALUES
(1, 2, '这个光线太美了，龙井在杯中真好看！', 12),
(1, 3, '雨后配龙井，绝配。', 8),
(1, 5, '请问是什么产区的龙井？', 3),
(2, 1, '干货收藏，谢谢分享！', 15),
(2, 5, '湿度控制确实很重要，受教了。', 6),
(3, 1, '照片拍得好棒，牛栏坑一直想去。', 20),
(3, 2, '求攻略，打算明年春茶季去。', 7),
(4, 6, '太实用了，刚想买茶具。', 9),
(5, 1, '桂花糕看起来好好吃，要试做。', 11),
(6, 4, '第一次喝岩茶都会被惊艳到~', 5);

-- 活动
INSERT INTO tea_activity (activity_id, title, summary, content, cover_image, activity_type, location, start_time, end_time, enroll_deadline, max_enroll, enrolled_count, price, status) VALUES
(1, '春季茶山游学：杭州龙井村采茶体验', '走进龙井村茶园，体验采茶、炒茶、品茶全流程。',
 '<p>活动内容：茶园参观、手工采茶、龙井炒制体验、茶艺品鉴、茶点分享。</p><p>集合地点：杭州龙井村茶文化广场。请穿着舒适运动鞋，做好防晒准备。</p>',
 '/static/images/act1.jpg', '线下', '浙江省杭州市西湖区龙井村', '2026-04-12 09:00:00', '2026-04-12 17:00:00', '2026-04-08 23:59:59', 30, 22, 198.00, 1),
(2, '茶文化大讲堂：宋代点茶复原体验', '听茶史专家讲宋代茶文化，亲手体验点茶技艺。',
 '<p>特邀茶文化研究专家主讲宋代点茶历史，现场展示点茶技艺，参与学员可亲手体验。</p>',
 '/static/images/act2.jpg', '线下', '上海市徐汇区茶文化中心', '2026-05-18 14:00:00', '2026-05-18 17:00:00', '2026-05-15 23:59:59', 50, 35, 0.00, 1),
(3, '线上茶会：六大茶类品鉴对比', '足不出户，线上盲品六大茶类，提升品鉴能力。',
 '<p>活动形式：线上直播+茶样快递。报名后将寄送六款茶样，活动当晚直播盲品讲解。</p>',
 '/static/images/act3.jpg', '线上', '线上直播', '2026-03-28 19:30:00', '2026-03-28 21:30:00', '2026-03-25 23:59:59', 100, 78, 68.00, 1),
(4, '茶艺师初级考证培训班', '系统学习茶艺知识，考取茶艺师职业资格证书。',
 '<p>培训内容：茶叶知识、茶艺技能、茶席设计、实操考核。考核通过颁发茶艺师初级证书。</p>',
 '/static/images/act4.jpg', '线下', '广州市天河区职业技能培训中心', '2026-06-01 09:00:00', '2026-06-03 17:00:00', '2026-05-28 23:59:59', 40, 18, 980.00, 1),
(5, '夏日冷泡茶创意大赛', '晒出你的冷泡茶配方，赢取丰厚茶礼。',
 '<p>参与方式：在茶友社区发布带#夏日冷泡茶#话题的帖子即可参赛。</p>',
 '/static/images/act5.jpg', '线上', '茶友社区', '2026-07-01 00:00:00', '2026-07-31 23:59:59', '2026-07-31 23:59:59', 500, 126, 0.00, 1);

INSERT INTO activity_enroll (activity_id, user_id, real_name, phone, remark, status) VALUES
(1, 1, '张明', '13900000001', '希望安排靠窗座位', 1),
(1, 3, '王建国', '13900000003', '', 1),
(1, 5, '李岩', '13900000005', '两人同行', 1),
(2, 2, '陈雨', '13900000002', '', 1),
(2, 4, '林小满', '13900000004', '第一次体验点茶', 1),
(2, 6, '赵萌', '13900000006', '', 1),
(3, 1, '张明', '13900000001', '', 1),
(3, 2, '陈雨', '13900000002', '', 1),
(3, 3, '王建国', '13900000003', '', 1),
(3, 4, '林小满', '13900000004', '', 1),
(3, 5, '李岩', '13900000005', '', 1),
(3, 6, '赵萌', '13900000006', '', 1),
(4, 3, '王建国', '13900000003', '需要发票', 1),
(4, 5, '李岩', '13900000005', '', 1);

-- 地址
INSERT INTO user_address (user_id, receiver, phone, province, city, district, detail, is_default) VALUES
(1, '张明', '13900000001', '浙江省', '杭州市', '西湖区', '文三路 259 号昌地火炬大厦 1 幢 801 室', 1),
(1, '张明(公司)', '13900000001', '浙江省', '杭州市', '滨江区', '江南大道 588 号恒鑫大厦 12 楼', 0),
(2, '陈雨', '13900000002', '上海市', '上海市', '徐汇区', '漕溪北路 88 号圣爱大厦 6 楼', 1),
(3, '王建国', '13900000003', '云南省', '昆明市', '五华区', '东风西路 121 号顺城写字楼 A 座 1503', 1);

-- 购物车
INSERT INTO cart_item (user_id, product_id, quantity, checked) VALUES
(1, 1, 1, 1),
(1, 5, 2, 1),
(1, 6, 1, 0),
(2, 2, 1, 1),
(3, 3, 1, 1);

-- 订单
INSERT INTO orders (order_id, order_no, user_id, total_amount, pay_amount, freight, status, pay_type, pay_time, ship_time, receive_time, receiver, receiver_phone, receiver_address, remark) VALUES
(1, 'TC202603150001', 1, 368.00, 368.00, 0.00, 3, 1, '2026-03-15 10:23:00', '2026-03-15 16:00:00', '2026-03-18 14:20:00', '张明', '13900000001', '浙江省杭州市西湖区文三路259号', ''),
(2, 'TC202603180002', 1, 596.00, 596.00, 0.00, 2, 1, '2026-03-18 09:15:00', '2026-03-18 15:30:00', NULL, '张明', '13900000001', '浙江省杭州市西湖区文三路259号', '麻烦包严实些'),
(3, 'TC202603100003', 2, 268.00, 278.00, 10.00, 3, 2, '2026-03-10 20:45:00', '2026-03-11 09:00:00', '2026-03-14 11:30:00', '陈雨', '13900000002', '上海市徐汇区漕溪北路88号', ''),
(4, 'TC202603200004', 3, 458.00, 458.00, 0.00, 1, 1, '2026-03-20 14:30:00', NULL, NULL, '王建国', '13900000003', '云南省昆明市五华区东风西路121号', ''),
(5, 'TC202603210005', 3, 298.00, 298.00, 0.00, 0, 1, NULL, NULL, NULL, '王建国', '13900000003', '云南省昆明市五华区东风西路121号', ''),
(6, 'TC202603080006', 4, 128.00, 138.00, 10.00, 3, 3, '2026-03-08 11:20:00', '2026-03-08 16:00:00', '2026-03-11 10:15:00', '林小满', '13900000004', '江苏省南京市玄武区中山路100号', ''),
(7, 'TC202603220007', 5, 916.00, 916.00, 0.00, 1, 1, '2026-03-22 08:50:00', NULL, NULL, '李岩', '13900000005', '福建省武夷山市崇阳溪畔路1号', '送礼用'),
(8, 'TC202603010008', 6, 188.00, 188.00, 0.00, 3, 1, '2026-03-01 19:30:00', '2026-03-02 10:00:00', '2026-03-05 16:40:00', '赵萌', '13900000006', '广东省广州市天河区天河路385号', ''),
(9, 'TC202603230009', 1, 258.00, 258.00, 0.00, 4, 1, NULL, NULL, NULL, '张明', '13900000001', '浙江省杭州市西湖区文三路259号', ''),
(10, 'TC202603120010', 2, 520.00, 520.00, 0.00, 3, 2, '2026-03-12 13:10:00', '2026-03-12 17:00:00', '2026-03-15 09:50:00', '陈雨', '13900000002', '上海市徐汇区漕溪北路88号', '');

INSERT INTO order_item (order_id, order_no, product_id, product_name, product_image, price, quantity) VALUES
(1, 'TC202603150001', 1, '明前西湖龙井特级 250g', '/static/images/p_longjing.jpg', 368.00, 1),
(2, 'TC202603180002', 5, '云南古树熟普洱茶饼 357g', '/static/images/p_puer.jpg', 298.00, 2),
(3, 'TC202603100003', 2, '正山小种红茶礼盒 200g', '/static/images/p_zhengshan.jpg', 268.00, 1),
(4, 'TC202603200004', 3, '武夷肉桂岩茶 125g', '/static/images/p_rougui.jpg', 458.00, 1),
(5, 'TC202603210005', 5, '云南古树熟普洱茶饼 357g', '/static/images/p_puer.jpg', 298.00, 1),
(6, 'TC202603080006', 6, '苏州茉莉花茶 150g', '/static/images/p_moli.jpg', 128.00, 1),
(7, 'TC202603220007', 3, '武夷肉桂岩茶 125g', '/static/images/p_rougui.jpg', 458.00, 2),
(8, 'TC202603010008', 8, '安溪铁观音清香型 250g', '/static/images/p_tieguanyin.jpg', 188.00, 1),
(9, 'TC202603230009', 7, '安吉白茶 2024新茶 250g', '/static/images/p_anji.jpg', 258.00, 1),
(10, 'TC202603120010', 4, '福鼎白毫银针 100g', '/static/images/p_yinzhen.jpg', 520.00, 1);

-- 消息
INSERT INTO message_notice (user_id, title, content, msg_type, is_read) VALUES
(1, '订单发货通知', '您的订单 TC202603180002 已发货，请注意查收。', 2, 0),
(1, '活动报名成功', '您已成功报名"春季茶山游学：杭州龙井村采茶体验"。', 3, 1),
(1, '社区互动通知', '用户"雨前龙井"评论了你的帖子《今日茶席：雨后窗边的一杯龙井》。', 4, 0),
(1, '系统维护通知', '系统将于本周日 02:00-04:00 进行例行维护，届时服务暂停。', 1, 1),
(2, '订单发货通知', '您的订单 TC202603100003 已发货，请注意查收。', 2, 1),
(2, '欢迎加入茶友社区', '欢迎来到茶文化主题App！这里有丰富的茶文化知识与茶友交流。', 1, 1),
(3, '订单待付款提醒', '您的订单 TC202603210005 待付款，请尽快完成支付。', 2, 0),
(0, '新茶上市公告', '2024明前龙井新茶已全面上架，欢迎选购。', 1, 0);

-- Banner
INSERT INTO banner (title, image_url, link_url, position, sort_order, status) VALUES
('明前龙井新茶上市', '/static/images/banner1.jpg', '/pages/product/detail?id=1', 'home', 1, 1),
('春季茶山游学报名中', '/static/images/banner2.jpg', '/pages/activity/detail?id=1', 'home', 2, 1),
('茶文化知识专栏', '/static/images/banner3.jpg', '/pages/knowledge/list', 'home', 3, 1),
('武夷岩茶专场', '/static/images/banner4.jpg', '/pages/mall/index', 'mall', 1, 1),
('茶艺教程免费学', '/static/images/banner5.jpg', '/pages/course/list', 'home', 4, 1);

-- 收藏
INSERT INTO user_favorite (user_id, target_id, target_type) VALUES
(1, 1, 1), (1, 4, 1), (1, 2, 2), (1, 5, 2), (1, 3, 3), (1, 1, 5),
(2, 1, 1), (2, 2, 1), (2, 3, 2),
(3, 5, 1), (3, 3, 2), (3, 5, 2),
(4, 2, 1), (4, 6, 1), (4, 4, 2),
(5, 5, 1), (5, 3, 2), (5, 1, 5),
(6, 3, 1), (6, 8, 2);

-- 点赞
INSERT INTO content_like (user_id, target_id, target_type) VALUES
(1, 1, 1), (1, 2, 1), (1, 4, 1), (1, 1, 3), (1, 3, 3),
(2, 1, 1), (2, 4, 1), (2, 1, 3), (2, 4, 3),
(3, 5, 1), (3, 2, 3), (3, 3, 3),
(4, 2, 1), (4, 7, 1), (4, 5, 3),
(5, 1, 1), (5, 5, 1), (5, 3, 1), (5, 3, 3),
(6, 4, 1), (6, 6, 3);

-- 操作日志
INSERT INTO sys_operation_log (admin_id, admin_name, operation, method, params, ip, status) VALUES
(1, 'admin', '登录系统', 'POST /api/admin/auth/login', '{}', '127.0.0.1', 1),
(1, 'admin', '新增商品：安溪铁观音清香型', 'POST /api/admin/products', '{"productName":"安溪铁观音清香型"}', '127.0.0.1', 1),
(1, 'admin', '审核帖子通过：《武夷山寻茶记》', 'PUT /api/admin/posts/3/audit', '{"status":1}', '127.0.0.1', 1),
(2, 'editor', '发布知识：《宋代点茶与斗茶文化》', 'POST /api/admin/knowledge', '{"title":"宋代点茶与斗茶文化"}', '127.0.0.1', 1),
(1, 'admin', '创建活动：春季茶山游学', 'POST /api/admin/activities', '{"title":"春季茶山游学"}', '127.0.0.1', 1);
