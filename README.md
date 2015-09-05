[Ogify][ogify] - others get it for you!
====================

Url: http://ogify.net

Treat people the way you want them to treat you.

**Table of Contents:**

- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Configure Database](#DB)
- [Configure Tomcat](#tomcat)
- [Additional Information](#additional-information)
- [Contacts](#contacts)


Prerequisites
=================

1. [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and [JRE 7+](http://www.oracle.com/technetwork/java/javase/downloads/server-jre8-downloads-2133154.html)
2. [Maven 3+](https://maven.apache.org/download.cgi)
3. [Tomcat 7](https://tomcat.apache.org/download-70.cgi)
4. [NodeJS](https://nodejs.org/en/download/)
5. [MySQL 5.6+](http://www.mysql.com/downloads/)
6. [Git 1.7+](https://git-scm.com/downloads)

Setup
=================

First of all make sure that all prerequisites are installed and configured properly. If you have any problems ont this step - please, read instalation notes of each product.

Don't forget to set *JAVA_HOME, JRE_HOME, MAVEN_HOME* and other environment variables. Make sure that 'path' variable contains all binary folders of installed prerequisites.

Clone project from main repo using git(Build_0 branch):

      git clone https://github.com/ogify/ogify.git
Then, open console and move to the project root dir.

Execute maven task to build web archive package like:

      cd srv
      mvn package

[Configure database](#DB)

[Copy artifacts and configure Tomcat](#tomcat)

Start Tomcat using startup script.

Move to 'webclient' folder, install and run frontend web server:

      cd webclient
      npm install
      npm start

Verify that http://localhost:8000 working properly.

Configure Database
=================

Access MySQL as root user like:

      mysql -u root -p

Create Database for [ogify][ogify] project:

      CREATE DATABASE `ogify` CHARACTER SET utf8 COLLATE utf8_general_ci;

Create user and grunt DB:

      CREATE USER 'ogify'@'localhost' IDENTIFIED BY 'ogify';
      GRANT ALL PRIVILEGES ON ogify.* TO 'ogify'@'localhost';

Configure Tomcat
=================

Copy built war from 'target' to tomcat 'webapps' folder and rename it:

      cp target/net.ogify-1.0-SNAPSHOT.war $TOMCAT_HOME/webapps/ogify.war

Modify $TOMCAT_HOME/conf/server.xml file to use port 8080.

Provide link resource to already configured DB. Add to $TOMCAT_HOME/conf/context.xml string like:

      <Resource name="jdbc/ogify" auth="Container" type="javax.sql.DataSource"  username="ogify" password="ogify" driverClassName="com.mysql.jdbc.Driver" url="jdbc:mysql://localhost:3306/ogify"/>

Additional Information
=================

You could configure WebStorm IDE client to run frontend scripts like:
![webstorm_config](https://cloud.githubusercontent.com/assets/5920970/9700893/617ad06e-541d-11e5-8d0f-4b8d15743e62.PNG)
Where JavaScript file pointed to already installed npm-cli.js

Current working version located at Build_0 git branch.

### Contacts
If you have any question please fill free to contact as: [melges.morgen@gmail.com](mailto:melges.morgen@gmail.com), [sergey.shilin@phystech.edu](sergey.shilin@phystech.edu) or [kentilini@gmail.com](mailto:kentilini@gmail.com)


[ogify]: http://ogify.net
