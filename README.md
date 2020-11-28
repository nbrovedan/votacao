# Votação

### Installation

Votação requer os seguintes programas:
* [Docker](https://docs.docker.com/desktop/): v19.03 ou superior
* [Git](https://git-scm.com/downloads/): v2.0 ou superior

Baixar o repositório:
```sh
$ git clone https://github.com/nbrovedan/votacao.git
```
Subir os serviços
```sh
$ docker-compose up --build -d
```
Após terminar de subir, os serviços estarão acessíveis nos seguintes endereços:
* [Backend](http://localhost:15125/swagger-ui/index.html)
* [rabbitMQ](http://localhost:15123/)
* [Banco de dados](http://localhost:15124)
