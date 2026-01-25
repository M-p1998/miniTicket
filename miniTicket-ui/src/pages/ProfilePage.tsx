import React,{ useEffect, useState } from "react";
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
  const [title, setTitle] = useState("");
  const [funFacts, setFunFacts] = useState("");
  const [status, setStatus] = useState("Loading profile...");

  useEffect(() => {
  if (!token) return;

  const run = async () => {
    try {
      // 1️⃣ Try load profile
      const res = await fetch(`${GATEWAY_BASE}/api/users/me`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (res.ok) {
        const data = await res.json();
        setProfile(data);
        setTitle(data.title ?? "");
        setFunFacts(data.funFacts ?? "");
        setStatus("Profile loaded");
        return;
      }

      // 2️⃣ Only create on REAL 404
      if (res.status === 404) {
        const parsed = JSON.parse(atob(token.split(".")[1]));
        const emailFromToken = parsed.email ?? "unknown@example.com";
        const usernameFromToken =
          parsed.preferred_username ?? parsed.sub;

        const created = await apiFetch<UserResponse>(
          `${GATEWAY_BASE}/api/users`,
          token,
          {
            method: "POST",
            body: JSON.stringify({
              username: usernameFromToken,
              email: emailFromToken,
              title: "",
              funFacts: "",
            }),
          }
        );

        setProfile(created);
        setTitle(created.title ?? "");
        setFunFacts(created.funFacts ?? "");
        setStatus("Profile created");
        return;
      }

      throw new Error(`Unexpected status ${res.status}`);
    } catch (e: any) {
      console.error(e);
      setStatus("Failed to load profile");
    }
  };

  run();
}, [token]);


  const saveProfile = async () => {
    if (!profile || !token) return;

    try {
      const updated = await apiFetch<UserResponse>(
        `${GATEWAY_BASE}/api/users/${profile.id}`,
        token,
        {
          method: "PUT",
          body: JSON.stringify({
            // username: profile.username,
            // email: profile.email,
            title,
            funFacts,
          }),
        }
      );

      setProfile(updated);
      setStatus("Profile saved");
    } catch (e: any) {
      console.error(e);
      setStatus("Save failed");
    }
  };

  return (
    <div style={{ maxWidth: 500 }}>
      <h3>My Profile</h3>
      <p>{status}</p>

      {profile && (
        <>
          <input value={profile.username} disabled />
          <input value={profile.email} disabled />

          <input value={title} onChange={(e) => setTitle(e.target.value)} />
          <textarea
            value={funFacts}
            onChange={(e) => setFunFacts(e.target.value)}
          />

          <button onClick={saveProfile}>Save</button>
        </>
      )}
    </div>
  );
}
