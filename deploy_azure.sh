#!/bin/bash

# Nome do grupo de recursos onde tudo será criado.
RESOURCE_GROUP="rg-geomottu-app"

# A localização do data center da Azure.
LOCATION="brazilsouth"

# O nome para o servidor de banco de dados MySQL.
DB_SERVER_NAME="mysql-geomottu-app"

# O nome para o banco de dados dentro do servidor.
DB_NAME="geomottu_db"

# Credenciais de administrador para o banco de dados.
DB_ADMIN_USER="mottuadmin"
DB_ADMIN_PASSWORD="GeoMottu5892!"

# O nome para o Serviço de Aplicativo (Web App).
WEB_APP_NAME="webapp-geomottu-app"

# O nome do plano de serviço de aplicativo.
APP_SERVICE_PLAN="plan-geomottu-app"

echo "### Iniciando o deploy do GeoMottu na Azure ###"

# --- 1. Criação do Grupo de Recursos ---
echo "--> Criando Grupo de Recursos: $RESOURCE_GROUP..."
az group create --name $RESOURCE_GROUP --location $LOCATION

# --- 2. Criação do Servidor de Banco de Dados MySQL ---
echo "--> Criando Servidor MySQL: $DB_SERVER_NAME..."
az mysql flexible-server create \
    --name $DB_SERVER_NAME \
    --resource-group $RESOURCE_GROUP \
    --location $LOCATION \
    --admin-user $DB_ADMIN_USER \
    --admin-password "$DB_ADMIN_PASSWORD" \
    --sku-name B_Standard_B1ms \
    --tier Burstable \
    --public-access 0.0.0.0 \
    --storage-size 32 \
    --version 8.0

# --- 3. Criação do Banco de Dados ---
echo "--> Criando Banco de Dados: $DB_NAME..."
az mysql flexible-server db create \
    --resource-group $RESOURCE_GROUP \
    --server-name $DB_SERVER_NAME \
    --database-name $DB_NAME

# --- 4. Criação do Plano do Serviço de Aplicativo ---
echo "--> Criando Plano do App Service: $APP_SERVICE_PLAN..."
az appservice plan create \
    --name $APP_SERVICE_PLAN \
    --resource-group $RESOURCE_GROUP \
    --location $LOCATION \
    --is-linux \
    --sku F1 # Plano Gratuito

# --- 5. Criação do Serviço de Aplicativo (Web App) ---
echo "--> Criando Web App: $WEB_APP_NAME..."
az webapp create \
    --name $WEB_APP_NAME \
    --plan $APP_SERVICE_PLAN \
    --resource-group $RESOURCE_GROUP \
    --runtime "JAVA:21:JavaSE"

# --- 6. Configuração da Conexão com o Banco de Dados ---
echo "--> Configurando a conexão do Web App com o Banco de Dados..."
JDBC_URL=$(az mysql flexible-server show --name $DB_SERVER_NAME --resource-group $RESOURCE_GROUP --query "connectionString" -o tsv)
JDBC_URL="${JDBC_URL}${DB_NAME}?useSSL=true&requireSSL=false"

az webapp config appsettings set \
    --name $WEB_APP_NAME \
    --resource-group $RESOURCE_GROUP \
    --settings \
    SPRING_DATASOURCE_URL="$JDBC_URL" \
    SPRING_DATASOURCE_USERNAME="${DB_ADMIN_USER}@${DB_SERVER_NAME}" \
    SPRING_DATASOURCE_PASSWORD="$DB_ADMIN_PASSWORD" \
    SPRING_JPA_HIBERNATE_DDL_AUTO="validate"

# --- 7. Build e Deploy da Aplicação ---
echo "--> Compilando o projeto Java..."
mvn clean package -DskipTests

echo "--> Fazendo o deploy do arquivo .jar para o Web App..."
az webapp deploy \
    --name $WEB_APP_NAME \
    --resource-group $RESOURCE_GROUP \
    --type jar \
    --src-path "target/api-0.0.1-SNAPSHOT.jar"

echo "### Deploy concluído com sucesso! ###"
echo "Acesse sua aplicação em: http://${WEB_APP_NAME}.azurewebsites.net"