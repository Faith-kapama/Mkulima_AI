# Mwongozo wa Kuunganisha MkulimaAI na Africa's Talking (USSD Menu)
### Africa's Talking USSD Integration Guide for Tanzania (Vodacom, Airtel, Tigo, Halotel)

---

## 1. Jinsi USSD ya Africa's Talking Inavyofanya Kazi (How It Works)

Wakati mkulima mwenye simu ya tochi (feature phone) au smartphone anapopiga namba ya USSD (k.m. `*384*100#`):
1. **Mnara wa Mawasiliano (Vodacom / Airtel / Tigo / Halotel)** unapokea ombi.
2. Mtandao unapeleka ombi kwa **Africa's Talking Gateway**.
3. **Africa's Talking** inatuma ombi la HTTP `POST` kwenye **Webhook URL** yako (Supabase Edge Function au Node.js Server).
4. Webhook inasoma vigezo:
   - `phoneNumber`: Namba ya simu ya mkulima (mfano: `+255712345678`)
   - `serviceCode`: Namba ya USSD iliyopigwa (mfano: `*384*100#`)
   - `text`: Majibu ambayo mkulima ameingiza, yakiwa yametenganishwa na alama ya nyota `*` (k.m. `"1"`, `"1*2"`, `"2*1*2.5*2"`).
5. Webhook inajibu kwa **Maandishi Tu (Plain Text)**:
   - **`CON `** (Continue) — Ikiwa menyu inaendelea na mkulima anapaswa kuingiza namba au jibu.
   - **`END `** (End) — Ikiwa huduma imekamilika au imeonyesha matokeo, simu inafunga kikao cha USSD.

---

## 2. Hatua kwa Hatua: Kuanza kwenye Africa's Talking (Setup Steps)

### Hatua ya 1: Fungua Akaunti Africa's Talking
1. Tembelea [account.africastalking.com](https://account.africastalking.com/) kisha ujisajili au uingie.
2. Baada ya kuingia, bofya **"Go to Sandbox"** (eneo la majaribio ya bure).

### Hatua ya 2: Tengeneza USSD Service Code (Channel)
1. Kwenye menyu ya kushoto, bofya **USSD** ➡️ **Service Codes**.
2. Bofya kitufe cha **"Channel"** au **"Create Channel"**.
3. Weka maelezo:
   - **Channel**: Chagua namba inayopatikana (mfano: `*384*123#` au namba yoyote ya majaribio inayotolewa bure).
   - **Callback URL**: Hapa unaweka kiungo cha Webhook yako:
     - Ikiwa unatumia Supabase Edge Function: `https://<YOUR-PROJECT-REF>.supabase.co/functions/v1/ussd`
     - Ikiwa unajaribu kwenye kompyuta yako (Localhost): tumia Ngrok (`https://xxxx.ngrok-free.app/ussd`)
4. Bonyeza **Save / Submit**.

### Hatua ya 3: Jaribu Menyu Kwenye Simulator ya Africa's Talking
1. Kwenye dashboard ya Sandbox, bofya **Launch Simulator** (au tembelea [simulator.africastalking.com](https://simulator.africastalking.com/)).
2. Weka namba ya simu ya majaribio ya Tanzania (mfano: `+255712345678`).
3. Bonyeza **Connect**.
4. Kwenye kiolesura cha simu, piga namba yako ya USSD (k.m. `*384*123#`) na ubonyeze **Call**.
5. Utaona menyu ya MkulimaAI inafunguka moja kwa moja!

---

## 3. Menyu ya MkulimaAI USSD (Menu Architecture)

```
*384*XXX#
├── 1. Bei za Mazao Sokoni (Mahindi na Nyanya Tu)
│   ├── Chagua Zao (1. Mahindi, 2. Nyanya)
│   ├── Chagua Soko (1. Kariakoo/Tandale DSM, 2. Kibaigwa/Majengo Dodoma, 3. Arusha, 4. Mbeya)
│   └── [END] Bei halisi (kilo/gunia/kreti) kutoka Supabase + Mwelekeo
│
├── 2. Utabiri wa Mavuno (Mahindi na Nyanya Tu)
│   ├── Chagua Zao (1. Mahindi, 2. Nyanya)
│   ├── Weka Ekari (k.m. 1, 2, 5)
│   ├── Chagua Majira (1. Vuli, 2. Masika, 3. Kipupwe)
│   └── [END] Makisio ya magunia/kreti + Ushauri wa mbolea na viuatilifu
│
├── 3. Taarifa za Hali ya Hewa
│   ├── Chagua Mkoa (1. Dodoma, 2. Morogoro, 3. Arusha, 4. Mbeya, 5. Mwanza)
│   └── [END] Hali ya hewa ya sasa na ushauri wa kilimo
│
├── 4. Sajili Shamba Lako
│   ├── Weka Jina Kamili
│   ├── Chagua Mkoa
│   ├── Weka Ekari
│   └── [END] Taarifa zinahifadhiwa kwenye Supabase moja kwa moja!
│
└── 5. Wasiliana na Afisa Kilimo
    └── [END] Namba ya bure ya huduma na WhatsApp
```

---

## 4. Jinsi ya Ku-deploy kwenye Supabase (Supabase Edge Function)

Mafaili yote yapo tayari kwenye folda ya `/ussd`:
1. `ussd/index.ts` — Deno/Supabase Edge Function.
2. `ussd/server.js` — Node.js Express server.

### Ku-deploy kwa Supabase CLI:
```bash
# Ingia kwenye Supabase
supabase login

# Link mradi wako wa Supabase
supabase link --project-ref <your-project-ref>

# Deploy USSD Edge Function
supabase functions deploy ussd --no-verify-jwt
```
> **Zingatio Muhimu**: Weka alama ya `--no-verify-jwt` kwa sababu Africa's Talking inatuma maombi bila JSON Web Token ya Supabase.

URL yako ya Webhook itakuwa:
```
https://<your-project-ref>.supabase.co/functions/v1/ussd
```
Ibandike hiyo URL moja kwa moja kwenye **Callback URL** ya Africa's Talking!

---

## 5. Mpito wa Kwenda "Live" nchini Tanzania (Production Rollout)
Wakati uko tayari kuzindua kwa wakulima wote:
1. Nenda kwenye **Live Mode** kwenye dashboard ya Africa's Talking.
2. Omba **Dedicated Short Code** (mfano: `*149*88#`) au **Shared USSD Code** (k.m. `*384*55#`).
3. Africa's Talking inashughulikia usajili na **TCRA** pamoja na mitandao yote 4 ya Tanzania:
   - **Vodacom Tanzania**
   - **Airtel Tanzania**
   - **Tigo / Yas Tanzania**
   - **Halotel**
4. Mkulima yeyote akiwa shambani hata bila bando la intaneti ataweza kupata taarifa za soko na mavuno kutoka MkulimaAI!
