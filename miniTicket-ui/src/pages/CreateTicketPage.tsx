import React, { useState } from "react";
import { apiFetch, GATEWAY_BASE } from "../api/http";
import { useAuth } from "../auth/AuthProvider";

type TicketPriority = "LOW" | "MEDIUM" | "HIGH";

type TicketResponse = {
  id: number;
  subject: string;
  description: string;
  status: string;
  priority: TicketPriority;
};

export default function CreateTicketPage() {
  const { token, username } = useAuth();

  const [subject, setSubject] = useState("");
  const [description, setDescription] = useState("");
  const [priority, setPriority] = useState<TicketPriority>("LOW");
  const [status, setStatus] = useState("");

  // ✅ MUST be inside component
  const createTicket = async () => {
    if (!token) {
      setStatus("Not authenticated");
      return;
    }

    if (!subject.trim()) {
      setStatus("Subject is required");
      return;
    }

    try {
      const ticket = await apiFetch<TicketResponse>(
        `${GATEWAY_BASE}/api/tickets`,
        token,
        {
          method: "POST",
          body: JSON.stringify({
            subject,
            description,
            priority,
            createdBy: username ?? "anon",
          }),
        }
      );

      setStatus(`Ticket created (ID: ${ticket.id})`);
      setSubject("");
      setDescription("");
      setPriority("LOW");
    } catch (e) {
      console.error(e);
      setStatus("Failed to create ticket");
    }
  };

  
  return (
    <div style={{ maxWidth: 600, margin: "0 auto" }}>
      <h2>Create Ticket</h2>

      {status && <p>{status}</p>}

      {/* Subject */}
      <div style={{ marginBottom: 12 }}>
        <label>Subject</label>
        <input
          type="text"
          value={subject}
          onChange={(e) => setSubject(e.target.value)}
          style={{ width: "100%" }}
        />
      </div>

      {/* Description */}
      <div style={{ marginBottom: 12 }}>
        <label>Description</label>
        <textarea
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          style={{ width: "100%", height: 120 }}
        />
      </div>

      {/* Priority */}
      <div style={{ marginBottom: 12 }}>
        <label>Priority</label>
        <div>
          <label>
            <input
              type="radio"
              checked={priority === "LOW"}
              onChange={() => setPriority("LOW")}
            />
            Low
          </label>

          <label style={{ marginLeft: 10 }}>
            <input
              type="radio"
              checked={priority === "MEDIUM"}
              onChange={() => setPriority("MEDIUM")}
            />
            Medium
          </label>

          <label style={{ marginLeft: 10 }}>
            <input
              type="radio"
              checked={priority === "HIGH"}
              onChange={() => setPriority("HIGH")}
            />
            High
          </label>
        </div>
      </div>

      <button onClick={createTicket}>Create Ticket</button>
    </div>
  );
}
