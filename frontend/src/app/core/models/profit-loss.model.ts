export interface CreateEntryRequest {
  income: number;
  cost: number;
  additionalCost: number | null;
}

export interface EntryResponse {
  id: number;
  shipmentNumber: string;
  income: number;
  totalCosts: number;
  profitOrLoss: number;
  createdAt: string;
}


export interface ShipmentProfitLossResponse {
  shipmentNumber: string;
  totalIncome: number;
  totalCosts: number;
  profitOrLoss: number;
  entries: EntryResponse[];
}

export interface ShipmentResponse {
  shipmentNumber: string;
}

export interface ApiProblem {
  title: string;
  status: number;
  detail: string;
  errors?: Record<string, string>;
}
