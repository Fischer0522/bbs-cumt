FROM java:8
COPY *.jar /app.jar

CMD ["--server.port=8080"]

#定义时区参数
ENV TZ=Asia/Shanghai

#设置时区
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo '$TZ' > /etc/timezone

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]
