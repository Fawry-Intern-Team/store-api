import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StockHistory } from '../models/stock-history.model';

@Injectable({
  providedIn: 'root'
})
export class StockHistoryService {
  private apiUrl = 'http://localhost:8080/api/store';

  constructor(private http: HttpClient) { }

  getStockHistoryForStore(storeId: string): Observable<StockHistory[]> {
    return this.http.get<StockHistory[]>(`${this.apiUrl}/${storeId}/history`);
  }
} 