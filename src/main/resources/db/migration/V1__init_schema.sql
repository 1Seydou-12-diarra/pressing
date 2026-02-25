-- =========================
-- TABLE : agence
-- =========================
CREATE TABLE agence (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL,
    adresse TEXT,
    telephone VARCHAR(30),
    code_agence VARCHAR(50) UNIQUE NOT NULL,
    actif BOOLEAN DEFAULT TRUE
);

-- =========================
-- TABLE : employe
-- =========================
CREATE TABLE employe (
    id BIGSERIAL PRIMARY KEY,
    keycloak_id VARCHAR(100) UNIQUE NOT NULL,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    agence_id BIGINT,
    actif BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_employe_agence
        FOREIGN KEY (agence_id) REFERENCES agence(id)
);

-- =========================
-- TABLE : client
-- =========================
CREATE TABLE client (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100),
    telephone VARCHAR(30) UNIQUE NOT NULL,
    email VARCHAR(150),
    adresse TEXT,
    points_fidelite INT DEFAULT 0,
    actif BOOLEAN DEFAULT TRUE,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TABLE : commande
-- =========================
CREATE TABLE commande (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL,
    agence_id BIGINT,
    employe_id BIGINT,
    statut VARCHAR(30) NOT NULL,
    montant_total NUMERIC(12,2) DEFAULT 0,
    date_depot TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_retrait_prevue TIMESTAMP,

    CONSTRAINT fk_commande_client
        FOREIGN KEY (client_id) REFERENCES client(id),

    CONSTRAINT fk_commande_agence
        FOREIGN KEY (agence_id) REFERENCES agence(id),

    CONSTRAINT fk_commande_employe
        FOREIGN KEY (employe_id) REFERENCES employe(id)
);

-- =========================
-- TABLE : article_commande
-- =========================
CREATE TABLE article_commande (
    id BIGSERIAL PRIMARY KEY,
    commande_id BIGINT NOT NULL,
    type_vetement VARCHAR(100) NOT NULL,
    service VARCHAR(100) NOT NULL,
    tarif_unitaire NUMERIC(10,2) NOT NULL,
    observations TEXT,
    code_barres VARCHAR(100) UNIQUE NOT NULL,
    statut VARCHAR(30) NOT NULL,

    CONSTRAINT fk_article_commande
        FOREIGN KEY (commande_id) REFERENCES commande(id)
);

-- =========================
-- TABLE : paiement
-- =========================
CREATE TABLE paiement (
    id BIGSERIAL PRIMARY KEY,
    commande_id BIGINT NOT NULL,
    montant NUMERIC(12,2) NOT NULL,
    mode_paiement VARCHAR(50) NOT NULL,
    date_paiement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reference VARCHAR(100),

    CONSTRAINT fk_paiement_commande
        FOREIGN KEY (commande_id) REFERENCES commande(id)
);

-- =========================
-- TABLE : tarif
-- =========================
CREATE TABLE tarif (
    id BIGSERIAL PRIMARY KEY,
    type_vetement VARCHAR(100) NOT NULL,
    type_service VARCHAR(100) NOT NULL,
    prix NUMERIC(10,2) NOT NULL,
    actif BOOLEAN DEFAULT TRUE,
    date_debut DATE,
    date_fin DATE
);

-- =========================
-- TABLE : produit_stock
-- =========================
CREATE TABLE produit_stock (
    id BIGSERIAL PRIMARY KEY,
    agence_id BIGINT NOT NULL,
    nom VARCHAR(150) NOT NULL,
    quantite INTEGER NOT NULL,
    seuil_alerte INTEGER NOT NULL,
    unite VARCHAR(30) NOT NULL,

    CONSTRAINT fk_stock_agence
        FOREIGN KEY (agence_id) REFERENCES agence(id)
);

-- =========================
-- TABLE : historique_statut
-- =========================
CREATE TABLE historique_statut (
    id BIGSERIAL PRIMARY KEY,
    commande_id BIGINT NOT NULL,
    statut_precedent VARCHAR(30),
    statut_nouveau VARCHAR(30) NOT NULL,
    employe_id BIGINT,
    date_changement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_historique_commande
        FOREIGN KEY (commande_id) REFERENCES commande(id),

    CONSTRAINT fk_historique_employe
        FOREIGN KEY (employe_id) REFERENCES employe(id)
);

-- =========================
-- TABLE : audit_log
-- =========================
CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,
    action VARCHAR(100) NOT NULL,
    entite VARCHAR(100) NOT NULL,
    entite_id BIGINT,
    utilisateur VARCHAR(150),
    details_json VARCHAR(150),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- INDEXES (PERFORMANCE)
-- =========================
CREATE INDEX idx_client_telephone ON client(telephone);
CREATE INDEX idx_commande_statut ON commande(statut);
CREATE INDEX idx_commande_date_depot ON commande(date_depot);
CREATE INDEX idx_article_code_barres ON article_commande(code_barres);
CREATE INDEX idx_audit_entite ON audit_log(entite, entite_id);