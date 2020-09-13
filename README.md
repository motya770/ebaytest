## program setup

1) you need to git pull 3 project 
a) ebayeuroka - discovery server 
b) ebaydemo - auction service 
c) ebaychecker - program that calls to auction service 

you should launch those applications (because of service discovery)
in specific order - 
<b>a</b> - than wait 10 seconds
<b>b</b> - than wait 10 seconds 
<c>c<c> - this is the program that creates auctions 

## if you want to see swagger web interface of ebaydemo

go to 
http://localhost:8080/swagger-ui.html#

## if you need to see what happening in db 
 
go to, when application is launched, 
url to db is 
<b>jdbc:h2:mem:testdb</b>
user the same as default, not need for a password 

http://localhost:8080/h2-console