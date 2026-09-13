/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : tcm_health_platform_max

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 27/03/2026 21:48:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ai_chat_record
-- ----------------------------
DROP TABLE IF EXISTS `ai_chat_record`;
CREATE TABLE `ai_chat_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `session_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会话ID',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色：user/assistant',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息内容',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_session_time`(`user_id`, `session_id`, `create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'AI聊天记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_chat_record
-- ----------------------------
INSERT INTO `ai_chat_record` VALUES (1, 5, 'session_user5_001', 'user', '请问黄芪有什么功效？', '2026-02-03 19:16:32');
INSERT INTO `ai_chat_record` VALUES (2, 5, 'session_user5_001', 'assistant', '黄芪是一味常用的补气药材，主要功效包括：\n\n1. **补气升阳**：适用于气虚乏力、食少便溏等症状\n2. **固表止汗**：对于体虚自汗有很好的效果\n3. **利水消肿**：可用于气虚水肿\n4. **托毒生肌**：促进伤口愈合\n\n建议用量：煎服9-30克，也可以用开水泡饮。', '2026-02-03 19:16:32');
INSERT INTO `ai_chat_record` VALUES (3, 5, 'session_user5_001', 'user', '黄芪可以和什么一起泡水喝？', '2026-02-03 19:16:32');
INSERT INTO `ai_chat_record` VALUES (4, 5, 'session_user5_001', 'assistant', '黄芪可以搭配多种药材泡水，常见的搭配有：\n\n1. **黄芪+枸杞**：补气养血，适合气血两虚者\n2. **黄芪+红枣**：补气健脾，增强免疫力\n3. **黄芪+当归**：即\"当归补血汤\"，补血效果更佳\n4. **黄芪+党参**：双重补气，适合气虚明显者\n5. **黄芪+西洋参**：补气不上火，适合阴虚火旺者\n\n根据您的体质选择合适的搭配哦～', '2026-02-03 19:16:32');
INSERT INTO `ai_chat_record` VALUES (5, 6, 'session_user6_001', 'user', '我想买一些补血的药材，有什么推荐吗？', '2026-02-03 19:16:32');
INSERT INTO `ai_chat_record` VALUES (6, 6, 'session_user6_001', 'assistant', '为您推荐几款补血效果好的药材：\n\n1. **阿胶** - 补血滋阴的首选，特别适合女性\n2. **当归** - \"血中圣药\"，补血活血\n3. **红枣** - 日常补血佳品，可直接食用\n4. **桂圆** - 补血安神，适合心脾两虚\n5. **枸杞** - 滋补肝肾，养血明目\n\n我们平台都有售哦，需要我为您推荐具体商品吗？', '2026-02-03 19:16:32');
INSERT INTO `ai_chat_record` VALUES (7, 15, 'session_1770355624921_s2xt2de57', 'user', '黄芪有什么功效？', '2026-02-06 13:27:06');
INSERT INTO `ai_chat_record` VALUES (8, 15, 'session_1770355624921_s2xt2de57', 'assistant', '黄芪是中医临床常用的**补气要药**，性微温，味甘，归脾、肺经。其主要功效包括：\n\n✅ **补气升阳**：适用于气虚乏力、食少便溏、中气下陷（如胃下垂、子宫脱垂、久泻脱肛）等症。  \n✅ **固表止汗**：用于表虚自汗、易感风寒者，可增强卫气、提高抵抗力。  \n✅ **利水消肿**：对气虚引起的水肿、小便不利有一定调理作用。  \n✅ **托毒生肌**：促进疮疡溃后恢复，常用于气血不足导致的久不收口、疮疡难愈。\n\n🌿 **现代研究也表明**：黄芪富含黄芪甲苷、多糖等活性成分，具有调节免疫、抗氧化、保护心脑血管、改善疲劳等作用。\n\n📌 **温馨提示**：  \n- 黄芪偏温，**阴虚火旺、实热证（如口干舌燥、面红目赤、便秘尿黄）、感冒发热初期者慎用**；  \n- 孕妇、哺乳期女性及有特殊疾病者，建议在中医师指导下使用。\n\n需要我为您推荐适合体质的黄芪养生方案，或介绍如何搭配其他药材（如党参、枸杞、大枣）一起使用吗？😊', '2026-02-06 13:27:15');
INSERT INTO `ai_chat_record` VALUES (9, 15, 'session_1774612295803_qog5h4qxz', 'user', '黄芪有什么功效？', '2026-03-27 19:55:57');
INSERT INTO `ai_chat_record` VALUES (10, 15, 'session_1774612295803_qog5h4qxz', 'assistant', '黄芪是中医常用的补气要药，性微温、味甘，归脾、肺经，具有以下主要功效：\n\n✅ **补气升阳**：适用于气虚乏力、食欲不振、久病体虚、中气下陷（如胃下垂、子宫脱垂、脱肛等）。\n\n✅ **固表止汗**：改善自汗（白天不活动也容易出汗）、易感冒、抵抗力低下等问题。\n\n✅ **利水消肿**：用于气虚所致的水肿、小便不利，尤以脾肾气虚型水肿为宜。\n\n✅ **托毒生肌**：促进疮疡溃后久不愈合、伤口难收口、气血不足导致的皮肤溃烂等恢复。\n\n🌿 **温馨提示**：  \n- 黄芪偏温补，**阴虚火旺（如口干舌燥、潮热盗汗、舌红少苔）、实热证（如上火严重、咽喉肿痛、便秘尿黄）者慎用**。  \n- 一般建议**辨体质后使用**，气虚体质人群（常感疲乏、气短懒言、易感冒、舌淡胖有齿痕）最为适宜。\n\n📌 **平台推荐**：  \n👉 [精选黄芪片](#)｜道地药材 · 切片厚薄均匀 · 易泡易煮  \n🔹功效：补气升阳、固表止汗、利水消肿、托毒生肌  \n💰价格：**68.00元/份**  \n\n📚 想深入了解？欢迎阅读平台文章：  \n📖《[常用养生药材的功效与选购指南](#)》——教您识好药、选对药、用得准。\n\n需要我帮您判断是否适合用黄芪，或推荐搭配方案（如黄芪+枸杞/党参/山药）吗？😊', '2026-03-27 19:56:07');
INSERT INTO `ai_chat_record` VALUES (11, 15, 'session_1774612655951_xz45ywn5u', 'user', '黄芪有什么功效？', '2026-03-27 19:57:37');
INSERT INTO `ai_chat_record` VALUES (12, 15, 'session_1774612655951_xz45ywn5u', 'assistant', '黄芪是中医常用的**补气要药**，性微温，味甘，归脾、肺经。其核心功效可概括为以下五方面：\n\n✅ **补气升阳**：适用于气虚乏力、少气懒言、食欲不振、久泻脱肛、子宫下垂等中气下陷证。  \n✅ **固表止汗**：用于表虚自汗、易感冒者，增强卫气，提高抵抗力。  \n✅ **利水消肿**：对气虚所致的水肿、小便不利（如慢性肾炎、术后浮肿）有辅助调理作用。  \n✅ **托毒生肌**：促进疮疡溃后难敛、久不收口，常配当归、金银花等用于术后或体弱者伤口恢复。  \n✅ **益卫固表 + 补益肺脾**：长期服用有助于改善气虚体质，提升整体正气，是“扶正防病”的经典药材。\n\n🌿 **温馨提示**：  \n- 黄芪偏温补，**阴虚火旺、实热证（如口苦、面红、便秘、舌红苔黄）、感冒发热初期者不宜单用**；  \n- 孕妇、儿童及特殊疾病患者建议在医师指导下使用。\n\n📌 **平台推荐**：  \n👉 [精选黄芪片](#)｜道地内蒙古/山西产，切片均匀、色黄质坚、气香微甜  \n🔹 功效：补气升阳、固表止汗、利水消肿、托毒生肌  \n🔹 价格：**¥68.00**（500g，独立密封包装，方便煲汤、泡茶、煮粥）\n\n📚 延伸阅读：  \n📖《[常用养生药材的功效与选购指南](#)》——教你辨优劣、识真伪、巧搭配  \n\n需要我帮您根据体质（如容易疲劳、爱出汗、常感冒）定制一个黄芪食养方案，或搭配其他药材（如党参、枸杞、大枣）的养生茶方吗？ 😊', '2026-03-27 19:57:48');

-- ----------------------------
-- Table structure for announcement
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告内容',
  `type` tinyint(4) NULL DEFAULT 1 COMMENT '公告类型：1-系统公告，2-活动公告，3-其他',
  `status` tinyint(4) NULL DEFAULT 0 COMMENT '状态：0-草稿，1-已发布',
  `is_top` tinyint(4) NULL DEFAULT 0 COMMENT '是否置顶',
  `start_time` datetime NULL DEFAULT NULL COMMENT '生效开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '生效结束时间',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of announcement
