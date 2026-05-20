Faça a implementação dos smartpots

Rotas
POST   /smart-pots
GET    /smart-pots
GET    /smart-pots/{id}
PUT    /smart-pots/{id}
DELETE /smart-pots/{id}

Estrutura de dados

id uuid PRIMARY KEY,
user_id uuid NOT NULL REFERENCES users(id),
plant_name varchar(255) NOT NULL,
humidity_min integer NOT NULL CHECK (humidity_min BETWEEN 0 AND 100),
device_id varchar(255) UNIQUE,
created_at timestamp with time zone NOT NULL,
updated_at timestamp with time zone NOT NULL

não precisa passar user_id, pois o backend deve extrair o id do usuário autenticado a partir do token JWT presente no header Authorization. O campo device_id é opcional, mas se fornecido, deve ser único para cada smart pot.