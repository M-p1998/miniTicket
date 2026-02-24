// export async function apiFetch<T>(
//   url: string,
//   token: string | null,
//   options: RequestInit = {}
// ): Promise<T> {
//   const headers = new Headers(options.headers);

//   headers.set("Content-Type", "application/json");
//   if (token) headers.set("Authorization", `Bearer ${token}`);

//   const res = await fetch(url, { ...options, headers });

//   if (!res.ok) {
//     const text = await res.text();
//     throw new Error(`HTTP ${res.status}: ${text}`);
//   }
//   if (res.status === 204) {
//     return null as T;
//   }

//   return (await res.json()) as T;
// }

// export const GATEWAY_BASE = "http://localhost:9001";


// src/api/http.ts
// import keycloak from "../auth/keycloak";

// export async function apiFetch<T>(
//   url: string,
//   options: RequestInit = {}
// ): Promise<T> {
//   // 🔑 Ensure token is fresh
//   if (!keycloak.authenticated) {
//     throw new Error("User not authenticated");
//   }

//   await keycloak.updateToken(30);

//   const headers = new Headers(options.headers);
//   headers.set("Content-Type", "application/json");
//   headers.set("Authorization", `Bearer ${keycloak.token}`);

//   const res = await fetch(url, {
//     ...options,
//     headers,
//   });

//   if (!res.ok) {
//     const text = await res.text();
//     throw new Error(`HTTP ${res.status}: ${text}`);
//   }

//   if (res.status === 204) {
//     return null as T;
//   }

//   return (await res.json()) as T;
// }

// export const GATEWAY_BASE = "http://localhost:9001";


import keycloak from "../auth/keycloak";

export async function apiFetch<T>(
  url: string,
  options: RequestInit = {}
): Promise<T> {
  if (!keycloak.authenticated) {
    throw new Error("Not authenticated");
  }

  // Ensure token is fresh
  await keycloak.updateToken(30);

  const headers = new Headers(options.headers);
  headers.set("Authorization", `Bearer ${keycloak.token}`);
  headers.set("Content-Type", "application/json");

  const response = await fetch(url, {
    ...options,
    headers,
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(`HTTP ${response.status}: ${text}`);
  }

  // Handle empty responses (204)
  if (response.status === 204) {
    return null as T;
  }

  return response.json();
}

export const GATEWAY_BASE = "https://api.miniticket.online";
// export const GATEWAY_BASE = "http://20.190.206.2:9001";
