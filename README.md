# Java-seckill
Introduction

This is a second kill project demo .It can be used in the
 seckilling(the quick sell out of newly-advertised goods) of e-shop.
 I learn this project from imooc.


   Software environment: Tomcat V7.0+ Mysql V5.7+Redis
　　Hardware environment: Mac OS
　　Development tools: IDEA
　　The main language: JAVA

　　
Project language and frame: 

Back-end:  SpringMVC+Sping+MyBatis,log4j,commons-collections
 			protostuff
Front-end: jQuery,Bootstrap,jQuery.countdown,jQuery.cookie,
 			javascript,html,css,jsp
  

Run

Run the project in Tomcat7.0 and visit http://localhost:8080/seckill/list



Project description

After loading the list page, one product can be choose to be seckilled.
After click the seckill button, brower jumps to another detail page.
if you first login the system or long time ago,the cookie doesn't 
contain your phone number, so there will be a pop-up window asking for your
phone number.If the format of your number is right,you can click the 
'submit' button and get in the seckill page,then if the product has already
been sold, you can click the seckill button;if otherwise,there will be a countdown board.You can't buy the product repeatedly
with the same phone number and product id.


Something learned:

1. Understand the seckill business process and code logic;

2. Understand operation and integration of SpringMVC+Sping+MyBatis;

3. Analyse the bottleneck of the seckill process and optimize
the code to make the program have the ability to provide for a high degree of concurrency
and fast access.The optimization is related to redis cache and 
transaction,lock and procedure in Mysql;















