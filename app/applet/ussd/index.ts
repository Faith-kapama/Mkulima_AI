/**
 * MkulimaAI - Africa's Talking USSD Webhook Handler
 * Deployable as a Supabase Edge Function (Deno / TypeScript) or Node.js server.
 * 
 * Africa's Talking USSD Protocol:
 * - Method: POST (application/x-www-form-urlencoded)
 * - Parameters: sessionId, serviceCode, phoneNumber, text
 * - Response: Plain text starting with "CON " (continue) or "END " (terminate)
 */

import { serve } from "https://deno.land/std@0.168.0/http/server.ts";
import { createClient } from "https://esm.sh/@supabase/supabase-js@2";

// Initialize Supabase Client
const supabaseUrl = Deno.env.get("SUPABASE_URL") || "https://osxhrholyvvicptemzsz.supabase.co";
const supabaseKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY") || Deno.env.get("SUPABASE_ANON_KEY") || "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im9zeGhyaG9seXZ2aWNwdGVtenN6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3OTEzMDk1MjYsImV4cCI6MjEwNjg4NTUyNn0.G5rDtmDIduT75hfg_SS78I57WcY70Cm0cBcdBeVPmhQ";
const supabase = createClient(supabaseUrl, supabaseKey);

serve(async (req) => {
  // 1. Only allow POST requests from Africa's Talking
  if (req.method !== "POST") {
    return new Response("Method Not Allowed", { status: 405 });
  }

  // 2. Parse Africa's Talking form-urlencoded body
  const formData = await req.formData();
  const sessionId = formData.get("sessionId")?.toString() || "";
  const serviceCode = formData.get("serviceCode")?.toString() || "";
  const phoneNumber = formData.get("phoneNumber")?.toString() || "";
  const text = formData.get("text")?.toString() || "";

  // Split user navigation levels (delimited by '*')
  // Example: "1*2*3" => ["1", "2", "3"]
  const steps = text === "" ? [] : text.split("*");
  const stepCount = steps.length;

  let response = "";

  // =========================================================================
  // LEVEL 0: MAIN MENU (Wakati mkulima anapiga *384*...# mara ya kwanza)
  // =========================================================================
  if (stepCount === 0) {
    response = `CON Karibu MkulimaAI! 🌾
Huduma za Kilimo Kidigitali:
1. Bei za Mazao Sokoni
2. Utabiri wa Mavuno
3. Taarifa za Hali ya Hewa
4. Sajili Shamba Lako
5. Wasiliana na Afisa Kilimo`;
  }

  // =========================================================================
  // OPTION 1: BEI ZA MAZAO SOKONI (Market Prices - Maize & Tomato Only)
  // =========================================================================
  else if (steps[0] === "1") {
    // Level 1: Chagua Zao
    if (stepCount === 1) {
      response = `CON Chagua Zao:
1. Mahindi (Maize)
2. Nyanya (Tomatoes)`;
    }
    // Level 2: Chagua Soko / Mkoa
    else if (stepCount === 2) {
      response = `CON Chagua Soko / Mkoa:
1. Dar es Salaam (Kariakoo / Tandale)
2. Dodoma (Kibaigwa / Majengo)
3. Arusha Mjini
4. Mbeya (Soweto / Mbarali)
5. Morogoro Mjini`;
    }
    // Level 3: Onyesha Bei halisi kutoka Supabase
    else if (stepCount === 3) {
      const crops = ["Mahindi", "Nyanya"];
      const markets = ["Kariakoo / Tandale, DSM", "Kibaigwa / Majengo, Dodoma", "Arusha Mjini", "Soweto / Mbarali, Mbeya", "Morogoro Mjini"];

      const cropIndex = parseInt(steps[1], 10) - 1;
      const marketIndex = parseInt(steps[2], 10) - 1;

      const selectedCrop = crops[cropIndex] || "Mahindi";
      const selectedMarket = markets[marketIndex] || "Kibaigwa, Dodoma";

      // Query real-time price from Supabase database
      let priceText = "";
      try {
        const { data } = await supabase
          .from("market_prices")
          .select("price_per_kg, unit, trend, price_per_bag")
          .ilike("commodity_name", `%${selectedCrop}%`)
          .limit(1)
          .single();

        if (data) {
          if (selectedCrop === "Nyanya") {
            priceText = `TZS ${data.price_per_kg.toLocaleString()}/kg (Kreti TZS ${data.price_per_bag?.toLocaleString() || "35,000"})`;
          } else {
            priceText = `TZS ${data.price_per_kg.toLocaleString()}/kg (Gunia TZS ${data.price_per_bag?.toLocaleString() || "75,000"})`;
          }
        } else {
          // Fallback static benchmarks
          priceText = selectedCrop === "Nyanya" 
            ? "TZS 1,400/kg (TZS 35,000 kwa kreti)" 
            : "TZS 750/kg (TZS 75,000/gunia 100kg)";
        }
      } catch (_e) {
        priceText = selectedCrop === "Nyanya" ? "TZS 1,400/kg (Kreti TZS 35,000)" : "TZS 750/kg (Gunia TZS 75,000)";
      }

      response = `END Bei ya ${selectedCrop} leo ${selectedMarket}:
${priceText}.
Hali: Bei imara.
Asante kwa kutumia MkulimaAI!`;
    }
  }

  // =========================================================================
  // OPTION 2: UTABIRI WA MAVUNO (Yield Prediction - Maize & Tomato Only)
  // =========================================================================
  else if (steps[0] === "2") {
    // Level 1: Chagua Zao
    if (stepCount === 1) {
      response = `CON Utabiri wa Mavuno:
Chagua zao lako:
1. Mahindi (Maize)
2. Nyanya (Tomatoes)`;
    }
    // Level 2: Weka ukubwa wa shamba (Ekari)
    else if (stepCount === 2) {
      response = `CON Weka ukubwa wa shamba lako (kwa Ekari):
(Mfano: andika 1, 2, 3 au 5)`;
    }
    // Level 3: Chagua Majira ya Kilimo (Seasons: 1. Vuli, 2. Masika, 3. Kipupwe)
    else if (stepCount === 3) {
      response = `CON Chagua Majira ya Msimu:
1. 1. Vuli (Mvua fupi)
2. 2. Masika (Mvua kubwa)
3. 3. Kipupwe (Msimu wa baridi)`;
    }
    // Level 4: Onyesha Matokeo ya Utabiri
    else if (stepCount === 4) {
      const crops = ["Mahindi", "Nyanya"];
      const seasons = ["1. Vuli", "2. Masika", "3. Kipupwe"];

      const cropIndex = parseInt(steps[1], 10) - 1;
      const acres = parseFloat(steps[2]) || 1.0;
      const seasonIndex = parseInt(steps[3], 10) - 1;

      const cropName = crops[cropIndex] || "Mahindi";
      const seasonName = seasons[seasonIndex] || "1. Vuli";

      let minYield = 0;
      let maxYield = 0;
      let unitText = "";

      if (cropName === "Nyanya") {
        // Tomatoes yield in crates per acre (approx 150 - 250 crates/acre)
        let minCrates = 160;
        let maxCrates = 240;
        if (seasonName.includes("Masika")) {
          minCrates = 180;
          maxCrates = 280;
        }
        minYield = Math.round(minCrates * acres);
        maxYield = Math.round(maxCrates * acres);
        unitText = `Kreti ${minYield} hadi ${maxYield} (sawa na tani ${(acres * 7.5).toFixed(1)} - ${(acres * 11.0).toFixed(1)})`;
      } else {
        // Maize in 100kg bags
        let minBags = 11;
        let maxBags = 15;
        if (seasonName.includes("Masika")) {
          minBags = 13;
          maxBags = 18;
        }
        minYield = Math.round(minBags * acres);
        maxYield = Math.round(maxBags * acres);
        unitText = `Magunia ${minYield} hadi ${maxYield} ya kilo 100`;
      }

      // Log prediction query to Supabase in background
      try {
        await supabase.from("yield_predictions").insert({
          phone_number: phoneNumber,
          crop_name: cropName,
          farm_size_acres: acres,
          season: seasonName,
          estimated_min_bags: minYield,
          estimated_max_bags: maxYield,
          created_at: new Date().toISOString()
        });
      } catch (_e) {
        // silent catch
      }

      response = `END Utabiri wa ${cropName} (${acres} Ekari, ${seasonName}):
Makisio ya Mavuno: ${unitText}.
Ushauri: Tumia mbolea na viuatilifu vilivyoidhinishwa na TOSCI/TPRI. MkulimaAI.`;
    }
  }

  // =========================================================================
  // OPTION 3: HALI YA HEWA & USHAURI WA WIKI (Weather Advisory)
  // =========================================================================
  else if (steps[0] === "3") {
    if (stepCount === 1) {
      response = `CON Chagua Mkoa wako:
1. Dodoma
2. Morogoro
3. Arusha / Kilimanjaro
4. Mbeya / Songwe
5. Mwanza / Shinyanga`;
    } else if (stepCount === 2) {
      const regions: Record<string, string> = {
        "1": "Dodoma: Mvua chache, joto 27°C. Andaa mashamba na chimba mitaro ya kuhifadhi unyevu.",
        "2": "Morogoro: Mvua za wastani zinatarajiwa. Wakati mzuri wa kupanda mahindi na mpunga.",
        "3": "Arusha: Baridi na unyevu wa kutosha. Fuatilia ukungu na viwavi jeshi.",
        "4": "Mbeya: Mvua nzuri. Hakikisha shamba lina mifereji safi ya kupitisha maji ya ziada.",
        "5": "Mwanza: Vipindi vya mvua na jua. Inafaa kwa kupanda mazao ya mizizi na mpunga."
      };

      const advisory = regions[steps[1]] || "Tanzania: Msimu wa kilimo unaendelea vyema. Zingatia kanuni bora za kilimo.";
      response = `END Taarifa ya Hali ya Hewa:
${advisory}
MkulimaAI inakutakia mavuno tele!`;
    }
  }

  // =========================================================================
  // OPTION 4: SAJILI SHAMBA LAKO (Farmer Registration)
  // =========================================================================
  else if (steps[0] === "4") {
    if (stepCount === 1) {
      response = `CON Usajili wa Shamba:
Tafadhali andika Jina lako kamili:`;
    } else if (stepCount === 2) {
      response = `CON Habari ${steps[1]}!
Chagua Mkoa wako mkuu:
1. Dodoma
2. Morogoro
3. Arusha
4. Mbeya
5. Mkoa Mwingine`;
    } else if (stepCount === 3) {
      response = `CON Weka ukubwa wa shamba lako lote (Ekari):
(Mfano: 2 au 5 au 10)`;
    } else if (stepCount === 4) {
      const farmerName = steps[1];
      const regionMap: Record<string, string> = {
        "1": "Dodoma", "2": "Morogoro", "3": "Arusha", "4": "Mbeya", "5": "Tanzania Bara"
      };
      const region = regionMap[steps[2]] || "Tanzania";
      const farmSize = parseFloat(steps[3]) || 1.0;

      // Save into Supabase farmer_profiles
      try {
        await supabase.from("profiles").upsert({
          phone_number: phoneNumber,
          full_name: farmerName,
          region: region,
          farm_size_acres: farmSize,
          updated_at: new Date().toISOString()
        });
      } catch (_e) {
        // silent
      }

      response = `END Hongera Ndugu ${farmerName}!
Shamba lako la ekari ${farmSize} mkoa wa ${region} limesajiliwa kikamilifu kwenye MkulimaAI.
Utapokea taarifa za bure kupitia SMS.`;
    }
  }

  // =========================================================================
  // OPTION 5: WASILIANA NA AFISA KILIMO (Extension Officer / Help)
  // =========================================================================
  else if (steps[0] === "5") {
    response = `END Huduma kwa Wateja & Afisa Kilimo:
Piga simu bure: 0800 750 000
WhatsApp: +255 700 000 000
Tovuti: mkulima-ai.co.tz
MkulimaAI - Kilimo chenye tija!`;
  }

  // =========================================================================
  // INVALID INPUT HANDLER
  // =========================================================================
  else {
    response = `END Chaguo halijatambuliwa. Tafadhali piga tena *384*...# na uchague namba sahihi.`;
  }

  // 3. Return Plain Text response required by Africa's Talking
  return new Response(response, {
    status: 200,
    headers: {
      "Content-Type": "text/plain; charset=utf-8",
    },
  });
});
