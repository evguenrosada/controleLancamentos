# Controle de Lançamentos

Solução do desafio de arquitetura com três projetos:

| Projeto | Porta | Schema PostgreSQL | Descrição |
|---------|-------|-------------------|-----------|
| `lancamentos-api` | 8081 | `lancamentos` | Auth JWT, CRUD de lançamentos, publicação Redis |
| `saldos-api` | 8082 | `saldos` | Consolidação diária assíncrona, consulta de saldos |
| `lancamentos-web` | 5173 | — | React SPA (login, lançamentos, saldos) |

## Pré-requisitos

- Java 17+
- Maven 3.9+
- Node.js 20+
- PostgreSQL local (`postgresql://postgres:postgres@localhost:5432/pontuacao_servicos`) 
- Redis local (`localhost:6379`)

## Subir infraestrutura

```bash
# PostgreSQL e Redis (se ainda não estiverem rodando)
docker start pontuacao-postgres 2>/dev/null || true
docker run -d --name redis-lancamentos -p 6379:6379 redis:7-alpine 2>/dev/null || docker start redis-lancamentos
```

## lancamentos-api

```bash
cd lancamentos-api
mvn spring-boot:run
```

Endpoints principais:
- `POST /api/auth/register` — cadastro de comerciante
- `POST /api/auth/login` — login (JWT)
- `GET /api/lancamentos` — listar lançamentos do comerciante autenticado
- `POST /api/lancamentos` — registrar débito/crédito

## saldos-api

```bash
cd saldos-api
mvn spring-boot:run
```

- Consome fila Redis `lancamentos:events`
- `GET /api/saldos` — relatório de saldo diário consolidado

## lancamentos-web

```bash
cd lancamentos-web
npm install
npm run dev
```

Acesse http://localhost:5173

## Testes unitários

```bash
cd lancamentos-api && mvn test
cd ../saldos-api && mvn test
```

## Padrão Command

Cada operação de negócio é um **Command** + **CommandHandler**, orquestrado por um **CommandBus**:

- `RegisterComercianteCommand`, `LoginCommand`
- `RegistrarLancamentoCommand`, `ListarLancamentosCommand`
- `ConsolidarLancamentoCommand`, `ListarSaldosDiariosCommand`

## Documentação

Ver `RequisitosObrigatorios.MD` para arquitetura alvo e requisitos funcionais/não funcionais.
