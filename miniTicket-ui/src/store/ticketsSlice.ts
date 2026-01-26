import { createSlice, PayloadAction } from "@reduxjs/toolkit";

export type TicketPriority = "LOW" | "MEDIUM" | "HIGH";
export type TicketStatus = "OPEN" | "CLOSED";

export type TicketResponse = {
  id: number;
  subject: string;
  description: string;
  status: TicketStatus;
  priority: TicketPriority;
  createdBy: string;
  createdAt: string;
};

type TicketsState = {
  items: TicketResponse[];
  loading: boolean;
  error: string | null;
};

const initialState: TicketsState = {
  items: [],
  loading: false,
  error: null,
};

const ticketsSlice = createSlice({
  name: "tickets",
  initialState,
  reducers: {
    setTickets(state, action: PayloadAction<TicketResponse[]>) {
      state.items = action.payload;
      state.loading = false;
      state.error = null;
    },
    setLoading(state) {
      state.loading = true;
      state.error = null;
    },
    setError(state, action: PayloadAction<string>) {
      state.loading = false;
      state.error = action.payload;
    },
    removeTicket(state, action: PayloadAction<number>) {
      state.items = state.items.filter((t) => t.id !== action.payload);
    },
  },
});

export const { setTickets, setLoading, setError, removeTicket } =
  ticketsSlice.actions;

export default ticketsSlice.reducer;
