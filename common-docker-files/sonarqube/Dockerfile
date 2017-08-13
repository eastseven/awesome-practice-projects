FROM sonarqube:alpine

RUN echo "Asia/Shanghai" > /etc/timezone;
VOLUME /etc/localtime:/etc/localtime
ADD sonar-l10n-zh-plugin-1.16.jar /opt/sonarqube/extensions/plugins/