/**
 * MkulimaAI - Africa's Talking USSD Webhook Server (Node.js + Express)
 * Run locally with: node server.js
 * Or deploy to Render / Railway / Heroku / DigitalOcean
 */

const express = require('express');
const bodyParser = require('body-parser');
const { createClient } = require('@supabase/supabase-js');

const app = express();
const port = process.env.PORT || 3000;

// Africa's Talking sends x-www-form-urlencoded payloads
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

// Supabase integration with live project defaults
const supabaseUrl = process.env.SUPABASE_URL || 'https://osxhrholyvvicptemzsz.supabase.co';
const supabaseKey = process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.SUPABASE_ANON_KEY || 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im9zeGhyaG9seXZ2aWNwdGVtenN6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3OTEzMDk1MjYsImV4cCI6MjEwNjg4NTUyNn0.G5rDtmDIduT75hfg_SS78I57WcY70Cm0cBcdBeVPmhQ';
const supabase = createClient(supabaseUrl, supabaseKey);

app.post('/ussd', async (req, res) => {
  // Read parameters sent by Africa's Talking
  const { sessionId, serviceCode, phoneNumber, text } = req.body;

  const steps = text ? text.split('*') : [];
  const stepCount = steps.length;
  let response = '';

  // LEVEL 0: Main Menu
  if (stepCount === 0) {
    response = `CON Karibu MkulimaAI! 🌾
1. Bei za Mazao Sokoni
2. Utabiri wa Mavuno
3. Taarifa za Hali ya Hewa
4. Sajili Shamba Lako
5. Wasiliana na Afisa Kilimo`;
  }
  // OPTION 1: Bei za Mazao (Maize & Tomato Only)
  else if (steps[0] === '1') {
    if (stepCount === 1) {
      response = `CON Chagua Zao:
1. Mahindi (Maize)
2. Nyanya (Tomatoes)`;
    } else if (stepCount === 2) {
      response = `CON Chagua Soko:
1. Dar es Salaam (Kariakoo / Tandale)
2. Dodoma (Kibaigwa / Majengo)
3. Arusha Mjini
4. Mbeya (Soweto / Mbarali)`;
    } else if (stepCount === 3) {
      const crops = ['Mahindi', 'Nyanya'];
      const markets = ['Kariakoo / Tandale, DSM', 'Kibaigwa / Majengo, Dodoma', 'Arusha Mjini', 'Soweto / Mbarali, Mbeya'];
      const crop = crops[parseInt(steps[1], 10) - 1] || 'Mahindi';
      const market = markets[parseInt(steps[2], 10) - 1] || 'Kibaigwa';

      const prices = {
        'Mahindi': 'TZS 750/kg (TZS 75,000 gunia la 100kg)',
        'Nyanya': 'TZS 1,400/kg (TZS 35,000 kwa kreti)'
      };

      response = `END Bei ya ${crop} leo (${market}):
${prices[crop] || 'TZS 750/kg'}.
Mwelekeo: Bei imara.
MkulimaAI.`;
    }
  }
  // OPTION 2: Utabiri wa Mavuno (Seasons: 1. Vuli, 2. Masika, 3. Kipupwe - Maize & Tomato Only)
  else if (steps[0] === '2') {
    if (stepCount === 1) {
      response = `CON Utabiri wa Mavuno:
Chagua zao:
1. Mahindi (Maize)
2. Nyanya (Tomatoes)`;
    } else if (stepCount === 2) {
      response = `CON Weka ukubwa wa shamba (Ekari):
(Mfano: 1, 2 au 5)`;
    } else if (stepCount === 3) {
      response = `CON Chagua Majira ya Msimu:
1. 1. Vuli (Mvua fupi)
2. 2. Masika (Mvua kubwa)
3. 3. Kipupwe (Baridi)`;
    } else if (stepCount === 4) {
      const crops = ['Mahindi', 'Nyanya'];
      const seasons = ['1. Vuli', '2. Masika', '3. Kipupwe'];
      const crop = crops[parseInt(steps[1], 10) - 1] || 'Mahindi';
      const acres = parseFloat(steps[2]) || 1;
      const season = seasons[parseInt(steps[3], 10) - 1] || '1. Vuli';

      let yieldText = '';
      if (crop === 'Nyanya') {
        let minCrates = Math.round(160 * acres);
        let maxCrates = Math.round(240 * acres);
        if (season.includes('Masika')) {
          minCrates = Math.round(minCrates * 1.15);
          maxCrates = Math.round(maxCrates * 1.15);
        }
        yieldText = `Kreti ${minCrates} - ${maxCrates} (sawa na tani ${(acres * 7.5).toFixed(1)} - ${(acres * 11).toFixed(1)})`;
      } else {
        let minBags = Math.round(11 * acres);
        let maxBags = Math.round(15 * acres);
        if (season.includes('Masika')) {
          minBags = Math.round(minBags * 1.2);
          maxBags = Math.round(maxBags * 1.2);
        }
        yieldText = `Magunia ${minBags} - ${maxBags} ya kilo 100`;
      }

      response = `END Utabiri wa ${crop} (${acres} Ekari, ${season}):
Makisio: ${yieldText}.
Ushauri: Tumia mbegu zilizothibitishwa na TOSCI. MkulimaAI.`;
    }
  }
  // OPTION 3: Hali ya Hewa
  else if (steps[0] === '3') {
    if (stepCount === 1) {
      response = `CON Chagua Mkoa wako:
1. Dodoma
2. Morogoro
3. Arusha
4. Mbeya
5. Mwanza`;
    } else if (stepCount === 2) {
      const regions = {
        '1': 'Dodoma: Mvua chache, 28°C. Andaa mashamba na chimba mitaro ya kuhifadhi unyevu.',
        '2': 'Morogoro: Mvua za wastani. Wakati mzuri wa kupanda mahindi na mpunga.',
        '3': 'Arusha: Baridi na unyevu wa wastani. Fuatilia wadudu na magonjwa ya majani.',
        '4': 'Mbeya: Mvua za kuridhisha. Zingatia mbolea ya kupandia kwa wakati.',
        '5': 'Mwanza: Vipindi vya mvua na jua. Inafaa kwa mazao ya mizizi na mpunga.'
      };
      response = `END Hali ya Hewa:
${regions[steps[1]] || 'Msimu unaendelea vizuri.'}
MkulimaAI.`;
    }
  }
  // OPTION 4: Sajili Shamba
  else if (steps[0] === '4') {
    if (stepCount === 1) {
      response = `CON Usajili wa Mkulima:
Andika Jina lako kamili:`;
    } else if (stepCount === 2) {
      response = `CON Chagua Mkoa:
1. Dodoma
2. Morogoro
3. Arusha
4. Mbeya`;
    } else if (stepCount === 3) {
      response = `CON Weka ukubwa wa shamba (Ekari):`;
    } else if (stepCount === 4) {
      const farmerName = steps[1];
      const farmSize = steps[3];
      response = `END Hongera ${farmerName}!
Shamba lako (${farmSize} Ekari) limesajiliwa kikamilifu kwenye MkulimaAI. Utapokea SMS za ushauri.`;
    }
  }
  // OPTION 5: Msaada
  else if (steps[0] === '5') {
    response = `END Huduma ya Wateja & Afisa Kilimo:
Simu bure: 0800 750 000
WhatsApp: +255 700 000 000
MkulimaAI - Kilimo chenye Tija.`;
  } else {
    response = `END Namba uliyoingiza si sahihi. Piga tena ${serviceCode}.`;
  }

  // Africa's Talking requires text/plain Content-Type
  res.set('Content-Type', 'text/plain; charset=utf-8');
  res.send(response);
});

app.listen(port, () => {
  console.log(`MkulimaAI USSD server running on port ${port}`);
});
