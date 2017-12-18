@Author: barna10
@Description: 
This is a spring-mvc web app built with an embedded tomcat server; i.e. spring boot
The application is compiled to a jar file and can be executed with java -jar command, 
It can also be compiled to a war file and deployed as usual to any application server.  

To build this project: 
1. Right click on Run as > Maven build

To run this project on an embedded tomcat server: 
1. Right click on StartAppServer class > Run as > Java Application
2. browse to: http://localhost:8080/ to view the main page

To debug this project on a remote server: 
1. Run this application with the following debug flags: 
C:\Users\barna10\workspace\spring-boot-with-tomcat\target>java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8888,suspend=n -jar migration-wizard-0.0.1
-SNAPSHOT.jar
2. Run your eclipse project on remote debug: 
Right click on StartAppServer class > Debug as > Debug Configuration > Remote java application > specify the host and port (e.g. localhost, 8888) 