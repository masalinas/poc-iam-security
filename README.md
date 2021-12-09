# Description
Poc Keycloak Authentication/Authorization SpringBoot Microservice

# Keycloak resources: realm, client, roles and users

## Create realm

- **Name**: poc

![Create Realm](captures/keycloak_realm.png "Create Realm")

## Create client

- **Name**: admin-api
- **Root URL**: http://localhost:8081 (This is the domain where security microservice is running)

![Create Client](captures/keycloak_client.png "Create Client")

## Create client roles
We must create each roles inside all microservices. These are all common roles inside each client:

- **name**: admin, operator, user

![Client Roles](captures/client_roles.png "Client Roles")

## Create realm roles (composite roles)
We are going to group all common client roles inside **realm composite roles** to assign then to users easily.

The roles created are:

- **name**: **app-admin** include all **admin** client roles
- **name**: **app-operator** include all **operator** client roles
- **name**: **app-user** include all **user** client roles

For the **app-admin** composite role inside **poc** realm

![Realm Roles](captures/realm_roles.png "Realm Roles")

## Create users

The users created are:

- **name**: **admin** user attached to realm role **app-admin** and **all roles** in **realm-management** client to access keycloak admin api (user/role management)
- **name**: **operator** user attached to realm role **app-operator**
- **name**: **user** user attached to realm role **app-user**

The **admin** user with **app-admin** realm role and all roles from **realm-management** client role also attached

![Realm Manager Roles](captures/roles_real-manager.png "Realm Manager Roles")

The **user** user has **app-user** realm role only

![Poc Roles](captures/roles_poc.png "Poc Roles")

## Client configuration
We could obtain the client configuration from client to used inside spring boot microservice

![Client Configuration](captures/client_config.png "Client Configuration")

## Test Login with curl
```shell
curl -d 'client_id=admin-cli' -d 'username=<USERNAME>' -d 'password=<PASSWORD>' -d 'grant_type=password' 'http://localhost:8080/auth/realms/<CLIENT_ID>/protocol/openid-connect/token' | python -m json.tool
```

## Test Login with postman
Postman login inside test realm with the credentials user/user

![Login Postman Test](captures/postman_login.png "Login Postman Test")

## Test Refresh Token with postman
Postman refresh token test realm with from the old refresh token

![Refresh Token Postman Test](captures/postman_refresh.png "Refresh Token Postman Test")

## Test Logout with curl
```shell
curl -d 'client_id=admin-cli' -d 'refresh_token=<REFRESH_TOKEN>' 'http://localhost:8080/auth/realms/<CLIENT_ID>/protocol/openid-connect/logout'
```

## Test Logout with Postman
Postman login inside test realm with the credentials user/user

![Logout Postman Test](captures/postman_logout.png "Logout Postman Test")

## Test Get clients from realm with Postman
This endpoint it's securized with **access token** and **admin, operator** role

![Clients Postman Test](captures/postman_clients.png "Clients Postman Test")

## Test Get all users from realm with Postman
This endpoint it's securized with **access token** and **admin, operator** role

![Users Postman Test](captures/postman_users.png "Users Postman Test")

## Test Get a user from realm with Postman
This endpoint it's securized with **access token** and **admin** role

![User Postman Test](captures/postman_user.png "User Postman Test")

## Test Get roles By user from realm with Postman
This endpoint it's securized with **access token** and **admin, operator** role

![User Roles Postman Test](captures/postman_roles.png "User Roles Postman Test")

## Test Create user from realm with Postman
This endpoint it's securized with **access token** and **admin** role

![Create User Postman Test](captures/postman_create_users.png "Create User Postman Test")

## Test Update user from realm with Postman
This endpoint it's securized with **access token** and **admin** role

![Update User Postman Test](captures/postman_update_user.png "Update User Postman Test")

## Test Remove user from realm with Postman
This endpoint it's securized with **access token** and **admin** role

![Delete User Postman Test](captures/postman_delete_user.png "Delete User Postman Test")