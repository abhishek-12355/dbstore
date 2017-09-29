FROM tomcat:9-alpine
MAINTAINER Abhishek Shekhar <abhishek12355@gmail.com>

ENV JPDA_ADDRESS 5005
EXPOSE 5005

ADD target/dbstore-web.war /usr/local/tomcat/webapps

ENTRYPOINT ["catalina.sh", "jpda", "run"]