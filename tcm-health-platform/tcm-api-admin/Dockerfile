# 后端模块 tcm-api-admin 镜像
# 依赖 Maven 构建产物 target/tcm-api-admin-1.0.0.jar
FROM swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/library/eclipse-temurin:21-jre-jammy
ENV TZ=Asia/Shanghai
WORKDIR /app
COPY target/tcm-api-admin-1.0.0.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
