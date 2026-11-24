import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ShipmentResponse } from '../models/profit-loss.model';

@Injectable({ providedIn: 'root' })
export class ShipmentService {
  private readonly http = inject(HttpClient);

  /** All shipments, ordered by number. */
  findAll(): Observable<ShipmentResponse[]> {
    return this.http.get<ShipmentResponse[]>('/api/shipments');
  }
}
