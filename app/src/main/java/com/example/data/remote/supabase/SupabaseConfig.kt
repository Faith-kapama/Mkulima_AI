package com.example.data.remote.supabase

import android.util.Log

/**
 * Supabase configuration credentials for MkulimaAI.
 * 
 * To connect your live Supabase project:
 * 1. Go to your Supabase Dashboard -> Project Settings -> API.
 * 2. Copy the "Project URL" and paste it into [url].
 * 3. Copy the "anon / public key" and paste it into [anonKey].
 * 
 * You can also set these in .env as SUPABASE_URL and SUPABASE_ANON_KEY.
 */
object SupabaseConfig {

  // Live user Supabase project
  @Volatile
  var url: String = "https://osxhrholyvvicptemzsz.supabase.co"

  // Live anon public key
  @Volatile
  var anonKey: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im9zeGhyaG9seXZ2aWNwdGVtenN6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3OTEzMDk1MjYsImV4cCI6MjEwNjg4NTUyNn0.G5rDtmDIduT75hfg_SS78I57WcY70Cm0cBcdBeVPmhQ"

  val isConfigured: Boolean
    get() = url.isNotBlank() && 
            !url.contains("your-project-id") && 
            anonKey.isNotBlank() && 
            !anonKey.contains("your-anon-key")

  fun configure(supabaseUrl: String, supabaseAnonKey: String) {
    url = supabaseUrl.trim().removeSuffix("/")
    anonKey = supabaseAnonKey.trim()
    Log.d("SupabaseConfig", "Supabase configured: url=$url, isConfigured=$isConfigured")
  }
}
