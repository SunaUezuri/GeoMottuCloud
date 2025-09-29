#!/bin/bash

# --- Configurações Iniciais ---
RESOURCE_GROUP="rg-geomottu-app"
LOCATION="canadacentral"
DB_SERVER_NAME="sql-geomottu-api-srv"
DB_NAME="geomottu_db"
DB_ADMIN_USER="mottuadmin"
DB_ADMIN_PASSWORD="MottuSenhaSegura2025"
WEB_APP_NAME="webapp-geomottu-api"
APP_SERVICE_PLAN="plan-geomottu-app"

echo "### Iniciando o deploy com Azure SQL Database (Serverless) ###"

echo "--> Criando Grupo de Recursos..."
az group create --name $RESOURCE_GROUP --location $LOCATION

echo "--> Criando Servidor SQL Lógico: $DB_SERVER_NAME..."
az sql server create \
    --name $DB_SERVER_NAME \
    --resource-group $RESOURCE_GROUP \
    --location $LOCATION \
    --admin-user $DB_ADMIN_USER \
    --admin-password "$DB_ADMIN_PASSWORD"

# --- CONFIGURAÇÃO DE FIREWALL SIMPLIFICADA ---
echo "--> Configurando o firewall do SQL Server..."

echo "--> 1. Permitindo que todos os serviços da Azure se conectem (incluindo nosso Web App)..."
az sql server firewall-rule create \
    --resource-group $RESOURCE_GROUP \
    --server $DB_SERVER_NAME \
    --name "AllowAllWindowsAzureIps" \
    --start-ip-address "0.0.0.0" \
    --end-ip-address "0.0.0.0"

echo "--> 2. Permitindo a conexão da sua máquina local (DBeaver)..."
MY_IP=$(curl -4 -s ifconfig.me)
az sql server firewall-rule create \
    --resource-group $RESOURCE_GROUP \
    --server $DB_SERVER_NAME \
    --name "AllowMyComputer" \
    --start-ip-address $MY_IP \
    --end-ip-address $MY_IP

echo "--> Criando Banco de Dados SQL no modo Serverless (econômico)..."
az sql db create \
    --resource-group $RESOURCE_GROUP \
    --server $DB_SERVER_NAME \
    --name $DB_NAME \
    --edition "GeneralPurpose" \
    --family "Gen5" \
    --capacity 1 \
    --compute-model "Serverless"

echo "--> Criando Plano do App Service (S1 para estabilidade)..."
az appservice plan create \
    --name $APP_SERVICE_PLAN \
    --resource-group $RESOURCE_GROUP \
    --location $LOCATION \
    --is-linux \
    --sku S1

echo "--> Criando Web App..."
az webapp create \
    --name $WEB_APP_NAME \
    --plan $APP_SERVICE_PLAN \
    --resource-group $RESOURCE_GROUP \
    --runtime "JAVA|21-java21"

echo "--> Configurando a conexão do Web App com o Banco de Dados SQL..."
JDBC_URL="jdbc:sqlserver://${DB_SERVER_NAME}.database.windows.net:1433;database=${DB_NAME};encrypt=true;trustServerCertificate=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
az webapp config appsettings set \
    --name $WEB_APP_NAME \
    --resource-group $RESOURCE_GROUP \
    --settings \
    SPRING_DATASOURCE_URL="$JDBC_URL" \
    SPRING_DATASOURCE_USERNAME="$DB_ADMIN_USER" \
    SPRING_DATASOURCE_PASSWORD="$DB_ADMIN_PASSWORD" \
    SPRING_JPA_HIBERNATE_DDL_AUTO="none"

echo "--> Compilando o projeto Java (lembre-se de ajustar o pom.xml)..."
mvn clean package -DskipTests

echo "--> Procurando arquivo .jar gerado..."
JAR_FILE=$(ls target/*.jar | head -n 1)
if [ -z "$JAR_FILE" ]; then
    echo "❌ Nenhum arquivo JAR encontrado."
    exit 1
fi
echo "--> Arquivo JAR encontrado: $JAR_FILE"

echo "--> Fazendo o deploy do arquivo .jar..."
az webapp deploy \
    --name $WEB_APP_NAME \
    --resource-group $RESOURCE_GROUP \
    --type jar \
    --src-path "$JAR_FILE"

echo "### ✅ Deploy concluído com sucesso! ###"
echo "🌐 Acesse sua aplicação em: http://${WEB_APP_NAME}.azurewebsites.net"