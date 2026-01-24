import { useEffect, useState } from "react";
import { useAuth } from "../auth/AuthProvider";
import { apiFetch, GATEWAY_BASE } from "../api/http";

type UserResponse = {
  id: number;
  username: string;
  email: string;
  title: string;
  funFacts: string | null;
  createdAt: string;
};

export default function ProfilePage() {
  const { token, username } = useAuth();
  const [profile, setProfile] = useState<UserResponse | null>(null);
  const [status, setStatus] = useState<string>("Loading…");

  useEffect(() => {
    if (!token || !username) return;

    const run = async () => {
      try {
        // 1) try fetch profile
        const res = await fetch(`${GATEWAY_BASE}/api/users/by-username/${username}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (res.status === 404) {
          // 2) create profile if missing
          // NOTE: email is not guaranteed unless you configure it in Keycloak and the user has email
          // We'll pull email from token if present; otherwise you can prompt UI.
          const parsed = JSON.parse(atob(token.split(".")[1]));
          const emailFromToken = parsed.email ?? `${username}@example.com`;

          const created = await apiFetch<UserResponse>(
            `${GATEWAY_BASE}/api/users`,
            token,
            {
              method: "POST",
              body: JSON.stringify({
                username,
                email: emailFromToken,
                title: "Engineer",
                funFacts: "Created from Keycloak login",
              }),
            }
          );
          setProfile(created);
          setStatus("Profile created ✅");
          return;
        }

        if (!res.ok) {
          const text = await res.text();
          throw new Error(`GET profile failed: ${res.status} ${text}`);
        }

        const data = (await res.json()) as UserResponse;
        setProfile(data);
        setStatus("Profile loaded ✅");
      } catch (e: any) {
        console.error(e);
        setStatus(e.message ?? "Error");
      }
    };

    run();
  }, [token, username]);

  return (
    <div>
      <h3>Profile</h3>
      <div>{status}</div>

      {profile && (
        <pre style={{ background: "#f6f6f6", padding: 12 }}>
          {JSON.stringify(profile, null, 2)}
        </pre>
      )}
    </div>
  );
}
