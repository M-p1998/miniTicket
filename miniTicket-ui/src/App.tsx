import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "./auth/AuthProvider";
import Footer from "./components/Footer";
import Navbar from "./components/Navbar";
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
  return (
    <>
      <div className="app-layout">
      <Navbar />
      <main className="app-content">
      <Routes>
        {/* ✅ Redirect root to dashboard */}
        <Route path="/" element={<Navigate to="/tickets" />} />

        <Route
          path="/tickets"
          element={
            <Protected>
              <TicketsDashboardPage />
            </Protected>
          }
        />

        <Route
          path="/tickets/create"
          element={
            <Protected>
              <CreateTicketPage />
            </Protected>
          }
        />

        <Route
          path="/tickets/:id"
          element={
            <Protected>
              <TicketDetailPage />
            </Protected>
          }
        />

        <Route
          path="/profile"
          element={
            <Protected>
              <ProfilePage />
            </Protected>
          }
        />

        <Route path="*" element={<Navigate to="/tickets" />} />
        <Route
          path="/footer"
          element={
            <Protected>
              <Footer />
            </Protected>
          }
        />
      </Routes>
      </main>
      <Footer />
      </div>
    </>
    
  );
}
