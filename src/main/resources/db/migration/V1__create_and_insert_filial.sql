-- Criação da tabela
CREATE TABLE T_GEOMOTTU_FILIAL (
    id_filial BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    nm_filial VARCHAR(255) NOT NULL,
    pais_filial VARCHAR(255) NOT NULL,
    nm_telefone VARCHAR(20),
    ds_email VARCHAR(50),
    estado VARCHAR(255),
    sigla_estado VARCHAR(2),
    cidade VARCHAR(255),
    rua VARCHAR(255)
);

-- Inserts de filiais para dados iniciais
INSERT INTO T_GEOMOTTU_FILIAL (nm_filial, pais_filial, nm_telefone, ds_email, estado, sigla_estado, cidade, rua) VALUES
('Mottu - Sede São Paulo', 'BRASIL', '+5511999990001', 'contato.sp@mottu.com.br', 'São Paulo', 'SP', 'São Paulo', 'Av. Paulista, 1000'),
('Mottu - Hub Rio de Janeiro', 'BRASIL', '+5521999990002', 'contato.rj@mottu.com.br', 'Rio de Janeiro', 'RJ', 'Rio de Janeiro', 'Av. Atlântica, 2000'),
('Mottu - CDMX Polanco', 'MEXICO', '+5255999990003', 'contacto.cdmx@mottu.mx', 'Ciudad de México', 'CM', 'Ciudad de México', 'Av. Presidente Masaryk, 300'),
('Mottu - Guadalajara Centro', 'MEXICO', '+5233999990004', 'contacto.gdl@mottu.mx', 'Jalisco', 'JA', 'Guadalajara', 'Av. Juárez, 400'),
('Mottu - Monterrey', 'MEXICO', '+5281999990005', 'contacto.mty@mottu.mx', 'Nuevo León', 'NL', 'Monterrey', 'Calz. del Valle, 500');