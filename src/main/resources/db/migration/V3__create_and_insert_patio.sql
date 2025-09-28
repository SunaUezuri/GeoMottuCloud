CREATE TABLE T_GEOMOTTU_PATIO (
    id_patio BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nm_patio VARCHAR(50),
    nr_capacidade INT NOT NULL,
    id_filial BIGINT,
    CONSTRAINT fk_patio_filial FOREIGN KEY (id_filial) REFERENCES T_GEOMOTTU_FILIAL(id_filial)
);

-- Inserts iniciais de pátios
INSERT INTO T_GEOMOTTU_PATIO (nm_patio, nr_capacidade, id_filial) VALUES
('Pátio Central SP', 250, 1),
('Pátio Zona Sul SP', 150, 1),
('Pátio Copacabana RJ', 180, 2),
('Pátio Polanco CDMX', 300, 3),
('Pátio Jalisco GDL', 220, 4);
