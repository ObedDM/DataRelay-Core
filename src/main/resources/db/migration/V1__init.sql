CREATE TABLE user (
    user_id UUID PRIMARY KEY DEFAULT uuidv7(),
    username VARCHAR(20) NOT NULL UNIQUE,
    password CHAR(60) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE dtype(
    dtype TEXT PRIMARY KEY
);

CREATE TABLE ds_schema(
    schema_id UUID PRIMARY KEY DEFAULT uuidv7(),
    user_id UUID NOT NULL,
    name VARCHAR(25) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_schema_user FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    CONSTRAINT uniq_schema_name_userid UNIQUE (name, user_id)
);

CREATE TABLE feature(
    feature_id UUID PRIMARY KEY DEFAULT uuidv7(),
    schema_id UUID NOT NULL,
    dtype TEXT NOT NULL,
    name VARCHAR(25) NOT NULL,
    position SMALLINT NOT NULL,

    CONSTRAINT fk_feature_schema FOREIGN KEY (schema_id) REFERENCES ds_schema(schema_id) ON DELETE CASCADE,
    CONSTRAINT fk_feature_dtype FOREIGN KEY (dtype) REFERENCES dtype(dtype),

    CONSTRAINT uniq_feature_schemaid_name UNIQUE (schema_id, name)
)