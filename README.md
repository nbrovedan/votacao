# Votação

### Instalação

Votação requer os seguintes programas:
* [Java](https://jdk.java.net/java-se-ri/11): v11
* [Docker](https://docs.docker.com/desktop/): v19.03 ou superior
* [Git](https://git-scm.com/downloads/): v2.0 ou superior

Baixar o repositório:
```sh
$ git clone https://github.com/nbrovedan/votacao.git
```
#### Iniciar os serviços
Utilizando Docker:
```sh
$ docker-compose up --build -d
```
Utilizando Maven:
> 1. Necessário ter o MySQL/MariaDB e o RabbitMQ instalado
> 2. Corrigir os apontamentos em application-local.yml
```sh
$ mvn clean install
$ mvn spring-boot:run
```

Após terminar de subir, os serviços estarão acessíveis nos seguintes endereços:
* [Backend](http://localhost:15125/swagger-ui.html)
* [rabbitMQ](http://localhost:15123/)
* [Banco de dados](http://localhost:15124)
