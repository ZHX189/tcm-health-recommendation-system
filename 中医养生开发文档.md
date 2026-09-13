# 中医养生平台开发文档

> **作者：zhx**  
> **创建时间：2026-02-03**  
> **版本：v1.0.0**

---

## 一、项目概述

### 1.1 项目背景

中医养生平台是一个集**个人健康管理**、**中医药材购物**、**养生互动分享**于一体的综合性平台，面向普通用户提供养生服务，同时为商城运营人员和管理员提供高效的后台管理工具。

### 1.2 核心目标

- 用户端：提供优质的养生服务体验和便捷的药材购物流程
- 员工端：高效执行商城日常运营任务
- 管理端：全局管控平台运营，保障内容质量和数据安全

---

## 二、技术栈

### 2.1 后端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| JDK | 21 | Java开发环境 |
| Spring Boot | 3.2.x | 后端框架 |
| MyBatis Plus | 3.5.x | ORM框架 |
| MySQL | 8.0+ | 关系型数据库 |
| JWT | - | 身份认证 |
| Spring Security | 6.x | 权限控制 |
| Hutool | 5.8.x | 工具类库 |
| Knife4j | 4.x | API文档 |

### 2.2 前端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.4.x | 前端框架 |
| Element Plus | 2.5.x | UI组件库 |
| Pinia | 2.x | 状态管理 |
| Vue Router | 4.x | 路由管理 |
| ECharts | 5.x | 数据可视化 |
| Axios | 1.x | HTTP请求 |
| Vite | 5.x | 构建工具 |

---

## 三、系统角色与权限

### 3.1 角色定义

| 角色 | 权限层级 | 核心职责 |
|------|----------|----------|
| **普通用户（USER）** | 1级（最低） | 个人健康管理、商城购物、社区互动 |
| **员工（STAFF）** | 2级（中等） | 商品管理、订单处理、库存管理 |
| **管理员（ADMIN）** | 3级（最高） | 全局管控、人员管理、内容审核、数据统计 |

### 3.2 权限矩阵

```
┌─────────────────────────────────────────────────────────────────┐
│                        权限层级结构                              │
├─────────────────────────────────────────────────────────────────┤
│  管理端 (ADMIN)                                                  │
│  ├── 全局数据统计与可视化                                        │
│  ├── 人员权限管理（员工/用户）                                    │
│  ├── 内容审核与发布                                              │
│  ├── 商品/订单总控                                               │
│  └── 系统配置                                                    │
├─────────────────────────────────────────────────────────────────┤
│  员工端 (STAFF) - 权限由管理端分配                                │
│  ├── 商品信息维护（需审核）                                       │
│  ├── 订单发货处理                                                │
│  ├── 库存管理                                                    │
│  └── 内容草稿提交                                                │
├─────────────────────────────────────────────────────────────────┤
│  用户端 (USER)                                                   │
│  ├── 个人信息管理                                                │
│  ├── 健康档案管理                                                │
│  ├── 商城购物                                                    │
│  ├── 养生服务                                                    │
│  └── 社区互动                                                    │
└─────────────────────────────────────────────────────────────────┘
```

---

## 四、功能模块拆分

### 4.1 用户端功能模块

#### 模块一：账号与个人基础管理
| 功能点 | 功能描述 | 优先级 |
|--------|----------|--------|
| 用户注册 | 账号密码注册，手机号验证 | P0 |
| 用户登录 | 账号密码登录，JWT令牌 | P0 |
| 个人信息修改 | 头像、昵称、密码修改 | P1 |
| 收货地址管理 | 地址CRUD，默认地址设置 | P1 |

#### 模块二：健康与养生专属
| 功能点 | 功能描述 | 优先级 |
|--------|----------|--------|
| 健康档案 | 创建/查看/更新健康档案（体质、病史、过敏源） | P1 |
| 养生方案浏览 | 按分类浏览、收藏养生方案 | P1 |
| 养生知识中心 | 文章浏览、搜索、收藏 | P2 |

#### 模块三：商城购物与订单管理
| 功能点 | 功能描述 | 优先级 |
|--------|----------|--------|
| 商品浏览 | 商品列表、详情、分类筛选、搜索 | P0 |
| 购物车 | 添加、修改数量、删除商品 | P0 |
| 订单管理 | 创建订单、支付、取消、确认收货 | P0 |
| 商品评价 | 发表评价、星级评分、上传图片 | P2 |
| 收藏管理 | 商品/文章/方案统一收藏列表 | P2 |

