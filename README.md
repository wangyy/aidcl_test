# 用户增删改查 Demo

这是一个前后端分离 Demo：

- 前端：Vue 3 + Vite
- 后端：Spring Boot 3 + Spring Data JPA
- 数据库：MySQL，后续可替换为火山引擎公有云 MySQL 连接地址

## 目录结构

```text
.
├── backend   # Spring Boot 用户 CRUD API
├── frontend  # Vue 用户管理页面
└── docs      # 开发计划文档
```

## 后端启动

进入后端目录：

```bash
cd backend
```

配置 MySQL 连接。你后续提供火山引擎 MySQL 地址后，替换下面的环境变量即可：

```bash
export MYSQL_URL='jdbc:mysql://你的火山引擎MySQL地址:3306/user_demo?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true'
export MYSQL_USERNAME='你的用户名'
export MYSQL_PASSWORD='你的密码'
```

启动后端：

```bash
mvn spring-boot:run
```

也可以用 IntelliJ IDEA 打开 `backend` 目录，直接运行 `UserDemoApplication`。

后端默认监听：`http://localhost:8080`

## 前端启动

进入前端目录并安装依赖：

```bash
cd frontend
npm install
npm run dev
```

前端默认监听：`http://localhost:5173`

Vite 已配置 `/api` 代理到 `http://localhost:8080`。

## Docker 打包运行

项目提供前后端分离的两个 Dockerfile：

- 后端镜像文件：`backend/Dockerfile`
- 前端镜像文件：`frontend/Dockerfile`

### 使用 Docker Compose 启动

复制环境变量模板：

```bash
cp .env.example .env
```

编辑 `.env`，填入火山引擎 MySQL 连接信息：

```bash
MYSQL_URL=jdbc:mysql://你的火山引擎MySQL地址:3306/user_demo?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
MYSQL_USERNAME=你的用户名
MYSQL_PASSWORD=你的密码
FRONTEND_ORIGIN=http://localhost:5173
```

构建并启动：

```bash
docker compose up --build
```

访问前端页面：

```text
http://localhost:5173
```

访问后端接口：

```text
http://localhost:8080/api/users
```

### 分别构建镜像

构建后端镜像：

```bash
docker build -t user-demo-backend:latest ./backend
```

构建前端镜像：

```bash
docker build -t user-demo-frontend:latest ./frontend
```

单独运行后端：

```bash
docker run --rm -p 8080:8080 \
  -e MYSQL_URL='jdbc:mysql://你的火山引擎MySQL地址:3306/user_demo?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true' \
  -e MYSQL_USERNAME='你的用户名' \
  -e MYSQL_PASSWORD='你的密码' \
  user-demo-backend:latest
```

单独运行前端时，`frontend/nginx.conf` 默认把 `/api` 代理到 Docker Compose 服务名 `backend:8080`。如果不使用 Compose，需要按你的网络环境调整代理地址。

## API 列表

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/users` | 查询用户列表 |
| GET | `/api/users/{userId}` | 查询单个用户 |
| POST | `/api/users` | 新增用户 |
| PUT | `/api/users/{userId}` | 修改用户 |
| DELETE | `/api/users/{userId}` | 删除用户 |

请求体示例：

```json
{
  "username": "zhangsan",
  "organization": "研发部"
}
```

## 数据表

后端使用 JPA 自动维护表结构，表名为 `sys_user`，字段包括：

- `user_id`：用户 ID，主键，自增
- `username`：用户名
- `organization`：用户所属机构

Demo 阶段 `spring.jpa.hibernate.ddl-auto` 默认为 `update`，便于快速开发。生产环境建议改成 Flyway/Liquibase 管理数据库变更。
