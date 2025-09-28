CREATE SEQUENCE SQ_T_GEOMOTTU_FILIAL START WITH 1 INCREMENT BY 1;

-- Cria a tabela de filial
CREATE TABLE T_GEOMOTTU_FILIAL (
    id_filial NUMBER(19) NOT NULL PRIMARY KEY,
    nm_filial VARCHAR2(255) NOT NULL,
    pais_filial VARCHAR2(255) NOT NULL,
    nm_telefone VARCHAR2(20),
    ds_email VARCHAR2(50),
    estado VARCHAR2(255),
    sigla_estado VARCHAR2(2),
    cidade VARCHAR2(255),
    rua VARCHAR2(255)
);

INSERT INTO T_GEOMOTTU_FILIAL (id_filial, nm_filial, pais_filial, nm_telefone, ds_email, estado, sigla_estado, cidade, rua) VALUES
(SQ_T_GEOMOTTU_FILIAL.nextval, 'Mottu - Sede São Paulo', 'BRASIL', '+5511999990001', 'contato.sp@mottu.com.br', 'São Paulo', 'SP', 'São Paulo', 'Av. Paulista, 1000');

INSERT INTO T_GEOMOTTU_FILIAL (id_filial, nm_filial, pais_filial, nm_telefone, ds_email, estado, sigla_estado, cidade, rua) VALUES
(SQ_T_GEOMOTTU_FILIAL.nextval, 'Mottu - Hub Rio de Janeiro', 'BRASIL', '+5521999990002', 'contato.rj@mottu.com.br', 'Rio de Janeiro', 'RJ', 'Rio de Janeiro', 'Av. Atlântica, 2000');

INSERT INTO T_GEOMOTTU_FILIAL (id_filial, nm_filial, pais_filial, nm_telefone, ds_email, estado, sigla_estado, cidade, rua) VALUES
(SQ_T_GEOMOTTU_FILIAL.nextval, 'Mottu - CDMX Polanco', 'MEXICO', '+5255999990003', 'contacto.cdmx@mottu.mx', 'Ciudad de México', 'CM', 'Ciudad de México', 'Av. Presidente Masaryk, 300');

INSERT INTO T_GEOMOTTU_FILIAL (id_filial, nm_filial, pais_filial, nm_telefone, ds_email, estado, sigla_estado, cidade, rua) VALUES
(SQ_T_GEOMOTTU_FILIAL.nextval, 'Mottu - Guadalajara Centro', 'MEXICO', '+5233999990004', 'contacto.gdl@mottu.mx', 'Jalisco', 'JA', 'Guadalajara', 'Av. Juárez, 400');

INSERT INTO T_GEOMOTTU_FILIAL (id_filial, nm_filial, pais_filial, nm_telefone, ds_email, estado, sigla_estado, cidade, rua) VALUES
(SQ_T_GEOMOTTU_FILIAL.nextval, 'Mottu - Monterrey', 'MEXICO', '+5281999990005', 'contacto.mty@mottu.mx', 'Nuevo León', 'NL', 'Monterrey', 'Calz. del Valle, 500');
