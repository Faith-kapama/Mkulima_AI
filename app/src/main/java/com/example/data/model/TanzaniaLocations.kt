package com.example.data.model

object TanzaniaLocations {

  val regionsWithDistricts: Map<String, List<String>> = linkedMapOf(
    "Arusha" to listOf("Arusha City", "Arusha DC", "Meru", "Karatu", "Monduli", "Longido", "Ngorongoro"),
    "Dar es Salaam" to listOf("Ilala", "Kinondoni", "Temeke", "Ubungo", "Kigamboni"),
    "Dodoma" to listOf("Dodoma Mjini", "Bahi", "Chamwino", "Chemba", "Kondoa", "Kongwa", "Mpwapwa"),
    "Geita" to listOf("Geita Mjini", "Geita DC", "Chato", "Bukombe", "Mbogwe", "Nyang'hwale"),
    "Iringa" to listOf("Iringa Mjini", "Iringa DC", "Kilolo", "Mufindi"),
    "Kagera" to listOf("Bukoba Mjini", "Bukoba DC", "Muleba", "Karagwe", "Ngara", "Biharamulo", "Kyerwa", "Missenyi"),
    "Katavi" to listOf("Mpanda Mjini", "Mpanda DC", "Mlele", "Tanganyika"),
    "Kigoma" to listOf("Kigoma Ujiji", "Kasulu Mjini", "Kasulu DC", "Kibondo", "Buhigwe", "Kakonko", "Uvinza"),
    "Kilimanjaro" to listOf("Moshi Mjini", "Moshi Vijijini", "Hai", "Siha", "Rombo", "Mwanga", "Same"),
    "Lindi" to listOf("Lindi Mjini", "Kilwa", "Ruangwa", "Nachingwea", "Liwale"),
    "Manyara" to listOf("Babati Mjini", "Babati DC", "Hanang", "Mbulu", "Simanjiro", "Kiteto"),
    "Mara" to listOf("Musoma Mjini", "Musoma DC", "Tarime", "Rorya", "Serengeti", "Bunda", "Butiama"),
    "Mbeya" to listOf("Mbeya City", "Mbeya DC", "Chunya", "Kyela", "Rungwe", "Mbarali"),
    "Morogoro" to listOf("Morogoro Mjini", "Morogoro DC", "Kilosa", "Kilombero", "Gairo", "Mvomero", "Ulanga", "Malinyi"),
    "Mtwara" to listOf("Mtwara Mjini", "Mtwara DC", "Masasi", "Nanyumbu", "Newala", "Tandahimba"),
    "Mwanza" to listOf("Ilemela", "Nyamagana", "Sengerema", "Magu", "Misungwi", "Kwimba", "Ukerewe"),
    "Njombe" to listOf("Njombe Mjini", "Njombe DC", "Makambako", "Ludewa", "Makete", "Wanging'ombe"),
    "Pwani" to listOf("Kibaha Mjini", "Kibaha DC", "Bagamoyo", "Chalinze", "Kisarawe", "Mkuranga", "Rufiji", "Kibiti", "Mafia"),
    "Rukwa" to listOf("Sumbawanga Mjini", "Sumbawanga DC", "Kalambo", "Nkasi"),
    "Ruvuma" to listOf("Songea Mjini", "Songea DC", "Mbinga", "Tunduru", "Namtumbo", "Nyasa"),
    "Shinyanga" to listOf("Shinyanga Mjini", "Shinyanga DC", "Kishapu", "Kahama Mjini", "Ushetu", "Msalala"),
    "Simiyu" to listOf("Bariadi", "Busega", "Itilima", "Maswa", "Meatu"),
    "Singida" to listOf("Singida Mjini", "Singida DC", "Iramba", "Manyoni", "Ikungi", "Mkalama", "Itigi"),
    "Songwe" to listOf("Vwawa", "Mbozi", "Momba", "Ileje", "Songwe DC"),
    "Tabora" to listOf("Tabora Mjini", "Igunga", "Nzega", "Sikonge", "Urambo", "Uyui", "Kaliua"),
    "Tanga" to listOf("Tanga City", "Muheza", "Korogwe", "Lushoto", "Handeni", "Pangani", "Kilindi", "Mkinga", "Bumbuli"),
    "Zanzibar - Mjini Magharibi" to listOf("Mjini", "Magharibi A", "Magharibi B"),
    "Zanzibar - Kaskazini Unguja" to listOf("Kaskazini A", "Kaskazini B"),
    "Zanzibar - Kusini Unguja" to listOf("Kusini", "Kati"),
    "Pemba - Kaskazini" to listOf("Wete", "Micheweni"),
    "Pemba - Kusini" to listOf("Chake Chake", "Mkoani")
  )

  val allRegions: List<String> = regionsWithDistricts.keys.toList()

  fun getDistricts(region: String): List<String> {
    return regionsWithDistricts[region] ?: listOf("Mjini / Central")
  }

  val popularCrops: List<Pair<String, String>> = listOf(
    "Maize" to "Mahindi",
    "Tomato" to "Nyanya"
  )

  val farmingTypes: List<Pair<String, String>> = listOf(
    "Smallholder Farmer" to "Kilimo cha Kujikimu (< 5 Ekari)",
    "Commercial Farmer" to "Kilimo cha Biashara (5+ Ekari)",
    "Horticulture & Greenhouse" to "Bustani na Greenhouse",
    "Mixed Crop & Livestock" to "Mazao na Mifugo"
  )

  val waterSources: List<Pair<String, String>> = listOf(
    "Rainfed" to "Mvua za Msimu",
    "Irrigation Scheme" to "Mfereji wa Umwagiliaji",
    "Borehole / Well" to "Kisima cha Maji",
    "River / Lake Pump" to "Mto / Ziwa"
  )
}
