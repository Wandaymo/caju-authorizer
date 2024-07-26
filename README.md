# Caju Authorizer

API REST para a realização de transações.

## Tecnologias
* Spring Boot
* Hibernate
* Swagger Open API
* H2 (Para facilitar os testes)

## Rodando o projeto
De forma a facilitar os testes, criei um Dockerfile simples na raiz, basta fazer o build da imagem e rodar o container.

```sh
$ ./mvnw package -DskipTests
$ docker build -t wandaymo/caju-authorizer .
$ docker run -p 8080:8080 wandaymo/caju-authorizer  
```

Para realizar requisições é necessário autenticar-se usado um token JWT. Já existe um usuário cadastrado para que você
possa usá-lo e assim obter o token JWT(username=admin, password=admin) ou você pode criar outro usuário acessando
http://localhost:8080/v1/user

Tudo está documento no Swagger!


Além disso pode ser feito o build do projeto e rodar com `java -jar`, ou por fim importar com uma IDE.

### Teste da API
É possível acessar o swagger-ui no seguinte endpoint http://127.0.0.1:8080/swagger-ui.html#
