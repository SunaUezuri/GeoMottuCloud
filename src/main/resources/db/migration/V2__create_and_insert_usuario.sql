CREATE TABLE T_GEOMOTTU_USUARIO (
    id_usuario BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    nm_usuario VARCHAR(25) NOT NULL UNIQUE,
    ds_senha VARCHAR(100) NOT NULL,
    tp_perfil INT NOT NULL,
    id_filial BIGINT,
    CONSTRAINT fk_usuario_filial FOREIGN KEY (id_filial) REFERENCES T_GEOMOTTU_FILIAL(id_filial)
);

-- admin - senha: @Admin123
-- joao.silva - senha: $User5432
-- carlos.gomez - senha: $User5892
INSERT INTO T_GEOMOTTU_USUARIO (nm_usuario, ds_senha, tp_perfil, id_filial) VALUES
('admin', '$2a$10$vawSWrW.lgiFSg/YMYIY8u6Ws57vplQz/l8.zy2F7hmQ0.UPEyauG', 1, 1),
('joao.silva', '$2a$10$75qbILRsPk4hzuIuJRl7y.1ces11wP.hb4sOmZkCowcpuvhiqzYxG', 2, 1),
('carlos.gomez', '$2a$10$4AuDcVHbLO.wq3HdkvQp3OJexvd7RdgE7HHoKYJDMF0SVSz1aRIRK', 2, 3);

