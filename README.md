# Trading-platform
I have developed a trading platform that functions as a simulation of a stock exchange system. Within the platform, there are two primary types of users:

 - Broker – a user who executes trades of financial instruments on behalf of clients. In this simulation, the instrument being traded is an option, which can represent various assets such as gold, oil, or other resources.

 - Manager – a user with administrative privileges. The manager oversees instruments, users, and orders, and has access to all system data.

To ensure realistic market behavior, the platform relies on real instrument prices for specific dates. These prices are imported from .csv files, which serve as the system’s input data. Based on this information, the platform calculates the OTE (Open Trade Equity) – the current profit or loss of open positions.

The OTE calculation is performed automatically at predefined intervals (e.g., daily or monthly), allowing for continuous monitoring of trading performance and a realistic simulation of stock market operations.
# Develop
The trading platform has been developed using a microservices architecture, ensuring scalability, modularity, and maintainability. Each service has a well-defined responsibility and communicates asynchronously through Apache Kafka.

 - OTE Calculation Service – a dedicated backend service responsible for calculating the Open Trade Equity (OTE). It processes real market prices (imported from .csv files) and continuously evaluates the profit or loss of open trading positions based on predefined intervals (daily, monthly, etc.).

 - Broker Service – a separate backend service that manages all core entities of the system, including users (Brokers and Managers), instruments, and orders. It provides CRUD operations and ensures consistency of business data.

The services are decoupled and interact via Kafka topics, enabling reliable and scalable event-driven communication. User access is protected by an authorization and authentication layer, ensuring secure interaction with the platform. Additionally, the system leverages WebSockets to provide real-time updates of trading data and market changes to the frontend, enhancing the responsiveness and realism of the simulation.

Both services, along with the supporting infrastructure, are containerized using Docker, making the system portable and easy to deploy across environments. A Docker Compose setup orchestrates the services, database, and message broker, enabling reproducible builds and simplified deployment.

Technologies

 - PostgreSQL – relational database for persistent storage

 - Spring Boot (Microservices) – backend services for OTE calculation and entity management

 - Angular – web-based frontend for user interaction

 - Apache Kafka – message broker for asynchronous, event-driven communication between services

 - Docker & Docker Compose – containerization and orchestration of services
