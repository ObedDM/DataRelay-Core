CREATE TABLE dimension(
    dimension_id UUID PRIMARY KEY DEFAULT uuidv7(),
    schema_id UUID NOT NULL,
    dtype TEXT NOT NULL,
    name VARCHAR(25) NOT NULL,
    axis_order SMALLINT NOT NULL,

    CONSTRAINT fk_dimension_schema FOREIGN KEY (schema_id) REFERENCES ds_schema(schema_id) ON DELETE CASCADE,
    CONSTRAINT fk_dimension_dtype FOREIGN KEY (dtype) REFERENCES dtype(dtype),

    CONSTRAINT uniq_dimension_schemaid_name UNIQUE (schema_id, name)
);

ALTER TABLE ds_schema ADD has_index BOOLEAN NOT NULL DEFAULT TRUE;