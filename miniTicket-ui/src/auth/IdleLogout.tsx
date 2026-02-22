import { useEffect, useRef } from "react";
import { useAuth } from "./AuthProvider";

const IDLE_TIMEOUT_MS = 10 * 60 * 1000; // 10 minutes

export default function IdleLogout() {
  const { token, logout } = useAuth();
  const timeoutRef = useRef<number | null>(null);

  const resetTimer = () => {
    if (timeoutRef.current) window.clearTimeout(timeoutRef.current);

    if (!token) return;

    timeoutRef.current = window.setTimeout(() => {
      console.log("Auto-logout: 10 minutes of inactivity");
      logout();
    }, IDLE_TIMEOUT_MS);
  };

  useEffect(() => {
    if (!token) {
      if (timeoutRef.current) window.clearTimeout(timeoutRef.current);
      return;
    }

    resetTimer();

    const events: (keyof WindowEventMap)[] = [
      "mousemove",
      "mousedown",
      "keydown",
      "scroll",
      "touchstart",
      "click",
    ];

    events.forEach((event) =>
      window.addEventListener(event, resetTimer, { passive: true })
    );

    return () => {
      if (timeoutRef.current) window.clearTimeout(timeoutRef.current);
      events.forEach((event) => window.removeEventListener(event, resetTimer));
    };
  }, [token, logout]);

  return null;
}