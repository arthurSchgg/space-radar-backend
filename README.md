# 🌌 Space Radar — Backend

API REST para explorar **asteroides próximos da Terra (Near-Earth Objects, NEOs)** e seus eventos de aproximação, com dados da [NASA NeoWs](https://api.nasa.gov/).

> **Escopo científico:** a API registra aproximações previstas/observadas com distância, velocidade, data UTC, diâmetro estimado e classificação de potencial perigo. **Não** fornece coordenadas 3D em tempo real, trajetórias completas na Via Láctea, nem deteções de meteoritos em solo. A interface do radar deve mostrar posições **esquemáticas** e identificá-las como ilustrativas.

## Tecnologias

- Java 21, Spring Boot 4.1.1, Spring Web MVC, RestClient, Spring Data JPA
- PostgreSQL 17 hospedado no **Neon** (produção), PostgreSQL local via Docker Compose e migrações Flyway
- Maven, Docker Compose e GitHub Actions
- NASA NeoWs como fonte externa de informações

## Arquitetura

```text
NASA NeoWs → NasaClient → AsteroidService → PostgreSQL (Flyway)
                                     ↑
                         AdminImportController
PostgreSQL → Repository → RadarService → RadarController → Frontend (HTML/CSS/JS)
```

Todos os endpoints públicos de leitura entregam **DTOs**, não entidades JPA. A importação só pode ser iniciada mediante token administrativo no cabeçalho; ela não é chamada diretamente pelo navegador.

## Executar localmente

1. Copie `.env.example` para `.env` na raiz do repositório.
2. Edite `DB_PASSWORD` com senha única e `RADAR_ADMIN_TOKEN` com um token longo gerado aleatoriamente. Configure sua chave `NASA_API_KEY` se disponível (a `DEMO_KEY` é limitada).
3. Inicie com Docker, executando **na raiz do projeto**:

```bash
docker compose --env-file .env -f docker/docker-compose.yml up --build -d
```

API Docker: `http://localhost:8181`. Saúde: `http://localhost:8181/actuator/health`. O app dentro do Docker escuta em `8888` (mapeamento `8181:8888`); PostgreSQL usa `5432` internamente e está publicado **somente no loopback do host** como `127.0.0.1:5433` para integração com o IntelliJ.

Para rodar **somente a API** localmente com JDK 21, inicialize um PostgreSQL próprio e forneça as variáveis `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, `NASA_API_KEY` e `RADAR_ADMIN_TOKEN` ao processo:

```bash
./mvnw spring-boot:run
```

Executando a aplicação **pelo IntelliJ/terminal fora do Docker**, a API usa por padrão a porta `8484`. Como o PostgreSQL do Compose é publicado em `127.0.0.1:5433`, configure `DB_HOST=localhost`, `DB_PORT=5433` e os demais `DB_*` na configuração de execução do IntelliJ. As variáveis do `.env` do Compose **não são carregadas automaticamente** pelo IntelliJ. Para não iniciar duas APIs durante a depuração, execute `docker compose --env-file .env -f docker/docker-compose.yml stop app` e mantenha o serviço `postgres` ativo. Execute `./mvnw test` para os testes unitários. Os testes de integração com PostgreSQL executam no CI com `DB_INTEGRATION_TEST=true` e um banco temporário; para rodar localmente, configure um PostgreSQL de testes separado.

## Endpoints

| Verbo | Rota | Descrição |
| --- | --- | --- |
| GET | `/v1/radar` | Eventos para o radar, filtrados e paginados |
| GET | `/v1/space-radar` | Asteroides cadastrados, com busca e filtro de risco |
| GET | `/v1/space-radar/{nasaId}` | Detalhes de um asteroide pelo ID NASA |
| GET | `/v1/close-approach` | Eventos de aproximação paginados |
| GET | `/v1/close-approach/{id}` | Evento por UUID |
| GET | `/v1/close-approach/asteroid/{asteroidId}` | Aproximações de um asteroide por UUID |
| POST | `/v1/admin/import` | Importa NASA para o banco (**token obrigatório**) |
| GET | `/actuator/health` | Saúde do serviço |

### Radar

```http
GET /v1/radar?startDate=2026-11-11&endDate=2026-11-18&hazardousOnly=false&maxDistanceKm=10000000&page=0&size=50
```

- `startDate` e `endDate` são inclusivas, formato `yyyy-MM-dd`; máximo de 365 dias.
- `hazardousOnly` é opcional, `false` por padrão.
- `maxDistanceKm` opcional, número positivo.
- `page` inicia em `0`, `size` vai de `1` a `100`.
- O retorno contém `visualizationMode: "SCHEMATIC_DISTANCE_ONLY"`, `realTimeCoordinatesAvailable: false`, `distanceUnit: "km"`, `velocityUnit: "km/s"`, além de `events` paginados.

Exemplo simplificado **ilustrativo** (os números não representam uma observação real):

```json
{
  "visualizationMode": "SCHEMATIC_DISTANCE_ONLY",
  "realTimeCoordinatesAvailable": false,
  "distanceUnit": "km",
  "velocityUnit": "km/s",
  "startDate": "2026-11-11",
  "endDate": "2026-11-18",
  "events": {
    "content": [{
      "id": "11111111-1111-4111-8111-111111111111",
      "asteroidId": "22222222-2222-4222-8222-222222222222",
      "nasaId": 123456,
      "asteroidName": "EXEMPLO-NEO",
      "hazardous": false,
      "approachAtUtc": "2026-11-13T10:25:00Z",
      "velocityKmS": 12.7,
      "distanceKm": 3500000,
      "lunarDistance": 9.1
    }],
    "page": 0,
    "size": 50,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

### Importar eventos da NASA

O feed admite até **7 dias** de intervalo (extremos incluídos). Autentique o comando com um token secreto **somente no servidor/terminal**, nunca no código HTML/JS:

```bash
curl -X POST 'http://localhost:8181/v1/admin/import?startDate=2026-11-11&endDate=2026-11-17' \
  -H 'X-Admin-Token: SEU_TOKEN_SECRETO'
```

A resposta informa `asteroidsCreated`, `asteroidsUpdated`, `approachesCreated` e `approachesUpdated`. Repetir o mesmo período atualiza os registros; o índice único protege contra duplicidade de aproximações por asteroide + data/hora.

**Atenção:** o radar consulta **apenas os períodos importados para o banco**. Uma consulta de data não importada retorna uma página vazia; não dispara importação automática.

## Sincronização automática opcional

Para não depender apenas de importações manuais, habilite as variáveis abaixo no Web Service do backend. A configuração é **desativada por padrão** e nunca executa sincronização sem ativação explícita:

```dotenv
RADAR_SYNC_ENABLED=true
RADAR_SYNC_ON_STARTUP=true
# opcional: 03:15 UTC todos os dias (cron Spring de seis campos)
RADAR_SYNC_CRON=0 15 3 * * *
```

- Busca eventos de **hoje até seis dias adiante**, em UTC; o período está dentro do limite NASA.
- Em cada execução, atualiza asteroides e eventos já existentes, evitando duplicatas.
- `RADAR_SYNC_ON_STARTUP=true` ativa uma carga inicial na inicialização, em vez de esperar pelo horário diário.
- Em caso de erro externo, registra uma advertência sem divulgar URL com API key e mantém a aplicação online; tenta novamente na próxima execução programada.
- Utilize sua própria `NASA_API_KEY`, respeite a cota do serviço e acompanhe os logs; o `DEMO_KEY` é destinado a testes com limites mais baixos.
- A sincronização **não** fornece coordenadas espaciais reais nem garante monitoramento em tempo real. Eventos fora dos períodos importados continuam exigindo importação adicional.

## Integração com o frontend

O frontend está no repositório [space-radar-frotend](https://github.com/arthurSchgg/space-radar-frotend) e consulta `GET /v1/radar`. Configure seu domínio na variável de ambiente `RADAR_CORS_ORIGINS` (origens separadas por vírgula, sem barra final).

**Nunca** coloque `NASA_API_KEY`, `DB_PASSWORD` ou `RADAR_ADMIN_TOKEN` no navegador. Não há autenticação pública de usuário; a API é voltada para leitura pública.

## Segurança, migrações e deploy

- `.env` e segredos locais são ignorados pelo Git, mas um `.env` **foi versionado anteriormente na `develop`**. Removê-lo da versão atual **não apaga o histórico**. **Rotacione todas as credenciais que já estiveram nele**, incluindo chave NASA, senha do banco e qualquer token, antes de publicar.
- Migrações em `src/main/resources/db/migration`. Um banco antigo criado com Hibernate pode conter diferenças/duplicidades: **faça backup e valide V2** antes de migrar; `baseline-on-migrate` não corrige schema antigo automaticamente.
- A importação usa token administrativo fixo. Para exposição pública séria, recomenda-se reforçar autenticação, limite de requisições e observabilidade; a proteção atual é mínima.
- Para publicar o backend no Render, crie um **Web Service** com runtime **Docker**, repositório `space-radar-backend`, branch `main`, contexto de build na raiz do repositório e Dockerfile em `docker/Dockerfile`. O banco PostgreSQL de produção está hospedado no **[Neon](https://neon.tech/)**, enquanto a API Spring Boot utiliza o Render. No Web Service, configure `SPRING_DATASOURCE_URL` com o endereço **JDBC da conexão direta** fornecido pelo Neon (prefixo `jdbc:postgresql://`, nome do banco `space_radar` e `sslmode=require`), além de `DB_USERNAME` e `DB_PASSWORD` nas variáveis de ambiente privadas. O Flyway aplica as migrações ao iniciar. Não use `localhost`, não inclua credenciais no repositório e não use Docker Compose para provisionar o serviço no Render.

Configure `NASA_API_KEY`, `RADAR_ADMIN_TOKEN` e `RADAR_CORS_ORIGINS=https://seu-projeto.vercel.app` como variáveis **privadas** do Render; para mais de um domínio, separe as origens com vírgulas. Para importar automaticamente a semana atual ao iniciar, use `RADAR_SYNC_ENABLED=true` e `RADAR_SYNC_ON_STARTUP=true`, com chave NASA válida e atenção às cotas. O Spring usa `PORT` (se o Render definir) com prioridade sobre `SERVER_PORT`, de modo que não é necessário fixar a porta do container de desenvolvimento. Depois confirme `/actuator/health` e `/v1/radar` na URL HTTPS pública.

No deploy da Vercel, defina `SPACE_RADAR_API_URL` **apenas** com a origem HTTPS pública deste serviço. Esta variável no frontend **não é secreta**. Nunca envie os valores de `NASA_API_KEY`, `DB_PASSWORD` ou `RADAR_ADMIN_TOKEN` para Vercel ou GitHub.
- Endpoint de health: `/actuator/health`.
- Os testes/CI devem passar antes de fazer merge `feature/* → develop → main`.

## Fluxo de contribuição (Git Flow)

```text
main (releases)
└── develop (integração)
    └── feature/radar-backend-mvp (mudanças em revisão)
```

1. Abra uma issue com motivação e critérios de aceite.
2. Crie `feature/<escopo>` a partir de `develop`.
3. Faça commits semânticos (`feat:`, `fix:`, `docs:`, `test:`).
4. Abra PR para `develop` com link da issue.
5. Execute CI, revise código e testes, faça merge só depois de validar.
6. Abra PR de release `develop → main` e use tag semântica.

## Status

Implementação inicial concluída na branch de feature; **não homologado para produção** até CI e testes com PostgreSQL real. Consulte as issues do projeto para as pendências.
