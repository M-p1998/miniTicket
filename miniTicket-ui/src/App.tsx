import React from "react"; 
import { Routes, Route, Link, Navigate } from "react-router-dom";
import { useAuth } from "./auth/AuthProvider";
import ProfilePage from "./pages/ProfilePage";
import CreateTicketPage from "./pages/CreateTicketPage";
import TicketsDashboardPage from "./pages/TicketsDashboardPage";
import TicketDetailPage from "./pages/TicketDetailPage";


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
            <Link to="/profile">Profile</Link>
            <Link to="/tickets/create">Create Ticket</Link>
            <Link to="/tickets">Tickets</Link>
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
          path="/tickets/create"
          element={
            <Protected>
              <CreateTicketPage  />
            </Protected>
          }
        />
        <Route
          path="/tickets"
          element={
            <Protected>
              <TicketsDashboardPage  />
            </Protected>
          }
        />
        <Route 
          path="/tickets/:id" 
          element={
            <Protected>
              <TicketDetailPage />
            </Protected>
          } /> 
        
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </div>
  );
}
