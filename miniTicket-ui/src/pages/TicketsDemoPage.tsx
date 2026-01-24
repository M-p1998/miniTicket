import { useState } from "react";
import { useAuth } from "../auth/AuthProvider";
import { apiFetch, GATEWAY_BASE } from "../api/http";

export default function TicketsDemoPage() {
  const { token } = useAuth();
  const [result, setResult] = useState<string>("");

  const createTicket = async () => {
    try {
      const data = await apiFetch<any>(`${GATEWAY_BASE}/api/tickets`, token, {
        method: "POST",
        body: JSON.stringify({
          subject: "Support Request",
          description: "Created from React after Keycloak login",
          priority: "HIGH",
        }),
      });
      setResult(JSON.stringify(data, null, 2));
    } catch (e: any) {
      setResult(e.message ?? "Error");
    }
  };

  return (
    <div>
      <h3>Tickets</h3>
      <button onClick={createTicket}>Create Ticket</button>
      <pre style={{ background: "#f6f6f6", padding: 12, marginTop: 12 }}>
        {result}
      </pre>
    </div>
  );
}
