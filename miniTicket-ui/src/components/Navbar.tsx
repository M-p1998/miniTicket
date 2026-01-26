import { NavLink } from "react-router-dom";
import { useAuth } from "../auth/AuthProvider";
import "../styles/Navbar.css";
import React from "react";

export default function Navbar() {
  const { isAuthenticated, username, login, register, logout } = useAuth();

  return (
    <header className="navbar">
      <div className="nav-left">
        <span className="brand">🎫 miniTicket</span>

        {isAuthenticated && (
          <nav className="nav-links">
            <NavLink to="/tickets">Tickets</NavLink>
            <NavLink to="/tickets/create">Create</NavLink>
            <NavLink to="/profile">Profile</NavLink>
          </nav>
        )}
      </div>

      <div className="nav-right">
        {!isAuthenticated ? (
          <>
            <button onClick={login}>Login</button>
            <button onClick={register}>Register</button>
          </>
        ) : (
          <div className="user-menu">
            <span className="user">👤 {username}</span>
            <button className="danger" onClick={logout}>Logout</button>
          </div>
        )}
      </div>
    </header>
  );
}
