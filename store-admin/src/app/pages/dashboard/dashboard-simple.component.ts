import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

import { Store } from '../../models/store.model';
import { Stock } from '../../models/stock.model';
import { StockHistory } from '../../models/stock-history.model';
import { StoreService } from '../../services/store.service';
import { StockService } from '../../services/stock.service';
import { StockHistoryService } from '../../services/stock-history.service';

@Component({
  selector: 'app-dashboard-simple',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="dashboard">
      <h1>Store Admin Dashboard</h1>
      
      <!-- Stats Cards -->
      <div class="stats-grid">
        <div class="stats-card">
          <div class="number">{{ stores.length }}</div>
          <div class="label">Total Stores</div>
        </div>
        <div class="stats-card">
          <div class="number">{{ totalStocks }}</div>
          <div class="label">Total Stock Items</div>
        </div>
        <div class="stats-card">
          <div class="number">{{ totalQuantity }}</div>
          <div class="label">Total Quantity</div>
        </div>
      </div>

      <!-- Stores Section -->
      <div class="section">
        <div class="section-header">
          <h2>Store Locations</h2>
          <button (click)="loadStores()" [disabled]="loadingStores">
            {{ loadingStores ? 'Loading...' : 'Refresh' }}
          </button>
        </div>
        
        <div class="table-container">
          <table class="data-table">
            <thead>
              <tr>
                <th>Store ID</th>
                <th>Location</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let store of stores">
                <td>{{ store.id }}</td>
                <td>{{ store.location }}</td>
                <td>
                  <button (click)="viewStocks(store.id)" class="btn-small">
                    View Stocks
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Stocks Section -->
      <div class="section">
        <div class="section-header">
          <h2>Stock Items</h2>
          <button (click)="loadStocks()" [disabled]="loadingStocks">
            {{ loadingStocks ? 'Loading...' : 'Refresh' }}
          </button>
        </div>
        
        <div class="table-container">
          <table class="data-table">
            <thead>
              <tr>
                <th>Stock ID</th>
                <th>Store ID</th>
                <th>Product ID</th>
                <th>Quantity</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let stock of stocks">
                <td>{{ stock.id }}</td>
                <td>{{ stock.storeId }}</td>
                <td>{{ stock.productId }}</td>
                <td>{{ stock.quantity }}</td>
                <td>
                  <button (click)="viewHistory(stock.storeId)" class="btn-small">
                    View History
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Stock History Section -->
      <div class="section">
        <div class="section-header">
          <h2>Stock History</h2>
          <button (click)="loadHistory()" [disabled]="loadingHistory">
            {{ loadingHistory ? 'Loading...' : 'Refresh' }}
          </button>
        </div>
        
        <div class="table-container">
          <table class="data-table">
            <thead>
              <tr>
                <th>History ID</th>
                <th>Store ID</th>
                <th>Product ID</th>
                <th>Quantity Change</th>
                <th>Reason</th>
                <th>Timestamp</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let history of stockHistory">
                <td>{{ history.id }}</td>
                <td>{{ history.storeId }}</td>
                <td>{{ history.productId }}</td>
                <td [class.positive]="history.quantityChange > 0" 
                    [class.negative]="history.quantityChange < 0">
                  {{ history.quantityChange > 0 ? '+' : '' }}{{ history.quantityChange }}
                </td>
                <td>{{ history.reason }}</td>
                <td>{{ history.timestamp | date:'medium' }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Error Messages -->
      <div *ngIf="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
    </div>
  `,
  styles: [`
    .dashboard {
      padding: 20px;
      max-width: 1200px;
      margin: 0 auto;
    }

    .dashboard h1 {
      color: #333;
      margin-bottom: 30px;
      text-align: center;
    }

    .stats-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 20px;
      margin-bottom: 40px;
    }

    .stats-card {
      background: white;
      border-radius: 8px;
      padding: 20px;
      text-align: center;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      border: 1px solid #e0e0e0;
    }

    .stats-card .number {
      font-size: 2rem;
      font-weight: bold;
      color: #2196F3;
      margin-bottom: 8px;
    }

    .stats-card .label {
      color: #666;
      font-size: 0.9rem;
    }

    .section {
      background: white;
      border-radius: 8px;
      padding: 20px;
      margin-bottom: 30px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      border: 1px solid #e0e0e0;
    }

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
    }

    .section-header h2 {
      margin: 0;
      color: #333;
    }

    button {
      background: #2196F3;
      color: white;
      border: none;
      padding: 10px 20px;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
    }

    button:hover {
      background: #1976D2;
    }

    button:disabled {
      background: #ccc;
      cursor: not-allowed;
    }

    .btn-small {
      padding: 6px 12px;
      font-size: 12px;
    }

    .table-container {
      overflow-x: auto;
    }

    .data-table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 10px;
    }

    .data-table th,
    .data-table td {
      padding: 12px;
      text-align: left;
      border-bottom: 1px solid #e0e0e0;
    }

    .data-table th {
      background: #f5f5f5;
      font-weight: 600;
      color: #333;
    }

    .data-table tr:hover {
      background: #f9f9f9;
    }

    .positive {
      color: #4CAF50;
      font-weight: bold;
    }

    .negative {
      color: #f44336;
      font-weight: bold;
    }

    .error-message {
      background: #ffebee;
      color: #c62828;
      padding: 15px;
      border-radius: 4px;
      margin: 20px 0;
      border: 1px solid #ffcdd2;
    }

    @media (max-width: 768px) {
      .stats-grid {
        grid-template-columns: 1fr;
      }
      
      .section-header {
        flex-direction: column;
        gap: 10px;
        align-items: flex-start;
      }
      
      .data-table {
        font-size: 14px;
      }
      
      .data-table th,
      .data-table td {
        padding: 8px;
      }
    }
  `]
})
export class DashboardSimpleComponent implements OnInit {
  stores: Store[] = [];
  stocks: Stock[] = [];
  stockHistory: StockHistory[] = [];
  
  loadingStores = false;
  loadingStocks = false;
  loadingHistory = false;
  
  selectedStoreId: string | null = null;
  errorMessage = '';

  constructor(
    private storeService: StoreService,
    private stockService: StockService,
    private stockHistoryService: StockHistoryService
  ) {}

  ngOnInit() {
    this.loadStores();
    this.loadAllStocks();
    this.loadAllHistory();
  }

  loadStores() {
    this.loadingStores = true;
    this.errorMessage = '';
    this.storeService.getStores().subscribe({
      next: (stores: Store[]) => {
        this.stores = stores;
        this.loadingStores = false;
      },
      error: (error: any) => {
        console.error('Error loading stores:', error);
        this.errorMessage = 'Failed to load stores. Please check your backend connection.';
        this.loadingStores = false;
      }
    });
  }

  loadAllStocks() {
    this.loadingStocks = true;
    this.errorMessage = '';
    if (this.stores.length > 0) {
      this.stockService.getStocksByStoreId(this.stores[0].id).subscribe({
        next: (stocks: Stock[]) => {
          this.stocks = stocks;
          this.loadingStocks = false;
        },
        error: (error: any) => {
          console.error('Error loading stocks:', error);
          this.errorMessage = 'Failed to load stocks. Please check your backend connection.';
          this.loadingStocks = false;
        }
      });
    } else {
      this.loadingStocks = false;
    }
  }

  loadAllHistory() {
    this.loadingHistory = true;
    this.errorMessage = '';
    if (this.stores.length > 0) {
      this.stockHistoryService.getStockHistoryForStore(this.stores[0].id).subscribe({
        next: (history: StockHistory[]) => {
          this.stockHistory = history;
          this.loadingHistory = false;
        },
        error: (error: any) => {
          console.error('Error loading stock history:', error);
          this.errorMessage = 'Failed to load stock history. Please check your backend connection.';
          this.loadingHistory = false;
        }
      });
    } else {
      this.loadingHistory = false;
    }
  }

  loadStocks() {
    this.loadAllStocks();
  }

  loadHistory() {
    this.loadAllHistory();
  }

  viewStocks(storeId: string) {
    this.selectedStoreId = storeId;
    this.loadingStocks = true;
    this.errorMessage = '';
    this.stockService.getStocksByStoreId(storeId).subscribe({
      next: (stocks: Stock[]) => {
        this.stocks = stocks;
        this.loadingStocks = false;
      },
      error: (error: any) => {
        console.error('Error loading stocks for store:', error);
        this.errorMessage = 'Failed to load stocks for this store.';
        this.loadingStocks = false;
      }
    });
  }

  viewHistory(storeId: string) {
    this.selectedStoreId = storeId;
    this.loadingHistory = true;
    this.errorMessage = '';
    this.stockHistoryService.getStockHistoryForStore(storeId).subscribe({
      next: (history: StockHistory[]) => {
        this.stockHistory = history;
        this.loadingHistory = false;
      },
      error: (error: any) => {
        console.error('Error loading history for store:', error);
        this.errorMessage = 'Failed to load history for this store.';
        this.loadingHistory = false;
      }
    });
  }

  get totalStocks(): number {
    return this.stocks.length;
  }

  get totalQuantity(): number {
    return this.stocks.reduce((sum, stock) => sum + stock.quantity, 0);
  }
} 