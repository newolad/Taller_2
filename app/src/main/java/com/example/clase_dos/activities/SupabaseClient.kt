package com.example.clase_dos.activities

import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest



object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://oompdxlqxalrrmxfgwmu.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im9vbXBkeGxxeGFscnJteGZnd211Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzU1MTY0NzMsImV4cCI6MjA5MTA5MjQ3M30.tEG2ccOprqQL_2E2RF8pr5aOnTkI_3Em2bSUYgmC4uk"
    ) {
        install(plugin = Auth)
        install(plugin = Postgrest)
        install(plugin = Storage)
    }
}