import { NavLink, Link } from "react-router-dom";
import { useAuth } from "../auth/AuthProvider";
import { FaUserCircle, FaPlus } from "react-icons/fa";
import "../styles/Navbar.css";
import React from "react";

export default function Navbar() {
  const { isAuthenticated, username, login, register, logout } = useAuth();

  return (
    <header className="navbar">
      {/* LEFT */}
      {/* <div className="nav-left">
        <Link to="/tickets" className="brand">
          🎫 miniTicket
        </Link>
         */}


        {isAuthenticated && (
          <div className="nav-left">
            <Link to="/tickets" className="brand">MiniTicket</Link>
          </div>
        )}

      {/* </div> */}

      {/* RIGHT */}
      <div className="nav-right">
        {!isAuthenticated ? (
          <>
            <button onClick={login}>Login</button>
            <button onClick={register}>Register</button>
          </>
        ) : (
          <>
            {/* Create Ticket */}
            <NavLink to="/tickets/create" className="icon-btn">
              <FaPlus />
              <span>Create</span>
            </NavLink>

            {/* Profile */}
            <NavLink to="/profile" className="profile-link">
              <FaUserCircle />
              <span>{username}</span>
            </NavLink>

            {/* Logout */}
            <button className="danger" onClick={logout}>
              Logout
            </button>
          </>
        )}
      </div>
    </header>
  );
}
