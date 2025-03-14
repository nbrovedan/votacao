# Votação

### Instalação

##### Clonar o repositório a partir do git:
Requisito:
* [Git](https://git-scm.com/downloads/): v2.0 ou superior
```sh
$ git clone https://github.com/nbrovedan/votacao.git
```
#### Iniciar os serviços
##### Utilizando Docker:

Requisitos:
* [Docker](https://docs.docker.com/desktop/): v19.03 ou superior
```sh
$ docker-compose up --build -d
```
##### Utilizando Maven:

Requisitos:
* [Maven](https://maven.apache.org/download.cgi): v3.6.1 ou superior
* [Java](https://jdk.java.net/java-se-ri/11): v11
* [MariaDB](https://downloads.mariadb.org/mariadb/): v10.2
* [RabbitMQ](https://www.rabbitmq.com/download.html): v3.7.17 ou superior
q
> Ajustar as configurações em application-local.yml
```sh
$ mvn clean install
$ mvn spring-boot:run
```

Após terminar de subir, os serviços estarão acessíveis nos seguintes endereços:
* [Backend](http://localhost:15125/swagger-ui.html)
* [RabbitMQ](http://localhost:15123/)
* [Banco de dados](http://localhost:15124)