#### 模块四：信息查询与互动服务
| 功能点 | 功能描述 | 优先级 |
|--------|----------|--------|
| 资讯公告 | 浏览资讯/公告列表及详情 | P2 |
| 社区分享 | 发帖、评论、点赞、分享 | P2 |
| AI智能客服 | 药材咨询、SSE流式输出、上下文记忆 | P1 |

---

### 4.2 员工端功能模块

#### 模块一：员工账号基础
| 功能点 | 功能描述 | 优先级 |
|--------|----------|--------|
| 员工登录 | 账号密码登录（管理端创建账号） | P0 |
| 个人信息 | 查看/修改个人资料 | P2 |

#### 模块二：商城日常运营管理
| 功能点 | 功能描述 | 优先级 |
|--------|----------|--------|
| 商品管理 | 修改商品信息、上传图片、上下架（需审核） | P0 |
| 订单管理 | 查看订单、筛选、发货操作 | P0 |
| 库存管理 | 库存查看、预警提醒、批量更新 | P1 |

#### 模块三：辅助运营
| 功能点 | 功能描述 | 优先级 |
|--------|----------|--------|
| 评价查看 | 查看商品评价（无删除权限） | P2 |
| 内容草稿 | 撰写资讯/公告草稿提交审核 | P2 |

---

### 4.3 管理端功能模块

#### 模块一：账号与权限管理
| 功能点 | 功能描述 | 优先级 |
|--------|----------|--------|
| 管理员认证 | 超级管理员注册/登录 | P0 |
| 员工管理 | 创建/审核员工账号、权限分配、禁用/启用 | P0 |
| 用户管理 | 查看用户列表、禁用违规用户 | P1 |

#### 模块二：数据统计与可视化
| 功能点 | 功能描述 | 优先级 |
|--------|----------|--------|
| 销售统计 | 销售额、销量排行 | P1 |
| 订单统计 | 订单量、履约率、取消率 | P1 |
| 用户统计 | 注册量、活跃用户、留存率 | P2 |
| 可视化图表 | ECharts折线图、柱状图、饼图 | P1 |

#### 模块三：商城全局运营管理
| 功能点 | 功能描述 | 优先级 |
|--------|----------|--------|
| 商品总控 | 商品CRUD、批量上传、上下架终审 | P0 |
| 分类管理 | 商品分类CRUD、排序 | P1 |
| 订单总控 | 全量订单查看、状态修改、数据导出 | P0 |
| 库存总控 | 批量调整、库存规则配置 | P1 |

#### 模块四：内容审核与管理
| 功能点 | 功能描述 | 优先级 |
|--------|----------|--------|
| 内容审核 | 帖子/文章/评论审核（通过/驳回/删除） | P1 |
| 资讯公告管理 | 资讯/公告CRUD、封面上传 | P1 |
| 养生方案管理 | 创建/编辑/发布/下架养生方案 | P1 |

#### 模块五：系统配置
| 功能点 | 功能描述 | 优先级 |
|--------|----------|--------|
| 系统参数 | 订单超时规则、支付配置、AI客服配置 | P2 |
| 数据备份 | 数据导出、定时备份配置 | P2 |

---

## 五、数据库设计

### 5.1 数据库表清单

#### 用户相关（6张表）
```
├── sys_user              # 用户基础表（含三种角色）
├── sys_role              # 角色表
├── sys_user_role         # 用户角色关联表
├── user_address          # 用户收货地址表
├── user_health_record    # 用户健康档案表
└── user_collection       # 用户收藏表（商品/文章/方案）
```

#### 商品相关（5张表）
```
├── product               # 商品表
├── product_category      # 商品分类表
├── product_image         # 商品图片表
├── product_stock         # 商品库存表
└── product_stock_log     # 库存变更日志表
```

#### 订单相关（3张表）
```
├── order_info            # 订单主表
├── order_item            # 订单明细表
└── order_payment         # 订单支付记录表
```

#### 购物车（1张表）
```
└── shopping_cart         # 购物车表
```

#### 评价相关（2张表）
```
├── product_review        # 商品评价表
└── review_image          # 评价图片表
```

