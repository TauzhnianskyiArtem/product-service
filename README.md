# Product service

Product service holds data about all known products. Product itself is a main artifact for sales and marketing
departments, that may be active whether deactivated. Main operable entities: Product, Price, Currency, Discount. 


# Performance Testing Product Service

## Overview
This text provides instructions on how to set up and run performance tests for the `product-service` using Docker and K6.

## Prerequisites
- Docker and Docker Compose installed on your machine.
- Basic understanding of Docker and performance testing concepts.

## Setup
1. **Environment Variables**:
   Ensure that the necessary environment variables are set. These variables are used by the services defined in `docker-compose.yml`. These include:
    - `HOST`
    - `POSTGRES_DATABASE`
    - `POSTGRES_USERNAME`
    - `POSTGRES_PASSWORD`
    - `PGADMIN_EMAIL`
    - `PGADMIN_PASSWORD`

   You can set these variables in your environment or use a `.env` file at the root of your project.

2. **Build and Start Services**:
   Use the following command to build and start the necessary services:
   ```
   docker-compose up -d
   ```
   This will start the `product-service`, `postgres`, `pgadmin`, `influxdb`, `grafana`, `k6`, and `prometheus` services as defined in your `docker-compose.yml`.

## Running Performance Tests
1. **K6 Script**:
   Prepare your K6 script for performance testing. The script should be located in the `performance_scripts` directory as specified in the `docker-compose.yml`.

2. **Execute Test**:
   To run the performance test, use the following command:
    ```
    docker-compose run -e VUS=<number_of_virtual_users> k6 run /scripts/performance-tests-product-service.js
    ```
- Replace `<number_of_virtual_users>` with the desired number of virtual users for the test.


## Monitoring and Results
- **Grafana**:  
  Grafana is set up to visualize the results. Access it at `http://localhost:3000`. Default login details are usually `admin/admin`.

- **InfluxDB**:  
  K6 sends the test results to InfluxDB, which Grafana uses as a data source.

- **Prometheus**:  
  Prometheus is used for monitoring; access it at `http://localhost:9090`.

## Note
- The performance test configuration can be adjusted in the K6 script.
- Ensure that all services in the `docker-compose.yml` are properly configured and running before executing the tests.
