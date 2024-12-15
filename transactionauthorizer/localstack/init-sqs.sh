#!/bin/sh

echo "Tentei 1"

#while true; do
#  response=$(curl -s http://localhost:4566/_localstack/health)
#  echo $response
#
#  # Extrair o valor de 'sqs' usando awk
#  sqs_status=$(echo "$response" | awk -F'"sqs": "' '{print $2}' | awk -F'"' '{print $1}')
#  echo "$sqs_status"
#
#  if [ "$sqs_status" = "available" ]; then
#    echo "LocalStack está pronto!"
#    break
#  else
#    sleep 5
#  fi
#done

until curl -s http://localstack:4566/_localstack/health | grep -q '"sqs": "available"'; do
  echo "Aqui esperando LocalStack iniciar..."
  echo "http://localstack:4566/_localstack/health"
  echo $(curl http://localstack:4566/_localstack/health)
  sleep 5
done
echo "Tentei 2"
echo $(curl http://localstack:4566/_localstack/health)

set -euo pipefail
LOCALSTACK_HOST=localstack
AWS_REGION=sa-east-1

create_queue() {
  echo "tentando criar fila"
  local QUEUE_NAME_TO_CREATE=$1
  aws --endpoint-url=http://${LOCALSTACK_HOST}:4566 sqs create-queue --queue-name ${QUEUE_NAME_TO_CREATE} #--region ${AWS_REGION} --attributes VisibilityTimeout=30
  echo "criei"

}

create_queue "transaction-queue"
create_queue "transaction-queue-dlq"

#aws sqs list-queues
#aws sqs list-queues --endpoint-url=http://localhost:4566 --no-verify-ssl

#aws --endpoint-url=http://localhost:4566 sqs receive-message --queue-url=http://localhost:4566/000000000000/transaction-queue