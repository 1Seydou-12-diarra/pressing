-- =====================================================
-- DONNÉES DE RÉFÉRENCE – PRESSING
-- Version : V2
-- =====================================================

-- =========================
-- TYPES DE VÊTEMENTS
-- =========================
CREATE TABLE IF NOT EXISTS type_vetement (
    code VARCHAR(50) PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL
);

INSERT INTO type_vetement (code, libelle) VALUES
('CHEMISE', 'Chemise'),
('PANTALON', 'Pantalon'),
('ROBE', 'Robe'),
('VESTE', 'Veste'),
('MANTEAU', 'Manteau'),
('JUPE', 'Jupe'),
('DRAP', 'Drap'),
('COUVERTURE', 'Couverture')
ON CONFLICT (code) DO NOTHING;

-- =========================
-- TYPES DE SERVICES
-- =========================
CREATE TABLE IF NOT EXISTS type_service (
    code VARCHAR(50) PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL
);

INSERT INTO type_service (code, libelle) VALUES
('NETTOYAGE_SEC', 'Nettoyage à sec'),
('REPASSAGE', 'Repassage'),
('LAVAGE', 'Lavage simple'),
('DETACHAGE', 'Détachage'),
('EXPRESS', 'Service express')
ON CONFLICT (code) DO NOTHING;

-- =========================
-- AGENCE PAR DÉFAUT
-- =========================
INSERT INTO agence (nom, adresse, telephone, code_agence, actif)
VALUES (
    'Agence Principale',
    'Centre-ville',
    '+22500000000',
    'AGENCE-001',
    TRUE
)
ON CONFLICT (code_agence) DO NOTHING;

-- =========================
-- TARIFS INITIAUX
-- =========================
INSERT INTO tarif (
    type_vetement,
    type_service,
    prix,
    actif,
    date_debut
) VALUES
('CHEMISE', 'REPASSAGE', 500, TRUE, CURRENT_DATE),
('CHEMISE', 'NETTOYAGE_SEC', 800, TRUE, CURRENT_DATE),
('PANTALON', 'REPASSAGE', 700, TRUE, CURRENT_DATE),
('PANTALON', 'NETTOYAGE_SEC', 1000, TRUE, CURRENT_DATE),
('ROBE', 'NETTOYAGE_SEC', 1500, TRUE, CURRENT_DATE),
('VESTE', 'NETTOYAGE_SEC', 2000, TRUE, CURRENT_DATE),
('MANTEAU', 'NETTOYAGE_SEC', 3000, TRUE, CURRENT_DATE)
ON CONFLICT DO NOTHING;

-- =====================================================
-- FIN V2
-- =====================================================