import React, { useEffect, useMemo, useState } from "react";
import { apiFetch, GATEWAY_BASE } from "../api/http";
import { useAuth } from "../auth/AuthProvider";
import { Link } from "react-router-dom";
import "../styles/TicketsDashboardPage.css";

type TicketPriority = "LOW" | "MEDIUM" | "HIGH";
type TicketStatus = "OPEN" | "CLOSED";

type TicketResponse = {
  id: number;
  subject: string;
  description: string;
  status: TicketStatus;
  priority: TicketPriority;
};

type Tab = "ALL" | "OPEN" | "CLOSED";

export default function TicketsDashboardPage() {
  const { token } = useAuth();

  const [tickets, setTickets] = useState<TicketResponse[]>([]);
  const [tab, setTab] = useState<Tab>("ALL");
  const [q, setQ] = useState("");
  const [statusMsg, setStatusMsg] = useState("Loading tickets...");

  // 1) load tickets
  useEffect(() => {
    if (!token) return;

    const load = async () => {
      try {
        const data = await apiFetch<TicketResponse[]>(
          `${GATEWAY_BASE}/api/tickets`,
          token
        );
        setTickets(data);
        setStatusMsg(data.length ? "" : "No tickets yet.");
      } catch (e: any) {
        console.error(e);
        setStatusMsg(e.message || "Failed to load tickets");
      }
    };

    load();
  }, [token]);

  // 2) filter by tab + search
  const filtered = useMemo(() => {
    const search = q.trim().toLowerCase();

    return tickets
      .filter((t) => (tab === "ALL" ? true : t.status === tab))
      .filter((t) => {
        if (!search) return true;
        return (
          t.subject.toLowerCase().includes(search) ||
          String(t.id).includes(search)
        );
      });
  }, [tickets, tab, q]);

  const counts = useMemo(() => {
    const open = tickets.filter((t) => t.status === "OPEN").length;
    const closed = tickets.filter((t) => t.status === "CLOSED").length;
    return { open, closed, all: tickets.length };
  }, [tickets]);

  return (
    <div className="dashPage">
      <div className="dashHeader">
        <h2>My Tickets</h2>

        <Link className="primaryBtn" to="/tickets/create">
          Create Ticket
        </Link>
      </div>

      <div className="searchRow">
        <input
          value={q}
          onChange={(e) => setQ(e.target.value)}
          placeholder="Search tickets..."
        />
      </div>

      <div className="tabs">
        <button
          className={tab === "ALL" ? "tab active" : "tab"}
          onClick={() => setTab("ALL")}
        >
          All ({counts.all})
        </button>
        <button
          className={tab === "OPEN" ? "tab active" : "tab"}
          onClick={() => setTab("OPEN")}
        >
          Open ({counts.open})
        </button>
        <button
          className={tab === "CLOSED" ? "tab active" : "tab"}
          onClick={() => setTab("CLOSED")}
        >
          Closed ({counts.closed})
        </button>
      </div>

      {statusMsg && <p className="msg">{statusMsg}</p>}

      <div className="tableWrap">
        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Subject</th>
              <th>Status</th>
              <th>Priority</th>
            </tr>
          </thead>

          <tbody>
            {filtered.map((t) => (
              <tr key={t.id} className="row">
                <td>#{t.id}</td>
                <td className="subject">{t.subject}</td>
                <td>
                  <span className={`pill ${t.status.toLowerCase()}`}>
                    {t.status}
                  </span>
                </td>
                <td>
                  <span className={`pill ${t.priority.toLowerCase()}`}>
                    {t.priority}
                  </span>
                </td>
              </tr>
            ))}

            {!statusMsg && filtered.length === 0 && (
              <tr>
                <td colSpan={4} className="empty">
                  No results.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
