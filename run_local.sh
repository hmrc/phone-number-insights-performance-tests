# remove any existing Postgres container and start a new one
docker rm -f phone-number-insights-postgres
docker run --name phone-number-insights-postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=phonenumberinsights \
  -d -p 5432:5432 postgres

# Stop any existing LocalStack instance and start a new one
export set GATEWAY_LISTEN=0.0.0.0:54566
localstack stop
localstack start -d
localstack wait

# Create the secrets in LocalStack Secrets Manager
aws secretsmanager --endpoint-url=http://localhost:54566 create-secret \
  --name rds/paas-secret-phone-number-insights/root \
  --secret-string "{\"username\": \"postgres\", \"password\": \"postgres\"}"

aws secretsmanager --endpoint-url=http://localhost:54566 create-secret \
  --name paas-secret/cip-phone-number-insights/phone-number-insights-basic-auth-token \
  --secret-string "{\"basicAuthToken\": \"validUser:validPassword\"}"

# Run the Play application with evolutions enabled and pointing to LocalStack Secrets Manager
sbt "run 9978 \
 -DsecretsManager.local.enabled=true \
 -DsecretsManager.local.endpoint=http://localhost:54566 \
 -Dplay.evolutions.enabled=true \
"