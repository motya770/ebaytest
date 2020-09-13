## program setup

1) you need to git pull 3 project 
a) ebayeuroka - discovery server https://github.com/motya770/ebayeuroka <br/>
b) ebaydemo - auction service https://github.com/motya770/ebaytest <br/>
c) ebaychecker - program that calls to auction service https://github.com/motya770/ebaychecker <br/>

you should launch
 (each application is spring boot project - 
 you can run them in your IDE as main java program)
 those applications (because of service discovery)
in specific order - 
<br/><b>a</b> - than wait 10 seconds
<br/><b>b</b> - than wait 10 seconds 
<br/><c>c<c> - this is the program that creates auctions 

## if you want to see swagger web interface of ebaydemo

go to 
http://localhost:8080/swagger-ui.html#

## if you need to see what happening in db 
 
go to, when ebaydemo is launched, 
url to db is 
<b>jdbc:h2:mem:testdb</b>
user the same as default, not need for a password 

http://localhost:8080/h2-console

## know problems 
1) need tests 
2) fix eureka fast rediscovery 
3) check overlapping 
4) 