#### 养生相关（3张表）
```
├── health_plan           # 养生方案表
├── health_article        # 养生文章表
└── article_category      # 文章分类表
```

#### 内容相关（4张表）
```
├── news                  # 资讯表
├── announcement          # 公告表
├── community_post        # 社区帖子表
└── post_comment          # 帖子评论表
```

#### 系统相关（2张表）
```
├── sys_config            # 系统配置表
└── operation_log         # 操作日志表
```

**共计：26张数据表**

---

### 5.2 核心表结构设计

#### sys_user（用户表）
```sql
CREATE TABLE `sys_user` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `avatar` VARCHAR(255) COMMENT '头像URL',
  `phone` VARCHAR(20) COMMENT '手机号',
  `email` VARCHAR(100) COMMENT '邮箱',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `user_type` TINYINT NOT NULL COMMENT '用户类型：1-普通用户，2-员工，3-管理员',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
) COMMENT='用户表';
```

#### product（商品表）
```sql
CREATE TABLE `product` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
  `name` VARCHAR(200) NOT NULL COMMENT '商品名称',
  `category_id` BIGINT NOT NULL COMMENT '分类ID',
  `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
  `original_price` DECIMAL(10,2) COMMENT '原价',
  `main_image` VARCHAR(255) COMMENT '主图URL',
  `description` TEXT COMMENT '商品描述',
  `detail` TEXT COMMENT '商品详情（富文本）',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0-下架，1-上架，2-待审核',
  `sales` INT DEFAULT 0 COMMENT '销量',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT DEFAULT 0
) COMMENT='商品表';
```

#### order_info（订单表）
```sql
CREATE TABLE `order_info` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
  `order_no` VARCHAR(50) NOT NULL UNIQUE COMMENT '订单编号',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `total_amount` DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
  `pay_amount` DECIMAL(10,2) COMMENT '实付金额',
  `status` TINYINT DEFAULT 0 COMMENT '订单状态：0-待支付，1-已支付，2-已发货，3-已完成，4-已取消',
  `address_id` BIGINT COMMENT '收货地址ID',
  `receiver_name` VARCHAR(50) COMMENT '收货人姓名',
  `receiver_phone` VARCHAR(20) COMMENT '收货人电话',
  `receiver_address` VARCHAR(500) COMMENT '收货地址详情',
  `remark` VARCHAR(500) COMMENT '订单备注',
  `pay_time` DATETIME COMMENT '支付时间',
  `ship_time` DATETIME COMMENT '发货时间',
  `complete_time` DATETIME COMMENT '完成时间',
  `cancel_time` DATETIME COMMENT '取消时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT DEFAULT 0
) COMMENT='订单表';
```

---

## 六、项目结构

### 6.1 后端项目结构（Spring Boot）

```
tcm-health-platform/
├── tcm-common/                          # 公共模块
│   ├── src/main/java/com/tcm/common/
│   │   ├── config/                      # 配置类
│   │   ├── constant/                    # 常量定义
│   │   ├── enums/                       # 枚举类
│   │   ├── exception/                   # 自定义异常
│   │   ├── result/                      # 统一响应
│   │   └── utils/                       # 工具类
│   └── pom.xml
│
├── tcm-model/                           # 实体模块
│   ├── src/main/java/com/tcm/model/
│   │   ├── entity/                      # 实体类
│   │   ├── dto/                         # 数据传输对象
│   │   ├── vo/                          # 视图对象
│   │   └── query/                       # 查询参数
│   └── pom.xml
│
├── tcm-mapper/                          # 数据访问模块
│   ├── src/main/java/com/tcm/mapper/
│   │   └── *Mapper.java                 # MyBatis Plus Mapper
│   ├── src/main/resources/mapper/
│   │   └── *.xml                        # Mapper XML
│   └── pom.xml
│
├── tcm-service/                         # 业务逻辑模块
│   ├── src/main/java/com/tcm/service/
│   │   ├── user/                        # 用户服务
│   │   ├── product/                     # 商品服务
│   │   ├── order/                       # 订单服务
│   │   ├── health/                      # 健康养生服务
│   │   ├── content/                     # 内容服务
│   │   ├── statistics/                  # 统计服务
│   │   └── system/                      # 系统服务
│   └── pom.xml
│
├── tcm-security/                        # 安全模块
│   ├── src/main/java/com/tcm/security/
│   │   ├── config/                      # 安全配置
│   │   ├── filter/                      # JWT过滤器
│   │   ├── handler/                     # 认证处理器
│   │   └── service/                     # 认证服务
│   └── pom.xml
│
├── tcm-api-user/                        # 用户端API（端口：8081）
│   ├── src/main/java/com/tcm/api/user/
│   │   └── controller/                  # 用户端接口
│   └── pom.xml
│
├── tcm-api-staff/                       # 员工端API（端口：8082）
│   ├── src/main/java/com/tcm/api/staff/
│   │   └── controller/                  # 员工端接口
│   └── pom.xml
│
├── tcm-api-admin/                       # 管理端API（端口：8083）
│   ├── src/main/java/com/tcm/api/admin/
│   │   └── controller/                  # 管理端接口
│   └── pom.xml
│
└── pom.xml                              # 父POM
```

---

### 6.2 前端项目结构（Vue3）

```
tcm-health-web/
├── packages/
│   ├── user-web/                        # 用户端前端（端口：3001）
│   │   ├── src/
│   │   │   ├── api/                     # API接口
│   │   │   ├── assets/                  # 静态资源
│   │   │   ├── components/              # 公共组件
│   │   │   ├── composables/             # 组合式函数
│   │   │   ├── layouts/                 # 布局组件
│   │   │   ├── router/                  # 路由配置
│   │   │   ├── stores/                  # Pinia状态管理
│   │   │   ├── styles/                  # 样式文件
│   │   │   ├── utils/                   # 工具函数
│   │   │   ├── views/                   # 页面组件
│   │   │   │   ├── home/                # 首页
│   │   │   │   ├── user/                # 用户中心
│   │   │   │   ├── product/             # 商品相关
│   │   │   │   ├── order/               # 订单相关
│   │   │   │   ├── health/              # 健康养生
│   │   │   │   ├── community/           # 社区
│   │   │   │   └── chat/                # AI客服
│   │   │   ├── App.vue
│   │   │   └── main.js
│   │   ├── index.html
│   │   ├── vite.config.js
│   │   └── package.json
│   │
│   ├── staff-web/                       # 员工端前端（端口：3002）
│   │   ├── src/
│   │   │   ├── api/
│   │   │   ├── components/
│   │   │   ├── router/
│   │   │   ├── stores/
│   │   │   ├── views/
│   │   │   │   ├── dashboard/           # 工作台
│   │   │   │   ├── product/             # 商品管理
│   │   │   │   ├── order/               # 订单管理
│   │   │   │   ├── stock/               # 库存管理
│   │   │   │   └── profile/             # 个人信息
│   │   │   ├── App.vue
│   │   │   └── main.js
│   │   └── ...
│   │
│   └── admin-web/                       # 管理端前端（端口：3003）
│       ├── src/
│       │   ├── api/
│       │   ├── components/
│       │   │   └── charts/              # ECharts图表组件
│       │   ├── router/
│       │   ├── stores/
│       │   ├── views/
│       │   │   ├── dashboard/           # 数据大盘
│       │   │   ├── user/                # 用户管理
│       │   │   ├── staff/               # 员工管理
│       │   │   ├── product/             # 商品总控
│       │   │   ├── order/               # 订单总控
│       │   │   ├── content/             # 内容管理
│       │   │   ├── audit/               # 审核中心
│       │   │   ├── statistics/          # 数据统计
│       │   │   └── system/              # 系统配置
│       │   ├── App.vue
│       │   └── main.js
│       └── ...
│
├── shared/                              # 共享模块
│   ├── api/                             # 共享API定义
│   ├── components/                      # 共享组件
│   ├── constants/                       # 常量定义
│   └── utils/                           # 共享工具
│
├── package.json
└── pnpm-workspace.yaml                  # PNPM工作空间配置
```

---

## 七、接口设计规范

### 7.1 RESTful API规范

| HTTP方法 | 用途 | 示例 |
|----------|------|------|
| GET | 查询资源 | GET /api/products |
| POST | 创建资源 | POST /api/products |
| PUT | 全量更新 | PUT /api/products/{id} |
| PATCH | 部分更新 | PATCH /api/products/{id}/status |
| DELETE | 删除资源 | DELETE /api/products/{id} |

### 7.2 统一响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1706918400000
}
```

