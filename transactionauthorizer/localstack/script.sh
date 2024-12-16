#!/bin/sh

docker container prune -y


docker-compose up &


until curl -s http://localhost:4566/_localstack/health | jq -e '.services.sqs == "available"' > /dev/null; do
  echo "Esperando LocalStack iniciar..."
  sleep 5
done

echo " LocalStack iniciado..."



aws sqs create-queue --endpoint-url http://localhost:4566 --queue-name transaction-queue #--profile localstack

#aws sqs list-queues

#aws sqs send-message --endpoint-url http://localhost:4566 --queue-url http://localhost:4566/000000000000/user-queue --message-body "Mensagem de Teste" --message-attributes file://message.json --profile localstack
#
#aws sqs send-message --endpoint-url http://localstack:4566 --queue-url http://localhost:4566/000000000000/user-queue --message-body "Mensagem de Teste" --message-attributes file://message.json
#
#http://sqs.sa-east-1.localhost.localstack.cloud:4566/000000000000/user-queue"