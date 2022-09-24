import { createClient } from "@supabase/supabase-js";

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
const supabaseUrl = import.meta.env.VITE_SUPABASE_URL || "";
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
const supabaseAnonKey = import.meta.env.VITE_SUPABASE_ANON_KEY || "";
export const supabase = createClient(supabaseUrl, supabaseAnonKey);
