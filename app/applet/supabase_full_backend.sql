-- ============================================================================
-- MKULIMAAI - COMPLETE PRODUCTION SUPABASE BACKEND (FULL VERSION)
-- Run this in your Supabase SQL Editor (https://supabase.com/dashboard/project/osxhrholyvvicptemzsz/sql)
-- ============================================================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ----------------------------------------------------------------------------
-- 1. PROFILES (Wakulima wa Mahindi na Nyanya)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS public.profiles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
    phone_number TEXT UNIQUE,
    full_name TEXT NOT NULL,
    region TEXT NOT NULL DEFAULT 'Dodoma',
    district TEXT DEFAULT 'Chamwino',
    ward TEXT DEFAULT 'Kati',
    farm_size_acres NUMERIC(6, 2) DEFAULT 1.0,
    primary_crop TEXT DEFAULT 'Mahindi', -- 'Mahindi' au 'Nyanya'
    farming_type TEXT DEFAULT 'Smallholder Farmer',
    water_source TEXT DEFAULT 'Rainfed',
    preferred_language TEXT DEFAULT 'sw',
    receive_sms_alerts BOOLEAN DEFAULT true,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- ----------------------------------------------------------------------------
-- 2. MARKET PRICES (Bei za Masoko ya Mahindi na Nyanya Tanzania)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS public.market_prices (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    commodity_name TEXT NOT NULL, -- 'Mahindi' au 'Nyanya'
    market_name TEXT NOT NULL,
    region TEXT NOT NULL,
    price_per_kg NUMERIC(10, 2) NOT NULL,
    unit TEXT NOT NULL DEFAULT 'kg',
    price_per_bag NUMERIC(10, 2), -- Gunia 100kg kwa mahindi, Kreti kwa nyanya
    trend TEXT DEFAULT 'stable', -- 'rising', 'falling', 'stable'
    change_percentage NUMERIC(5, 2) DEFAULT 0.0,
    source TEXT DEFAULT 'Wizara ya Kilimo / Soko Kuu',
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- ----------------------------------------------------------------------------
-- 3. YIELD PREDICTIONS (Utabiri wa Mavuno: 1. Vuli, 2. Masika, 3. Kipupwe)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS public.yield_predictions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    profile_id UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    phone_number TEXT,
    crop_name TEXT NOT NULL, -- 'Mahindi' au 'Nyanya'
    farm_size_acres NUMERIC(6, 2) NOT NULL,
    region TEXT NOT NULL DEFAULT 'Dodoma',
    season TEXT NOT NULL, -- '1. Vuli', '2. Masika', '3. Kipupwe'
    estimated_min_bags NUMERIC(8, 2) NOT NULL,
    estimated_max_bags NUMERIC(8, 2) NOT NULL,
    confidence_rate NUMERIC(4, 2) DEFAULT 0.90,
    fertilizer_recommendation TEXT DEFAULT 'DAP wakati wa kupanda, UREA/CAN kukuzia',
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ----------------------------------------------------------------------------
-- 4. CROP DIAGNOSES (Utambuzi wa Magonjwa: Mahindi & Nyanya)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS public.crop_diagnoses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    profile_id UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    phone_number TEXT,
    crop_name TEXT NOT NULL, -- 'Maize / Mahindi' au 'Tomato / Nyanya'
    disease_name TEXT NOT NULL,
    disease_name_sw TEXT NOT NULL,
    confidence INT NOT NULL DEFAULT 85,
    severity TEXT NOT NULL DEFAULT 'Moderate',
    symptoms TEXT,
    treatment TEXT,
    prevention TEXT,
    image_url TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ----------------------------------------------------------------------------
-- 5. MARKETPLACE PRODUCTS & BUYER REQUESTS (Sokoni & Pembejeo)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS public.marketplace_products (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    seller_id UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    title TEXT NOT NULL,
    description TEXT,
    category TEXT NOT NULL, -- 'Mbegu', 'Mbolea', 'Dawa', 'Mazao', 'Vifaa'
    crop_type TEXT DEFAULT 'Maize', -- 'Maize' au 'Tomato'
    price_tzs NUMERIC(12, 2) NOT NULL,
    unit TEXT NOT NULL DEFAULT 'Mfuko',
    quantity_available NUMERIC(8, 2) DEFAULT 1,
    region TEXT NOT NULL,
    district TEXT,
    seller_name TEXT DEFAULT 'Mkulima Mzalendo',
    seller_phone TEXT NOT NULL,
    is_buyer_request BOOLEAN DEFAULT false,
    status TEXT DEFAULT 'active',
    image_url TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ----------------------------------------------------------------------------
-- 6. WEATHER ADVISORIES (Taarifa na Ushauri wa Hali ya Hewa kwa Mikoa)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS public.weather_advisories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    region TEXT NOT NULL UNIQUE,
    current_temp INT DEFAULT 28,
    weather_condition TEXT DEFAULT 'Mawingu kiasi na Jua',
    rainfall_forecast TEXT NOT NULL,
    farming_advisory TEXT NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- ----------------------------------------------------------------------------
-- 7. USSD SESSIONS & LOGS (Kumbukumbu za USSD Africa's Talking)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS public.ussd_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id TEXT NOT NULL,
    phone_number TEXT NOT NULL,
    service_code TEXT NOT NULL,
    user_input TEXT,
    response_sent TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================================================
-- 8. ROW LEVEL SECURITY (RLS) POLICIES - OPEN & SECURE ACCESS
-- ============================================================================
ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.market_prices ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.yield_predictions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.crop_diagnoses ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.marketplace_products ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.weather_advisories ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.ussd_logs ENABLE ROW LEVEL SECURITY;

-- Drop existing policies to prevent duplicate errors
DROP POLICY IF EXISTS "Public read prices" ON public.market_prices;
DROP POLICY IF EXISTS "Public write prices" ON public.market_prices;
DROP POLICY IF EXISTS "Public can view market prices" ON public.market_prices;
DROP POLICY IF EXISTS "Public read products" ON public.marketplace_products;
DROP POLICY IF EXISTS "Public write products" ON public.marketplace_products;
DROP POLICY IF EXISTS "Public can view active products" ON public.marketplace_products;
DROP POLICY IF EXISTS "Public read profiles" ON public.profiles;
DROP POLICY IF EXISTS "Public write profiles" ON public.profiles;
DROP POLICY IF EXISTS "Public profiles" ON public.profiles;
DROP POLICY IF EXISTS "Allow read profiles" ON public.profiles;
DROP POLICY IF EXISTS "Allow insert profiles" ON public.profiles;
DROP POLICY IF EXISTS "Allow update profiles" ON public.profiles;
DROP POLICY IF EXISTS "Public read predictions" ON public.yield_predictions;
DROP POLICY IF EXISTS "Public write predictions" ON public.yield_predictions;
DROP POLICY IF EXISTS "Allow insert yield_predictions" ON public.yield_predictions;
DROP POLICY IF EXISTS "Allow read yield_predictions" ON public.yield_predictions;
DROP POLICY IF EXISTS "Public read diagnoses" ON public.crop_diagnoses;
DROP POLICY IF EXISTS "Public write diagnoses" ON public.crop_diagnoses;
DROP POLICY IF EXISTS "Public read weather" ON public.weather_advisories;
DROP POLICY IF EXISTS "Public write ussd_logs" ON public.ussd_logs;

-- Re-create clean comprehensive policies for App & USSD
CREATE POLICY "Public read prices" ON public.market_prices FOR SELECT USING (true);
CREATE POLICY "Public write prices" ON public.market_prices FOR ALL USING (true) WITH CHECK (true);

CREATE POLICY "Public read products" ON public.marketplace_products FOR SELECT USING (true);
CREATE POLICY "Public write products" ON public.marketplace_products FOR ALL USING (true) WITH CHECK (true);

CREATE POLICY "Public read profiles" ON public.profiles FOR SELECT USING (true);
CREATE POLICY "Public write profiles" ON public.profiles FOR ALL USING (true) WITH CHECK (true);

CREATE POLICY "Public read predictions" ON public.yield_predictions FOR SELECT USING (true);
CREATE POLICY "Public write predictions" ON public.yield_predictions FOR ALL USING (true) WITH CHECK (true);

CREATE POLICY "Public read diagnoses" ON public.crop_diagnoses FOR SELECT USING (true);
CREATE POLICY "Public write diagnoses" ON public.crop_diagnoses FOR ALL USING (true) WITH CHECK (true);

CREATE POLICY "Public read weather" ON public.weather_advisories FOR SELECT USING (true);
CREATE POLICY "Public write weather" ON public.weather_advisories FOR ALL USING (true) WITH CHECK (true);

CREATE POLICY "Public write ussd_logs" ON public.ussd_logs FOR ALL USING (true) WITH CHECK (true);

-- ============================================================================
-- 9. POPULATE COMPLETE SEED DATA (MAHINDI & NYANYA TU)
-- ============================================================================

-- Clean up any non-maize and non-tomato entries
DELETE FROM public.market_prices WHERE commodity_name NOT IN ('Mahindi', 'Nyanya');

-- Insert / Refresh Market Prices
INSERT INTO public.market_prices (commodity_name, market_name, region, price_per_kg, unit, price_per_bag, trend, change_percentage, source)
VALUES
    ('Mahindi', 'Soko Kuu la Kariakoo', 'Dar es Salaam', 750.00, 'kg', 75000.00, 'rising', 2.50, 'Soko la Kariakoo'),
    ('Mahindi', 'Soko Kuu la Kibaigwa', 'Dodoma', 680.00, 'kg', 68000.00, 'stable', 0.00, 'Kibaigwa Grain Hub'),
    ('Mahindi', 'Soko Kuu la Soweto', 'Mbeya', 620.00, 'kg', 62000.00, 'falling', -1.80, 'Soweto Market'),
    ('Mahindi', 'Soko Kuu la Arusha', 'Arusha', 700.00, 'kg', 70000.00, 'rising', 1.50, 'Arusha Central'),
    ('Mahindi', 'Soko Kuu la Morogoro', 'Morogoro', 660.00, 'kg', 66000.00, 'stable', 0.00, 'Morogoro Market'),
    ('Nyanya', 'Soko Kuu la Tandale', 'Dar es Salaam', 1400.00, 'kg', 35000.00, 'falling', -5.20, 'Tandale (Kreti Kubwa)'),
    ('Nyanya', 'Soko la Ilala Boma', 'Dar es Salaam', 1500.00, 'kg', 38000.00, 'falling', -3.00, 'Ilala Market'),
    ('Nyanya', 'Soko la Majengo', 'Dodoma', 1200.00, 'kg', 30000.00, 'stable', 0.00, 'Majengo Dodoma'),
    ('Nyanya', 'Soko la Kilombero', 'Arusha', 1300.00, 'kg', 32000.00, 'rising', 3.50, 'Kilombero Market'),
    ('Nyanya', 'Soko Kuu la Mbarali', 'Mbeya', 1100.00, 'kg', 28000.00, 'stable', 0.00, 'Mbarali Farmers Market')
ON CONFLICT DO NOTHING;

-- Insert Marketplace Products & Inputs
INSERT INTO public.marketplace_products (title, description, category, crop_type, price_tzs, unit, region, seller_name, seller_phone, status, is_buyer_request)
VALUES
    ('Mbegu ya Mahindi (DK 777)', 'Mbegu chotara bora ya mahindi inayovumilia ukame, inakomaa siku 110-120 na kutoa magunia 30+ kwa ekari.', 'Mbegu', 'Maize', 16500.00, 'Mfuko 2kg', 'Dodoma', 'Kibaigwa Agrovet', '+255712345678', 'active', false),
    ('Mbegu ya Nyanya (Assila F1)', 'Mbegu bora ya nyanya inayostahimili magonjwa ya ukungu na kutoa nyanya ngumu zenye soko kubwa.', 'Mbegu', 'Tomato', 42000.00, 'Kopo 10g', 'Morogoro', 'Kilimo Bora Supplies', '+255784112233', 'active', false),
    ('Mbolea ya Kupandia (DAP)', 'Mbolea halisi ya fosfati inayojenga mizizi imara kwa mahindi na miche ya nyanya.', 'Mbolea', 'Maize', 88000.00, 'Mfuko 50kg', 'Dodoma', 'TFC Dodoma', '+255755112244', 'active', false),
    ('Dawa ya Viwavi Jeshi (Belt Expert)', 'Inatokomeza viwavi jeshi wavamizi (Fall Armyworm) kwenye mahindi ndani ya saa 24.', 'Dawa', 'Maize', 25000.00, 'Chupa 200ml', 'Arusha', 'Northern Agrovet', '+255755998877', 'active', false),
    ('Dawa ya Ukungu wa Nyanya (Ridomil Gold)', 'Inatibu na kuzuia ukungu wa mapema na chelewa (Early & Late Blight) kwenye nyanya.', 'Dawa', 'Tomato', 32000.00, 'Mfuko 250g', 'Iringa', 'Southern Highlands Inputs', '+255768554321', 'active', false),
    ('Mahindi Makavu Safi (Gunia 100kg)', 'Mahindi meupe safi yaliyokaushwa vizuri, unyevu chini ya 13%. Yapo tayari kwa usafirishaji.', 'Mazao', 'Maize', 72000.00, 'Gunia 100kg', 'Dodoma', 'Mkulima Juma Rashidi', '+255754123456', 'active', false),
    ('Nyanya Ngumu za Kisu (Fresh)', 'Nyanya bora za Assila F1, ngumu na safi zilizovunwa asubuhi. Tayari kwa soko la jumla.', 'Mazao', 'Tomato', 32000.00, 'Kreti Kubwa', 'Morogoro', 'Bwana Shamba Nassoro', '+255714987654', 'active', false),
    ('Nahitaji Magunia 100 ya Mahindi', 'Mnunuzi wa Kariakoo anahitaji magunia 100 ya mahindi meupe haraka. Malipo papo hapo.', 'Mazao', 'Maize', 75000.00, 'Gunia', 'Dar es Salaam', 'Kariakoo Wholesale Trader', '+255762001122', 'active', true),
    ('Nahitaji Kreti 50 za Nyanya', 'Hoteli inahitaji kreti 50 za nyanya ngumu zenye ubora wa juu kila wiki.', 'Mazao', 'Tomato', 34000.00, 'Kreti', 'Arusha', 'Mount Meru Catering', '+255754999888', 'active', true)
ON CONFLICT DO NOTHING;

-- Insert Weather Advisories
INSERT INTO public.weather_advisories (region, current_temp, weather_condition, rainfall_forecast, farming_advisory)
VALUES
    ('Dodoma', 29, 'Jua na Upepo wa Wastani', 'Mvua chache zinatarajiwa wiki hii.', 'Andaa mashamba na chimba mitaro ya kuhifadhi unyevu kwa msimu wa Masika. Tumia mbegu fupi za mahindi kama DK 777.'),
    ('Morogoro', 27, 'Mawingu Kiasi na Mvua Fupi', 'Mvua za wastani zinatarajiwa mwishoni mwa wiki.', 'Muda mzuri wa kupanda mahindi na kuhamishia miche ya nyanya shambani. Hakikisha mifereji ya maji iko safi.'),
    ('Arusha', 23, 'Kibaridi na Ukungu Asubuhi', 'Vipindi vya mvua nyepesi za mara kwa mara.', 'Zingatia kunyunyizia dawa ya ukungu (Ridomil Gold) kwenye nyanya kuzuia ugonjwa wa ukungu wa mapema.'),
    ('Mbeya', 22, 'Mvua Nzuri za Kuridhisha', 'Mvua za kutosha katika wilaya za Mbarali na Rungwe.', 'Muda muafaka wa kuweka mbolea ya kukuzia (UREA/CAN) kwenye mahindi wiki ya 4 hadi 6 baada ya kuota.'),
    ('Dar es Salaam', 31, 'Joto na Mvua za Mara kwa Mara', 'Vipindi vifupi vya manyunyu.', 'Zingatia unyevu wa kutosha kwenye vitalu vya nyanya na zuia viluwiluwi vya inzi weupe (Whiteflies).')
ON CONFLICT (region) DO UPDATE 
SET current_temp = EXCLUDED.current_temp,
    rainfall_forecast = EXCLUDED.rainfall_forecast,
    farming_advisory = EXCLUDED.farming_advisory,
    updated_at = NOW();

-- Verification
SELECT 'MKULIMAAI SUPABASE BACKEND FULLY DEPLOYED!' AS status;
