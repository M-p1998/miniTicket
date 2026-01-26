import React, { useEffect, useMemo, useState } from "react";
import { apiFetch, GATEWAY_BASE } from "../api/http";
import { useAuth } from "../auth/AuthProvider";
import { Link,useNavigate } from "react-router-dom";
import "../styles/TicketsDashboardPage.css"
import { useDispatch, useSelector } from "react-redux";
import type { RootState, AppDispatch } from "../store/store";
import { setTickets, setLoading, setError, removeTicket } from "../store/ticketsSlice";
import $ from "jquery";
import "../styles/pills.css";

type TicketPriority = "LOW" | "MEDIUM" | "HIGH";
type TicketStatus = "OPEN" | "CLOSED";

type TicketResponse = {
  id: number;
  subject: string;
  description: string;
  status: TicketStatus;
  priority: TicketPriority;
  createdBy: string;
  createdAt: string;
};

type Tab = "ALL" | "OPEN" | "CLOSED";


function timeAgo(iso: string) {
  const then = new Date(iso).getTime();
  const now = Date.now();
  const diffSec = Math.max(0, Math.floor((now - then) / 1000));

  if (diffSec < 60) return `${diffSec}s ago`;
  const diffMin = Math.floor(diffSec / 60);
  if (diffMin < 60) return `${diffMin} min ago`;
  const diffHr = Math.floor(diffMin / 60);
  if (diffHr < 24) return `${diffHr} hr ago`;
  const diffDay = Math.floor(diffHr / 24);
  return `${diffDay} day${diffDay === 1 ? "" : "s"} ago`;
}


export default function TicketsDashboardPage() {
  const { token,username } = useAuth();
  // const [tickets, setTickets] = useState<TicketResponse[]>([]);
  const [tab, setTab] = useState<Tab>("ALL");
  const [q, setQ] = useState("");
  const [statusMsg, setStatusMsg] = useState("Loading tickets...");
  const navigate = useNavigate();

  const dispatch = useDispatch<AppDispatch>();
  const tickets = useSelector((state: RootState) => state.tickets.items);
  const loading = useSelector((state: RootState) => state.tickets.loading);
  const error = useSelector((state: RootState) => state.tickets.error);


  // 1) load tickets
  // useEffect(() => {
  //   if (!token) return;

  //   const load = async () => {
  //     try {
  //       const data = await apiFetch<TicketResponse[]>(
  //         `${GATEWAY_BASE}/api/tickets`,
  //         token
  //       );
  //       setTickets(data);
  //       setStatusMsg(data.length ? "" : "No tickets yet.");
  //     } catch (e: any) {
  //       console.error(e);
  //       setStatusMsg(e.message || "Failed to load tickets");
  //     }
  //   };

  //   load();
  // }, [token]);

  useEffect(() => {
  if (!token) return;

  const load = async () => {
    dispatch(setLoading());

    try {
      const data = await apiFetch<TicketResponse[]>(
        `${GATEWAY_BASE}/api/tickets`,
        token
      );
      dispatch(setTickets(data));
      setStatusMsg(data.length ? "" : "No tickets yet.");
    } catch (e: any) {
      console.error(e);
      dispatch(setError(e.message || "Failed to load tickets"));
      setStatusMsg(e.message || "Failed to load tickets");
    }
  };

  load();
}, [token, dispatch]);


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

  const deleteTicket = async (id: number) => {
    if (!token) return;

    if (!confirm("Delete this ticket?")) return;

    await apiFetch(`${GATEWAY_BASE}/api/tickets/${id}`, token, {
      method: "DELETE",
    });

    // setTickets((prev) => prev.filter((t) => t.id !== id));
    dispatch(removeTicket(id));

  };


  const counts = useMemo(() => {
    const open = tickets.filter((t) => t.status === "OPEN").length;
    const closed = tickets.filter((t) => t.status === "CLOSED").length;
    return { open, closed, all: tickets.length };
  }, [tickets]);

  useEffect(() => {
    // highlight row on hover using jquery
    $(".row").on("mouseenter", function () {
      $(this).addClass("jq-hover");
    });

    $(".row").on("mouseleave", function () {
      $(this).removeClass("jq-hover");
    });

    // cleanup to avoid duplicate bindings
    return () => {
      $(".row").off("mouseenter mouseleave");
    };
  }, [filtered.length]);


  return (
    <div className="dashPage">
      {/* <div className="dashHeader">
        <h2>My Tickets</h2>

        <Link className="primaryBtn" to="/tickets/create">
          Create Ticket
        </Link>
      </div> */}

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
              <th>Created By</th>
              <th>Created</th>
              <th>Actions</th>
            </tr>
          </thead>

          <tbody>
            {filtered.map((t) => (
              <tr key={t.id} 
              onClick={() => navigate(`/tickets/${t.id}`)}
              style={{ cursor: "pointer" }} 
              className="row">
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
                <td>{t.createdBy }</td>
                <td>{timeAgo(t.createdAt)}</td>

                <td>
                  {t.createdBy === username && (
                    <button
                      className="deleteBtn"
                      onClick={(e) => {
                        e.stopPropagation(); // prevents row click navigation
                        deleteTicket(t.id);
                      }}
                    >
                      Delete
                    </button>
                  )}
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
