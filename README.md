# Simple-chat-OpenJMS
Simple chat written with Java Messaging Service OpenJMS: http://openjms.sourceforge.net/
In order to run the application, OpenJMS server has to be started first. 
Instructions for Mac:
1) Download openjms. 
2) Set JAVA_HOME(if you don't have it set up already), and OPENJMS_HOME. 
  This is how variables were set up on my computer:
  export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_251.jdk/Contents/Home
  export OPENJMS_HOME=/Users/user_name/openjms
3)From openjms/bin start the server using this command in the console:
  bash openjms.sh start
4) Run Java application in the code editor. 

You can open as many windows as you want. Login to chat with different usernames and observe how messages are being displayed from different users' perspective. 
