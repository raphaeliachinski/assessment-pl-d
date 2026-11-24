import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import {
  CreateEntryRequest,
  EntryResponse,
  ShipmentProfitLossResponse,
} from '../models/profit-loss.model';

@Injectable({ providedIn: 'root' })
export class ProfitLossService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/shipments';

  findByShipmentNumber(shipmentNumber: string): Observable<ShipmentProfitLossResponse> {
    return this.http.get<ShipmentProfitLossResponse>(this.url(shipmentNumber));
  }

  create(shipmentNumber: string, request: CreateEntryRequest): Observable<EntryResponse> {
    return this.http.post<EntryResponse>(this.url(shipmentNumber), request);
  }

  private url(shipmentNumber: string): string {
    return `${this.baseUrl}/${encodeURIComponent(shipmentNumber.trim())}/profit-loss`;
  }
}
