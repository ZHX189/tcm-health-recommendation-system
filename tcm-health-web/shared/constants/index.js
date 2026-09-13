/**
 * 共享常量定义
 * @author Ti
 * @since 2026-02-03
 */

// 用户类型
export const USER_TYPE = {
  USER: 1,      // 普通用户
  STAFF: 2,     // 员工
  ADMIN: 3      // 管理员
}

// 用户类型标签
export const USER_TYPE_LABEL = {
  [USER_TYPE.USER]: '普通用户',
  [USER_TYPE.STAFF]: '员工',
  [USER_TYPE.ADMIN]: '管理员'
}

// 订单状态
export const ORDER_STATUS = {
  PENDING_PAY: 0,     // 待支付
  PAID: 1,            // 已支付
  SHIPPED: 2,         // 已发货
  COMPLETED: 3,       // 已完成
  CANCELLED: 4        // 已取消
}

// 订单状态标签
export const ORDER_STATUS_LABEL = {
  [ORDER_STATUS.PENDING_PAY]: '待支付',
  [ORDER_STATUS.PAID]: '已支付',
  [ORDER_STATUS.SHIPPED]: '已发货',
  [ORDER_STATUS.COMPLETED]: '已完成',
  [ORDER_STATUS.CANCELLED]: '已取消'
}

// 订单状态颜色
export const ORDER_STATUS_COLOR = {
  [ORDER_STATUS.PENDING_PAY]: '#E6A23C',
  [ORDER_STATUS.PAID]: '#409EFF',
  [ORDER_STATUS.SHIPPED]: '#67C23A',
  [ORDER_STATUS.COMPLETED]: '#909399',
  [ORDER_STATUS.CANCELLED]: '#F56C6C'
}

// 商品状态
export const PRODUCT_STATUS = {
  OFF_SHELF: 0,       // 下架
  ON_SHELF: 1,        // 上架
  PENDING: 2          // 待审核
}

// 商品状态标签
export const PRODUCT_STATUS_LABEL = {
  [PRODUCT_STATUS.OFF_SHELF]: '已下架',
  [PRODUCT_STATUS.ON_SHELF]: '已上架',
  [PRODUCT_STATUS.PENDING]: '待审核'
}

// 用户状态
export const USER_STATUS = {
  DISABLED: 0,        // 禁用
  ENABLED: 1          // 启用
}

// 用户状态标签
export const USER_STATUS_LABEL = {
  [USER_STATUS.DISABLED]: '已禁用',
  [USER_STATUS.ENABLED]: '已启用'
}

// 性别
export const GENDER = {
  UNKNOWN: 0,
  MALE: 1,
  FEMALE: 2
}

// 性别标签
export const GENDER_LABEL = {
  [GENDER.UNKNOWN]: '未知',
  [GENDER.MALE]: '男',
  [GENDER.FEMALE]: '女'
}

// 分页配置
export const PAGE_CONFIG = {
  DEFAULT_PAGE: 1,
  DEFAULT_SIZE: 10,
  PAGE_SIZES: [10, 20, 50, 100]
}

// API 响应码
export const RESULT_CODE = {
  SUCCESS: 200,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  ERROR: 500
}

// 本地存储键名
export const STORAGE_KEYS = {
  TOKEN: 'tcm_token',
  USER_INFO: 'tcm_user_info',
  USER_TYPE: 'tcm_user_type'
}
