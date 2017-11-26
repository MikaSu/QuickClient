# QuickClient

DFC to maven:
Windows, create cmd-file with following contents, run in directory where dfc is located.
call mvn install:install-file -Dfile=activation.jar -DgroupId=com.documentum -DartifactId=activation -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=All-MB.jar -DgroupId=com.documentum -DartifactId=All-MB -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=aspectjrt.jar -DgroupId=com.documentum -DartifactId=aspectjrt -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=bpmutil.jar -DgroupId=com.documentum -DartifactId=bpmutil -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=certj.jar -DgroupId=com.documentum -DartifactId=certj -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=ci.jar -DgroupId=com.documentum -DartifactId=ci -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=collaboration.jar -DgroupId=com.documentum -DartifactId=collaboration -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=commons-codec-1.3.jar -DgroupId=com.documentum -DartifactId=commons-codec-1.3 -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=commons-lang-2.4.jar -DgroupId=com.documentum -DartifactId=commons-lang-2.4 -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=configservice-api.jar -DgroupId=com.documentum -DartifactId=configservice-api -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=configservice-impl.jar -DgroupId=com.documentum -DartifactId=configservice-impl -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=cryptojce.jar -DgroupId=com.documentum -DartifactId=cryptojce -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=cryptojcommon.jar -DgroupId=com.documentum -DartifactId=cryptojcommon -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=cryptoj.jar -DgroupId=com.documentum -DartifactId=cryptoj -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=DmcRecords.jar -DgroupId=com.documentum -DartifactId=DmcRecords -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=dms-client-api.jar -DgroupId=com.documentum -DartifactId=dms-client-api -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=jaxb-api.jar -DgroupId=com.documentum -DartifactId=jaxb-api -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=jaxb-impl.jar -DgroupId=com.documentum -DartifactId=jaxb-impl -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=jcifs-krb5-1.3.1.jar -DgroupId=com.documentum -DartifactId=jcifs-krb5-1.3.1 -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=jcmandroidfips.jar -DgroupId=com.documentum -DartifactId=jcmandroidfips -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=jcmFIPS.jar -DgroupId=com.documentum -DartifactId=jcmFIPS -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=jcm.jar -DgroupId=com.documentum -DartifactId=jcm -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=jsr173_api.jar -DgroupId=com.documentum -DartifactId=jsr173_api -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=krbutil.jar -DgroupId=com.documentum -DartifactId=krbutil -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=log4j.jar -DgroupId=com.documentum -DartifactId=log4j -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=messageArchive.jar -DgroupId=com.documentum -DartifactId=messageArchive -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=messageService.jar -DgroupId=com.documentum -DartifactId=messageService -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=questFixForJDK7.jar -DgroupId=com.documentum -DartifactId=questFixForJDK7 -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=servlet-api.jar -DgroupId=com.documentum -DartifactId=servlet-api -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=subscription.jar -DgroupId=com.documentum -DartifactId=subscription -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=util.jar -DgroupId=com.documentum -DartifactId=util -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=vsj-license.jar -DgroupId=com.documentum -DartifactId=vsj-license -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=vsj-standard-3.3.jar -DgroupId=com.documentum -DartifactId=vsj-standard-3.3 -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=workflow.jar -DgroupId=com.documentum -DartifactId=workflow -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=xtrim-api.jar -DgroupId=com.documentum -DartifactId=xtrim-api -Dversion=7.3 -Dpackaging=jar
call mvn install:install-file -Dfile=xtrim-server.jar -DgroupId=com.documentum -DartifactId=xtrim-server -Dversion=7.3 -Dpackaging=jar

(In Linux, create shell script without "call" command)

And finally install dfc.jar with dependencies:

mvn install:install-file -Dfile=dfc.jar -DpomFile=pom.xml

with following pom.xml

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.documentum</groupId>
  <artifactId>dfc</artifactId>
  <version>7.3</version>
  <name>Documentum DFC</name>
  <description>Documentum DFC classes</description>
  <url>http://www.google.com/q=documentum</url>
  <dependencies>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>activation</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>All-MB</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>aspectjrt</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>bpmutil</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>certj</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>ci</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>collaboration</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>commons-codec-1.3</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>commons-lang-2.4</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>configservice-api</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>configservice-impl</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>cryptojce</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>cryptojcommon</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>cryptoj</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>DmcRecords</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>dms-client-api</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>jaxb-api</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>jaxb-impl</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>jcifs-krb5-1.3.1</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>jcmandroidfips</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>jcmFIPS</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>jcm</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>jsr173_api</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>krbutil</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>log4j</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>messageArchive</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>messageService</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>questFixForJDK7</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>servlet-api</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>subscription</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>util</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>vsj-license</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>vsj-standard-3.3</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>workflow</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>xtrim-api</artifactId>
      <version>7.3</version>
    </dependency>
    <dependency>
      <groupId>com.documentum</groupId>
      <artifactId>xtrim-server</artifactId>
      <version>7.3</version>
    </dependency>
  </dependencies>
</project>

