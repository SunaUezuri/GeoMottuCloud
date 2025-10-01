# GeoMottu - Sistema de Gerenciamento de Frotas 🛵

![Status: Concluído](https://img.shields.io/badge/status-concluído-green)
![Java](https://img.shields.io/badge/Java-21-blue?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?logo=spring)
![Azure SQL](https://img.shields.io/badge/Azure-SQL-blue?logo=azuresqldatabase)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Frontend-green?logo=thymeleaf)



O **GeoMottu** é uma aplicação web completa (full-stack) desenvolvida para simular um sistema robusto de gerenciamento de frotas de motocicletas. Seu principal objetivo é atender às necessidades da empresa Mottu, oferecendo uma plataforma centralizada para o controle logístico e operacional de sua frota. 

A aplicação é construída em `Java 21` utilizando o framework `Spring Boot 3`, com uma interface de usuário renderizada no servidor via `Thymeleaf`. Para persistência de dados, emprega Spring Data JPA e `Azure SQL Database`, com migrações de schema gerenciadas por `Flyway`. A segurança é um pilar central, implementada com `Spring Security` para autenticação e autorização baseadas em perfis (ADMIN e USER). 

---

## Benefícios

* **Centralização da Informação:** Consolida dados cruciais de filiais, pátios e frotas em uma única plataforma, proporcionando uma visão unificada e em tempo real da operação. 

* **Controle Eficiente de Inventário:** Permite o rastreamento preciso de cada motocicleta, seu status operacional (Livre, Alugada, Manutenção) e localização (pátio atual), otimizando a alocação de recursos e a programação de manutenções. 

* **Segurança e Segregação de Acesso:** Implementação robusta de Spring Security, garantindo que cada usuário acesse apenas as informações e funcionalidades pertinentes ao seu perfil e filial, protegendo dados sensíveis e prevenindo acessos indevidos. 

* **Tomada de Decisão Baseada em Dados:** O dashboard administrativo fornece insights rápidos e visualmente intuitivos sobre a saúde da frota, como a distribuição de motos por status, tipo e a taxa de ocupação dos pátios, auxiliando gestores na tomada de decisões estratégicas. 

* **Escalabilidade e Robustez na Nuvem:** A implantação na Azure, utilizando App Services e Azure SQL Database (Serverless), garante que a aplicação seja escalável e resiliente, pronta para crescer com a demanda da Mottu, com otimização de custos em períodos de baixa utilização. 

---

## 🧑‍💻 Autores

<div align="center">

| Nome | RM |
| :--- | :--- |
| **Wesley Sena dos Santos** | 558043 |
| **Vanessa Yukari Iwamoto** | 558092 |
| **Samara Victoria Ferraz dos Santos** | 558719 |

</div>

---

## 📋 Índice

1.  [Visão Geral e Objetivo do Projeto](#-visão-geral-e-objetivo-do-projeto)
2.  [Funcionalidades Principais](#-funcionalidades-principais)
3.  [Arquitetura do Sistema](#️-arquitetura-do-sistema)
4.  [Detalhes de Segurança](#-detalhes-de-segurança)
5.  [Tecnologias Utilizadas](#️-tecnologias-utilizadas)
6.  [Como Executar Localmente](#-como-executar-localmente)
7.  [Deploy na Azure com CLI](#-deploy-na-azure-com-cli)
8.  [Credenciais de Acesso](#-credenciais-de-acesso)
9. [Próximos Passos (Roadmap)](#-próximos-passos-roadmap)
10. [Links](#-links)

---

## 🌟 Visão Geral e Objetivo do Projeto

A gestão de uma frota de veículos distribuída geograficamente apresenta desafios complexos, como controle de inventário, alocação de recursos e visibilidade operacional. O **GeoMottu** foi concebido para resolver esses desafios, oferecendo uma plataforma centralizada e segura para:

* **Gerenciar a estrutura física da empresa:** Cadastro e controle de Filiais e seus respectivos Pátios.
* **Controlar a frota de veículos:** Inventário detalhado de todas as motocicletas, incluindo status operacional (Livre, Alugada, Manutenção) e localização (pátio atual).
* **Segregar permissões:** Garantir que usuários operacionais (`USER`) possam apenas gerenciar recursos da sua própria filial, enquanto administradores (`ADMIN`) tenham visão e controle total sobre o sistema.
* **Visualizar dados operacionais:** Oferecer um Dashboard para administradores com métricas importantes sobre a saúde da frota e da operação.

---

## ✨ Funcionalidades Principais

-   [x] **Autenticação & Autorização:** Sistema de login seguro com perfis `ADMIN` e `USER`, utilizando Spring Security.
-   [x] **CRUD Completo e Seguro:** Operações de Criar, Ler, Atualizar e Deletar para todas as entidades principais:
    -   **Filiais:** Gerenciamento exclusivo para `ADMIN`.
    -   **Pátios:** `ADMIN` gerencia todos; `USER` gerencia apenas os da sua filial.
    -   **Motos:** `ADMIN` gerencia todas; `USER` gerencia apenas as da sua filial.
    -   **Usuários:** `ADMIN` pode gerenciar perfis e filiais de outros usuários.
-   [x] **Validações de Negócio:**
    -   Impedimento de cadastro de motos em pátios com capacidade esgotada.
    -   Validação de unicidade de campos críticos (placa, chassi, nome de usuário).
-   [x] **Dashboard Administrativo Interativo:**
    -   Cards com estatísticas em tempo real (total de usuários, pátios, motos).
    -   Gráficos dinâmicos (distribuição por status e por modelo).
    -   Tabela com ranking de pátios por ocupação.
-   [x] **Busca Dinâmica:** Funcionalidade de busca por placa/chassi na lista de motos e por nome na lista de usuários.

---

## 🏗️ Arquitetura do Sistema

A aplicação segue uma arquitetura em camadas (Layered Architecture), padrão em projetos Spring, para garantir a separação de responsabilidades e a manutenibilidade do código.

+-------------------+      +--------------------+      +-----------------------+
|  Frontend         |----->|  Controller Layer  |----->|  Service Layer        |
|  (Thymeleaf, JS)  |      |  (API Endpoints)   |      |  (Business Logic)     |
+-------------------+      +--------------------+      +-----------------------+
|
|
+-------------------+      +--------------------+      +----v------------------+
|  Database         |<-----|  Repository Layer  |<-----|  Domain Model         |
|  (Oracle)         |      |  (Spring Data JPA) |      |  (Entities & DTOs)    |
+-------------------+      +--------------------+      +-----------------------+

* **Controller Layer:** Recebe as requisições HTTP, valida os dados de entrada (usando DTOs) e delega a lógica de negócio para a camada de serviço.
* **Service Layer:** Contém as regras de negócio centrais, orquestra as operações e garante a segurança das transações.
* **Repository Layer:** Abstrai o acesso aos dados, utilizando Spring Data JPA para interagir com o banco de dados.
* **Domain Model:** Representa os dados da aplicação através de Entidades (mapeadas para o banco) e DTOs (Data Transfer Objects) para comunicação segura entre as camadas.

---

## 🛡️ Detalhes de Segurança

A segurança é um pilar central da aplicação, implementada com Spring Security.
* **Autenticação:** Baseada em formulário, com senhas armazenadas de forma segura usando o algoritmo **BCrypt**.
* **Autorização:** Controle de acesso baseado em perfis (`ROLE_ADMIN`, `ROLE_USER`). Endpoints e métodos de serviço são protegidos para garantir que apenas usuários autorizados possam executar operações críticas.
* **Proteção CSRF:** Todos os formulários que alteram o estado do sistema (`POST`) são protegidos contra ataques Cross-Site Request Forgery.
* **Controle de Acesso por Filial:** A lógica de serviço garante que usuários `USER` só possam visualizar e manipular dados (pátios, motos) pertencentes à sua própria filial.

---

## 🛠️ Tecnologias Utilizadas

#### **Backend**
* **Java 21**
* **Spring Boot 3.x**
* **Spring Web (MVC)**
* **Spring Data JPA**
* **Spring Security**
* **Lombok**

#### **Frontend**
* **Thymeleaf**
* **HTML5 / CSS3**
* **Bootstrap 5**
* **JavaScript (ES6)**
* **Chart.js**

#### **Banco de Dados & Ferramentas**
* **Azure SQL Database**
* **Flyway** (Versionamento de Banco de Dados)
* **Maven** (Gerenciamento de Dependências)

---

## 🚀 Como Executar Localmente

### **Pré-requisitos**
* Java JDK 21 ou superior
* Apache Maven 3.8 ou superior
* Uma instância do Oracle Database acessível
* Git

### **Configuração e Execução**
1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/GeoMottuJava.git](https://github.com/seu-usuario/GeoMottuJava.git)
    cd GeoMottuJava
    ```
2.  **Configure o Banco de Dados:**
    * Abra o arquivo `src/main/resources/application.properties`.
    * Altere as propriedades `spring.datasource.url`, `spring.datasource.username`, e `spring.datasource.password` com as credenciais do seu banco de dados Oracle.
3.  **Execute a Aplicação:**
    ```bash
    mvn spring-boot:run
    ```
4.  **Acesse:** A aplicação estará disponível em `http://localhost:8080`.

---

## ☁️ Deploy na Azure com CLI

Esta seção detalha como provisionar toda a infraestrutura na Azure e fazer o deploy da aplicação de forma automatizada usando a Azure CLI.

**Pré-requisitos (Azure)**

Antes de executar o script, você precisa preparar seu ambiente:

1. **Instalar a Azure CLI:** A Interface de Linha de Comando da Azure é a ferramenta principal para gerenciar recursos. Se você ainda não a tem, instale-a seguindo as instruções oficiais.
2. **Fazer Login na sua Conta:** Abra seu terminal bash e execute o comando abaixo. Uma janela do navegador será aberta para você se autenticar.

    ```bash
        az login
    ```

3. **Selecionar a Assinatura Correta:** Se você tiver múltiplas assinaturas (subscriptions) na sua conta, é crucial selecionar aquela onde os recursos serão criados.

   * Primeiro, liste todas as suas assinaturas para ver os nomes e IDs:
     ```bash
        az account list --output table
     ```

   * Em seguida, defina a assinatura que deseja usar:
     ```bash
        az account set --subscription "NOME_OU_ID_DA_SUA_ASSINATURA"
     ```
4. **Dar Permissão Para o Arquivo.**: Dê permissões para o arquivo `deploy_azure.sh` para poder ser executado:

    ```bash
        chmod +X deploy_azure.sh
    ```
Com o ambiente configurado, você está pronto para usar o script de deploy com o comando:.

```bash
    ./deploy_azure.sh
```

### Script de Deploy Automatizado

O script a seguir cria todos os recursos necessários (Grupo de Recursos, Banco de Dados SQL, App Service) e faz o deploy da aplicação. Salve o conteúdo abaixo em um arquivo chamado `deploy_azure.sh` na raiz do projeto.

```
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
```

#### 🔍 Entendendo o Script Passo a Passo

1. **Configurações Iniciais:** Define variáveis para os nomes dos recursos, localização e credenciais, facilitando a reutilização e manutenção.
2. `az group create`: Cria um Grupo de Recursos, que é um contêiner lógico para agrupar todos os recursos da aplicação.
3. `az sql server create`: Provisiona um servidor SQL lógico na Azure. É nele que nosso banco de dados irá residir.
4. `az sql server firewall-rule create` **(Regra 1)**: Cria uma regra de firewall no servidor SQL que permite o acesso de qualquer serviço da Azure. Isso é essencial para que o nosso Web App consiga se conectar ao banco de dados.
5. `az sql server firewall-rule create` **(Regra 2)**: Adiciona outra regra de firewall para permitir o acesso do seu endereço IP atual. Isso é útil para que você possa se conectar ao banco de dados a partir de ferramentas como DBeaver ou SSMS na sua máquina local.
6. `az sql db create`: Cria o banco de dados em si. O modo --compute-model "Serverless" é uma opção econômica que pausa o banco de dados automaticamente quando não está em uso, ideal para ambientes de desenvolvimento e aplicações com tráfego intermitente.
7. `az appservice plan create`: Cria um Plano do App Service, que define a capacidade computacional (CPU, memória) para a nossa aplicação. O SKU S1 (Standard) é uma boa escolha para produção, oferecendo recursos dedicados.
8. `az webapp create`: Cria a aplicação web (Web App) onde o nosso código Java será executado. Especificamos o runtime `JAVA|21-java21` para garantir a compatibilidade.
9. `az webapp config appsettings set`: Este é um passo crucial. Ele configura as variáveis de ambiente para a aplicação web. O Spring Boot automaticamente detecta essas variáveis (SPRING_DATASOURCE_URL, etc.) e as utiliza para configurar a conexão com o banco de dados, sobrescrevendo o `application.properties`.
10. `mvn clean package`: Executa o build do projeto Maven, compilando o código e empacotando-o em um arquivo `.jar` executável na pasta `target/`.
11. **Localização do JAR**: O script localiza o arquivo `.jar` recém-criado na pasta `target`.
12. `az webapp deploy`: O comando final que envia o arquivo `.jar` para o Azure App Service, publicando a aplicação e tornando-a acessível na web.

---

## 🔑 Credenciais de Acesso

Use os seguintes usuários (criados via migração do Flyway) para testar:

| Perfil | Usuário | Senha |
| :--- | :--- | :--- |
| **Administrador** | `admin` | `@Admin123` |
| **Usuário Comum** | `joao.silva` | `$User5432` |

---

## 🔮 Próximos Passos (Roadmap)
O projeto está pronto para ser expandido com novas funcionalidades:
-   `[ ]` **Implementar Histórico de Alterações da Moto (Auditoria)**
-   `[ ]` **Implementar Mapa Interativo de Filiais e Pátios**
-   `[ ]` **Implementar Geração de Relatórios da Frota em PDF/Excel**
-   `[ ]` **Implementar Sistema de Recuperação de Senha com Token**

---

## 🔗 Links

[![Deploy Online](https://img.shields.io/badge/🌍%20Abrir%20Aplicação-000?style=for-the-badge&logo=vercel)](https://geomottujava.onrender.com)
