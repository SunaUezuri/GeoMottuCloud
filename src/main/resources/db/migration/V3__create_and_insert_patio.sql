CREATE SEQUENCE SQ_T_GEOMOTTU_PATIO START WITH 1 INCREMENT BY 1;

CREATE TABLE T_GEOMOTTU_PATIO (
    id_patio NUMBER(19) NOT NULL PRIMARY KEY,
    nm_patio VARCHAR2(50),
    nr_capacidade NUMBER(10) NOT NULL,
    id_filial NUMBER(19),
    CONSTRAINT fk_patio_filial FOREIGN KEY (id_filial) REFERENCES T_GEOMOTTU_FILIAL(id_filial)
);

INSERT INTO T_GEOMOTTU_PATIO (id_patio, nm_patio, nr_capacidade, id_filial) VALUES (SQ_T_GEOMOTTU_PATIO.nextval, 'Pátio Central SP', 250, 1);
INSERT INTO T_GEOMOTTU_PATIO (id_patio, nm_patio, nr_capacidade, id_filial) VALUES (SQ_T_GEOMOTTU_PATIO.nextval, 'Pátio Zona Sul SP', 150, 1);
INSERT INTO T_GEOMOTTU_PATIO (id_patio, nm_patio, nr_capacidade, id_filial) VALUES (SQ_T_GEOMOTTU_PATIO.nextval, 'Pátio Copacabana RJ', 180, 2);
INSERT INTO T_GEOMOTTU_PATIO (id_patio, nm_patio, nr_capacidade, id_filial) VALUES (SQ_T_GEOMOTTU_PATIO.nextval, 'Pátio Polanco CDMX', 300, 3);
INSERT INTO T_GEOMOTTU_PATIO (id_patio, nm_patio, nr_capacidade, id_filial) VALUES (SQ_T_GEOMOTTU_PATIO.nextval, 'Pátio Jalisco GDL', 220, 4);