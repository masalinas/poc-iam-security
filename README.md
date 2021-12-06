# Description
Poc Keycloak Authentication/Authorization SpringBoot Microservice

# Keycloak resources: realm, client, roles and users

## Create realm

- **Name**: poc

![Create Realm](captures/keycloak_realm.png "Create Realm")

## Create client

- **Name**: test
- **Root URL**: http://localhost:8081 (This is the domain where microservice is running)

![Create Client](captures/keycloak_client.png "Create Client")

## Create client roles
Inside the client we created these roles:

- **name**: admin, operator, user

![Create Roles](captures/client_roles.png "Client Roles")

## Create users
- **name**: admin with roles: **admin** in **poc** client and **all roles** in **realm-management** client
- **name**: operator with role: **operator** in **poc** client
- **name**: user with role: **user** in **poc** client

![User Roles](captures/user_roles.png "User Roles")

The **admin** user has **admin** rol in **poc** client and **all roles** from **realm-manager** client

![Realm Manager Roles](captures/roles_real-manager.png "Realm Manager Roles")

![Poc Roles](captures/roles_poc.png "Poc Roles")


## Client configuration
We could obtain the client configuration from client to used insides springboot microserice

![Client Configuration](captures/client_config.png "Client Configuration")

## Test Login with curl
```shell
curl -d 'client_id=admin-cli' -d 'username=<USERNAME>' -d 'password=<PASSWORD>' -d 'grant_type=password' 'http://localhost:8080/auth/realms/<CLIENT_ID>/protocol/openid-connect/token' | python -m json.tool
```

## Test Login with postman
Postman login inside test realm with the credentials user/user

![Login Postman Test](captures/postman_login.png "Login Postman Test")

## Test Logout with curl
```shell
curl -d 'client_id=admin-cli' -d 'refresh_token=<REFRESH_TOKEN>' 'http://localhost:8080/auth/realms/<CLIENT_ID>/protocol/openid-connect/logout'
```

## Test Logout with Postman
Postman login inside test realm with the credentials user/user

![Logout Postman Test](captures/postman_logout.png "Logout Postman Test")

## Test Get Users from realm with Postman

![Users Postman Test](captures/postman_users.png "Users Postman Test")

## Test Create User from realm with Postman

![Create User Postman Test](captures/postman_create_users.png "Create User Postman Test")

## Test Update User from realm with Postman

![Update User Postman Test](captures/postman_update_user.png "Update User Postman Test")

## Test Delete User from realm with Postman

![Delete User Postman Test](captures/postman_delete_user.png "Delete User Postman Test")

## Test get clients from realm with Postman

![Clients Postman Test](captures/postman_clients.png "Clients Postman Test")