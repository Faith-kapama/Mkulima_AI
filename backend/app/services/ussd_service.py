class USSDService:

    def handle_request(self, session_id: str, phone_number: str, text: str) -> str:
        text_parts = text.strip().split("*") if text else []

        # Level 0: Root Menu
        if not text:
            return (
                "CON Karibu MkulimaAI (Tanzania)\n"
                "1. Bei za Masoko Leo\n"
                "2. Utabiri wa Mavuno\n"
                "3. Ripoti ya Magonjwa ya Mazao\n"
                "4. Wasiliana na Afisa Kilimo\n"
                "5. Badili Lugha (Swahili/English)"
            )

        # Level 1: Market prices
        if text_parts[0] == "1":
            if len(text_parts) == 1:
                return (
                    "CON Chagua Zao:\n"
                    "1. Mahindi (Kariakoo - DSM)\n"
                    "2. Nyanya (Mwanjelwa - Mbeya)\n"
                    "3. Vitunguu (Tandale)\n"
                    "4. Mchele (Mbeya)"
                )
            elif text_parts[1] == "1":
                return "END Mahindi (Kariakoo): TSh 1,200/kg. Mwenendo: Unapanda. Asante kwa kutumia MkulimaAI!"
            elif text_parts[1] == "2":
                return "END Nyanya (Mwanjelwa): TSh 1,800/kg. Mwenendo: Unashuka kidogo."
            elif text_parts[1] == "3":
                return "END Vitunguu (Tandale): TSh 2,500/kg. Mahitaji ni makubwa."
            elif text_parts[1] == "4":
                return "END Mchele (Mbeya): TSh 2,800/kg. Bei imetulia."

        # Level 1: Yield prediction prompt
        if text_parts[0] == "2":
            if len(text_parts) == 1:
                return "CON Ingiza idadi ya ekari za shamba lako (mfano: 2):"
            else:
                acres = text_parts[1]
                return f"END Kwa ekari {acres}, unakadiriwa kuvuna magunia 40-50 ya Mahindi (Tani 4.5) ukitumia mbolea ya kupandia na kukuzia."

        # Level 1: Disease info
        if text_parts[0] == "3":
            return "END Piga picha jani la mmea wako kupitia App ya MkulimaAI kupata utambuzi wa papo hapo wa vimelea na dawa inayofaa."

        # Level 1: Contact extension officer
        if text_parts[0] == "4":
            return "END Ujumbe umetumwa kwa Afisa Kilimo wa kata yako. Atakupigia simu kupitia namba hii ndani ya masaa 2."

        return "END Chaguo si sahihi. Tafadhali jaribu tena kupiga *384*55#"
