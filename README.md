# Autorizador de transações

## Tecnologias usadas

Para a criação desse sistema, foram usados:
- Kotlin
- Docker
- LocalStack - para simular o ambiente da nuvem
  - SQS
  - Redis
  - Postgres


## Lógica do sistema

O sistema do autorizador de transações foi pensado conforme o desenho abaixo

![desenho_desafio(1)](https://github.com/user-attachments/assets/6fd8f5bc-f50b-448d-bd7b-418acd6d29f1)



A partir de um sistema que gere uma transação, supondo uma maquininha de cartão, uma API POST recebe uma requisição.

Após essa requisição são feitas algumas validações verificando tanto os dados enviados quanto o saldo do cliente. A aplicação verifica se há informações do cliente nos dados em cache, se sim, segue com a validação a partir daí.
Do contrário, a aplicação verifica no banco de dados.

Em um caso de transação autorizada, os dados são enviados a uma aplicação que fará o processamento das informações  e persistindo no banco de dados e atualizando a carteira do cliente.

## Como executar
Através da pasta raiz, execute:

`docker-compose up --build`

Para gerar uma transação

curl --request POST \
  --url http://localhost:8080/api/v1/transactions \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/9.3.3' \
  --data '{
	"account": "1",
	"totalAmount": 0.10,
	"mcc": "5811",
	"merchant": "PADARIA DO ZE               SAO PAULO BR"
}'

Por padrão, assim que a aplicação subir, um usuário com 3 carteiras é criado.

Para visualizar os valores da carteira

curl --request GET \
--url http://localhost:8080/api/v1/fundsAccount/1 \
