# Kirana Application

Welcome to the Kirana Application!

## Description

The Kirana Application is designed to perform transaction functionalities of a day to day business such as recording a transaction and fetching transactions. This README file provides instructions on how to set up, run, and use the application.

## Features
 - Containerized app with docker-compose
 - Built in swagger documenation at http://localhost:8080/swagger-ui/index.html
 - Database Migrations
 - Record Transactions
 - fetch trasactions
 - Group trnsactions based on a given date or range of dates and get the aggregated amount.

## Getting Started

### Dependencies

* Java JDK [version]
* Maven [version]
* PostgreSQL [version]

### Installing

* Clone the repository:
  ```
  https://github.com/your-username/kirana-application.git
  ```

* Navigate to the project directory:
  ```
  cd kirana-application
  ```
* Install dependencies with:
  ```
  mvn clean install
  ```

* Configure environment variable in application.properties(src\main\resources\application.properties):
  ```
  spring.datasource.url=jdbc:postgresql://localhost:5432/Kirana
  spring.datasource.username=postgres
  spring.datasource.password=postgres
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
  logging.level.root=INFO
  logging.level.org.springframework=INFO
  fx.rates.url = https://api.fxratesapi.com/latest
  springfox.documentation.swagger-ui.enabled=true
  ```
  ### Note : replace the above values with correct ones for your env

* Run the Application with this command:
  ```
  mvn spring-boot:run
  ```
* This will start the application on http://localhost:8080.

* Visit : http://localhost:8080/swagger-ui/index.html to try out apis


*  Or fork this postman collection : 
[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://app.getpostman.com/run-collection/25361983-ef01fea2-7703-4445-99cd-812676f3cb98?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D25361983-ef01fea2-7703-4445-99cd-812676f3cb98%26entityType%3Dcollection%26workspaceId%3Da25f03ef-1850-4aeb-9ee1-2ece3d1a6d84)


### Run with Docker


* Navigate to the project directory and run docker build:
  ```
  docker build --platform linux/amd64 -t spring-kirana .
  ```
* Once the build is complete Run docker-compsoe:
  ```
  docker-compose up -d
  ```
* Navigate to : http://localhost:8080/swagger-ui/index.html for testing
  
### Note: Replace env variable in application.properties as mentioned above 



## API Documentaion : 
This API manages transactions within the Kirana Application.


## Record Transaction

### POST /api/transactions/record

Endpoint to record a transaction.

**Request Body**

```json
{
  "amount": 100.00,
  "currency": "USD"
}
```

## Response
```
{
  "id": 1,
  "amountINR": 7500.00,
  "amountUSD": 100.00,
  "currency": "USD",
  "transactionDate": "2024-01-04T15:30:00"
}

```

## Fetch Transactions

### GET /api/transactions/fetch

Endpoint to fetch transactions based on date, date range, and grouping.

**Parameters**

```
- date: Single date to fetch transactions.
- startDate: Start date for date range.
- endDate: End date for date range.
- group: Boolean flag to enable daily reports grouping (default: false).
```

### Response for `/api/transactions/fetch?date=2024-01-04`
```
{
  "transactions": [
    {
      "id": 1,
      "amountINR": 7500.00,
      "amountUSD": 100.00,
      "currency": "USD",
      "transactionDate": "2024-01-04T15:30:00"
    },
    {
      "id": 2,
      "amountINR": 5000.00,
      "amountUSD": 80.00,
      "currency": "USD",
      "transactionDate": "2024-01-04T16:00:00"
    }
  ],
  "reports": null
}
```

### Response for `/api/transactions/fetch?date=2024-01-04&group=true`
```
{
  "transactions": null,
  "reports": [
    {
      "date": "2024-01-04",
      "transactions": [
        {
          "id": 1,
          "amountINR": 7500.00,
          "amountUSD": 100.00,
          "currency": "USD",
          "transactionDate": "2024-01-04T15:30:00"
        },
        {
          "id": 2,
          "amountINR": 5000.00,
          "amountUSD": 80.00,
          "currency": "USD",
          "transactionDate": "2024-01-04T16:00:00"
        }
      ],
      "totalAmountINR": 12500.00,
      "totalAmountUSD": 180.00
    }
  ]
}
```

  
Author : 
Manjunath kotabal 
[@manjunathmkotabal](https://github.com/manjunathmkotabal)

## Version History

* 0.1
    * Initial Release
