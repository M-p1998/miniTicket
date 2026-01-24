import React, { createContext, useContext, useEffect, useMemo, useState } from "react";
import keycloak from "./keycloak";

type AuthContextType = {
  isAuthenticated: boolean;
  token: string | null;
  username: string | null;
  login: () => void;
  register: () => void;
  logout: () => void;
};

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [ready, setReady] = useState(false);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [token, setToken] = useState<string | null>(null);
  const [username, setUsername] = useState<string | null>(null);

  useEffect(() => {
    keycloak
      .init({
        onLoad: "check-sso",          // don’t force login on first load
        pkceMethod: "S256",
        checkLoginIframe: false,      // simpler for local dev
      })
      .then((auth) => {
        setIsAuthenticated(auth);
        setToken(keycloak.token ?? null);

        // preferred_username is usually what you want
        const u = (keycloak.tokenParsed as any)?.preferred_username ?? null;
        setUsername(u);

        setReady(true);

        // refresh token periodically
        window.setInterval(async () => {
          try {
            const refreshed = await keycloak.updateToken(60); // refresh if <60s left
            if (refreshed) setToken(keycloak.token ?? null);
          } catch (e) {
            console.error("Token refresh failed", e);
          }
        }, 10_000);
      })
      .catch((err) => {
        console.error("Keycloak init error", err);
        setReady(true);
      });
  }, []);

  const value = useMemo<AuthContextType>(
    () => ({
      isAuthenticated,
      token,
      username,
      login: () => keycloak.login(),
      register: () => keycloak.register(),
      logout: () => keycloak.logout({ redirectUri: "http://localhost:5173/" }),
    }),
    [isAuthenticated, token, username]
  );

  if (!ready) return <div style={{ padding: 24 }}>Loading auth…</div>;

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used inside AuthProvider");
  return ctx;
}
