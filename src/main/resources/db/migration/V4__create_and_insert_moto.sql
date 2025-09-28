CREATE SEQUENCE SQ_T_GEOMOTTU_MOTO START WITH 1 INCREMENT BY 1;

CREATE TABLE T_GEOMOTTU_MOTO (
    id_moto NUMBER(19) NOT NULL PRIMARY KEY,
    nr_placa VARCHAR2(8) UNIQUE,
    nr_chassi VARCHAR2(50) UNIQUE,
    tp_modelo VARCHAR2(255) NOT NULL,
    st_operacional VARCHAR2(255) NOT NULL,
    id_patio NUMBER(19),
    CONSTRAINT fk_moto_patio FOREIGN KEY (id_patio) REFERENCES T_GEOMOTTU_PATIO(id_patio)
);

INSERT INTO T_GEOMOTTU_MOTO (id_moto, nr_placa, nr_chassi, tp_modelo, st_operacional, id_patio) VALUES (SQ_T_GEOMOTTU_MOTO.nextval, 'BRA1A11', 'CHASSIBR001', 'MOTTUPOP', 'LIVRE', 1);
INSERT INTO T_GEOMOTTU_MOTO (id_moto, nr_placa, nr_chassi, tp_modelo, st_operacional, id_patio) VALUES (SQ_T_GEOMOTTU_MOTO.nextval, 'BRA2B22', 'CHASSIBR002', 'MOTTUPOP', 'ALUGADA', 1);
INSERT INTO T_GEOMOTTU_MOTO (id_moto, nr_placa, nr_chassi, tp_modelo, st_operacional, id_patio) VALUES (SQ_T_GEOMOTTU_MOTO.nextval, 'BRA3C33', 'CHASSIBR003', 'MOTTUSPORT', 'MANUTENCAO', 1);
INSERT INTO T_GEOMOTTU_MOTO (id_moto, nr_placa, nr_chassi, tp_modelo, st_operacional, id_patio) VALUES (SQ_T_GEOMOTTU_MOTO.nextval, 'BRA4D44', 'CHASSIBR004', 'MOTTUE', 'LIVRE', 2);
INSERT INTO T_GEOMOTTU_MOTO (id_moto, nr_placa, nr_chassi, tp_modelo, st_operacional, id_patio) VALUES (SQ_T_GEOMOTTU_MOTO.nextval, 'BRA5E55', 'CHASSIBR005', 'MOTTUSPORT', 'LIVRE', 3);
INSERT INTO T_GEOMOTTU_MOTO (id_moto, nr_placa, nr_chassi, tp_modelo, st_operacional, id_patio) VALUES (SQ_T_GEOMOTTU_MOTO.nextval, 'MEX1X11', 'CHASSIMX001', 'MOTTUPOP', 'ALUGADA', 4);
INSERT INTO T_GEOMOTTU_MOTO (id_moto, nr_placa, nr_chassi, tp_modelo, st_operacional, id_patio) VALUES (SQ_T_GEOMOTTU_MOTO.nextval, 'MEX2Y22', 'CHASSIMX002', 'MOTTUE', 'LIVRE', 4);