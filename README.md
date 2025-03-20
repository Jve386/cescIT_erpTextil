# Textil ERP 
Full-stack ERP system for a textile enterprise, built with PostgreSQL, Docker, Spring Boot, and Quasar (Vue.js). It provides comprehensive management of procurement, inventory, costs, pricing, and warehouse operations. Additionally, it includes a customer ticketing system for efficient support and issue tracking.


## Features  
- **Back-end (Spring Boot)**: REST API, JWT Authentication, PostgreSQL/MySQL database.
- **Front-end (Quasar)**: Modern UI with Vue 3, API integration, and responsive design.  
- **Docker Support**: Easily deployable with Docker Compose.  


## Project Structure
myapp/
│── backend/    # Spring Boot application
│── frontend/   # Quasar (Vue.js) application
│── README.md   # Documentation


## Database
<a href="assets/bbdd.png" target="_blank">
  <img src="assets/bbdd.png" alt="db" width="300">
</a>


## Technologies Used
Quasar:
```pinia``` for state management
```vee-validate``` for form validation
```date-fns``` for date formatting
```echarts``` and ```vue-echarts``` for visualizations
```vue-i18n``` for multi-language support


Spring Boot:
```spring-boot-starter-security``` (authentication & authorization)
```spring-boot-starter-validation``` (request validation)
```flyway-core``` (DB migrations)
```spring-boot-starter-actuator``` (health monitoring)


## Screenshots

<a href="assets/erp_01.png" target="_blank">
  <img src="assets/dashboard.png" alt="Dashboard Screenshot" width="300">
</a>

<a href="assets/erp_02.png" target="_blank">
  <img src="assets/inventory.png" alt="Dashboard Screenshot" width="300">
</a>

<a href="assets/erp_03.png" target="_blank">
  <img src="assets/inventory.png" alt="Dashboard Screenshot" width="300">
</a>

<a href="assets/erp_04.png" target="_blank">
  <img src="assets/inventory.png" alt="Dashboard Screenshot" width="300">
</a>

<a href="assets/erp_05.png" target="_blank">
  <img src="assets/inventory.png" alt="Dashboard Screenshot" width="300">
</a>

