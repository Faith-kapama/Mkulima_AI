-- ============================================================================
-- MKULIMAAI - COMPLETE SUPABASE DATABASE SETUP & SEED DATA
-- Copy and paste this entire script into your Supabase SQL Editor and click RUN.
-- ============================================================================

-- 1. Enable UUID Extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================================================
-- 2. CREATE TABLES
-- ============================================================================

-- Table: profiles (Farmer details from App & USSD)
CREATE TABLE IF NOT EXISTS public.profiles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
    phone_number TEXT UNIQUE,
    full_name TEXT NOT NULL,
    region TEXT NOT NULL DEFAULT 'Dodoma',
    district TEXT,
    ward TEXT,
    farm_size_acres NUMERIC(6, 2) DEFAULT 1.0,
    primary_crop TEXT DEFAULT 'Mahindi',
    preferred_language TEXT DEFAULT 'sw', -- 'sw' for Swahili, 'en' for English
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- Table: market_prices (Real-time Tanzania Commodity Prices)
CREATE TABLE IF NOT EXISTS public.market_prices (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    commodity_name TEXT NOT NULL,
    market_name TEXT NOT NULL,
    region TEXT NOT NULL,
    price_per_kg NUMERIC(10, 2) NOT NULL,
    unit TEXT NOT NULL DEFAULT 'kg',
    price_per_bag NUMERIC(10, 2),
    trend TEXT DEFAULT 'stable', -- 'rising', 'falling', 'stable'
    change_percentage NUMERIC(5, 2) DEFAULT 0.0,
    source TEXT DEFAULT 'Wizara ya Kilimo / Soko Kuu',
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- Table: yield_predictions (History of predictions made via App or USSD)
CREATE TABLE IF NOT EXISTS public.yield_predictions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    profile_id UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    phone_number TEXT,
    crop_name TEXT NOT NULL,
    farm_size_acres NUMERIC(6, 2) NOT NULL,
    region TEXT NOT NULL DEFAULT 'Dodoma',
    season TEXT NOT NULL, -- '1. Vuli', '2. Masika', '3. Kipupwe'
    estimated_min_bags NUMERIC(8, 2) NOT NULL,
    estimated_max_bags NUMERIC(8, 2) NOT NULL,
    confidence_rate NUMERIC(4, 2) DEFAULT 0.88,
    fertilizer_recommendation TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Table: crop_diagnoses (AI Disease & Pest Scans)
CREATE TABLE IF NOT EXISTS public.crop_diagnoses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    profile_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE,
    crop_name TEXT NOT NULL,
    disease_name TEXT NOT NULL,
    disease_name_sw TEXT NOT NULL,
    confidence NUMERIC(4, 2) NOT NULL,
    severity TEXT NOT NULL DEFAULT 'Moderate',
    symptoms TEXT,
    treatment TEXT,
    prevention TEXT,
    image_url TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Table: marketplace_products (Pembejeo & Mazao Market)
CREATE TABLE IF NOT EXISTS public.marketplace_products (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    seller_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    description TEXT,
    category TEXT NOT NULL, -- 'Mbegu', 'Mbolea', 'Dawa', 'Mazao', 'Vifaa'
    price_tzs NUMERIC(12, 2) NOT NULL,
    unit TEXT NOT NULL DEFAULT 'Mfuko',
    quantity_available NUMERIC(8, 2) DEFAULT 1,
    region TEXT NOT NULL,
    district TEXT,
    seller_phone TEXT NOT NULL,
    image_url TEXT,
    status TEXT DEFAULT 'active',
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================================================
-- 3. ROW LEVEL SECURITY (RLS) POLICIES
-- ============================================================================
ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.market_prices ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.yield_predictions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.crop_diagnoses ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.marketplace_products ENABLE ROW LEVEL SECURITY;

-- Allow public read on market_prices and marketplace_products
CREATE POLICY "Public can view market prices" ON public.market_prices FOR SELECT USING (true);
CREATE POLICY "Public can view active products" ON public.marketplace_products FOR SELECT USING (status = 'active');

-- Allow authenticated users & service role full access to profiles
CREATE POLICY "Allow read profiles" ON public.profiles FOR SELECT USING (true);
CREATE POLICY "Allow insert profiles" ON public.profiles FOR INSERT WITH CHECK (true);
CREATE POLICY "Allow update profiles" ON public.profiles FOR UPDATE USING (true);

-- Allow public/USSD logging for predictions
CREATE POLICY "Allow insert yield_predictions" ON public.yield_predictions FOR INSERT WITH CHECK (true);
CREATE POLICY "Allow read yield_predictions" ON public.yield_predictions FOR SELECT USING (true);

-- ============================================================================
-- 4. SEED DATA (BEI ZA MASOKO YA TANZANIA - MAHINDI NA NYANYA TU)
-- ============================================================================
INSERT INTO public.market_prices (commodity_name, market_name, region, price_per_kg, unit, price_per_bag, trend, change_percentage, source)
VALUES
    ('Mahindi', 'Soko Kuu la Kariakoo', 'Dar es Salaam', 750.00, 'kg', 75000.00, 'rising', 2.5, 'Soko la Kariakoo'),
    ('Mahindi', 'Soko la Kibaigwa', 'Dodoma', 680.00, 'kg', 68000.00, 'stable', 0.0, 'Kibaigwa Grain Market'),
    ('Mahindi', 'Soko Kuu la Soweto', 'Mbeya', 620.00, 'kg', 62000.00, 'falling', -1.8, 'Soweto Market'),
    ('Mahindi', 'Soko Kuu la Arusha', 'Arusha', 700.00, 'kg', 70000.00, 'rising', 1.5, 'Arusha Central'),
    ('Mahindi', 'Soko Kuu la Morogoro', 'Morogoro', 660.00, 'kg', 66000.00, 'stable', 0.0, 'Morogoro Urban'),
    ('Nyanya', 'Soko Kuu la Tandale', 'Dar es Salaam', 1400.00, 'kg', 35000.00, 'falling', -5.2, 'Tandale Market (Kreti)'),
    ('Nyanya', 'Soko la Ilala', 'Dar es Salaam', 1500.00, 'kg', 38000.00, 'falling', -3.0, 'Ilala Boma'),
    ('Nyanya', 'Soko la Majengo', 'Dodoma', 1200.00, 'kg', 30000.00, 'stable', 0.0, 'Majengo Dodoma'),
    ('Nyanya', 'Soko la Kilombero', 'Arusha', 1300.00, 'kg', 32000.00, 'rising', 3.5, 'Kilombero Market'),
    ('Nyanya', 'Soko Kuu la Mbarali', 'Mbeya', 1100.00, 'kg', 28000.00, 'stable', 0.0, 'Mbarali Market')
ON CONFLICT DO NOTHING;

-- Seed Sample Products (Pembejeo za Mahindi na Nyanya Tu)
INSERT INTO public.marketplace_products (title, description, category, price_tzs, unit, region, seller_phone, status)
VALUES
    ('Mbegu ya Mahindi (DK 777)', 'Mbegu bora chotara ya mahindi inayovumilia ukame na kutoa magunia 30+ kwa ekari.', 'Mbegu', 16500.00, 'Mfuko 2kg', 'Dodoma', '+255712345678', 'active'),
    ('Mbegu ya Nyanya (Assila F1)', 'Mbegu bora ya nyanya inayostahimili magonjwa ya ukungu na kutoa nyanya ngumu zenye soko.', 'Mbegu', 42000.00, 'Kopo gramu 10', 'Morogoro', '+255784112233', 'active'),
    ('Mbolea ya Kupandia (DAP)', 'Mbolea halisi ya fosfati kwa mizizi imara wakati wa kupanda mahindi na nyanya.', 'Mbolea', 88000.00, 'Mfuko 50kg', 'Dodoma', '+255784112233', 'active'),
    ('Dawa ya Viwavi Jeshi (Belt Expert)', 'Inatokomeza viwavi jeshi wavamizi (Fall Armyworm) kwenye mahindi ndani ya saa 24.', 'Dawa', 25000.00, 'Chupa 200ml', 'Arusha', '+255755998877', 'active'),
    ('Dawa ya Ukungu wa Nyanya (Ridomil Gold)', 'Inatibu na kuzuia ukungu wa mapema na chelewa (Early & Late Blight) kwenye nyanya.', 'Dawa', 32000.00, 'Mfuko 250g', 'Iringa', '+255755998877', 'active')
ON CONFLICT DO NOTHING;

-- Verification query
SELECT 'MkulimaAI Supabase Setup Successfully Completed!' AS status;
