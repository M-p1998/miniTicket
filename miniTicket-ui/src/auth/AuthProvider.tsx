import React, {
  createContext,
  useContext,
  useEffect,
  useMemo,
  useRef,
  useState,
} from "react";
import keycloak from "./keycloak";

/* ================= TYPES ================= */

export type AuthContextType = {
  isAuthenticated: boolean;
  token: string | null;
  username: string | null;
  email: string | null;
  login: () => void;
  register: () => void;
  logout: () => void;
};

/* ================= CONTEXT ================= */

const AuthContext = createContext<AuthContextType | null>(null);

/* ================= PROVIDER ================= */

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const initialized = useRef(false); // ✅ prevents double init

  const [ready, setReady] = useState(false);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [token, setToken] = useState<string | null>(null);
  const [username, setUsername] = useState<string | null>(null);
  const [email, setEmail] = useState<string | null>(null);

  useEffect(() => {
    if (initialized.current) return;
    initialized.current = true;

    keycloak
      .init({
        onLoad: "check-sso",
        pkceMethod: "S256",
        checkLoginIframe: false,
      })
      .then((auth) => {
        setIsAuthenticated(auth);
        setToken(keycloak.token ?? null);

        const parsed: any = keycloak.tokenParsed ?? {};
        setUsername(parsed.preferred_username ?? null);
        setEmail(parsed.email ?? null);

        setReady(true);

        // refresh token
        setInterval(async () => {
          try {
            const refreshed = await keycloak.updateToken(60);
            if (refreshed) {
              setToken(keycloak.token ?? null);
              const p: any = keycloak.tokenParsed ?? {};
              setUsername(p.preferred_username ?? null);
              setEmail(p.email ?? null);
            }
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
      email,
      login: () => keycloak.login(),
      register: () => keycloak.register(),
      logout: () =>
        keycloak.logout({ redirectUri: "http://localhost:5173/" }),
    }),
    [isAuthenticated, token, username, email]
  );

  if (!ready) return <div style={{ padding: 24 }}>Loading auth…</div>;

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

/* ================= HOOK ================= */

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used inside AuthProvider");
  return ctx;
}
