import { useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "./AuthProvider";

const IDLE_TIMEOUT_MS = 15 * 60 * 1000; // 15 minutes

export default function IdleLogout() {
  const { token, logout } = useAuth();
  const navigate = useNavigate();

  const timeoutRef = useRef<number | null>(null);

  const resetTimer = () => {
    if (timeoutRef.current) window.clearTimeout(timeoutRef.current);

    timeoutRef.current = window.setTimeout(() => {
      // Only auto-logout if user is currently logged in
      if (token) {
        logout();            
        // navigate("/login");  
      }
    }, IDLE_TIMEOUT_MS);
  };

  useEffect(() => {
    // If user is not logged in, do nothing
    if (!token) return;

    // Start timer immediately when user becomes logged in
    resetTimer();

    const events: (keyof WindowEventMap)[] = [
      "mousemove",
      "mousedown",
      "keydown",
      "scroll",
      "touchstart",
      "click",
    ];

    const handleActivity = () => resetTimer();

    events.forEach((event) => window.addEventListener(event, handleActivity, { passive: true }));

    return () => {
      if (timeoutRef.current) window.clearTimeout(timeoutRef.current);
      events.forEach((event) => window.removeEventListener(event, handleActivity));
    };
  }, [token]);

  return null; // no UI
}
