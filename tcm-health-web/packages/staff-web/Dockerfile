# 前端站点 staff-web 镜像
# 依赖构建产物 dist/ (由 pnpm --filter staff-web build 生成)
FROM swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/library/nginx:1.27-alpine
COPY dist/ /usr/share/nginx/html/
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
