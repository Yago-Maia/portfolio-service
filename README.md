# Micro serviço de carteira de ações
## Projeto Integrado PUC Minas

## Imagem docker
Caso queira rodar a aplicação, ela está disponível em meu perfil do docker. Segue comando para baixá-la e executar.
```bash
docker pull yagogmaia/portfolio
docker run yagogmaia/portfolio
```

# Clonar e rodar projeto via maven
## Clonar o projeto:
Acesse o diretório desejado para alocar o projeto pelo terminal e execute o comando:
```bash
git clone https://github.com/Yago-Maia/portfolio.git
```

## Instalação do Maven:
Acesse o diretório pelo terminal e instale o Maven junto com as dependências necessárias para o projeto.
```bash
mvn install
```

## Executar a aplicação:
Ainda no terminal, executar o comando no diretório raiz do projeto.
```bash
mvn spring-boot:run
```

## Postman:
Segue na raiz do diretório uma coleção chamada 'postman_collection.json' para utilização das API's.
Foram criadas duas pastas no postman, cada uma para um controller diferente, de Portfolio (carteira) e Assets (ações).
Segue nome dos Endpoint's, suas descrições e permissões:

### Token de acesso
Para acessar alguns endpoint's, é necessário criar um usuário no micro serviço de autenticação e utilizar o token jwt e, em alguns endpoint's, é necessário possuir permissão de administrador, confome descrito abaixo.

## Assets:

### Reload assets
Esse serviço faz comunicação com um serviço de ações. Para atualizar as ações do serviço, é necessário realizar uma chamada get nesse outro serviço. Não é necessário estar autenticado.
```bash
curl --location --request GET 'http://ec2-18-116-63-102.us-east-2.compute.amazonaws.com:8080/reloadAssets'
```

### Get all asset
Recupar todas as ações registradas em nosso banco de dados. Acesso liberado para todos.
```bash
curl --location --request GET 'http://ec2-18-116-63-102.us-east-2.compute.amazonaws.com:8080/asset' \
--header 'Authorization: Bearer {token}}'
```

### Get asset by id
Recupera uma ação de acordo com o id dela. Acesso liberado para todos.
```bash
curl --location --request GET 'ec2-18-116-63-102.us-east-2.compute.amazonaws.com:8080/asset/1'
```

### Get asset by code
Recupera uma ação de acordo com o código dela. Acesso liberado para todos.
```bash
curl --location --request GET 'ec2-18-116-63-102.us-east-2.compute.amazonaws.com:8080/asset/SMTO3'
```

### Insert asset
Insere uma ação no banco. Necessário envio do token contendo role ADMINISTRATOR.
```bash
curl --location --request POST 'ec2-18-116-63-102.us-east-2.compute.amazonaws.com:8080/asset' \
--header 'Authorization: {token}' \
--header 'Content-Type: application/json' \
--data-raw '{
    "code": "VALE3",
    "name": "Vale S.A."
}'
```

### Update asset
Atualiza uma ação no banco. Necessário envio do token contendo role ADMINISTRATOR.
```bash
curl --location --request PUT 'ec2-18-116-63-102.us-east-2.compute.amazonaws.com:8080/asset' \
--header 'Authorization: {token}' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": 1,
    "code": "VALE3",
    "name": "Vale S.A."
}'
```

### Delete asset
Deleta uma ação no banco. Necessário envio do token contendo role ADMINISTRATOR.
```bash
curl --location --request DELETE 'ec2-18-116-63-102.us-east-2.compute.amazonaws.com:8080/asset/1' \
--header 'Authorization: {token}'
```

## Portfolio

### Get all portfolios
Recupera todos as carteira registradas me nosso banco. Necessário envio do token contendo role ADMINISTRATOR.
```bash
curl --location --request GET 'http://ec2-18-116-63-102.us-east-2.compute.amazonaws.com:8080/portfolio' \
--header 'Authorization: Bearer {token}}'
```

### Get portfolio
Recupera uma carteira de acordo com seu id. Necessário enviar o token de autenticação e ser o dono da carteira.
```bash
curl --location --request GET 'ec2-18-116-63-102.us-east-2.compute.amazonaws.com:8080/portfolio/2' \
--header 'Authorization: {token}'
```

### Insert portfolio
Cria uma carteira. Necessário enviar o token de autenticação e ser o dono da carteira.
```bash
curl --location --request POST 'ec2-18-116-63-102.us-east-2.compute.amazonaws.com:8080/portfolio' \
--header 'Authorization: {token}}' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Fiquei rico cedo"
}'
```

### Update portfolio
Atualiza uma carteira. Necessário enviar token de autenticação e ser o dono da carteira.
```bash
curl --location --request PUT 'http://ec2-18-116-63-102.us-east-2.compute.amazonaws.com:8080/portfolio' \
--header 'Authorization: {token}' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": 1,
    "name": "Fiquei rico cedo"
}'
```

### Delete portfolio
Deleta a carteira. Necessário enviar token de autenticação e ser o dono da carteira.
```bash
curl --location --request DELETE 'http://ec2-18-116-63-102.us-east-2.compute.amazonaws.com:8080/portfolio/1' \
--header 'Authorization: {token}'
```

### Insert asset in portfolio
Inserir ação em uma carteira. Necessário enviar token de autenticação e ser o dono da carteira.
```bash
curl --location --request POST 'ec2-18-116-63-102.us-east-2.compute.amazonaws.com:8080/portfolio/addAsset' \
--header 'Authorization: {token}' \
--header 'Content-Type: application/json' \
--data-raw '{
    "idPortfolio": 3,
    "codeAsset": "SMTO3"
}'
```

### Delete asset in portfolio
Deletar uma ação da carteira. Necessário enviar token de autenticação e ser o dono da carteira.
```bash
curl --location --request DELETE 'http://ec2-18-116-63-102.us-east-2.compute.amazonaws.com:8080/portfolio/3/asset/TOTS3' \
--header 'Authorization: {token}'
```

## Swagger:
Para acessar o Swagger é necessário acessar o link abaixo com o projeto em execução:
```bash
http://ec2-18-116-63-102.us-east-2.compute.amazonaws.com:8080/swagger-ui/
```
