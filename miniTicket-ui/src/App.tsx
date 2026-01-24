import React from "react"; // ✅ REQUIRED
import { Routes, Route, Link, Navigate } from "react-router-dom";
import { useAuth } from "./auth/AuthProvider";
import ProfilePage from "./pages/ProfilePage";
import TicketsDemoPage from "./pages/TicketsDemoPage";

function Protected({ children }: { children: JSX.Element }) {
  const { isAuthenticated, login } = useAuth();

  if (!isAuthenticated) {
    login();
    return <div style={{ padding: 24 }}>Redirecting to login…</div>;
  }

  return children;
}

export default function App() {
  const { isAuthenticated, username, login, register, logout } = useAuth();

  return (
    <div style={{ padding: 24, fontFamily: "system-ui" }}>
      <h2>miniTicket UI</h2>

      <div style={{ display: "flex", gap: 12 }}>
        {!isAuthenticated ? (
          <>
            <button onClick={login}>Login</button>
            <button onClick={register}>Register</button>
          </>
        ) : (
          <>
            <span>
              Logged in as <b>{username}</b>
            </span>
            <button onClick={logout}>Logout</button>
          </>
        )}
      </div>

      <hr />

      <Routes>
        <Route path="/" element={<div>Home</div>} />
        <Route
          path="/profile"
          element={
            <Protected>
              <ProfilePage />
            </Protected>
          }
        />
        <Route
          path="/tickets"
          element={
            <Protected>
              <TicketsDemoPage />
            </Protected>
          }
        />
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </div>
  );
}
