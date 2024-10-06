# Using DynamoDB on Spring Boot

1. DynamoDB Localのコンテナを起動

## 起動手順

```bash
docker compose up
```

2. Spring Boot起動

```bash
make start
```

3. GUIでDynamoDB Localを開く
>http://localhost:8001


## エンドポイント

- [POST] http://localhost:8080/api/customers 顧客を登録
- [GET] http://localhost:8080/api/customers/{id} idから顧客を取得
- [GET] http://localhost:8080/api/customers/email/{email} email(GSI)から顧客を取得
- [GET] http://localhost:8080/api/customers すべての顧客を取得
- [PUT] http://localhost:8080/api/customers/{id} 顧客の情報を更新
- [DELETE] http://localhost:8080/api/customers/{id} 顧客を削除

