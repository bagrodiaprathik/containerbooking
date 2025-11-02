# 🚀 Automated Azure Deployment Guide (CI/CD)

This comprehensive guide outlines the steps to automatically deploy your containerized application (the **maersk-booking-api**, **MongoDB**, and **Keycloak**) to Microsoft Azure using GitHub Actions.

> **💡 Bonus Points**: This plan fulfills the "Bonus Points" requirement for a fully automated CI/CD pipeline.

## 📋 The Strategy: From Local to Cloud

We cannot simply "run" `docker-compose` in production. Instead, we will leverage dedicated, managed Azure services for each component of your stack:

### 🔄 Service Mapping

| **Local Component** | **Azure Service** | **Description** |
|---------------------|-------------------|-----------------|
| `maersk-mongo` container | **Azure Cosmos DB** (MongoDB API) | Fully managed, globally-scalable, serverless database |
| `Dockerfile` | **Azure Container Registry** (ACR) | Private Docker registry for your images |
| App & Keycloak containers | **Azure Container Apps** (ACA) | Microservices platform with automatic networking |

### ✨ Benefits

- **🔗 Seamless Integration**: Your Spring Boot app won't know the difference
- **🔐 Private Registry**: Your own secure Docker Hub alternative
- **🌐 Auto-Networking**: Services automatically discover each other
- **📡 Public URLs**: Automatic external access configuration
- **⚡ Auto-Deployment**: GitHub Actions handles everything automatically

---

## 🏗️ Part 1: One-Time Azure Setup (Manual)

> **⚠️ Important**: You only need to perform these steps once before automation kicks in.

### 1️⃣ Install Azure CLI

Download and install the [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli).

### 2️⃣ Authentication

```bash
az login
```

### 3️⃣ Create Resource Group

Create a logical container for all your project resources:

```bash
az group create \
  --name maersk-booking-rg \
  --location "East US"
```

### 4️⃣ Create Azure Container Registry (ACR)

Set up your private Docker registry:

```bash
az acr create \
  --resource-group maersk-booking-rg \
  --name "maerskacr${RANDOM}" \
  --sku Basic \
  --admin-enabled true
```

> **📝 Note**: ACR names must be globally unique. We use `$RANDOM` to ensure uniqueness.

### 5️⃣ Create Azure Cosmos DB (MongoDB API)

Set up your managed database:

```bash
# Create the Cosmos DB account
az cosmosdb create \
  --name "maersk-cosmosdb-${RANDOM}" \
  --resource-group maersk-booking-rg \
  --kind MongoDB

# Retrieve your connection string (IMPORTANT!)
az cosmosdb keys list \
  --name "maersk-cosmosdb-<YOUR_UNIQUE_NAME>" \
  --resource-group maersk-booking-rg \
  --type connection-strings
```

> **🔑 Critical**: Copy the **"Primary Mongo DB Connection String"**. It will look like:
> ```
> mongodb://maersk-cosmosdb-xxxxx:xxxxx@maersk-cosmosdb-xxxxx.mongo.cosmos.azure.com:10255/?ssl=true&replicaSet=globaldb&maxIdleTimeMS=120000&appName=@maersk-cosmosdb-xxxxx@
> ```

### 6️⃣ Create Container App Environment

Set up the runtime environment for your containers:

```bash
az containerapp env create \
  --name maersk-env \
  --resource-group maersk-booking-rg \
  --location "East US"
```

---

## 🔐 Part 2: Connect GitHub to Azure (Manual)

Grant GitHub Actions permission to deploy to your Azure subscription.

### 1️⃣ Create Service Principal

Create a "robot" account for GitHub automation:

```bash
# Replace <subscription-id> with your actual Azure subscription ID
az ad sp create-for-rbac \
  --name "github-actions-maersk" \
  --role "Contributor" \
  --scopes "/subscriptions/<subscription-id>/resourceGroups/maersk-booking-rg" \
  --json-auth
```

### 2️⃣ Copy JSON Output

The command will output a JSON object similar to:

```json
{
  "clientId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
  "clientSecret": "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
  "subscriptionId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
  "tenantId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
}
```

**📋 Copy this entire JSON object.**

### 3️⃣ Configure GitHub Secrets

1. Navigate to your GitHub repository
2. Go to **Settings** → **Secrets and variables** → **Actions**
3. Create the following repository secrets:

| **Secret Name** | **Value** | **Description** |
|----------------|-----------|-----------------|
| `AZURE_CREDENTIALS` | Entire JSON object from Step 2 | Azure authentication |
| `ACR_LOGIN_SERVER` | `maerskacr12345.azurecr.io` | Your ACR server URL |
| `COSMOS_DB_CONNECTION_STRING` | MongoDB connection string | Database connection |
| `RESOURCE_GROUP_NAME` | `maersk-booking-rg` | Azure resource group |
| `CONTAINER_APP_ENV_NAME` | `maersk-env` | Container environment name |

> **💡 Tip**: You can find your ACR login server in the Azure portal under your Container Registry overview.

---

## ⚙️ Part 3: The Automation Script

🎉 **You're all set!** Now you just need to add the CI/CD pipeline to your repository.

### 📁 File Structure

Create this file at the exact path: `.github/workflows/deploy-to-azure.yml`

```
your-repository/
├── .github/
│   └── workflows/
│       └── deploy-to-azure.yml  ← Create this file
├── src/
├── Dockerfile
└── README.md
```

### 🚀 How It Works

Once you create the workflow file:

1. **🔄 Automatic Trigger**: GitHub detects pushes to the `main` branch
2. **🏗️ Build Process**: Builds your Docker image from the Dockerfile
3. **📦 Image Storage**: Pushes the image to Azure Container Registry
4. **🚀 Deployment**: Updates Azure Container Apps with the new version
5. **✅ Success**: Your application is live with the latest changes!

---

## 🎯 Next Steps

1. **✅ Complete Parts 1-2** (one-time setup)
2. **📝 Create the workflow file** (see Part 3)
3. **🚀 Push to main branch** and watch the magic happen!
4. **🌐 Access your application** via the Azure Container Apps URL

---

## 🆘 Troubleshooting

- **🔍 Check GitHub Actions logs** for deployment issues
- **📊 Monitor Azure Portal** for resource health
- **🔑 Verify all secrets** are correctly configured
- **🌐 Ensure ACR name uniqueness** if creation fails

---

**🎉 Congratulations!** You now have a fully automated CI/CD pipeline from GitHub to Azure! 🚀