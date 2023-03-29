# Expense-Tracker-application

**Overview :**

Expense tracker application is used to track our daily expense. User have to add their own expense by creating an account for them. In the dashboard page many options are available and one of them is **Add expense** which helps user to add current day expense by submitting notes, expensed amount. If in case user wants to add missed expense details that can be done using **Add missing expense** option. User can view their current day, month, year and overall expenses. In addition to adding expense details it also allows user to filter their expense by **specific date, duration, specific month and specific year**. Any time data can be **edited or deleted** from the list. For graphical representation data will be displayed in **bar graph for both yearly and monthly data**. User will get an email as a daily report upon a day completion if the had expensed anything in previous day, Monthly and Yearly reports will also be triggered from running cronjobs.


**Authentication and Authorization :**

Application is completely secure since all email ID's will be verified during the time of account registration. Two factor authentication has been incorporated in order to secure the application more. An OTP will be sent during login process, User can access their dashboard if and only if submitted OTP is valid. API guard helps to guard the API's from direct unauthorised request from outside world.


**Tech Stacks :**

   1. **Front end :** HTML and CSS
   2. **Back end :** Core Java and Java 17
   3. **Frameworks :** Bootstrap, AJAX ( for real data time refresh ), Spring boot
   4. **Databases :** SQL ( Postgres ) and NoSql ( MongoDB )


**Server and Microservices :**

Eureka server has been created and registed all running services with server. Available services are 
  1. **GUI service** ( GUI application )
  2. **Rest API** ( Where all API's developed )
  3. **Email notification service** ( For sending OTP, Daily, Monthly and Yearly reports to end user )
  4. **API Gateway** ( Where all routings have been configured )
  
  
**Message broker communication :**

RabbitMq used to pass data between one microservice and another service, It make sures that service listener is up before transmitting the data. If listener data service is down data will store temprorily in queue so there won't be conflict during the obsence of microservice.


**Glimpse from Expense tracker application :**

**Login and Registration :**

![Screenshot from 2023-03-28 19-11-09](https://user-images.githubusercontent.com/112934529/228463238-1af26211-ee46-4685-9356-96cb3d405e23.png)


**Invalid registration :**

![Screenshot from 2023-03-28 19-12-58](https://user-images.githubusercontent.com/112934529/228463266-804df826-be34-4d5a-b3db-57bf7f67c43e.png)


**Successful registraion :**

![Screenshot from 2023-03-28 19-14-12](https://user-images.githubusercontent.com/112934529/228463343-103cc46c-77b8-498c-830d-86af0a4a9f09.png)


**OTP alert box :**

![Screenshot from 2023-03-28 19-13-14](https://user-images.githubusercontent.com/112934529/228463512-db33ca89-a988-4ffd-baf1-84d2ee3ef4aa.png)


**Invalid login :**

![Screenshot from 2023-03-28 19-14-41](https://user-images.githubusercontent.com/112934529/228463569-22fd8e0d-ed1b-4f69-bc0c-edf5bc3e4f4b.png)


**Dashboard :**

![Screenshot from 2023-03-28 19-16-47](https://user-images.githubusercontent.com/112934529/228463608-1cb4a026-bf27-4c8b-b439-a2078b063135.png)


**Add new expense :**

![Screenshot from 2023-03-28 19-16-57](https://user-images.githubusercontent.com/112934529/228463649-aeb1b092-6578-48d2-a84f-9557ae815864.png)


**Add missing expense :**

![Screenshot from 2023-03-28 19-17-48](https://user-images.githubusercontent.com/112934529/228463697-c497afcb-9a88-49fa-9c64-b8d0b74d5375.png)


**Edit expense :**

![Screenshot from 2023-03-28 19-18-31](https://user-images.githubusercontent.com/112934529/228463832-33e96212-949a-481d-a733-6af3c86967c2.png)


**Delete expense :**

![Screenshot from 2023-03-28 19-18-37](https://user-images.githubusercontent.com/112934529/228463884-98b11c28-176f-4c69-b0b3-4044742f193b.png)


**Fetch by date :**

![Screenshot from 2023-03-28 19-19-02](https://user-images.githubusercontent.com/112934529/228463911-c4a71f6f-4260-4175-a92f-83ffd0b12396.png)


**Fetch between given duration :**

![Screenshot from 2023-03-28 19-19-32](https://user-images.githubusercontent.com/112934529/228463938-ac9d65ad-b444-4059-b2fd-93c4ee10254d.png)


**Fetch monthly expenses :**

![Screenshot from 2023-03-28 19-19-42](https://user-images.githubusercontent.com/112934529/228463974-d7c63c47-ea54-4086-a956-fbd6735c7d99.png)


**Fetch yearly expenses :**

![Screenshot from 2023-03-28 19-19-51](https://user-images.githubusercontent.com/112934529/228464008-600609a1-2fb1-4cd6-8b35-8f061367a7d6.png)


**When no data passed to show graph :**

![Screenshot from 2023-03-28 19-20-48](https://user-images.githubusercontent.com/112934529/228464063-e67d9a4b-cc39-4a9a-b139-54cabd46193c.png)


**Monthly graph :**

![Screenshot from 2023-03-28 19-22-46](https://user-images.githubusercontent.com/112934529/228464134-c9f67a3a-65ad-4b2e-8df4-d17e15b2f3a4.png)


**Yearly graoh :**

![Screenshot from 2023-03-28 19-20-56](https://user-images.githubusercontent.com/112934529/228464115-358e77ec-5eb2-4100-b5f5-62521403f35c.png)


**Logout and redirection :**

![Screenshot from 2023-03-28 19-22-54](https://user-images.githubusercontent.com/112934529/228464174-742ad339-a25c-4749-adda-f96e557a724b.png)


**Rabbit MQ :**

![Screenshot from 2023-03-28 19-23-06](https://user-images.githubusercontent.com/112934529/228464214-11b665b8-aea6-4d2c-a7e2-99ce508580d1.png)


**Eureka Virtual Server :**

![Screenshot from 2023-03-28 19-23-08](https://user-images.githubusercontent.com/112934529/228464250-f9024a6a-729e-4990-9d01-83a0107b615c.png)


**Postgres DB :**

![Screenshot from 2023-03-28 19-23-13](https://user-images.githubusercontent.com/112934529/228464278-f0d75df2-b536-4f99-a8b7-a4b1603ebf2d.png)


**Mongo DB :**

![Screenshot from 2023-03-28 19-23-16](https://user-images.githubusercontent.com/112934529/228464554-9250cb60-63cb-498f-9b58-335647532d89.png)


**!!Happy CodingG..!!** :-)
