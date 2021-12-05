# Description
Poc Olive Keycloak Authentication SpringBoot Microservice

# Keycloak resources: realm, client, roles and users

## Create realm

- **Name**: poc

![Create Realm](captures/keycloak_realm.png "Create Realm")

## Create client

- **Name**: test
- **Root URL**: http://localhost:8081

![Create Client](captures/keycloak_client.png "Create Client")

## Create client roles
- **name**: admin, operator, user

![Create Roles](captures/client_roles.png "Client Roles")

## Create users
- **name**: admin with **role**: admin
- **name**: operator with **role**: operator
- **name**: user with **role**: user

![User Roles](captures/user_roles.png "User Roles")

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

## Test Logout with postman
Postman login inside test realm with the credentials user/user

![Logout Postman Test](captures/postman_logout.png "Logout Postman Test")

## Test get users from realm with postman

![Users Postman Test](captures/postman_users.png "Users Postman Test")

## Test get clients from realm with postman

![Clients Postman Test](captures/postman_clients.png "Clients Postman Test")