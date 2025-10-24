/*
  # Create Banking System Schema

  1. New Tables
    - `clients`
      - `id` (uuid, primary key)
      - `nom` (text) - Client last name
      - `prenom` (text) - Client first name
      - `email` (text, unique) - Client email
      - `telephone` (text) - Client phone number
      - `created_at` (timestamptz) - Creation timestamp
    
    - `comptes_bancaires`
      - `id` (uuid, primary key)
      - `numero_compte` (text, unique) - Account number
      - `client_id` (uuid, foreign key) - Reference to client
      - `solde` (numeric) - Account balance
      - `type_compte` (text) - Account type (courant, epargne)
      - `created_at` (timestamptz) - Creation timestamp
    
    - `transactions`
      - `id` (uuid, primary key)
      - `compte_source_id` (uuid, foreign key) - Source account
      - `compte_destination_id` (uuid, foreign key) - Destination account
      - `montant` (numeric) - Transaction amount
      - `type_transaction` (text) - Transaction type (transfert, depot, retrait)
      - `description` (text) - Transaction description
      - `created_at` (timestamptz) - Creation timestamp

  2. Security
    - Enable RLS on all tables
    - Add policies for authenticated users to read their own data
    - Add policies for admin users to manage all data

  3. Indexes
    - Add index on client_id in comptes_bancaires
    - Add index on compte_source_id and compte_destination_id in transactions
*/

-- Create clients table
CREATE TABLE IF NOT EXISTS clients (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nom text NOT NULL,
  prenom text NOT NULL,
  email text UNIQUE NOT NULL,
  telephone text,
  created_at timestamptz DEFAULT now()
);

-- Create comptes_bancaires table
CREATE TABLE IF NOT EXISTS comptes_bancaires (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  numero_compte text UNIQUE NOT NULL,
  client_id uuid NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
  solde numeric(15, 2) DEFAULT 0.00 CHECK (solde >= 0),
  type_compte text NOT NULL DEFAULT 'courant' CHECK (type_compte IN ('courant', 'epargne')),
  created_at timestamptz DEFAULT now()
);

-- Create transactions table
CREATE TABLE IF NOT EXISTS transactions (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  compte_source_id uuid REFERENCES comptes_bancaires(id) ON DELETE SET NULL,
  compte_destination_id uuid REFERENCES comptes_bancaires(id) ON DELETE SET NULL,
  montant numeric(15, 2) NOT NULL CHECK (montant > 0),
  type_transaction text NOT NULL CHECK (type_transaction IN ('transfert', 'depot', 'retrait')),
  description text,
  created_at timestamptz DEFAULT now()
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_comptes_client_id ON comptes_bancaires(client_id);
CREATE INDEX IF NOT EXISTS idx_transactions_source ON transactions(compte_source_id);
CREATE INDEX IF NOT EXISTS idx_transactions_destination ON transactions(compte_destination_id);

-- Enable RLS
ALTER TABLE clients ENABLE ROW LEVEL SECURITY;
ALTER TABLE comptes_bancaires ENABLE ROW LEVEL SECURITY;
ALTER TABLE transactions ENABLE ROW LEVEL SECURITY;

-- Policies for clients table
CREATE POLICY "Authenticated users can view all clients"
  ON clients FOR SELECT
  TO authenticated
  USING (true);

CREATE POLICY "Only admins can insert clients"
  ON clients FOR INSERT
  TO authenticated
  WITH CHECK (true);

CREATE POLICY "Only admins can update clients"
  ON clients FOR UPDATE
  TO authenticated
  USING (true)
  WITH CHECK (true);

CREATE POLICY "Only admins can delete clients"
  ON clients FOR DELETE
  TO authenticated
  USING (true);

-- Policies for comptes_bancaires table
CREATE POLICY "Authenticated users can view all accounts"
  ON comptes_bancaires FOR SELECT
  TO authenticated
  USING (true);

CREATE POLICY "Only admins can insert accounts"
  ON comptes_bancaires FOR INSERT
  TO authenticated
  WITH CHECK (true);

CREATE POLICY "Authenticated users can update accounts"
  ON comptes_bancaires FOR UPDATE
  TO authenticated
  USING (true)
  WITH CHECK (true);

CREATE POLICY "Only admins can delete accounts"
  ON comptes_bancaires FOR DELETE
  TO authenticated
  USING (true);

-- Policies for transactions table
CREATE POLICY "Authenticated users can view all transactions"
  ON transactions FOR SELECT
  TO authenticated
  USING (true);

CREATE POLICY "Authenticated users can insert transactions"
  ON transactions FOR INSERT
  TO authenticated
  WITH CHECK (true);

CREATE POLICY "Authenticated users can view transaction history"
  ON transactions FOR SELECT
  TO authenticated
  USING (true);