### 7.3 接口路径规范

```
用户端API：  /api/user/**
员工端API：  /api/staff/**
管理端API：  /api/admin/**
```

---

## 八、核心接口清单

### 8.1 用户端核心接口

```
# 认证模块
POST   /api/user/auth/register           # 用户注册
POST   /api/user/auth/login              # 用户登录
POST   /api/user/auth/logout             # 退出登录

# 个人信息
GET    /api/user/profile                 # 获取个人信息
PUT    /api/user/profile                 # 修改个人信息
PUT    /api/user/profile/password        # 修改密码
POST   /api/user/profile/avatar          # 上传头像

# 收货地址
GET    /api/user/addresses               # 地址列表
POST   /api/user/addresses               # 新增地址
PUT    /api/user/addresses/{id}          # 修改地址
DELETE /api/user/addresses/{id}          # 删除地址
PATCH  /api/user/addresses/{id}/default  # 设置默认地址

# 健康档案
GET    /api/user/health-record           # 获取健康档案
POST   /api/user/health-record           # 创建健康档案
PUT    /api/user/health-record           # 更新健康档案

# 商品
GET    /api/user/products                # 商品列表
GET    /api/user/products/{id}           # 商品详情
GET    /api/user/products/categories     # 商品分类

# 购物车
GET    /api/user/cart                    # 购物车列表
POST   /api/user/cart                    # 添加商品到购物车
PUT    /api/user/cart/{id}               # 修改数量
DELETE /api/user/cart/{id}               # 删除商品
DELETE /api/user/cart/batch              # 批量删除

# 订单
GET    /api/user/orders                  # 订单列表
GET    /api/user/orders/{id}             # 订单详情
POST   /api/user/orders                  # 创建订单
POST   /api/user/orders/{id}/pay         # 支付订单
POST   /api/user/orders/{id}/cancel      # 取消订单
POST   /api/user/orders/{id}/confirm     # 确认收货

# 评价
GET    /api/user/reviews/product/{productId}  # 商品评价列表
POST   /api/user/reviews                      # 发表评价

# 收藏
GET    /api/user/collections             # 收藏列表
POST   /api/user/collections             # 添加收藏
DELETE /api/user/collections/{id}        # 取消收藏

# 养生方案
GET    /api/user/health-plans            # 养生方案列表
GET    /api/user/health-plans/{id}       # 方案详情

# 养生文章
GET    /api/user/articles                # 文章列表
GET    /api/user/articles/{id}           # 文章详情

# 资讯公告
GET    /api/user/news                    # 资讯列表
GET    /api/user/news/{id}               # 资讯详情
GET    /api/user/announcements           # 公告列表
GET    /api/user/announcements/{id}      # 公告详情

# 社区
GET    /api/user/posts                   # 帖子列表
GET    /api/user/posts/{id}              # 帖子详情
POST   /api/user/posts                   # 发布帖子
POST   /api/user/posts/{id}/comments     # 评论帖子
POST   /api/user/posts/{id}/like         # 点赞帖子

# AI客服
POST   /api/user/chat                    # AI对话（SSE）
```