-- ----------------------------
INSERT INTO `announcement` VALUES (1, '平台服务协议更新通知', '尊敬的用户：\n\n为了更好地保障您的权益，我们对平台服务协议进行了更新。主要更新内容包括：\n1. 用户隐私保护条款\n2. 商品退换货政策\n3. 会员权益说明\n\n请您仔细阅读更新后的协议内容，继续使用平台服务即表示您同意新的协议条款。\n\n中医养生平台', 1, 1, 1, '2026-01-01 00:00:00', '2026-12-31 23:59:59', '2026-01-01 08:00:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `announcement` VALUES (2, '春节物流配送时间调整公告', '尊敬的用户：\n\n春节期间（2026年1月28日-2月6日），物流配送将受到影响，发货时间可能有所延迟。\n\n建议您提前下单，以确保及时收到商品。\n\n节后我们将恢复正常发货，感谢您的理解与支持！\n\n中医养生平台', 1, 1, 1, '2026-01-20 00:00:00', '2026-02-10 23:59:59', '2026-01-20 10:00:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `announcement` VALUES (3, '新用户专享优惠活动', '欢迎新用户加入中医养生平台！\n\n新用户专享福利：\n1. 注册即送50元优惠券\n2. 首单满100减20\n3. 关注公众号再得积分100\n\n活动长期有效，欢迎体验！', 2, 1, 0, '2026-01-01 00:00:00', '2026-12-31 23:59:59', '2026-01-01 09:00:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `announcement` VALUES (4, '系统维护通知', '尊敬的用户：\n\n为了提供更好的服务体验，平台将于2026年2月5日凌晨2:00-6:00进行系统维护升级。\n\n维护期间部分功能可能无法正常使用，请您提前安排好相关操作。\n\n给您带来的不便，敬请谅解！\n\n中医养生平台技术部', 1, 1, 0, '2026-02-03 00:00:00', '2026-02-06 23:59:59', '2026-02-03 10:00:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `announcement` VALUES (5, 'dgn', 'sgsegesg', 1, 0, 1, '2026-01-20 00:00:00', '2026-02-10 23:59:59', NULL, '2026-02-03 21:36:46', '2026-02-03 21:46:58', 1);
INSERT INTO `announcement` VALUES (6, 'afwsgsgesga', 'hdrsgfrdhdfhdfj', 1, 0, 0, NULL, NULL, NULL, '2026-02-03 21:37:11', '2026-02-03 21:46:55', 1);
INSERT INTO `announcement` VALUES (7, '中医养生', '啊发发不网卡', 1, 1, 0, NULL, NULL, '2026-02-06 14:46:53', '2026-02-06 14:42:43', '2026-02-06 14:47:04', 1);
INSERT INTO `announcement` VALUES (8, '中医养生', '阿斯顿法国红酒看来', 1, 1, 0, NULL, NULL, '2026-02-06 14:53:23', '2026-02-06 14:53:08', '2026-02-06 14:53:08', 0);

-- ----------------------------
-- Table structure for article_category
-- ----------------------------
DROP TABLE IF EXISTS `article_category`;
CREATE TABLE `article_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `icon` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类图标',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of article_category
-- ----------------------------
INSERT INTO `article_category` VALUES (1, '中医基础', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `article_category` VALUES (2, '养生食疗', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `article_category` VALUES (3, '经络穴位', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 3, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `article_category` VALUES (4, '四季养生', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 4, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `article_category` VALUES (5, '体质调理', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 5, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `article_category` VALUES (6, '中药知识', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 6, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `article_category` VALUES (7, '养生运动', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 7, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `article_category` VALUES (8, '心理养生', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 8, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);

-- ----------------------------
-- Table structure for community_post
-- ----------------------------
DROP TABLE IF EXISTS `community_post`;
CREATE TABLE `community_post`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
  `user_id` bigint(20) NOT NULL COMMENT '发布用户ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '帖子标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '帖子内容',
  `images` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片URLs（JSON数组）',
  `view_count` int(11) NULL DEFAULT 0 COMMENT '浏览量',
  `like_count` int(11) NULL DEFAULT 0 COMMENT '点赞数',
  `comment_count` int(11) NULL DEFAULT 0 COMMENT '评论数',
  `share_count` int(11) NULL DEFAULT 0 COMMENT '分享数',
  `status` tinyint(4) NULL DEFAULT 0 COMMENT '状态：0-待审核，1-已发布，2-已驳回，3-已删除',
  `is_top` tinyint(4) NULL DEFAULT 0 COMMENT '是否置顶',
  `is_essence` tinyint(4) NULL DEFAULT 0 COMMENT '是否精华',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '社区帖子表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of community_post
-- ----------------------------
INSERT INTO `community_post` VALUES (1, 5, '分享我的养生日常：早起一杯黄芪水', '最近开始注重养生，每天早上用黄芪泡水喝，坚持了一个月，感觉精神好多了！分享给大家～\n\n我的做法：\n1. 取黄芪10克\n2. 开水冲泡\n3. 焖15分钟后饮用\n\n坚持就是胜利！', '[\"https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg\"]', 1560, 89, 12, 5, 1, 0, 0, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `community_post` VALUES (2, 6, '求助：阳虚体质应该怎么调理？', '最近总是手脚冰凉，去看了中医说是阳虚体质，想请教大家有什么好的调理方法？平时饮食需要注意什么？', NULL, 2350, 56, 28, 3, 1, 0, 0, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `community_post` VALUES (3, 7, '八段锦打卡第30天，效果真的很好！', '从上个月开始每天早上练习八段锦，坚持了30天，感觉身体轻松多了，睡眠质量也提高了。强烈推荐给久坐办公室的朋友们！\n\n附上我的打卡记录和体会～', '[\"https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg\",\"https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg\"]', 3683, 157, 36, 28, 1, 1, 1, '2026-02-03 19:16:31', '2026-02-06 15:22:32', 0);
INSERT INTO `community_post` VALUES (4, 8, '自制养生茶配方分享', '分享几个我常喝的养生茶配方：\n\n1. 枸杞红枣茶：补血养颜\n2. 菊花决明子茶：清肝明目\n3. 玫瑰花茶：疏肝解郁\n\n大家有什么好的配方也可以分享哦～', '[\"https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg\"]', 4521, 198, 42, 35, 1, 0, 1, '2026-02-03 19:16:31', '2026-02-06 15:20:39', 0);
INSERT INTO `community_post` VALUES (5, 9, '太极拳学习心得', '学习太极拳三个月了，从完全不会到现在能打完一套24式，很有成就感！\n\n太极拳真的很适合中老年人，动作缓慢，但是全身都能锻炼到。建议大家可以去公园找老师学习～', '[\"https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg\"]', 1890, 78, 15, 8, 1, 0, 0, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `community_post` VALUES (6, 10, '艾灸初体验，效果超出预期', '第一次尝试艾灸，在平台买了艾灸盒套装，按照教程灸了足三里和关元穴，温暖的感觉很舒服，灸完感觉腹部暖暖的。\n\n想问问大家艾灸有什么需要注意的吗？', '[\"https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg\",\"https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg\"]', 2680, 112, 25, 12, 1, 0, 0, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `community_post` VALUES (7, 11, '推荐一本中医入门好书', '最近在看《黄帝内经》的白话文版本，虽然是入门书籍，但是内容很丰富，让我对中医有了更深的了解。\n\n推荐给想了解中医养生的朋友们！', NULL, 1250, 45, 8, 6, 1, 0, 0, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `community_post` VALUES (8, 12, '春季养肝食谱分享', '春天到了，分享几道养肝护肝的食谱：\n\n1. 菠菜猪肝汤\n2. 枸杞菊花茶\n3. 芹菜炒百合\n\n都是简单易做的家常菜，大家可以试试～', '[\"https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg\"]', 3252, 145, 23, 18, 1, 0, 0, '2026-02-03 19:16:31', '2026-02-06 14:19:49', 0);
INSERT INTO `community_post` VALUES (9, 15, '中医养生', '好好好好好好', NULL, 0, 0, 0, 0, 0, 0, 0, '2026-02-06 14:00:28', '2026-02-06 14:05:30', 1);
INSERT INTO `community_post` VALUES (10, 15, '中医养生', '66666666666666666666666666666666666', NULL, 4, 1, 0, 0, 1, 0, 0, '2026-02-06 14:01:53', '2026-02-06 15:30:20', 0);
INSERT INTO `community_post` VALUES (11, 15, '可以', '1111111111', NULL, 0, 0, 0, 0, 1, 0, 0, '2026-02-06 15:21:21', '2026-02-06 15:21:45', 1);

-- ----------------------------
-- Table structure for health_article
-- ----------------------------
DROP TABLE IF EXISTS `health_article`;
CREATE TABLE `health_article`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章标题',
  `category_id` bigint(20) NOT NULL COMMENT '分类ID',
  `cover_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg' COMMENT '封面图',
  `summary` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文章摘要',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章内容（富文本）',
  `author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Ti' COMMENT '作者',
  `source` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '来源',
  `view_count` int(11) NULL DEFAULT 0 COMMENT '浏览量',
  `like_count` int(11) NULL DEFAULT 0 COMMENT '点赞数',
  `collect_count` int(11) NULL DEFAULT 0 COMMENT '收藏数',
  `status` tinyint(4) NULL DEFAULT 0 COMMENT '状态：0-草稿，1-已发布，2-待审核',
  `is_recommend` tinyint(4) NULL DEFAULT 0 COMMENT '是否推荐',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '养生文章表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of health_article
-- ----------------------------
INSERT INTO `health_article` VALUES (1, '中医养生的基本原则与方法', 1, '/uploads/2026/02/06/ff175de11fb1484c96d35cd0e3ab1408.png', '了解中医养生的核心理念，掌握日常保健的基本方法', '<h2>一、中医养生的核心理念</h2><p>中医养生强调\"天人合一\"、\"形神共养\"的整体观念...</p><h2>二、养生的基本原则</h2><p>1. 顺应自然</p><p>2. 形神兼养</p><p>3. 动静结合</p><h2>三、日常养生方法</h2><p>包括饮食调养、起居调摄、运动锻炼、精神调养等方面...</p>', 'Ti', NULL, 15682, 1256, 890, 1, 1, '2026-01-15 10:00:00', '2026-02-03 19:16:31', '2026-02-06 14:37:26', 0);
INSERT INTO `health_article` VALUES (2, '春季养肝护肝的饮食秘诀', 4, '/uploads/2026/02/06/8202823a39f146268baef20e651e6c0d.png', '春季是养肝的最佳时节，学会正确的饮食调养方法', '<h2>春季为何要养肝</h2><p>中医认为，春属木，与肝相应。春季万物复苏，肝气旺盛...</p><h2>养肝食材推荐</h2><p>1. 绿色蔬菜：菠菜、芹菜、韭菜</p><p>2. 酸味食物：山楂、柠檬</p><p>3. 护肝食材：枸杞、菊花</p>', 'Ti', NULL, 12560, 986, 756, 1, 1, '2026-01-20 14:30:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `health_article` VALUES (3, '认识九种体质，找到适合自己的养生方案', 5, '/uploads/2026/02/06/59e64f872afd4959bce1e30ad3a39e40.png', '中医体质学说将人分为九种体质类型，不同体质有不同的调养方法', '<h2>什么是中医体质</h2><p>体质是人体在先天禀赋和后天调养的基础上形成的相对稳定的特征...</p><h2>九种体质类型</h2><p>1. 平和质 2. 气虚质 3. 阳虚质 4. 阴虚质</p><p>5. 痰湿质 6. 湿热质 7. 血瘀质 8. 气郁质 9. 特禀质</p>', 'Ti', NULL, 18950, 1560, 1230, 1, 1, '2026-01-22 09:00:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `health_article` VALUES (4, '常用养生药材的功效与选购指南', 6, '/uploads/2026/02/06/18dac2d44c994ef7a3dd6a318e5c9710.png', '了解常见中药材的功效，学会正确选购优质药材', '<h2>补气类药材</h2><p>黄芪：补气升阳，固表止汗</p><p>人参：大补元气，复脉固脱</p><h2>补血类药材</h2><p>当归：补血活血，调经止痛</p><p>阿胶：补血滋阴，润燥止血</p><h2>选购技巧</h2><p>望、闻、问、切四法辨别药材品质...</p>', 'Ti', NULL, 9870, 756, 568, 1, 0, '2026-01-25 11:00:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `health_article` VALUES (5, '八段锦：简单易学的养生功法', 7, '/uploads/2026/02/06/885f85062a3b4ec8ba8de402679c40dd.png', '八段锦是一套简单易学的养生功法，适合各年龄段人群练习', '<h2>八段锦简介</h2><p>八段锦是我国古代导引术的精华，由八个动作组成...</p><h2>八个动作详解</h2><p>1. 双手托天理三焦</p><p>2. 左右开弓似射雕</p><p>3. 调理脾胃须单举...</p><h2>练习注意事项</h2><p>动作要缓慢柔和，呼吸要自然...</p>', 'Ti', NULL, 25680, 2150, 1890, 1, 1, '2026-01-28 08:30:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `health_article` VALUES (6, '失眠的中医调理方法', 8, '/uploads/2026/02/06/ecead263366841148700ae8ef5f1a8af.png', '从中医角度分析失眠原因，介绍多种自然调理方法', '<h2>中医看失眠</h2><p>失眠在中医称为\"不寐\"，与心、肝、脾、肾密切相关...</p><h2>常见证型</h2><p>1. 心脾两虚 2. 心肾不交 3. 肝郁化火</p><h2>调理方法</h2><p>食疗、穴位按摩、足浴、情志调节等...</p>', 'dawn', NULL, 32560, 2680, 2150, 1, 1, '2026-01-30 16:00:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `health_article` VALUES (7, '夏季防暑降温的养生之道', 4, '/uploads/2026/02/06/76e511b9a08e4d88917b2a6d8966df77.png', '夏季炎热，学会正确的防暑降温方法，安然度夏', '<h2>夏季养生原则</h2><p>夏季属火，与心相应，养生重在养心...</p><h2>饮食调养</h2><p>多吃清热解暑的食物，如绿豆、西瓜、苦瓜...</p><h2>起居注意</h2><p>晚睡早起，适当午休，避免贪凉...</p>', 'Ti', NULL, 8562, 680, 450, 1, 0, '2026-02-01 10:00:00', '2026-02-03 19:16:31', '2026-02-06 13:27:35', 0);
INSERT INTO `health_article` VALUES (8, '常用保健穴位及按摩方法', 3, '/uploads/2026/02/06/7990fc95057443ecb41903868d48c6ce.png', '介绍日常保健常用穴位，学会简单的自我按摩方法', '<h2>头面部穴位</h2><p>百会穴、太阳穴、印堂穴...</p><h2>四肢穴位</h2><p>合谷穴、足三里、三阴交...</p><h2>按摩手法</h2><p>点按、揉按、推拿等基本手法...</p>', 'Ti', NULL, 19874, 1580, 1260, 1, 1, '2026-02-02 14:00:00', '2026-02-03 19:16:31', '2026-02-06 14:10:43', 0);
INSERT INTO `health_article` VALUES (9, 'ggehejej', 1, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 'heheje', 'esheuewfhshahaw', 'Ti', NULL, 0, 0, 0, 0, 0, NULL, '2026-02-03 21:53:38', '2026-02-03 21:53:42', 1);
INSERT INTO `health_article` VALUES (10, '气虚体质如何通过食疗补气养生', 1, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '气虚体质的人容易疲劳、气短，本文介绍黄芪、党参、山药等补气食疗方案', '<p>气虚体质是中医九大体质之一，主要表现为容易疲劳、气短懒言、易出汗...</p><p>推荐食材：黄芪、人参、党参、山药、大枣等补气药材...</p>', '养生专家', NULL, 3680, 256, 180, 1, 1, '2026-03-01 09:00:00', '2026-03-27 21:23:26', '2026-03-27 21:23:26', 0);
INSERT INTO `health_article` VALUES (11, '阳虚怕冷怎么办？冬季温补养生指南', 1, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '阳虚体质的人畏寒怕冷、手脚冰凉，本文教你如何温补阳气', '<p>阳虚体质的人最怕冬天，容易手脚冰凉、精神不振...</p><p>温补推荐：肉桂、鹿茸、杜仲、巴戟天等温阳药材...</p>', '中医养生师', NULL, 4210, 312, 220, 1, 1, '2026-03-05 10:00:00', '2026-03-27 21:23:26', '2026-03-27 21:23:26', 0);
INSERT INTO `health_article` VALUES (12, '阴虚火旺？滋阴润燥的5个养生妙招', 1, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '阴虚体质的人容易口干、盗汗，枸杞、麦冬、百合是滋阴好帮手', '<p>阴虚体质是现代人常见的亚健康体质，与熬夜、压力大有关...</p><p>滋阴推荐：枸杞、麦冬、百合、石斛、玉竹等养阴药材...</p>', '健康顾问', NULL, 2890, 198, 150, 1, 0, '2026-03-08 14:00:00', '2026-03-27 21:23:26', '2026-03-27 21:23:26', 0);
INSERT INTO `health_article` VALUES (13, '痰湿体质必看：祛湿健脾的正确方法', 1, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '痰湿体质容易发胖、困倦，薏仁、茯苓、陈皮帮你健脾祛湿', '<p>痰湿体质的人体型偏胖，容易犯困，大便粘腻...</p><p>祛湿推荐：薏仁、茯苓、陈皮、山药等健脾药材...</p>', '中医营养师', NULL, 3450, 245, 170, 1, 0, '2026-03-10 09:00:00', '2026-03-27 21:23:26', '2026-03-27 21:23:26', 0);
INSERT INTO `health_article` VALUES (14, '春季养肝：枸杞菊花茶的正确喝法', 2, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '春季养肝护肝，枸杞菊花茶是最简单有效的养生茶饮', '<p>中医认为春季与肝相应，是养肝护肝的最佳季节...</p><p>推荐茶饮：枸杞菊花茶、决明子茶等疏肝明目...</p>', '茶道养生师', NULL, 5680, 420, 310, 1, 1, '2026-03-12 10:00:00', '2026-03-27 21:23:26', '2026-03-27 21:23:26', 0);
INSERT INTO `health_article` VALUES (15, '活血化瘀的日常养生：三七、丹参的妙用', 1, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '血瘀体质面色晦暗、易瘀青，三七、丹参、红花帮你活血通络', '<p>血瘀体质是指血液运行不畅，容易出现瘀血的体质状态...</p><p>活血推荐：三七粉、丹参、红花、桃仁等活血药材...</p>', '中医药学家', NULL, 2340, 165, 120, 1, 0, '2026-03-15 14:00:00', '2026-03-27 21:23:26', '2026-03-27 21:23:26', 0);

-- ----------------------------
-- Table structure for health_plan
-- ----------------------------
DROP TABLE IF EXISTS `health_plan`;
CREATE TABLE `health_plan`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '方案ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '方案标题',
  `cover_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg' COMMENT '封面图',
  `constitution_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '适用体质类型',
  `season` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '适用季节',
  `summary` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '方案摘要',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '方案详情（富文本）',
  `diet_advice` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '饮食建议',
  `exercise_advice` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '运动建议',
  `sleep_advice` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '睡眠建议',
  `medicine_advice` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '药材建议',
  `taboo` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '禁忌事项',
  `view_count` int(11) NULL DEFAULT 0 COMMENT '浏览量',
  `collect_count` int(11) NULL DEFAULT 0 COMMENT '收藏数',
  `status` tinyint(4) NULL DEFAULT 0 COMMENT '状态：0-草稿，1-已发布，2-已下架',
  `is_recommend` tinyint(4) NULL DEFAULT 0 COMMENT '是否推荐',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_constitution_type`(`constitution_type`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '养生方案表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of health_plan
-- ----------------------------
INSERT INTO `health_plan` VALUES (1, '气虚体质调理方案', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '气虚质', '四季通用', '针对气虚体质人群的综合调理方案，帮助补气健脾', '<h2>气虚体质特点</h2><p>常见表现：容易疲劳、气短懒言、易出汗、免疫力低下...</p><h2>调理目标</h2><p>补气健脾、增强体质、提高免疫力...</p>', '多食补气食物：黄芪、党参、山药、大枣、糯米等；少食耗气食物：萝卜、空心菜等', '选择柔和运动：太极拳、八段锦、散步等，避免剧烈运动', '保证充足睡眠，每天7-8小时，避免熬夜', '可选用四君子汤、补中益气汤等经典方剂', '忌过度劳累、忌食生冷、忌大汗淋漓', 8561, 680, 1, 1, '2026-01-10 09:00:00', '2026-02-03 19:16:31', '2026-02-06 14:32:06', 0);
INSERT INTO `health_plan` VALUES (2, '阳虚体质调理方案', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '阳虚质', '四季通用', '针对阳虚体质人群的温阳散寒调理方案', '<h2>阳虚体质特点</h2><p>常见表现：畏寒怕冷、手脚冰凉、精神不振、大便稀溏...</p><h2>调理目标</h2><p>温补阳气、驱寒暖身...</p>', '多食温热食物：羊肉、牛肉、韭菜、生姜、桂圆等；忌食生冷寒凉', '晨起运动最佳，推荐慢跑、快走、太极拳等', '早睡早起，睡前泡脚，保持足部温暖', '可选用金匮肾气丸、右归丸等', '忌贪凉饮冷、忌久居寒湿环境', 7651, 590, 1, 1, '2026-01-12 10:00:00', '2026-02-03 19:16:31', '2026-03-27 19:13:14', 0);
INSERT INTO `health_plan` VALUES (3, '痰湿体质调理方案', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '痰湿质', '四季通用', '针对痰湿体质人群的健脾祛湿调理方案', '<h2>痰湿体质特点</h2><p>常见表现：体型偏胖、腹部肥满、容易困倦、痰多、大便粘腻...</p><h2>调理目标</h2><p>健脾利湿、化痰降浊...</p>', '饮食清淡，多食健脾祛湿食物：薏米、赤小豆、冬瓜、荷叶等；忌肥甘厚腻', '增加运动量，推荐有氧运动：游泳、慢跑、骑行等', '不宜过多睡眠，午休不超过30分钟', '可选用参苓白术散、二陈汤等', '忌暴饮暴食、忌久坐久卧、忌潮湿环境', 6890, 480, 1, 0, '2026-01-15 11:00:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `health_plan` VALUES (4, '春季养肝方案', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '平和质', '春季', '春季养肝护肝的综合调理方案', '<h2>春季养生原则</h2><p>春属木，与肝相应，春季养生重在养肝...</p><h2>调理重点</h2><p>疏肝理气、养血柔肝...</p>', '多食绿色蔬菜，适当增加酸味食物，如山楂、柠檬等；推荐：菠菜、芹菜、荠菜', '春季宜户外活动，踏青、放风筝等舒展身心', '早睡早起，与春气相应，保证睡眠质量', '可用菊花、枸杞泡茶，疏肝明目', '忌动怒发火、忌过食油腻、忌熬夜伤肝', 12561, 980, 1, 1, '2026-01-18 09:00:00', '2026-02-03 19:16:31', '2026-03-27 21:24:54', 0);
INSERT INTO `health_plan` VALUES (5, '女性气血调理方案', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '气虚质', '四季通用', '专为女性设计的补气养血调理方案', '<h2>女性气血特点</h2><p>女性以血为本，经带胎产都与气血密切相关...</p><h2>调理目标</h2><p>补气养血、调经养颜...</p>', '多食补血食物：红枣、桂圆、阿胶、当归、枸杞等；经期可喝红糖姜水', '瑜伽、太极、散步等柔和运动，避免经期剧烈运动', '保证充足睡眠，经前注意休息', '可选用八珍汤、四物汤等经典方剂', '经期忌生冷、忌剧烈运动、忌情绪波动', 18951, 1560, 1, 1, '2026-01-20 14:00:00', '2026-02-03 19:16:31', '2026-02-06 14:10:39', 0);
INSERT INTO `health_plan` VALUES (6, '中老年健康养生方案', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '气虚质', '四季通用', '针对中老年人群的全面养生调理方案', '<h2>中老年养生特点</h2><p>随着年龄增长，脏腑功能逐渐减退...</p><h2>调理重点</h2><p>补肾固本、健脾养胃、活血通络...</p>', '饮食清淡易消化，多食优质蛋白，适当补钙；推荐：鱼肉、豆制品、牛奶等', '太极拳、八段锦、散步等舒缓运动，注意安全', '早睡早起，午间小憩，睡眠不宜过长', '可用党参、黄芪、枸杞等泡茶或煲汤', '忌烟酒、忌过度劳累、忌情绪激动', 15681, 1280, 1, 1, '2026-01-25 10:00:00', '2026-02-03 19:16:31', '2026-02-06 13:20:52', 0);
INSERT INTO `health_plan` VALUES (7, 'gse', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '平和质', '春季', NULL, 'sdhs', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 0, NULL, '2026-02-03 21:47:54', '2026-02-03 21:47:57', 1);
INSERT INTO `health_plan` VALUES (8, 'sfg', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '气虚质', '夏季', NULL, 'sgsagshdjhshsh', NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 0, NULL, '2026-02-03 21:50:02', '2026-02-03 21:50:09', 1);
INSERT INTO `health_plan` VALUES (9, '阴虚体质滋阴调理方案', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '阴虚质', '四季通用', '针对阴虚体质人群的滋阴润燥调理方案', '<h2>阴虚体质特点</h2><p>常见表现：手足心热、口干咽燥、面色潮红、盗汗、大便干燥...</p>', '多食滋阴食物：百合、银耳、枸杞、麦冬、莲子、梨等；少食辛辣燥热食物', '选择柔和运动：太极、瑜伽、游泳，避免大汗淋漓', '早睡不熬夜，保持卧室湿润', '可用六味地黄丸、百合固金汤等', '忌辛辣燥热、忌熬夜、忌烟酒', 5680, 420, 1, 1, '2026-01-22 10:00:00', '2026-03-27 21:23:26', '2026-03-27 21:23:26', 0);
INSERT INTO `health_plan` VALUES (10, '湿热体质清热祛湿方案', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '湿热质', '四季通用', '针对湿热体质人群的清热利湿调理方案', '<h2>湿热体质特点</h2><p>常见表现：面部油腻、口苦口干、身体困重、大便黏滞...</p>', '饮食清淡，多食清热祛湿食物：薏米、冬瓜、绿豆、荷叶茶等；忌油腻辛辣', '坚持有氧运动，出汗排湿', '不宜睡懒觉，早起活动', '可用龙胆泻肝汤、茵陈蒿汤等', '忌肥甘厚腻、忌饮酒、忌潮湿环境', 4890, 360, 1, 1, '2026-01-25 14:00:00', '2026-03-27 21:23:26', '2026-03-27 21:23:26', 0);
INSERT INTO `health_plan` VALUES (11, '气郁体质疏肝解郁方案', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '气郁质', '四季通用', '针对气郁体质人群的疏肝理气调理方案', '<h2>气郁体质特点</h2><p>常见表现：情绪抑郁、胸胁胀满、善太息、咽喉有异物感...</p>', '多食疏肝理气食物：玫瑰花茶、佛手、陈皮、薄荷等；忌辛辣刺激', '多做户外运动，跑步、登山等舒展身心', '保持规律作息，睡前冥想放松', '可用逍遥丸、柴胡疏肝散等', '忌郁闷独处、忌饮酒消愁', 3780, 280, 1, 0, '2026-01-28 09:00:00', '2026-03-27 21:23:26', '2026-03-27 21:23:26', 0);
INSERT INTO `health_plan` VALUES (12, '特禀体质固表抗敏方案', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '特禀质', '四季通用', '针对特禀体质（过敏体质）人群的固表脱敏调理方案', '<h2>特禀体质特点</h2><p>常见表现：容易过敏、皮肤敏感、打喷嚏、起荨麻疹...</p>', '饮食清淡均衡，避开已知过敏源；推荐：灵芝、黄芪、蜂蜜等增强免疫', '适度运动增强体质，避免寒冷刺激', '保证充足睡眠，增强抵抗力', '可用玉屏风散、消风散等', '忌接触过敏源、忌过食生冷、忌环境刺激', 2560, 190, 1, 0, '2026-02-01 10:00:00', '2026-03-27 21:23:26', '2026-03-27 21:23:26', 0);
INSERT INTO `health_plan` VALUES (13, '血瘀体质活血通络方案', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '血瘀质', '四季通用', '针对血瘀体质人群的活血化瘀调理方案', '<h2>血瘀体质特点</h2><p>常见表现：面色晦暗、皮肤粗糙、容易瘀青、经血暗红有血块...</p>', '多食活血食物：山楂、黑木耳、洋葱、三七粉等；忌寒凉收涩食物', '坚持运动促进血液循环，推荐：慢跑、游泳', '规律作息，避免久坐不动', '可用桃红四物汤、血府逐瘀汤等', '忌久坐久卧、忌情绪压抑、忌寒凉', 4120, 310, 1, 1, '2026-02-03 09:00:00', '2026-03-27 21:23:26', '2026-03-27 21:23:26', 0);
INSERT INTO `health_plan` VALUES (14, '夏季清热养心方案', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '平和质', '夏季', '夏季清热解暑、养心安神的综合调理方案', '<h2>夏季养生原则</h2><p>夏属火，与心相应，夏季养生重在清热养心...</p>', '多食清凉食物：绿豆、西瓜、莲子、百合等；适当饮用菊花茶、金银花茶', '晨起或傍晚运动，避免烈日暴晒', '适当午休，早睡早起', '可用酸梅汤、莲子心茶等', '忌贪凉过度、忌暴饮暴食、忌烈日运动', 9860, 720, 1, 1, '2026-02-05 10:00:00', '2026-03-27 21:23:26', '2026-03-27 21:23:26', 0);
INSERT INTO `health_plan` VALUES (15, '秋季润肺养阴方案', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '平和质', '秋季', '秋季润肺防燥、滋阴养颜的综合调理方案', '<h2>秋季养生原则</h2><p>秋属金，与肺相应，秋季养生重在润肺防燥...</p>', '多食润肺食物：雪梨、银耳、百合、蜂蜜、芝麻等；少食辛辣', '适当户外运动，呼吸新鲜空气', '早睡早起，保持心情平和', '可用川贝枇杷膏、秋梨膏等', '忌辛辣燥热、忌熬夜伤阴', 8540, 650, 1, 1, '2026-02-08 14:00:00', '2026-03-27 21:23:26', '2026-03-27 21:23:26', 0);
INSERT INTO `health_plan` VALUES (16, '冬季温补养肾方案', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '平和质', '冬季', '冬季温补养肾、藏精蓄锐的综合调理方案', '<h2>冬季养生原则</h2><p>冬属水，与肾相应，冬季养生重在温补养肾...</p>', '多食温补食物：羊肉、牛肉、核桃、板栗、黑芝麻等；适量进补', '室内运动为主，太极、八段锦；天气好时户外散步', '早卧晚起，保证充足睡眠', '可用人参、鹿茸、枸杞等泡酒或煲汤', '忌贪凉饮冷、忌大汗淋漓、忌过度进补', 11200, 880, 1, 1, '2026-02-10 09:00:00', '2026-03-27 21:23:26', '2026-03-27 21:23:26', 0);

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资讯ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '资讯标题',
  `cover_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg' COMMENT '封面图',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '资讯分类',
  `summary` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '摘要',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容（富文本）',
  `author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Ti' COMMENT '作者',
  `source` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '来源',
  `view_count` int(11) NULL DEFAULT 0 COMMENT '浏览量',
  `status` tinyint(4) NULL DEFAULT 0 COMMENT '状态：0-草稿，1-已发布，2-待审核',
  `is_top` tinyint(4) NULL DEFAULT 0 COMMENT '是否置顶',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_publish_time`(`publish_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '资讯表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of news
-- ----------------------------
INSERT INTO `news` VALUES (1, '中医养生平台正式上线，开启健康新生活', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '行业动态', '我们的中医养生平台今日正式上线，为广大用户提供专业的养生服务', '<p>经过团队数月的精心筹备，中医养生平台今日正式上线！</p><p>平台致力于传播中医养生文化，提供优质的中药材产品和专业的养生方案...</p><p>欢迎广大养生爱好者注册使用，开启健康新生活！</p>', 'Ti', NULL, 5680, 1, 1, '2026-01-01 10:00:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `news` VALUES (2, '新春送健康，养生药材大促活动开启', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '养生资讯', '新春佳节将至，平台推出养生药材大促活动', '<p>新春将至，中医养生平台特别推出\"新春送健康\"活动！</p><p>活动期间，精选养生药材全场8折起，下单即送养生茶包...</p><p>活动时间：2026年1月20日-2月20日</p>', 'Ti', NULL, 8950, 1, 1, '2026-01-18 09:00:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `news` VALUES (3, '专家解读：冬季进补的正确打开方式', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '养生科普', '冬季是进补的好时节，但进补也需要讲究方法', '<p>冬季天寒地冻，人体阳气内藏，是进补的最佳时机。</p><p>但进补不是越多越好，需要根据个人体质选择合适的补品...</p><p>本文邀请中医专家为您详细解读冬季进补的注意事项。</p>', 'Ti', NULL, 6780, 1, 0, '2026-01-22 14:00:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `news` VALUES (4, '中医养生知识竞赛活动圆满结束', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '平台动态', '为期一周的中医养生知识竞赛活动圆满落幕', '<p>由平台举办的中医养生知识竞赛活动已圆满结束。</p><p>本次活动共有5000余名用户参与，展现了大家对中医养生文化的热爱...</p><p>获奖名单已公布，请获奖用户关注站内消息。</p>', 'Ti', NULL, 3560, 1, 0, '2026-01-28 11:00:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `news` VALUES (5, '平台入驻多位知名中医专家，提供在线咨询服务', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '平台动态', '多位资深中医专家入驻平台，为用户提供专业的健康咨询', '<p>为了给用户提供更专业的养生指导，平台特邀多位知名中医专家入驻。</p><p>用户可通过AI智能客服获得初步咨询，复杂问题可预约专家在线问诊...</p>', 'Ti', NULL, 4890, 1, 0, '2026-02-01 10:00:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `news` VALUES (6, 'fxhsdhs', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '养生资讯', NULL, 'sgagdhdfhdsjd', 'Ti', NULL, 0, 0, 0, NULL, '2026-02-03 21:38:17', '2026-02-03 21:46:48', 1);
INSERT INTO `news` VALUES (7, '中医养生', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '行业动态', '十多个房价过快', '<p>啊方法户外hi<strong>覅全欧杰</strong></p>', 'Ti', NULL, 1, 1, 0, '2026-02-06 14:30:23', '2026-02-06 14:23:09', '2026-02-06 14:38:30', 1);

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '操作用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作用户名',
  `module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作模块',
  `operation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作描述',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求方法',
  `request_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求URL',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求方式',
  `request_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '请求参数',
  `response_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '返回结果',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户代理',
  `execute_time` bigint(20) NULL DEFAULT NULL COMMENT '执行时长(ms)',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态：0-失败，1-成功',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '错误信息',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '操作日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `pay_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '实付金额',
  `freight_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '运费',
  `discount_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '优惠金额',
  `status` tinyint(4) NULL DEFAULT 0 COMMENT '订单状态：0-待支付，1-已支付，2-已发货，3-已完成，4-已取消',
  `pay_type` tinyint(4) NULL DEFAULT NULL COMMENT '支付方式：1-微信，2-支付宝',
  `address_id` bigint(20) NULL DEFAULT NULL COMMENT '收货地址ID',
  `receiver_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收货人电话',
  `receiver_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收货地址详情',
  `express_company` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '快递公司',
  `express_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '快递单号',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '订单备注',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `ship_time` datetime NULL DEFAULT NULL COMMENT '发货时间',
  `complete_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  `cancel_time` datetime NULL DEFAULT NULL COMMENT '取消时间',
  `cancel_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '取消原因',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no`) USING BTREE,
  INDEX `idx_order_no`(`order_no`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单主表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES (1, 'ORD202601150001', 5, 456.00, 456.00, 0.00, 0.00, 3, 1, NULL, '张明', '13900000001', '北京市朝阳区建国路88号中医养生大厦1001室', '顺丰速运', 'SF1234567890', '请尽快发货', '2026-01-15 10:30:00', '2026-01-16 09:00:00', '2026-01-20 15:00:00', NULL, NULL, '2026-01-15 10:00:00', '2026-02-03 19:16:31', 0);
INSERT INTO `order_info` VALUES (2, 'ORD202601180002', 6, 688.00, 688.00, 0.00, 0.00, 3, 2, NULL, '李红', '13900000002', '上海市浦东新区陆家嘴环路1000号金融中心', '中通快递', 'ZT9876543210', '', '2026-01-18 14:30:00', '2026-01-19 10:00:00', '2026-01-25 11:00:00', NULL, NULL, '2026-01-18 14:00:00', '2026-02-03 19:16:31', 0);
INSERT INTO `order_info` VALUES (3, 'ORD202601200003', 7, 268.00, 268.00, 0.00, 0.00, 2, 1, NULL, '张伟', '13900000003', '广东省广州市天河区天河路385号太古汇', '韵达快递', 'YD1122334455', '发韵达', '2026-01-20 16:00:00', '2026-01-21 09:30:00', NULL, NULL, NULL, '2026-01-20 15:30:00', '2026-02-03 19:16:31', 0);
INSERT INTO `order_info` VALUES (4, 'ORD202601220004', 8, 128.00, 128.00, 0.00, 0.00, 1, 1, NULL, '王芳', '13900000004', '江苏省南京市鼓楼区中山北路101号', NULL, NULL, '', '2026-01-22 11:00:00', NULL, NULL, NULL, NULL, '2026-01-22 10:30:00', '2026-02-03 19:16:31', 0);
INSERT INTO `order_info` VALUES (5, 'ORD202601250005', 5, 1888.00, 1888.00, 0.00, 0.00, 3, 2, NULL, '张明', '13900000001', '北京市朝阳区建国路88号中医养生大厦1001室', '顺丰速运', 'SF2233445566', '贵重物品请小心', '2026-01-25 09:30:00', '2026-01-25 14:00:00', '2026-01-30 10:00:00', NULL, NULL, '2026-01-25 09:00:00', '2026-02-03 19:16:31', 0);
INSERT INTO `order_info` VALUES (6, 'ORD202601280006', 9, 198.00, 198.00, 0.00, 0.00, 4, NULL, NULL, '李强', '13900000005', '浙江省杭州市西湖区文三路269号', NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, '2026-01-28 16:00:00', '2026-02-03 19:16:31', 0);
INSERT INTO `order_info` VALUES (7, 'ORD202602010007', 10, 356.00, 356.00, 0.00, 0.00, 0, NULL, NULL, '陈美', '13900000006', '四川省成都市武侯区人民南路四段88号', NULL, NULL, '送货上门', NULL, NULL, NULL, NULL, NULL, '2026-02-01 10:00:00', '2026-02-03 19:16:31', 0);
INSERT INTO `order_info` VALUES (8, 'ORD202602020008', 11, 526.00, 526.00, 0.00, 0.00, 1, 1, NULL, '王建国', '13900000007', '湖北省武汉市武昌区中南路99号', NULL, NULL, '', '2026-02-02 15:00:00', NULL, NULL, NULL, NULL, '2026-02-02 14:30:00', '2026-02-03 19:16:31', 0);
INSERT INTO `order_info` VALUES (9, 'ORD202602030009', 12, 168.00, 168.00, 0.00, 0.00, 0, NULL, NULL, '陈晓', '13900000008', '福建省厦门市思明区厦禾路888号', NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, '2026-02-03 09:00:00', '2026-02-03 19:16:31', 0);
INSERT INTO `order_info` VALUES (10, 'ORD202602030010', 5, 298.00, 298.00, 0.00, 0.00, 0, NULL, NULL, '张明', '13900000001', '北京市海淀区中关村南大街5号', NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, '2026-02-03 10:30:00', '2026-02-03 19:16:31', 0);
INSERT INTO `order_info` VALUES (11, 'ORD202602061321507AEB36', 15, 136.00, 136.00, 0.00, 0.00, 3, 1, NULL, '洋芋饭', '18888888888', '湖北武汉洪山区狮子路106号', '顺丰速运', ' ORD202602061321507AEB36', NULL, '2026-02-06 13:21:52', '2026-02-06 13:23:46', '2026-02-06 13:24:05', NULL, NULL, '2026-02-06 13:21:50', '2026-02-06 13:21:50', 0);
INSERT INTO `order_info` VALUES (12, 'ORD202602061324296EB3B0', 15, 68.00, 68.00, 0.00, 0.00, 1, 1, NULL, '洋芋饭', '18888888888', '湖北武汉洪山区狮子路106号', NULL, NULL, NULL, '2026-02-06 13:24:30', NULL, NULL, NULL, NULL, '2026-02-06 13:24:29', '2026-02-06 13:24:29', 0);

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `product_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品图片',
  `product_price` decimal(10, 2) NOT NULL COMMENT '商品单价',
  `quantity` int(11) NOT NULL COMMENT '购买数量',
  `total_price` decimal(10, 2) NOT NULL COMMENT '小计金额',
  `is_reviewed` tinyint(4) NULL DEFAULT 0 COMMENT '是否已评价：0-否，1-是',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单明细表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (1, 1, 'ORD202601150001', 1, '精选黄芪片', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 68.00, 2, 136.00, 1, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (2, 1, 'ORD202601150001', 6, '当归片', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 35.00, 2, 70.00, 1, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (3, 1, 'ORD202601150001', 17, '玫瑰花茶', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 36.00, 2, 72.00, 0, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (4, 1, 'ORD202601150001', 18, '枸杞菊花茶', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 28.00, 2, 56.00, 0, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (5, 1, 'ORD202601150001', 14, '三七粉', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 128.00, 1, 128.00, 1, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (6, 2, 'ORD202601180002', 21, '即食燕窝', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 688.00, 1, 688.00, 1, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (7, 3, 'ORD202601200003', 5, '阿胶块', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 268.00, 1, 268.00, 0, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (8, 4, 'ORD202601220004', 14, '三七粉', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 128.00, 1, 128.00, 0, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (9, 5, 'ORD202601250005', 23, '冬虫夏草', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1888.00, 1, 1888.00, 1, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (10, 6, 'ORD202601280006', 29, '足浴盆', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 198.00, 1, 198.00, 0, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (11, 7, 'ORD202602010007', 17, '玫瑰花茶', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 36.00, 3, 108.00, 0, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (12, 7, 'ORD202602010007', 9, '菊花茶', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 42.00, 2, 84.00, 0, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (13, 7, 'ORD202602010007', 22, '鲜炖燕窝', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 168.00, 1, 168.00, 0, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (14, 8, 'ORD202602020008', 25, '艾灸盒套装', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 68.00, 2, 136.00, 0, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (15, 8, 'ORD202602020008', 26, '砭石刮痧板', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 128.00, 1, 128.00, 0, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (16, 8, 'ORD202602020008', 13, '陈皮', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 48.00, 2, 96.00, 0, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (17, 8, 'ORD202602020008', 18, '枸杞菊花茶', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 28.00, 2, 56.00, 0, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (18, 8, 'ORD202602020008', 8, '金银花', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 58.00, 2, 116.00, 0, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (19, 9, 'ORD202602030009', 22, '鲜炖燕窝', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 168.00, 1, 168.00, 0, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (20, 10, 'ORD202602030010', 28, '颈椎按摩仪', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 298.00, 1, 298.00, 0, '2026-02-03 19:16:31');
INSERT INTO `order_item` VALUES (21, 11, 'ORD202602061321507AEB36', 1, '精选黄芪片', '/uploads/2026/02/06/ec3d3d84808a4d67b70e9fb5632250b9.png', 68.00, 2, 136.00, 1, '2026-02-06 13:21:50');
INSERT INTO `order_item` VALUES (22, 12, 'ORD202602061324296EB3B0', 1, '精选黄芪片', '/uploads/2026/02/06/ec3d3d84808a4d67b70e9fb5632250b9.png', 68.00, 1, 68.00, 0, '2026-02-06 13:24:29');

-- ----------------------------
-- Table structure for order_payment
-- ----------------------------
DROP TABLE IF EXISTS `order_payment`;
CREATE TABLE `order_payment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '支付记录ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `pay_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '第三方支付流水号',
  `pay_type` tinyint(4) NOT NULL COMMENT '支付方式：1-微信，2-支付宝',
  `pay_amount` decimal(10, 2) NOT NULL COMMENT '支付金额',
  `pay_status` tinyint(4) NULL DEFAULT 0 COMMENT '支付状态：0-未支付，1-已支付，2-支付失败',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `callback_time` datetime NULL DEFAULT NULL COMMENT '回调时间',
  `callback_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '回调内容',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_order_no`(`order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单支付记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order_payment
-- ----------------------------
INSERT INTO `order_payment` VALUES (1, 1, 'ORD202601150001', 'WX202601151030001', 1, 456.00, 1, '2026-01-15 10:30:00', NULL, NULL, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `order_payment` VALUES (2, 2, 'ORD202601180002', 'ALI202601181430001', 2, 688.00, 1, '2026-01-18 14:30:00', NULL, NULL, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `order_payment` VALUES (3, 3, 'ORD202601200003', 'WX202601201600001', 1, 268.00, 1, '2026-01-20 16:00:00', NULL, NULL, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `order_payment` VALUES (4, 4, 'ORD202601220004', 'WX202601221100001', 1, 128.00, 1, '2026-01-22 11:00:00', NULL, NULL, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `order_payment` VALUES (5, 5, 'ORD202601250005', 'ALI202601250930001', 2, 1888.00, 1, '2026-01-25 09:30:00', NULL, NULL, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `order_payment` VALUES (6, 8, 'ORD202602020008', 'WX202602021500001', 1, 526.00, 1, '2026-02-02 15:00:00', NULL, NULL, '2026-02-03 19:16:31', '2026-02-03 19:16:31');

-- ----------------------------
-- Table structure for post_comment
-- ----------------------------
DROP TABLE IF EXISTS `post_comment`;
CREATE TABLE `post_comment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `post_id` bigint(20) NOT NULL COMMENT '帖子ID',
  `user_id` bigint(20) NOT NULL COMMENT '评论用户ID',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父评论ID，0表示一级评论',
  `reply_user_id` bigint(20) NULL DEFAULT NULL COMMENT '回复的用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `like_count` int(11) NULL DEFAULT 0 COMMENT '点赞数',
  `status` tinyint(4) NULL DEFAULT 0 COMMENT '状态：0-待审核，1-已通过，2-已驳回',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_post_id`(`post_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '帖子评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of post_comment
-- ----------------------------
INSERT INTO `post_comment` VALUES (1, 1, 6, 0, NULL, '很棒的分享！我也要试试', 5, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (2, 1, 7, 0, NULL, '黄芪确实很好，我也在喝', 3, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (3, 1, 8, 1, 6, '一起坚持！', 1, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (4, 2, 5, 0, NULL, '建议多吃羊肉、韭菜等温热食物，少吃生冷', 12, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (5, 2, 7, 0, NULL, '可以试试艾灸，对阳虚很有帮助', 8, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (6, 2, 9, 0, NULL, '我也是阳虚，吃了一段时间金匮肾气丸，感觉有改善', 6, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (7, 2, 5, 6, 9, '金匮肾气丸确实不错，我也在吃', 2, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (8, 3, 6, 0, NULL, '太厉害了！我也想学习八段锦', 8, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (9, 3, 8, 0, NULL, '八段锦真的很适合上班族，我也在练', 5, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (10, 3, 10, 0, NULL, '请问有推荐的教学视频吗？', 3, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (11, 3, 7, 10, 10, '可以在B站搜索\"八段锦教学\"，有很多不错的视频', 6, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (12, 4, 5, 0, NULL, '收藏了！回头试试', 4, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (13, 4, 9, 0, NULL, '我推荐再加点桂圆，补血效果更好', 7, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (14, 4, 6, 0, NULL, '玫瑰花茶我也爱喝，香气很好', 3, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (15, 5, 10, 0, NULL, '太极拳确实很好，我爸也在学', 2, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (16, 6, 5, 0, NULL, '艾灸注意不要烫伤，灸完多喝温水', 8, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (17, 6, 7, 0, NULL, '艾灸后不要吹风受凉哦', 5, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (18, 7, 8, 0, NULL, '好书推荐！已下单', 2, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (19, 8, 6, 0, NULL, '芹菜炒百合听起来不错，明天试试', 4, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (20, 8, 10, 0, NULL, '菠菜猪肝汤是经典的补血菜，很推荐', 3, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `post_comment` VALUES (21, 8, 15, 0, NULL, '是的', 0, 1, '2026-02-06 14:10:31', '2026-02-06 14:10:31', 0);
INSERT INTO `post_comment` VALUES (22, 3, 15, 0, NULL, '666', 0, 0, '2026-02-06 15:22:33', '2026-02-06 15:22:43', 1);

-- ----------------------------
-- Table structure for post_like
-- ----------------------------
DROP TABLE IF EXISTS `post_like`;
CREATE TABLE `post_like`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `post_id` bigint(20) NOT NULL COMMENT '帖子ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_post_user`(`post_id`, `user_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '帖子点赞表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of post_like
-- ----------------------------
INSERT INTO `post_like` VALUES (1, 1, 6, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (2, 1, 7, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (3, 1, 8, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (4, 1, 9, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (5, 2, 5, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (6, 2, 7, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (7, 2, 8, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (8, 3, 5, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (9, 3, 6, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (10, 3, 8, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (11, 3, 9, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (12, 3, 10, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (13, 3, 11, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (14, 3, 12, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (15, 4, 5, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (16, 4, 6, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (17, 4, 7, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (18, 4, 9, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (19, 4, 10, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (20, 5, 6, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (21, 5, 10, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (22, 6, 5, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (23, 6, 7, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (24, 6, 8, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (25, 6, 9, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (26, 7, 8, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (27, 7, 9, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (28, 8, 5, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (29, 8, 6, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (30, 8, 9, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (31, 8, 10, '2026-02-03 19:16:31');
INSERT INTO `post_like` VALUES (32, 3, 15, '2026-02-06 14:55:20');
INSERT INTO `post_like` VALUES (33, 10, 15, '2026-02-06 14:55:44');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `category_id` bigint(20) NOT NULL COMMENT '分类ID',
  `price` decimal(10, 2) NOT NULL COMMENT '销售价格',
  `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '原价',
  `main_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg' COMMENT '主图URL',
  `sub_title` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '副标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '商品简介',
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '商品详情（富文本）',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '克' COMMENT '单位',
  `spec` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规格',
  `origin` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '产地',
  `efficacy` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '功效说明',
  `usage_method` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '使用方法',
  `storage_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '储存方法',
  `status` tinyint(4) NULL DEFAULT 0 COMMENT '状态：0-下架，1-上架，2-待审核',
  `sales` int(11) NULL DEFAULT 0 COMMENT '销量',
  `view_count` int(11) NULL DEFAULT 0 COMMENT '浏览量',
  `is_recommend` tinyint(4) NULL DEFAULT 0 COMMENT '是否推荐：0-否，1-是',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_sales`(`sales`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, '精选黄芪片', 6, 68.00, 88.00, '/uploads/2026/02/06/ec3d3d84808a4d67b70e9fb5632250b9.png', '道地药材 补气固表', '精选甘肃岷县黄芪，切片炮制，补气升阳，固表止汗', NULL, '克', '250g/袋', '甘肃岷县', '补气升阳、固表止汗、利水消肿、托毒生肌。', '煎服，9-30g；或入丸、散', '密封，置阴凉干燥处', 1, 1259, 8532, 1, '2026-02-03 19:16:31', '2026-02-06 14:50:01', 0);
INSERT INTO `product` VALUES (2, '长白山人参', 15, 388.00, 468.00, '/uploads/2026/02/06/1c4470c08ef54944a7a129718dd4e9bd.png', '5年生人参 补气养血', '长白山野生人参，5年参龄，大补元气', NULL, '支', '约30g/支', '吉林长白山', '大补元气、复脉固脱、补脾益肺、生津养血', '煎服，3-9g；或研末吞服', '密封冷藏保存', 1, 856, 12350, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (3, '党参切片', 6, 45.00, 58.00, '/uploads/2026/02/06/3451d1aead264324a31fa800faa36159.png', '优质党参 健脾益肺', '甘肃产地直供，补中益气', NULL, '克', '200g/袋', '甘肃', '补中益气、健脾益肺、养血生津', '煎服，9-30g', '置通风干燥处', 1, 2340, 6782, 0, '2026-02-03 19:16:31', '2026-02-06 13:20:44', 0);
INSERT INTO `product` VALUES (4, '西洋参片', 15, 198.00, 238.00, '/uploads/2026/02/06/573d073c29294ff29e0b26a78367bd38.png', '进口西洋参 滋阴补气', '美国威斯康辛州进口，补气养阴', NULL, '克', '100g/罐', '美国', '补气养阴、清热生津', '泡水或含服，3-6g', '密封冷藏', 1, 1520, 9870, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (5, '阿胶块', 16, 268.00, 328.00, '/uploads/2026/02/06/acc95d2d24624b23965cb6cb1fd712a6.png', '东阿阿胶 补血滋阴', '山东东阿正宗阿胶，滋阴补血', NULL, '克', '250g/盒', '山东东阿', '补血滋阴、润燥、止血', '烊化兑服，3-9g', '密封，置阴凉干燥处', 1, 3250, 15680, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (6, '当归片', 7, 35.00, 45.00, '/uploads/2026/02/06/85f59d112e0f4b8e864e5db370d56998.png', '岷县当归 补血活血', '甘肃岷县道地当归，补血调经', NULL, '克', '200g/袋', '甘肃岷县', '补血活血、调经止痛、润肠通便', '煎服，6-12g', '置阴凉干燥处', 1, 4560, 18920, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (7, '熟地黄', 7, 28.00, 38.00, '/uploads/2026/02/06/a42e7f32c2c04837a59928840b22bd24.png', '河南怀地黄 滋阴补血', '四大怀药之一，滋阴补血', NULL, '克', '250g/袋', '河南焦作', '滋阴补血、益精填髓', '煎服，9-15g', '密封防潮', 1, 1890, 7650, 0, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (8, '金银花', 8, 58.00, 72.00, '/uploads/2026/02/06/729afa3ccf6d43e69b00e85806735833.png', '道地金银花 清热解毒', '河南封丘金银花，清热解毒', NULL, '克', '100g/罐', '河南封丘', '清热解毒、疏散风热', '煎服，6-15g；或泡茶', '密封避光保存', 1, 5680, 23450, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (9, '菊花茶', 11, 42.00, 55.00, '/uploads/2026/02/06/0d5d960540db4df8b7166936139cb6b4.png', '杭白菊 清肝明目', '浙江桐乡杭白菊，清肝明目', NULL, '克', '150g/罐', '浙江桐乡', '疏风清热、平肝明目、清热解毒', '泡茶饮用，3-9g', '密封避光保存', 1, 8920, 35681, 1, '2026-02-03 19:16:31', '2026-03-27 21:25:13', 0);
INSERT INTO `product` VALUES (10, '板蓝根颗粒', 8, 18.00, 25.00, '/uploads/2026/02/06/2aa6436b06f74637832d25b1659838aa.png', '清热解毒 凉血利咽', '板蓝根精制颗粒，方便冲服', NULL, '盒', '20袋/盒', '国产', '清热解毒、凉血利咽', '开水冲服，一次1-2袋', '密封干燥保存', 1, 12560, 45891, 1, '2026-02-03 19:16:31', '2026-02-05 20:54:19', 0);
INSERT INTO `product` VALUES (11, '茯苓块', 9, 32.00, 42.00, '/uploads/2026/02/06/b785e9680a86495ab9b2012d160a0974.png', '云南茯苓 健脾祛湿', '云南野生茯苓，利水渗湿', NULL, '克', '250g/袋', '云南', '利水渗湿、健脾宁心', '煎服，9-15g', '通风干燥保存', 1, 3450, 12380, 0, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (12, '薏苡仁', 9, 25.00, 32.00, '/uploads/2026/02/06/733f2b569e814e98987a2d6db4efcf89.png', '贵州薏仁 祛湿健脾', '优质薏苡仁，清热利湿', NULL, '克', '500g/袋', '贵州', '利水渗湿、健脾止泻、除痹排脓', '煎服或煮粥，9-30g', '干燥保存', 1, 6780, 28950, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (13, '陈皮', 9, 48.00, 62.00, '/uploads/2026/02/06/1d973a86b8a04e909cbfa8e0c19c6885.png', '新会陈皮 理气健脾', '广东新会陈皮，年份久，香气浓', NULL, '克', '100g/罐', '广东新会', '理气健脾、燥湿化痰', '煎服，3-10g；或泡茶', '通风阴凉保存', 1, 4560, 19870, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (14, '三七粉', 10, 128.00, 168.00, '/uploads/2026/02/06/8bafe93c7c2e4c4784d4ff0500074d06.png', '文山三七 活血化瘀', '云南文山三七研磨，活血止血', NULL, '克', '100g/罐', '云南文山', '散瘀止血、消肿定痛', '冲服，1-3g，每日2-3次', '密封避光保存', 1, 5890, 32560, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (15, '丹参片', 10, 38.00, 48.00, '/uploads/2026/02/06/2fefd2ce87ff4091a18b7eec15e53995.png', '活血化瘀 养心安神', '优质丹参切片，活血调经', NULL, '克', '200g/袋', '山东', '活血祛瘀、通经止痛、清心除烦', '煎服，9-15g', '干燥保存', 1, 2340, 9870, 0, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (16, '红花', 10, 85.00, 105.00, '/uploads/2026/02/06/d933a0bc8efc4aaebeafcafb84785065.png', '新疆红花 活血通经', '新疆藏红花，品质上乘', NULL, '克', '50g/罐', '新疆', '活血通经、散瘀止痛', '煎服，3-10g', '密封避光', 1, 1890, 8760, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (17, '玫瑰花茶', 11, 36.00, 45.00, '/uploads/2026/02/06/bf63f45d03bd4c0a8079458a5b6e1e6e.png', '平阴玫瑰 疏肝解郁', '山东平阴玫瑰花蕾，美容养颜', NULL, '克', '100g/罐', '山东平阴', '疏肝解郁、活血止痛、美容养颜', '泡茶，5-8朵', '密封避光保存', 1, 9560, 42350, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (18, '枸杞菊花茶', 12, 28.00, 35.00, '/uploads/2026/02/06/101ad704fdab433ab9f87a0db5175584.png', '明目养肝茶包', '枸杞+菊花组合，明目养肝', NULL, '盒', '30包/盒', '国产', '清肝明目、滋阴补肾', '一包泡水，可反复冲泡', '干燥保存', 1, 15680, 68950, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (19, '红枣桂圆茶', 12, 32.00, 42.00, '/uploads/2026/02/06/bb3bc215630e43719198b88d4233869a.png', '补血养颜茶包', '红枣+桂圆，补血安神', NULL, '盒', '30包/盒', '国产', '补血养颜、安神助眠', '一包泡水饮用', '干燥保存', 1, 12350, 52680, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (20, '决明子茶', 13, 22.00, 28.00, '/uploads/2026/02/06/fb5131e8c6a74b8fa9bfd812ac25bcd1.png', '清肝明目 润肠通便', '优质决明子，清肝明目', NULL, '克', '250g/袋', '国产', '清肝明目、润肠通便', '泡茶或煎服，10-15g', '干燥保存', 1, 8970, 38560, 0, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (21, '即食燕窝', 14, 688.00, 888.00, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '印尼金丝燕窝 滋阴润肺', '开盖即食，方便营养', NULL, '瓶', '70g*6瓶', '印尼', '滋阴润肺、补中益气、美容养颜', '早晚空腹食用', '冷藏保存', 1, 2560, 18790, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (22, '鲜炖燕窝', 14, 168.00, 198.00, '/uploads/2026/02/06/c8c1bb6d0ba24bab90af77af58bcefbb.png', '新鲜炖制 营养丰富', '每日鲜炖，冷链配送', NULL, '瓶', '100g/瓶', '印尼', '滋阴润肺、养颜美容', '开盖即食', '冷藏保存，7天内食用', 1, 3890, 21560, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (23, '冬虫夏草', 17, 1888.00, 2388.00, '/uploads/2026/02/06/4091e79094c3436c8a2b9988b7e9d66e.png', '那曲虫草 补肺益肾', '西藏那曲虫草，品质上乘', NULL, '克', '10g/盒', '西藏那曲', '补肺益肾、止血化痰', '研末冲服或炖汤', '密封冷藏', 1, 560, 8950, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (24, '灵芝孢子粉', 3, 298.00, 368.00, '/uploads/2026/02/06/fbac1defdf4e4d779404e772ac6e1f22.png', '破壁灵芝孢子粉', '长白山灵芝，破壁提取', NULL, '克', '60g/盒', '吉林', '增强免疫力、安神益气', '温水冲服，每次2g', '密封避光保存', 1, 1890, 12560, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (25, '艾灸盒套装', 18, 68.00, 88.00, '/uploads/2026/02/06/1e30af74087240c1821ffc1427ce4973.png', '家用艾灸盒 温经散寒', '竹制艾灸盒，配艾柱', NULL, '套', '1套', '国产', '温经散寒、活血通络', '点燃艾柱放入盒中使用', '干燥保存', 1, 4560, 23890, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (26, '砭石刮痧板', 19, 128.00, 168.00, '/uploads/2026/02/06/2d572dca3e3a455da97d5bd91bdea461.png', '泗滨砭石 疏经活络', '山东泗滨砭石，纯天然', NULL, '块', '1块', '山东泗滨', '疏经活络、排毒养颜', '配合刮痧油使用', '避免碰撞', 1, 2890, 15681, 1, '2026-02-03 19:16:31', '2026-02-05 20:37:34', 0);
INSERT INTO `product` VALUES (27, '真空拔罐器', 20, 58.00, 78.00, '/uploads/2026/02/06/38e87e7330034782bc3476dc7ddfea90.png', '家用拔罐套装', '12罐套装，带气枪', NULL, '套', '12罐/套', '国产', '活血化瘀、祛风散寒', '按说明书操作', '清洁干燥保存', 1, 5680, 28950, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (28, '颈椎按摩仪', 21, 298.00, 398.00, '/uploads/2026/02/06/a31896d9da7b445ab4532416fd02422c.png', '智能颈部按摩 缓解疲劳', '电脉冲+热敷，多功能', NULL, '台', '1台', '国产', '缓解颈椎疲劳、促进血液循环', '参照说明书使用', '避免潮湿', 1, 3450, 19870, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (29, '足浴盆', 21, 198.00, 268.00, '/uploads/2026/02/06/25da36bdd7874f5b9a9b93f1b2cc83f8.png', '自动加热泡脚桶', '恒温加热，红光按摩', NULL, '台', '1台', '国产', '促进足部血液循环、助眠养生', '加入热水和药包使用', '使用后擦干', 1, 4890, 26780, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product` VALUES (30, '上等肉桂', 22, 45.00, 58.00, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '温阳散寒 补火助阳', '广西上等肉桂，温补肾阳、散寒止痛', NULL, '克', '200g/袋', '广西', '补火助阳、温经散寒、活血通经', '煎服，2-5g，后下；或研末冲服', '密封干燥保存', 1, 2680, 9850, 1, '2026-03-27 21:38:45', '2026-03-27 21:38:45', 0);
INSERT INTO `product` VALUES (31, '鹿茸片', 22, 588.00, 788.00, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '梅花鹿茸 温补肾阳', '东北梅花鹿茸，补阳益精、强筋健骨', NULL, '克', '30g/盒', '吉林', '补阳益精、强筋健骨、温补肾阳', '研末冲服或泡酒，每次1-2g', '密封冷藏保存', 1, 980, 12560, 1, '2026-03-27 21:38:45', '2026-03-27 21:38:45', 0);
INSERT INTO `product` VALUES (32, '杜仲片', 22, 38.00, 48.00, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '道地杜仲 补肝益肾', '贵州道地杜仲皮，补阳强腰膝', NULL, '克', '250g/袋', '贵州', '补肝肾、强筋骨、温补腰膝', '煎服，6-10g', '置阴凉干燥处', 1, 3450, 11230, 1, '2026-03-27 21:38:45', '2026-03-27 21:38:45', 0);
INSERT INTO `product` VALUES (33, '干姜片', 22, 22.00, 28.00, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '温中散寒 回阳通脉', '精选干姜切片，温中回阳、温补脾胃', NULL, '克', '200g/袋', '云南', '温中散寒、回阳通脉、温补脾阳', '煎服，3-10g', '密封防潮保存', 1, 4560, 15680, 0, '2026-03-27 21:38:45', '2026-03-27 21:38:45', 0);
INSERT INTO `product` VALUES (34, '巴戟天', 22, 68.00, 88.00, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '补阳益肾 强筋壮骨', '广东道地巴戟天，温补肾阳', NULL, '克', '200g/袋', '广东', '补阳益肾、强筋壮骨、祛风除湿', '煎服，3-15g', '干燥避光保存', 1, 1890, 8760, 1, '2026-03-27 21:38:45', '2026-03-27 21:38:45', 0);
INSERT INTO `product` VALUES (35, '制附子', 22, 55.00, 72.00, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '回阳救逆 温补肾阳', '四川制附子，炮制减毒，温阳散寒', NULL, '克', '100g/袋', '四川', '回阳救逆、补火助阳、散寒止痛', '煎服，3-15g，需先煎30分钟', '密封干燥保存', 1, 1560, 7890, 0, '2026-03-27 21:38:45', '2026-03-27 21:38:45', 0);

-- ----------------------------
-- Table structure for product_category
-- ----------------------------
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父分类ID，0表示一级分类',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `icon` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类图标',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序值',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_category
-- ----------------------------
INSERT INTO `product_category` VALUES (1, 0, '中药材', '/uploads/2026/02/06/5ce0ca00e42144af89d566a89558a0a0.png', 1, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (2, 0, '养生茶饮', '/uploads/2026/02/06/12c7b98154464427bc063adf252938ad.png', 2, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (3, 0, '滋补品', '/uploads/2026/02/06/b55efbd00a374ba0a75abfa5299c87a5.png', 3, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (4, 0, '养生器具', '/uploads/2026/02/06/dea3e3ee552045e8b826f6a16ccf6232.png', 4, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (5, 0, '中医书籍', '/uploads/2026/02/06/61d5b5263fc74d369d49001b5e235420.png', 5, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (6, 1, '补气类', '/uploads/2026/02/06/ee45c265cb764a7bbed64dc2a757d0a5.png', 1, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (7, 1, '补血类', '/uploads/2026/02/06/c2eac1fcf11346afad01003cec31aa01.png', 2, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (8, 1, '清热类', '/uploads/2026/02/06/74c9e3b0dc32499abdc79bcd05ab719f.png', 3, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (9, 1, '祛湿类', '/uploads/2026/02/06/8ff9eedfe89a48a3b01f2ecf8f42f3e7.png', 4, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (10, 1, '活血化瘀类', '/uploads/2026/02/06/bfa48d562c734f28ae75584a2c713900.png', 5, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (11, 2, '花草茶', '/uploads/2026/02/06/379b4984bd734bcbb4c6cb7e25ffac5e.png', 1, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (12, 2, '养生茶包', '/uploads/2026/02/06/01a761eb020f4af990401711edb5e736.png', 2, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (13, 2, '代用茶', '/uploads/2026/02/06/0727fe2acba94b26bac2eb686a1a9185.png', 3, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (14, 3, '燕窝', '/uploads/2026/02/06/13a6cbfc73934c7cb1081f474e93db4a.png', 1, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (15, 3, '人参', '/uploads/2026/02/06/75199c2dcf1a4a9d9f523a43bf5194c9.png', 2, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (16, 3, '阿胶', '/uploads/2026/02/06/d0d91aa789de406f974294aeb9384ae6.png', 3, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (17, 3, '冬虫夏草', '/uploads/2026/02/06/a0daba7293bc4d49987fc045b2d3e6a1.png', 4, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (18, 4, '艾灸器具', '/uploads/2026/02/06/183d76f3cdb14b4cabdd0a4c9e6ee6d2.png', 1, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (19, 4, '刮痧工具', '/uploads/2026/02/06/16779e0c73d84f1b88d6c7615c04cffc.png', 2, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (20, 4, '拔罐器具', '/uploads/2026/02/06/e46f4109c73c48688d9e5e9ee7e19561.png', 3, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (21, 4, '按摩器械', '/uploads/2026/02/06/a0edb246723046588746c757f06e03a9.png', 4, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_category` VALUES (22, 1, '温阳类', '/uploads/2026/02/06/ee45c265cb764a7bbed64dc2a757d0a5.png', 6, 1, '2026-03-27 21:38:45', '2026-03-27 21:38:45', 0);

-- ----------------------------
-- Table structure for product_image
-- ----------------------------
DROP TABLE IF EXISTS `product_image`;
CREATE TABLE `product_image`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg' COMMENT '图片URL',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品图片表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_image
-- ----------------------------
INSERT INTO `product_image` VALUES (1, 1, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (2, 1, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (3, 1, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 3, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (4, 2, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (5, 2, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (6, 3, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (7, 3, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (8, 4, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (9, 4, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (10, 4, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 3, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (11, 5, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (12, 5, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (13, 6, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (14, 6, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (15, 7, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (16, 8, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (17, 8, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (18, 9, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (19, 9, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (20, 10, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (21, 11, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (22, 11, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (23, 12, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (24, 13, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (25, 13, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (26, 14, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (27, 14, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (28, 14, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 3, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (29, 15, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (30, 16, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (31, 16, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (32, 17, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (33, 17, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (34, 18, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (35, 19, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (36, 20, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (37, 21, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (38, 21, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (39, 22, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (40, 23, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (41, 23, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (42, 24, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (43, 25, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (44, 25, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (45, 26, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (46, 27, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (47, 27, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (48, 28, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (49, 29, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `product_image` VALUES (50, 29, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');

-- ----------------------------
-- Table structure for product_review
-- ----------------------------
DROP TABLE IF EXISTS `product_review`;
CREATE TABLE `product_review`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `order_item_id` bigint(20) NOT NULL COMMENT '订单明细ID',
  `rating` tinyint(4) NOT NULL DEFAULT 5 COMMENT '评分：1-5星',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '评价内容',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态：0-待审核，1-已通过，2-已驳回',
  `is_anonymous` tinyint(4) NULL DEFAULT 0 COMMENT '是否匿名：0-否，1-是',
  `reply_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '商家回复',
  `reply_time` datetime NULL DEFAULT NULL COMMENT '回复时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品评价表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_review
-- ----------------------------
INSERT INTO `product_review` VALUES (1, 5, 1, 1, 1, 5, '黄芪品质很好，切片均匀，泡水喝味道醇厚，已经回购多次了！', 1, 0, '感谢您的支持，我们会继续保持品质！', '2026-01-22 10:00:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_review` VALUES (2, 5, 6, 1, 2, 5, '当归片很香，煲汤效果很好，会继续购买', 1, 0, NULL, NULL, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_review` VALUES (3, 5, 14, 1, 5, 4, '三七粉细腻，就是感觉量有点少', 1, 0, '感谢反馈，我们的三七粉都是足量的哦~', '2026-01-23 09:00:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_review` VALUES (4, 6, 21, 2, 6, 5, '燕窝口感很好，包装精美，送人很有面子！强烈推荐！', 1, 0, '感谢您的好评，欢迎再次光临！', '2026-01-28 11:00:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_review` VALUES (5, 5, 23, 5, 9, 5, '虫草品质很好，个头均匀饱满，非常满意！', 1, 0, '感谢您对我们产品的认可！', '2026-02-01 14:00:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_review` VALUES (6, 7, 9, 0, 0, 4, '菊花很香，泡出来颜色金黄，清热效果不错', 1, 0, NULL, NULL, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_review` VALUES (7, 8, 17, 0, 0, 5, '玫瑰花茶香气浓郁，泡完花朵完整，很喜欢！', 1, 0, NULL, NULL, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_review` VALUES (8, 9, 25, 0, 0, 5, '艾灸盒做工精细，配的艾柱也很好用', 1, 1, NULL, NULL, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_review` VALUES (9, 10, 18, 0, 0, 4, '茶包方便，口感还行，就是感觉枸杞放少了', 1, 0, '感谢反馈，我们会考虑优化配方~', '2026-01-30 16:00:00', '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_review` VALUES (10, 11, 26, 0, 0, 5, '砭石刮痧板温润细腻，刮起来很舒服，已推荐给朋友', 1, 0, NULL, NULL, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `product_review` VALUES (11, 6, 1, 0, 0, 5, '黄芪片质量非常好，颜色金黄，泡水喝味道醇厚，补气效果明显，已经是第三次购买了！', 1, 0, '感谢您的支持与信任，我们会继续保持品质！', '2026-01-25 10:00:00', '2026-01-24 14:30:00', '2026-01-24 14:30:00', 0);
INSERT INTO `product_review` VALUES (12, 7, 1, 0, 0, 4, '药材品质不错，就是包装可以再精致一些，整体满意', 1, 0, NULL, NULL, '2026-01-26 09:15:00', '2026-01-26 09:15:00', 0);
INSERT INTO `product_review` VALUES (13, 8, 1, 0, 0, 5, '朋友推荐来买的，果然没让我失望，黄芪片切得很均匀，泡茶喝很方便', 1, 0, NULL, NULL, '2026-01-28 16:20:00', '2026-01-28 16:20:00', 0);
INSERT INTO `product_review` VALUES (14, 9, 1, 0, 0, 5, '一直在这家买黄芪，品质稳定，价格实惠，物流也快', 1, 1, NULL, NULL, '2026-01-30 11:45:00', '2026-01-30 11:45:00', 0);
INSERT INTO `product_review` VALUES (15, 5, 2, 0, 0, 5, '长白山人参，品质上乘，参须完整，炖汤效果很好，全家都说好！', 1, 0, '感谢您的好评，祝您身体健康！', '2026-01-22 09:00:00', '2026-01-21 15:30:00', '2026-01-21 15:30:00', 0);
INSERT INTO `product_review` VALUES (16, 6, 2, 0, 0, 5, '送给父母的，他们很喜欢，说是正宗的长白山人参味道', 1, 0, NULL, NULL, '2026-01-25 10:20:00', '2026-01-25 10:20:00', 0);
INSERT INTO `product_review` VALUES (17, 10, 2, 0, 0, 4, '人参品质很好，就是价格稍贵，不过物有所值', 1, 0, NULL, NULL, '2026-01-28 14:00:00', '2026-01-28 14:00:00', 0);
INSERT INTO `product_review` VALUES (18, 5, 6, 0, 0, 5, '当归片香气浓郁，煲汤补血效果很好，女性朋友必备！', 1, 0, '感谢您的认可，当归确实是补血佳品！', '2026-01-20 11:00:00', '2026-01-19 16:45:00', '2026-01-19 16:45:00', 0);
INSERT INTO `product_review` VALUES (19, 8, 6, 0, 0, 5, '买来炖乌鸡汤，味道很正，家人都说好喝', 1, 0, NULL, NULL, '2026-01-23 09:30:00', '2026-01-23 09:30:00', 0);
INSERT INTO `product_review` VALUES (20, 11, 6, 0, 0, 4, '当归品质不错，切片均匀，下次还会回购', 1, 1, NULL, NULL, '2026-01-27 15:15:00', '2026-01-27 15:15:00', 0);
INSERT INTO `product_review` VALUES (21, 5, 9, 0, 0, 5, '杭白菊品质很好，花朵完整，泡出来颜色金黄，清香扑鼻', 1, 0, NULL, NULL, '2026-01-18 10:30:00', '2026-01-18 10:30:00', 0);
INSERT INTO `product_review` VALUES (22, 6, 9, 0, 0, 5, '办公室必备，清热明目效果很好，同事们都来问我在哪买的', 1, 0, '感谢推荐，欢迎再次光临！', '2026-01-22 14:20:00', '2026-01-20 11:00:00', '2026-01-20 11:00:00', 0);
INSERT INTO `product_review` VALUES (23, 12, 9, 0, 0, 4, '菊花茶很香，就是感觉量少了点，希望能多送一些', 1, 0, NULL, NULL, '2026-01-25 16:40:00', '2026-01-25 16:40:00', 0);
INSERT INTO `product_review` VALUES (24, 5, 18, 0, 0, 5, '茶包很方便，枸杞和菊花搭配得很好，明目养肝', 1, 0, NULL, NULL, '2026-01-15 09:00:00', '2026-01-15 09:00:00', 0);
INSERT INTO `product_review` VALUES (25, 7, 18, 0, 0, 5, '每天泡一包，眼睛舒服多了，推荐给经常用电脑的朋友', 1, 0, '感谢您的分享！', '2026-01-20 10:30:00', '2026-01-18 14:15:00', '2026-01-18 14:15:00', 0);
INSERT INTO `product_review` VALUES (26, 13, 18, 0, 0, 4, '口感不错，就是枸杞感觉放得少了一点', 1, 0, '感谢反馈，我们会考虑优化配方~', '2026-01-28 09:00:00', '2026-01-26 11:30:00', '2026-01-26 11:30:00', 0);
INSERT INTO `product_review` VALUES (27, 6, 17, 0, 0, 5, '玫瑰花茶香气浓郁，泡完花朵还是很完整，美容养颜必备', 1, 0, NULL, NULL, '2026-01-16 15:20:00', '2026-01-16 15:20:00', 0);
INSERT INTO `product_review` VALUES (28, 9, 17, 0, 0, 5, '送给女朋友的，她很喜欢，说泡出来颜色很漂亮', 1, 0, '感谢您的支持！', '2026-01-22 11:00:00', '2026-01-20 16:45:00', '2026-01-20 16:45:00', 0);
INSERT INTO `product_review` VALUES (29, 14, 17, 0, 0, 5, '玫瑰花茶品质很好，香味持久，会继续回购', 1, 1, NULL, NULL, '2026-01-28 10:15:00', '2026-01-28 10:15:00', 0);
INSERT INTO `product_review` VALUES (30, 6, 5, 0, 0, 5, '东阿阿胶，品质有保证，打粉后泡牛奶喝，补血效果很好', 1, 0, '感谢您的好评！', '2026-01-25 09:30:00', '2026-01-23 14:00:00', '2026-01-23 14:00:00', 0);
INSERT INTO `product_review` VALUES (31, 10, 5, 0, 0, 5, '正品阿胶，包装精美，送人很有面子', 1, 0, NULL, NULL, '2026-01-27 16:20:00', '2026-01-27 16:20:00', 0);
INSERT INTO `product_review` VALUES (32, 7, 14, 0, 0, 5, '三七粉很细腻，冲水喝方便，活血化瘀效果不错', 1, 0, NULL, NULL, '2026-01-19 11:30:00', '2026-01-19 11:30:00', 0);
INSERT INTO `product_review` VALUES (33, 11, 14, 0, 0, 4, '三七粉品质可以，就是味道有点苦，不过效果是真的好', 1, 0, '三七粉确实有些苦味，可以加蜂蜜调味哦~', '2026-01-26 10:00:00', '2026-01-24 15:45:00', '2026-01-24 15:45:00', 0);
INSERT INTO `product_review` VALUES (34, 5, 25, 0, 0, 5, '艾灸盒做工精细，配的艾柱也很好用，在家就能做艾灸', 1, 0, NULL, NULL, '2026-01-17 14:00:00', '2026-01-17 14:00:00', 0);
INSERT INTO `product_review` VALUES (35, 12, 25, 0, 0, 5, '买来给妈妈用的，她说很方便，温度也合适', 1, 0, '感谢您的支持！', '2026-01-25 11:30:00', '2026-01-23 09:15:00', '2026-01-23 09:15:00', 0);
INSERT INTO `product_review` VALUES (36, 6, 26, 0, 0, 5, '砭石刮痧板手感很好，温润细腻，刮起来很舒服', 1, 0, NULL, NULL, '2026-01-20 10:00:00', '2026-01-20 10:00:00', 0);
INSERT INTO `product_review` VALUES (37, 13, 26, 0, 0, 5, '正宗泗滨砭石，品质很好，已经推荐给朋友了', 1, 0, '感谢您的推荐！', '2026-01-28 15:00:00', '2026-01-26 14:30:00', '2026-01-26 14:30:00', 0);
INSERT INTO `product_review` VALUES (38, 5, 10, 0, 0, 5, '家中常备，感冒初期喝效果很好，方便冲服', 1, 0, NULL, NULL, '2026-01-14 09:30:00', '2026-01-14 09:30:00', 0);
INSERT INTO `product_review` VALUES (39, 8, 10, 0, 0, 4, '板蓝根颗粒效果不错，就是甜度可以再低一些', 1, 0, NULL, NULL, '2026-01-22 16:00:00', '2026-01-22 16:00:00', 0);
INSERT INTO `product_review` VALUES (40, 7, 8, 0, 0, 5, '金银花品质很好，清热解毒效果明显，夏天必备', 1, 0, NULL, NULL, '2026-01-18 11:15:00', '2026-01-18 11:15:00', 0);
INSERT INTO `product_review` VALUES (41, 10, 8, 0, 0, 5, '泡水喝很清香，嗓子不舒服的时候喝特别管用', 1, 0, '感谢您的好评！', '2026-01-26 10:30:00', '2026-01-24 15:00:00', '2026-01-24 15:00:00', 0);
INSERT INTO `product_review` VALUES (42, 15, 1, 11, 21, 5, 'nice', 1, 0, NULL, NULL, '2026-02-06 13:25:29', '2026-02-06 13:25:29', 0);

-- ----------------------------
-- Table structure for product_stock
-- ----------------------------
DROP TABLE IF EXISTS `product_stock`;
CREATE TABLE `product_stock`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库存ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `stock` int(11) NOT NULL DEFAULT 0 COMMENT '库存数量',
  `warning_stock` int(11) NULL DEFAULT 10 COMMENT '预警库存',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `product_id`(`product_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品库存表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_stock
-- ----------------------------
INSERT INTO `product_stock` VALUES (1, 1, 500, 50, '2026-02-03 19:16:31', '2026-02-06 14:49:44');
INSERT INTO `product_stock` VALUES (2, 2, 100, 10, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (3, 3, 800, 80, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (4, 4, 200, 20, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (5, 5, 300, 30, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (6, 6, 1000, 100, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (7, 7, 600, 60, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (8, 8, 400, 40, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (9, 9, 800, 80, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (10, 10, 2000, 200, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (11, 11, 500, 50, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (12, 12, 1500, 150, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (13, 13, 300, 30, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (14, 14, 200, 20, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (15, 15, 400, 40, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (16, 16, 150, 15, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (17, 17, 600, 60, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (18, 18, 1200, 120, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (19, 19, 1000, 100, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (20, 20, 800, 80, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (21, 21, 50, 5, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (22, 22, 80, 8, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (23, 23, 30, 3, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (24, 24, 100, 10, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (25, 25, 300, 30, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (26, 26, 200, 20, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (27, 27, 400, 40, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (28, 28, 150, 15, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `product_stock` VALUES (29, 29, 250, 25, '2026-02-03 19:16:31', '2026-02-03 19:16:31');

-- ----------------------------
-- Table structure for product_stock_log
-- ----------------------------
DROP TABLE IF EXISTS `product_stock_log`;
CREATE TABLE `product_stock_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `change_type` tinyint(4) NOT NULL COMMENT '变更类型：1-入库，2-出库，3-盘点调整',
  `change_quantity` int(11) NOT NULL COMMENT '变更数量',
  `before_stock` int(11) NOT NULL COMMENT '变更前库存',
  `after_stock` int(11) NOT NULL COMMENT '变更后库存',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '关联订单号',
  `operator_id` bigint(20) NULL DEFAULT NULL COMMENT '操作人ID',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '库存变更日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_stock_log
-- ----------------------------
INSERT INTO `product_stock_log` VALUES (1, 1, 1, 100, 400, 500, NULL, 1, '初始入库', '2026-02-03 19:16:32');
INSERT INTO `product_stock_log` VALUES (2, 1, 2, -2, 500, 498, 'ORD202601150001', NULL, '订单出库', '2026-02-03 19:16:32');
INSERT INTO `product_stock_log` VALUES (3, 6, 2, -2, 1002, 1000, 'ORD202601150001', NULL, '订单出库', '2026-02-03 19:16:32');
INSERT INTO `product_stock_log` VALUES (4, 21, 2, -1, 51, 50, 'ORD202601180002', NULL, '订单出库', '2026-02-03 19:16:32');
INSERT INTO `product_stock_log` VALUES (5, 5, 2, -1, 301, 300, 'ORD202601200003', NULL, '订单出库', '2026-02-03 19:16:32');
INSERT INTO `product_stock_log` VALUES (6, 23, 2, -1, 31, 30, 'ORD202601250005', NULL, '订单出库', '2026-02-03 19:16:32');
INSERT INTO `product_stock_log` VALUES (7, 14, 3, 50, 150, 200, NULL, 4, '盘点调整-补货', '2026-02-03 19:16:32');
INSERT INTO `product_stock_log` VALUES (8, 1, 3, 3, 497, 500, NULL, 2, '', '2026-02-06 14:49:44');

-- ----------------------------
-- Table structure for review_image
-- ----------------------------
DROP TABLE IF EXISTS `review_image`;
CREATE TABLE `review_image`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `review_id` bigint(20) NOT NULL COMMENT '评价ID',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg' COMMENT '图片URL',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_review_id`(`review_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '评价图片表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of review_image
-- ----------------------------
INSERT INTO `review_image` VALUES (1, 1, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `review_image` VALUES (2, 1, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `review_image` VALUES (3, 4, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `review_image` VALUES (4, 4, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `review_image` VALUES (5, 4, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 3, '2026-02-03 19:16:31');
INSERT INTO `review_image` VALUES (6, 5, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `review_image` VALUES (7, 7, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `review_image` VALUES (8, 8, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `review_image` VALUES (9, 10, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-02-03 19:16:31');
INSERT INTO `review_image` VALUES (10, 10, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-02-03 19:16:31');
INSERT INTO `review_image` VALUES (11, 11, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-01-24 14:30:00');
INSERT INTO `review_image` VALUES (12, 11, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-01-24 14:30:00');
INSERT INTO `review_image` VALUES (13, 14, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-01-21 15:30:00');
INSERT INTO `review_image` VALUES (14, 17, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-01-19 16:45:00');
INSERT INTO `review_image` VALUES (15, 20, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-01-18 10:30:00');
INSERT INTO `review_image` VALUES (16, 25, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-01-16 15:20:00');
INSERT INTO `review_image` VALUES (17, 25, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 2, '2026-01-16 15:20:00');
INSERT INTO `review_image` VALUES (18, 28, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-01-23 14:00:00');
INSERT INTO `review_image` VALUES (19, 32, 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', 1, '2026-01-17 14:00:00');

-- ----------------------------
-- Table structure for shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `quantity` int(11) NOT NULL DEFAULT 1 COMMENT '数量',
  `selected` tinyint(4) NULL DEFAULT 1 COMMENT '是否选中：0-否，1-是',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_product`(`user_id`, `product_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '购物车表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of shopping_cart
-- ----------------------------
INSERT INTO `shopping_cart` VALUES (1, 5, 2, 1, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `shopping_cart` VALUES (2, 5, 24, 2, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `shopping_cart` VALUES (3, 6, 17, 3, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `shopping_cart` VALUES (4, 6, 5, 1, 0, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `shopping_cart` VALUES (5, 7, 1, 2, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `shopping_cart` VALUES (6, 7, 14, 1, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `shopping_cart` VALUES (7, 8, 21, 1, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `shopping_cart` VALUES (8, 9, 25, 1, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `shopping_cart` VALUES (9, 9, 27, 2, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `shopping_cart` VALUES (10, 10, 19, 2, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `shopping_cart` VALUES (11, 11, 4, 1, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `shopping_cart` VALUES (12, 12, 8, 3, 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置键',
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '配置值',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '配置名称',
  `config_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '配置类型',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `config_key`(`config_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, 'order_timeout_minutes', '30', '订单超时时间（分钟）', 'order', '未支付订单自动取消时间', '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `sys_config` VALUES (2, 'order_auto_complete_days', '7', '订单自动完成天数', 'order', '发货后自动确认收货天数', '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `sys_config` VALUES (3, 'stock_warning_threshold', '10', '库存预警阈值', 'stock', '库存低于此值时发出预警', '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `sys_config` VALUES (4, 'ai_context_max_rounds', '10', 'AI对话上下文轮数', 'ai', 'AI客服保留的最大对话轮数', '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `sys_config` VALUES (5, 'ai_context_expire_hours', '24', 'AI对话上下文过期时间（小时）', 'ai', '对话上下文保留时间', '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `sys_config` VALUES (6, 'site_name', '中医养生平台', '网站名称', 'site', '平台显示名称', '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `sys_config` VALUES (7, 'site_logo', '/uploads/2026/02/06/8b59e25c3d63487e897a463c6499144f.png', '网站Logo', 'site', '平台Logo图片', '2026-02-03 19:16:31', '2026-02-06 15:02:32');
INSERT INTO `sys_config` VALUES (8, 'contact_phone', '400-888-999', '客服电话', 'site', '平台客服联系电话', '2026-02-03 19:16:31', '2026-02-06 15:03:45');
INSERT INTO `sys_config` VALUES (9, 'contact_email', 'service@tcm.com', '客服邮箱', 'site', '平台客服邮箱', '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `sys_config` VALUES (10, 'free_shipping_amount', '99', '包邮金额', 'order', '订单满此金额包邮', '2026-02-03 19:16:31', '2026-02-03 19:16:31');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色编码',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色描述',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_code`(`role_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '普通用户', 'USER', '普通注册用户，可购物、浏览养生内容', 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `sys_role` VALUES (2, '员工', 'STAFF', '商城运营人员，负责商品、订单、库存管理', 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `sys_role` VALUES (3, '管理员', 'ADMIN', '超级管理员，拥有全部权限', 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（BCrypt加密）',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg' COMMENT '头像URL',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `gender` tinyint(4) NULL DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `user_type` tinyint(4) NOT NULL DEFAULT 1 COMMENT '用户类型：1-普通用户，2-员工，3-管理员',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  INDEX `idx_username`(`username`) USING BTREE,
  INDEX `idx_phone`(`phone`) USING BTREE,
  INDEX `idx_user_type`(`user_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$tytxkhlLaDatnQL5tYGEaeUrwFIr8IgFleKBm8iOVS1G6k080zbwq', '超级管理员', '/uploads/2026/02/06/95304f2a9ece45eb9c5a29d35b5a9dc2.jpg', '13800000002', 'admin@tcm.com', 1, NULL, 1, 3, '2026-02-06 14:48:59', '2026-02-03 19:16:31', '2026-02-03 20:38:17', 0);
INSERT INTO `sys_user` VALUES (2, 'zhx', '$2a$10$1N85TZtecU0tXfJM7MgpBOkpXwHA0lYZSVacwEDIJFKrhz7Hm/HJa', '运营小王', '/uploads/2026/02/06/005c443f43d148e6bdbec3354cb2e08f.jpg', '13800000003', 'staff001@tcm.com', 1, '2026-02-21', 1, 2, '2026-02-05 14:54:19', '2026-02-03 19:16:31', '2026-02-05 14:28:46', 0);
INSERT INTO `sys_user` VALUES (3, 'staff002', '$2a$10$tYkV27bynra8FsFoBQrfbO7q0eGk1pek8qgtP8UqRwzHNm3i5Ewmm', '运营小李', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '13800000003', 'staff002@tcm.com', 2, NULL, 1, 2, NULL, '2026-02-03 19:16:31', '2026-02-03 20:38:17', 0);
INSERT INTO `sys_user` VALUES (4, 'staff003', '$2a$10$tYkV27bynra8FsFoBQrfbO7q0eGk1pek8qgtP8UqRwzHNm3i5Ewmm', '库存管理员', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '13800000004', 'staff003@tcm.com', 1, NULL, 1, 2, NULL, '2026-02-03 19:16:31', '2026-02-03 20:38:17', 0);
INSERT INTO `sys_user` VALUES (5, 'user001', '$2a$10$tYkV27bynra8FsFoBQrfbO7q0eGk1pek8qgtP8UqRwzHNm3i5Ewmm', '养生达人小明', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '13900000001', 'user001@qq.com', 1, NULL, 1, 1, NULL, '2026-02-03 19:16:31', '2026-02-03 20:38:17', 0);
INSERT INTO `sys_user` VALUES (6, 'user002', '$2a$10$tYkV27bynra8FsFoBQrfbO7q0eGk1pek8qgtP8UqRwzHNm3i5Ewmm', '健康爱好者小红', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '13900000002', 'user002@qq.com', 2, NULL, 1, 1, '2026-03-27 21:40:17', '2026-02-03 19:16:31', '2026-02-03 20:38:17', 0);
INSERT INTO `sys_user` VALUES (7, 'user003', '$2a$10$tYkV27bynra8FsFoBQrfbO7q0eGk1pek8qgtP8UqRwzHNm3i5Ewmm', '中医粉丝老张', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '13900000003', 'user003@qq.com', 1, NULL, 1, 1, '2026-03-27 21:46:19', '2026-02-03 19:16:31', '2026-02-03 20:38:17', 0);
INSERT INTO `sys_user` VALUES (8, 'user004', '$2a$10$tYkV27bynra8FsFoBQrfbO7q0eGk1pek8qgtP8UqRwzHNm3i5Ewmm', '药膳爱好者小芳', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '13900000004', 'user004@qq.com', 2, NULL, 1, 1, NULL, '2026-02-03 19:16:31', '2026-02-03 20:38:17', 0);
INSERT INTO `sys_user` VALUES (9, 'user005', '$2a$10$tYkV27bynra8FsFoBQrfbO7q0eGk1pek8qgtP8UqRwzHNm3i5Ewmm', '太极拳老李', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '13900000005', 'user005@qq.com', 1, NULL, 1, 1, NULL, '2026-02-03 19:16:31', '2026-02-03 20:38:17', 0);
INSERT INTO `sys_user` VALUES (10, 'user006', '$2a$10$tYkV27bynra8FsFoBQrfbO7q0eGk1pek8qgtP8UqRwzHNm3i5Ewmm', '瑜伽爱好者小美', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '13900000006', 'user006@qq.com', 2, NULL, 1, 1, NULL, '2026-02-03 19:16:31', '2026-02-03 20:38:17', 0);
INSERT INTO `sys_user` VALUES (11, 'user007', '$2a$10$tYkV27bynra8FsFoBQrfbO7q0eGk1pek8qgtP8UqRwzHNm3i5Ewmm', '茶道达人老王', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '13900000007', 'user007@qq.com', 1, NULL, 1, 1, NULL, '2026-02-03 19:16:31', '2026-02-03 20:38:17', 0);
INSERT INTO `sys_user` VALUES (12, 'user008', '$2a$10$tYkV27bynra8FsFoBQrfbO7q0eGk1pek8qgtP8UqRwzHNm3i5Ewmm', '食疗专家小陈', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '13900000008', 'user008@qq.com', 2, NULL, 1, 1, NULL, '2026-02-03 19:16:31', '2026-02-03 20:38:17', 0);
INSERT INTO `sys_user` VALUES (13, 'user009', '$2a$10$tYkV27bynra8FsFoBQrfbO7q0eGk1pek8qgtP8UqRwzHNm3i5Ewmm', '养生新手小刘', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '13900000009', 'user009@qq.com', 1, NULL, 1, 1, NULL, '2026-02-03 19:16:31', '2026-02-03 20:38:17', 0);
INSERT INTO `sys_user` VALUES (14, 'user010', '$2a$10$tYkV27bynra8FsFoBQrfbO7q0eGk1pek8qgtP8UqRwzHNm3i5Ewmm', '保健达人小周', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '13900000010', 'user010@qq.com', 2, NULL, 1, 1, NULL, '2026-02-03 19:16:31', '2026-02-03 20:38:17', 0);
INSERT INTO `sys_user` VALUES (15, 'yyf', '$2a$10$tYkV27bynra8FsFoBQrfbO7q0eGk1pek8qgtP8UqRwzHNm3i5Ewmm', '顺恨之爱上情敌', '/uploads/2026/02/06/dd8da20e31914be7a4c3904a82cd388f.jpg', '16666666666', '123@qq.com', 2, '2026-02-03', 1, 1, '2026-03-27 21:46:45', '2026-02-03 20:37:23', '2026-02-03 20:38:17', 0);
INSERT INTO `sys_user` VALUES (16, 'staf005', '$2a$10$bN.xMKURJtnNns9CGKcI5.InqQqNDoKpY3IuD7rzs2QHfli/sk1.S', 'rewind', 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', '15555555555', '123@qq.com', 0, NULL, 0, 2, NULL, '2026-02-03 20:58:36', '2026-02-03 20:58:36', 0);
INSERT INTO `sys_user` VALUES (17, 'admin1', '$2a$10$.C9GmFizhbR7yLM36ew0peylIM06nRayXqNe.cjK/qmvdN22cMpyy', 'admin1', '/uploads/2026/02/06/0d500529a09e4fe2bd4178217c97a56f.jpg', '19999999999', '321@qq.com', 1, '2026-02-24', 1, 3, '2026-02-06 15:19:11', '2026-02-06 15:18:57', '2026-02-06 15:18:57', 0);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_role`(`user_id`, `role_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 3, '2026-02-03 19:16:31');
INSERT INTO `sys_user_role` VALUES (2, 2, 2, '2026-02-03 19:16:31');
INSERT INTO `sys_user_role` VALUES (3, 3, 2, '2026-02-03 19:16:31');
INSERT INTO `sys_user_role` VALUES (4, 4, 2, '2026-02-03 19:16:31');
INSERT INTO `sys_user_role` VALUES (5, 5, 1, '2026-02-03 19:16:31');
INSERT INTO `sys_user_role` VALUES (6, 6, 1, '2026-02-03 19:16:31');
INSERT INTO `sys_user_role` VALUES (7, 7, 1, '2026-02-03 19:16:31');
INSERT INTO `sys_user_role` VALUES (8, 8, 1, '2026-02-03 19:16:31');
INSERT INTO `sys_user_role` VALUES (9, 9, 1, '2026-02-03 19:16:31');
INSERT INTO `sys_user_role` VALUES (10, 10, 1, '2026-02-03 19:16:31');
INSERT INTO `sys_user_role` VALUES (11, 11, 1, '2026-02-03 19:16:31');
INSERT INTO `sys_user_role` VALUES (12, 12, 1, '2026-02-03 19:16:31');
INSERT INTO `sys_user_role` VALUES (13, 13, 1, '2026-02-03 19:16:31');
INSERT INTO `sys_user_role` VALUES (14, 14, 1, '2026-02-03 19:16:31');

-- ----------------------------
-- Table structure for user_address
-- ----------------------------
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `receiver_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货人电话',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '省份',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '城市',
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '区/县',
  `detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '详细地址',
  `is_default` tinyint(4) NULL DEFAULT 0 COMMENT '是否默认：0-否，1-是',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户收货地址表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_address
-- ----------------------------
INSERT INTO `user_address` VALUES (1, 5, '张明', '13900000001', '北京市', '北京市', '朝阳区', '建国路88号中医养生大厦1001室', 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `user_address` VALUES (2, 5, '张明', '13900000001', '北京市', '北京市', '海淀区', '中关村南大街5号', 0, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `user_address` VALUES (3, 6, '李红', '13900000002', '上海市', '上海市', '浦东新区', '陆家嘴环路1000号金融中心', 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `user_address` VALUES (4, 7, '张伟', '13900000003', '广东省', '广州市', '天河区', '天河路385号太古汇', 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `user_address` VALUES (5, 8, '王芳', '13900000004', '江苏省', '南京市', '鼓楼区', '中山北路101号', 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `user_address` VALUES (6, 9, '李强', '13900000005', '浙江省', '杭州市', '西湖区', '文三路269号', 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `user_address` VALUES (7, 10, '陈美', '13900000006', '四川省', '成都市', '武侯区', '人民南路四段88号', 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `user_address` VALUES (8, 11, '王建国', '13900000007', '湖北省', '武汉市', '武昌区', '中南路99号', 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `user_address` VALUES (9, 12, '陈晓', '13900000008', '福建省', '厦门市', '思明区', '厦禾路888号', 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `user_address` VALUES (10, 13, '刘洋', '13900000009', '山东省', '青岛市', '市南区', '香港中路67号', 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `user_address` VALUES (11, 14, '周婷', '13900000010', '辽宁省', '大连市', '中山区', '人民路50号', 1, '2026-02-03 19:16:31', '2026-02-03 19:16:31', 0);
INSERT INTO `user_address` VALUES (12, 15, '洋芋饭', '18888888888', '湖北', '武汉', '洪山区', '狮子路106号', 1, '2026-02-06 13:21:47', '2026-02-06 13:21:47', 0);

-- ----------------------------
-- Table structure for user_collection
-- ----------------------------
DROP TABLE IF EXISTS `user_collection`;
CREATE TABLE `user_collection`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `target_id` bigint(20) NOT NULL COMMENT '收藏目标ID',
  `target_type` tinyint(4) NOT NULL COMMENT '收藏类型：1-商品，2-养生文章，3-养生方案',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_target`(`user_id`, `target_id`, `target_type`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_target`(`target_id`, `target_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_collection
-- ----------------------------
INSERT INTO `user_collection` VALUES (1, 5, 2, 1, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (2, 5, 21, 1, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (3, 5, 23, 1, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (4, 6, 17, 1, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (5, 6, 5, 1, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (6, 6, 22, 1, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (7, 7, 14, 1, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (8, 7, 1, 1, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (9, 8, 21, 1, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (10, 8, 9, 1, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (11, 9, 25, 1, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (12, 9, 29, 1, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (13, 10, 17, 1, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (14, 10, 19, 1, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (15, 5, 1, 2, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (16, 5, 3, 2, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (17, 5, 5, 2, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (18, 6, 2, 2, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (19, 6, 6, 2, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (20, 7, 4, 2, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (21, 7, 8, 2, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (22, 8, 3, 2, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (23, 8, 5, 2, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (24, 5, 1, 3, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (25, 5, 4, 3, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (26, 6, 2, 3, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (27, 6, 5, 3, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (28, 7, 3, 3, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (29, 8, 5, 3, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (30, 8, 6, 3, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (31, 9, 6, 3, '2026-02-03 19:16:31');
INSERT INTO `user_collection` VALUES (32, 15, 1, 1, '2026-02-05 20:35:44');
INSERT INTO `user_collection` VALUES (33, 15, 8, 2, '2026-02-05 20:37:10');
INSERT INTO `user_collection` VALUES (34, 15, 7, 2, '2026-02-06 13:27:26');
INSERT INTO `user_collection` VALUES (35, 15, 2, 1, '2026-03-02 14:00:00');
INSERT INTO `user_collection` VALUES (36, 15, 5, 1, '2026-03-03 09:00:00');
INSERT INTO `user_collection` VALUES (37, 15, 21, 1, '2026-03-05 16:00:00');
INSERT INTO `user_collection` VALUES (38, 15, 1, 2, '2026-03-06 11:00:00');
INSERT INTO `user_collection` VALUES (39, 15, 3, 2, '2026-03-07 15:00:00');
INSERT INTO `user_collection` VALUES (40, 15, 1, 3, '2026-03-08 10:00:00');
INSERT INTO `user_collection` VALUES (41, 15, 5, 3, '2026-03-10 14:00:00');
INSERT INTO `user_collection` VALUES (43, 5, 1, 1, '2026-03-01 08:00:00');
INSERT INTO `user_collection` VALUES (44, 5, 5, 1, '2026-03-02 09:00:00');
INSERT INTO `user_collection` VALUES (45, 5, 14, 1, '2026-03-03 10:00:00');
INSERT INTO `user_collection` VALUES (46, 5, 5, 3, '2026-03-05 16:00:00');
INSERT INTO `user_collection` VALUES (48, 6, 2, 1, '2026-03-01 10:00:00');
INSERT INTO `user_collection` VALUES (49, 6, 14, 1, '2026-03-02 11:00:00');

-- ----------------------------
-- Table structure for user_health_record
-- ----------------------------
DROP TABLE IF EXISTS `user_health_record`;
CREATE TABLE `user_health_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '档案ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `age` int(11) NULL DEFAULT NULL COMMENT '年龄',
  `height` decimal(5, 2) NULL DEFAULT NULL COMMENT '身高(cm)',
  `weight` decimal(5, 2) NULL DEFAULT NULL COMMENT '体重(kg)',
  `blood_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '血型',
  `constitution_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '体质类型（如：平和质、气虚质、阳虚质等）',
  `medical_history` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '病史',
  `allergy_history` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '过敏史',
  `family_history` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '家族病史',
  `lifestyle` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '生活习惯描述',
  `diet_preference` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '饮食偏好',
  `sleep_quality` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '睡眠质量',
  `exercise_frequency` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '运动频率',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户健康档案表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_health_record
-- ----------------------------
INSERT INTO `user_health_record` VALUES (1, 5, '张明', 35, 175.00, 70.00, 'A型', '气虚质', '无重大疾病史', '对青霉素过敏', NULL, '作息规律，偶尔加班', '清淡为主，喜欢蔬菜', '良好，每天7小时', '每周3次', NULL, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `user_health_record` VALUES (2, 6, '李红', 28, 162.00, 52.00, 'B型', '阳虚质', '慢性胃炎', '无', NULL, '工作压力大，经常熬夜', '喜甜食，偏好面食', '一般，经常失眠', '很少运动', NULL, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `user_health_record` VALUES (3, 7, '张伟', 45, 170.00, 75.00, 'O型', '痰湿质', '高血压病史3年', '海鲜过敏', NULL, '喜静不喜动', '口味偏重，喜肉食', '较差，打鼾', '几乎不运动', NULL, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `user_health_record` VALUES (4, 8, '王芳', 32, 165.00, 55.00, 'AB型', '平和质', '无', '花粉过敏', NULL, '生活规律，注重养生', '均衡饮食', '很好', '每天跑步', NULL, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `user_health_record` VALUES (5, 9, '李强', 50, 168.00, 68.00, 'A型', '血瘀质', '糖尿病前期', '无', NULL, '退休生活，早睡早起', '控制糖分摄入', '良好', '每天太极拳', NULL, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `user_health_record` VALUES (6, 10, '陈美', 26, 160.00, 48.00, 'B型', '气郁质', '无', '芒果过敏', NULL, '工作繁忙，情绪波动', '不规律，经常不吃早餐', '一般', '每周瑜伽2次', NULL, '2026-02-03 19:16:31', '2026-02-03 19:16:31');
INSERT INTO `user_health_record` VALUES (7, 11, '茶道王', 42, 172.00, 65.00, 'O型', '阴虚质', '无', '无', NULL, '喜欢饮茶，作息规律', '喜清淡，爱喝汤', '一般，易醒', '每周散步3次', '爱好品茶养生', '2026-03-27 21:23:26', '2026-03-27 21:23:26');
INSERT INTO `user_health_record` VALUES (8, 12, '食疗陈', 30, 158.00, 50.00, 'A型', '湿热质', '偏头痛', '无', NULL, '久坐办公室', '喜辣，口味重', '较差，多梦', '很少运动', '希望通过食疗改善体质', '2026-03-27 21:23:26', '2026-03-27 21:23:26');
INSERT INTO `user_health_record` VALUES (9, 13, '养生刘', 25, 176.00, 72.00, 'B型', '气虚质', '无', '花粉过敏', NULL, '学生，经常熬夜', '不规律', '差，经常熬夜', '偶尔跑步', '想要改善气虚状态', '2026-03-27 21:23:26', '2026-03-27 21:23:26');
INSERT INTO `user_health_record` VALUES (10, 14, '保健周', 55, 165.00, 60.00, 'AB型', '阳虚质', '关节炎', '无', NULL, '退休，早睡早起', '喜温热食物', '良好', '每天太极拳', '注重冬季保暖养生', '2026-03-27 21:23:26', '2026-03-27 21:23:26');
INSERT INTO `user_health_record` VALUES (11, 15, '养生爱好者', 28, 170.00, 58.00, 'A型', '气虚质', '无', '无', NULL, '上班族，压力大', '清淡为主', '一般', '每周运动2次', '希望补气提升精力', '2026-03-27 21:23:26', '2026-03-27 21:23:26');

SET FOREIGN_KEY_CHECKS = 1;
