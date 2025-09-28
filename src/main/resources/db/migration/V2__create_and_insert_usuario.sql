CREATE SEQUENCE SQ_T_GEOMOTTU_USUARIO START WITH 1 INCREMENT BY 1;

CREATE TABLE T_GEOMOTTU_USUARIO (
    id_usuario NUMBER(19) NOT NULL PRIMARY KEY,
    nm_usuario VARCHAR2(25) NOT NULL UNIQUE,
    ds_senha VARCHAR2(100) NOT NULL,
    tp_perfil NUMBER(10) NOT NULL,
    id_filial NUMBER(19),
    CONSTRAINT fk_usuario_filial FOREIGN KEY (id_filial) REFERENCES T_GEOMOTTU_FILIAL(id_filial)
);

-- Senha para 'admin': @Admin123
INSERT INTO T_GEOMOTTU_USUARIO (id_usuario, nm_usuario, ds_senha, tp_perfil, id_filial)
VALUES (SQ_T_GEOMOTTU_USUARIO.nextval, 'admin', '$2a$10$vawSWrW.lgiFSg/YMYIY8u6Ws57vplQz/l8.zy2F7hmQ0.UPEyauG', 1, 1);

-- Senha para 'joao.silva': $User5432
INSERT INTO T_GEOMOTTU_USUARIO (id_usuario, nm_usuario, ds_senha, tp_perfil, id_filial)
VALUES (SQ_T_GEOMOTTU_USUARIO.nextval, 'joao.silva', '$2a$10$75qbILRsPk4hzuIuJRl7y.1ces11wP.hb4sOmZkCowcpuvhiqzYxG', 2, 1);

-- Senha para 'carlos.gomez': $User5892
INSERT INTO T_GEOMOTTU_USUARIO (id_usuario, nm_usuario, ds_senha, tp_perfil, id_filial)
VALUES (SQ_T_GEOMOTTU_USUARIO.nextval, 'carlos.gomez', '$2a$10$4AuDcVHbLO.wq3HdkvQp3OJexvd7RdgE7HHoKYJDMF0SVSz1aRIRK', 2, 3);