### 8.2 员工端核心接口

```
# 认证
POST   /api/staff/auth/login             # 员工登录

# 商品管理
GET    /api/staff/products               # 商品列表
PUT    /api/staff/products/{id}          # 修改商品
PATCH  /api/staff/products/{id}/status   # 上下架（需审核）
POST   /api/staff/products/{id}/images   # 上传商品图片

# 订单管理
GET    /api/staff/orders                 # 订单列表
GET    /api/staff/orders/{id}            # 订单详情
POST   /api/staff/orders/{id}/ship       # 订单发货

# 库存管理
GET    /api/staff/stocks                 # 库存列表
PUT    /api/staff/stocks/{productId}     # 更新库存
GET    /api/staff/stocks/warning         # 库存预警
GET    /api/staff/stocks/logs            # 库存变更记录

# 评价查看
GET    /api/staff/reviews                # 评价列表

# 内容草稿
POST   /api/staff/drafts/news            # 资讯草稿
POST   /api/staff/drafts/announcement    # 公告草稿
```

### 8.3 管理端核心接口

```
# 认证
POST   /api/admin/auth/register          # 超管注册（首次）
POST   /api/admin/auth/login             # 管理员登录

# 员工管理
GET    /api/admin/staffs                 # 员工列表
POST   /api/admin/staffs                 # 创建员工
PUT    /api/admin/staffs/{id}            # 修改员工
PATCH  /api/admin/staffs/{id}/status     # 禁用/启用
PUT    /api/admin/staffs/{id}/permissions # 权限分配

# 用户管理
GET    /api/admin/users                  # 用户列表
GET    /api/admin/users/{id}             # 用户详情
PATCH  /api/admin/users/{id}/status      # 禁用/启用

# 数据统计
GET    /api/admin/statistics/sales       # 销售统计
GET    /api/admin/statistics/orders      # 订单统计
GET    /api/admin/statistics/users       # 用户统计
GET    /api/admin/statistics/reviews     # 评价统计
GET    /api/admin/statistics/dashboard   # 大盘数据

# 商品总控
GET    /api/admin/products               # 商品列表
POST   /api/admin/products               # 新增商品
PUT    /api/admin/products/{id}          # 修改商品
DELETE /api/admin/products/{id}          # 删除商品
PATCH  /api/admin/products/{id}/audit    # 审核上下架

# 分类管理
GET    /api/admin/categories             # 分类列表
POST   /api/admin/categories             # 新增分类
PUT    /api/admin/categories/{id}        # 修改分类
DELETE /api/admin/categories/{id}        # 删除分类

# 订单总控
GET    /api/admin/orders                 # 全量订单
PATCH  /api/admin/orders/{id}/status     # 修改状态
GET    /api/admin/orders/export          # 导出订单

# 内容审核
GET    /api/admin/audit/posts            # 待审核帖子
GET    /api/admin/audit/articles         # 待审核文章
GET    /api/admin/audit/comments         # 待审核评论
POST   /api/admin/audit/{type}/{id}      # 审核操作

# 资讯公告管理
GET    /api/admin/news                   # 资讯列表
POST   /api/admin/news                   # 发布资讯
PUT    /api/admin/news/{id}              # 修改资讯
DELETE /api/admin/news/{id}              # 删除资讯

GET    /api/admin/announcements          # 公告列表
POST   /api/admin/announcements          # 发布公告
PUT    /api/admin/announcements/{id}     # 修改公告
DELETE /api/admin/announcements/{id}     # 删除公告

# 养生方案管理
GET    /api/admin/health-plans           # 方案列表
POST   /api/admin/health-plans           # 创建方案
PUT    /api/admin/health-plans/{id}      # 修改方案
DELETE /api/admin/health-plans/{id}      # 删除方案

# 系统配置
GET    /api/admin/configs                # 配置列表
PUT    /api/admin/configs                # 更新配置

# 数据导出
GET    /api/admin/export/users           # 导出用户
GET    /api/admin/export/orders          # 导出订单
GET    /api/admin/export/products        # 导出商品
```

