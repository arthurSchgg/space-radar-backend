# Contrato do frontend — Space Radar

## Propósito
O frontend em HTML/CSS/JS usa **GET /v1/radar** como origem dos eventos. Os dados descrevem **aproximações de asteroides à Terra**, não detecções de meteoritos em toda a Via Láctea.

## Mapeamento
| Campo API | Uso no frontend |
| --- | --- |
| `nasaId` | Identificador do NEO |
| `asteroidName` | Rótulo e busca |
| `hazardous` | Cor de alerta / filtro |
| `approachAtUtc` | Data/hora UTC |
| `distanceKm` | Distância mínima de aproximação, escala visual |
| `lunarDistance` | Distância em unidades lunares |
| `velocityKmS` | Velocidade na aproximação |
| `visualizationMode` | Advertência sobre radar esquemático |
| `realTimeCoordinatesAvailable` | Sempre falso neste escopo |
| `events.totalElements` | Total de itens encontrados |
| `events.content` | Eventos da página |

## Gráfico esquemático
A distância ao centro do radar pode representar aproximadamente `distanceKm`, mas a **direção angular não está disponível pela API**. Para a animação, pode-se distribuir objetos em ângulos determinísticos, desde que a interface sinalize claramente que são **posições ilustrativas**, nunca rastreamento real. Não mostrar ou alegar órbitas/rumos físicos reais.

## Erros e estados
- `200` com `events.content=[]`: intervalo sem eventos importados/sem ocorrências; orientar importação, se aplicável.
- `400`: intervalo/parametrização inválidos.
- `404`: identificador inexistente.
- `502`: a NASA não respondeu (na importação).
- `503`: token de importação não configurado.
- Conexão indisponível: mostrar erro, oferecer `Tentar novamente`, sem apresentar dados de demonstração como reais.

## Integração segura
Configure URL pública do backend em `js/config.js`; URL de API é pública, não é um segredo. CORS no servidor deve conter a origem real do frontend. **Não** colocar chaves NASA nem tokens de administração no frontend.
