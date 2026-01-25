import React,{ useEffect, useState } from "react";
import { useAuth } from "../auth/AuthProvider";
import { apiFetch, GATEWAY_BASE } from "../api/http";
import "../styles/ProfilePage.css"


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
    <div className="profile-container">
    <h2 className="profile-title">My Profile</h2>
    <p className="profile-status">{status}</p>

    {profile && (
      <div className="profile-card">
        <div className="profile-row">
          <label>Username</label>
          <input value={profile.username} disabled />
        </div>

        <div className="profile-row">
          <label>Email</label>
          <input value={profile.email} disabled />
        </div>

        <div className="profile-row">
          <label>Title</label>
          <input
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="Your role or title"
          />
        </div>

        <div className="profile-row">
          <label>Fun Facts</label>
          <textarea
            value={funFacts}
            onChange={(e) => setFunFacts(e.target.value)}
            placeholder="Tell us something interesting about you…"
            rows={4}
          />
        </div>

        <div className="profile-actions">
          <button onClick={saveProfile}>Save Changes</button>
        </div>
      </div>
    )}
  </div>
  );
}
