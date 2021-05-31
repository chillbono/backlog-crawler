FROM centos:7

RUN yum -y update
RUN yum -y install wget
RUN yum -y install unzip

# install locale
RUN localedef -f UTF-8 -i ja_JP ja_JP.UTF-8
ENV LANG="ja_JP.UTF-8" \
    LANGUAGE="ja_JP:ja" \
    LC_ALL="ja_JP.UTF-8"

# install mysql5.7
RUN yum localinstall -y https://dev.mysql.com/get/mysql57-community-release-el7-11.noarch.rpm
# setting for mysql version 5.6
#  RUN yum-config-manager --disable mysql57-community
#  RUN yum-config-manager --enable mysql56-community

RUN yum install -y mysql mysql-server

# install java 
RUN yum install -y java-11-openjdk
RUN yum install -y java-11-openjdk-devel

# install gradle
RUN wget https://services.gradle.org/distributions/gradle-6.7-bin.zip
RUN unzip -d /opt/gradle /gradle-6.7-bin.zip
RUN rm -rf /gradle-6.7-bin.zip
RUN ls /opt/gradle/

# setting path
ENV GRADLE_HOME /opt/gradle/gradle-6.7
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-11.0.11.0.9-1.el7_9.x86_64
ENV PATH ${JAVA_HOME}/bin:${GRADLE_HOME}/bin:${PATH}