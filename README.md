For setting up your environment. Make sure that:

- The project is imported into your IDE and it compiles and runs fine.
- You're able to run Docker containers on your computer.
- You have some necessary tools: a REST client (e.g. Postman, Swagger, curl, wget) and a database client.
- You're familiar with the project structure.

Notes:
- This project expects a PostgreSQL database listening on port 5432 and a spring boot application on 8080. there is a docker-compose.yml that you should use to run this application. To run it, simply execute: `docker-compose up --build`
- Please make sure that you have installed and properly configured Lombok ("Enable Annotation Processing" option in IntelliJ IDEA). In case of any issues please see: https://www.baeldung.com/lombok-ide
- If you're using Windows, we advise you to get VirtualBox with Linux in order to run Docker natively.