---

## 九、开发计划

### 9.1 里程碑规划

| 阶段 | 时间 | 目标 |
|------|------|------|
| **Phase 1** | 第1-2周 | 项目搭建 + 基础模块（认证、用户管理） |
| **Phase 2** | 第3-4周 | 商品模块 + 订单模块（核心购物流程） |
| **Phase 3** | 第5-6周 | 健康养生模块 + 社区模块 |
| **Phase 4** | 第7周 | 统计模块 + 审核模块 |
| **Phase 5** | 第8周 | AI客服 + 系统配置 + 联调测试 |

### 9.2 MVP核心功能（必须完成）

1. ✅ 三端用户认证与权限控制
2. ✅ 商品展示与购物车
3. ✅ 订单创建与支付流程
4. ✅ 员工发货处理
5. ✅ 管理员数据统计大盘

---

## 十、附录

### 10.1 状态码定义

| 状态码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未认证 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

### 10.2 业务状态枚举

**订单状态**
- 0: 待支付
- 1: 已支付
- 2: 已发货
- 3: 已完成
- 4: 已取消

**商品状态**
- 0: 下架
- 1: 上架
- 2: 待审核

**审核状态**
- 0: 待审核
- 1: 已通过
- 2: 已驳回

---

> **文档持续更新中...**  
> **如有疑问请联系开发负责人：zhx**
