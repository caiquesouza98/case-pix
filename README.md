# API PIX

## Tecnologias Usadas

- **Java 21**: Linguagem de programação principal do projeto.
- **Spring Boot**: Framework utilizado para desenvolvimento da aplicação.
- **Spring MVC**: Utilizado para criar a camada de controladores REST.
- **Spring Data JPA**: Utilizado para acesso a dados e persistência.
- **Lombok**: Biblioteca para redução de código boilerplate.
- **JUnit 5**: Framework para testes unitários.
- **Mockito**: Framework para criação de mocks em testes.
- **Gradle**: Ferramenta de automação de build e gerenciamento de dependências.
- **Docker**: Utilizado para containerização da aplicação (evidenciado pelo Dockerfile e docker-compose.yml).
- **PostgreSQL**: Provável banco de dados utilizado (inferido pela presença do JPA e docker-compose).

## Design Patterns Utilizados

- **Strategy Pattern**: Implementado no sistema de validação de chaves PIX através da interface `PixValidator` e suas implementações específicas (ValidatorCelular, ValidatorCPF, ValidatorCNPJ, ValidatorEmail, ValidatorAleatoria).

- **Repository Pattern**: Utilizado através do Spring Data JPA para abstração da camada de acesso a dados, evidenciado pela presença do `ContaPixRepository`.

- **DTO Pattern (Data Transfer Object)**: Implementado através de classes como `ContaPixCreateDTO` e `ContaPixUpdateDTO` para transferência de dados entre camadas.

- **Service Layer Pattern**: Implementado através da classe `ContaPixService` que encapsula a lógica de negócio.

- **MVC (Model-View-Controller)**: Arquitetura base do projeto, com separação clara entre modelos, controladores e serviços.

- **Dependency Injection**: Utilizado extensivamente através das anotações `@Autowired` do Spring Framework.

- **Factory Method**: Presente na estratégia de validação de chaves PIX, onde diferentes validadores são instanciados conforme o tipo de chave.

## Code Coverage

A cobertura de código pode ser observada principalmente na camada de validação, onde cada validador possui testes unitários correspondentes:

- **Validators**: Todos os validadores de chaves PIX (CPF, CNPJ, Email, Celular, Aleatória) possuem testes unitários completos.

- **Casos de Teste**: Os testes contemplam:
    - Casos válidos (retorno `true`)
    - Casos inválidos (retorno `false`)
    - Casos nulos (retorno `false`)

- **Abordagem de Testes**:
    - Uso de mocks para isolar dependências
    - Testes parametrizados para validar diferentes entradas
    - Perfil específico para testes (`@ActiveProfiles("test")`)

A estrutura de testes demonstra uma preocupação com a qualidade do código e cobertura adequada das regras de validação, elemento crítico para a segurança e confiabilidade de um sistema de PIX.

Para melhorar a cobertura de código, recomenda-se implementar testes adicionais para:
- Camada de serviço (`ContaPixService`)
- Camada de controladores (`ContaPixController`)
- Fluxos de exceção e tratamento de erros

---

Este projeto implementa uma API para gerenciamento de chaves PIX, permitindo operações como criação, atualização, consulta e inativação de chaves, com validação específica para cada tipo de chave (CPF, CNPJ, Email, Celular, Aleatória).