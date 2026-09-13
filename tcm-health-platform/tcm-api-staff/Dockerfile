# 后端模块 tcm-api-staff 镜像
# 依赖 Maven 构建产物 target/tcm-api-staff-1.0.0.jar
FROM swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/library/eclipse-temurin:21-jre-jammy
ENV TZ=Asia/Shanghai
WORKDIR /app
COPY target/tcm-api-staff-1.0.0.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
