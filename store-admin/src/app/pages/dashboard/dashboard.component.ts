import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';

import { Store } from '../../models/store.model';
import { Stock } from '../../models/stock.model';
import { StockHistory } from '../../models/stock-history.model';
import { StoreService } from '../../services/store.service';
import { StockService } from '../../services/stock.service';
import { StockHistoryService } from '../../services/stock-history.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    CardModule,
    TableModule,
    ButtonModule,
    ToastModule
  ],
  providers: [MessageService],
  template: `
    <p-toast></p-toast>
    
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

    <div class="content-sections">
      <!-- Stores Section -->
      <div class="p-dashboard-card">
        <div class="card-header">
          <h3>Store Locations</h3>
          <p-button 
            label="Refresh" 
            icon="pi pi-refresh" 
            (onClick)="loadStores()">
          </p-button>
        </div>
        
        <p-table 
          [value]="stores" 
          [tableStyle]="{ 'min-width': '50rem' }"
          styleClass="p-datatable-sm">
          <ng-template pTemplate="header">
            <tr>
              <th>Store ID</th>
              <th>Location</th>
              <th>Actions</th>
            </tr>
          </ng-template>
          <ng-template pTemplate="body" let-store>
            <tr>
              <td>{{ store.id }}</td>
              <td>{{ store.location }}</td>
              <td>
                <p-button 
                  label="View Stocks" 
                  icon="pi pi-box" 
                  size="small"
                  (onClick)="viewStocks(store.id)">
                </p-button>
              </td>
            </tr>
          </ng-template>
        </p-table>
      </div>

      <!-- Stocks Section -->
      <div class="p-dashboard-card">
        <div class="card-header">
          <h3>Stock Items</h3>
          <p-button 
            label="Refresh" 
            icon="pi pi-refresh" 
            (onClick)="loadStocks()">
          </p-button>
        </div>
        
        <p-table 
          [value]="stocks" 
          [tableStyle]="{ 'min-width': '50rem' }"
          styleClass="p-datatable-sm">
          <ng-template pTemplate="header">
            <tr>
              <th>Stock ID</th>
              <th>Store ID</th>
              <th>Product ID</th>
              <th>Quantity</th>
              <th>Actions</th>
            </tr>
          </ng-template>
          <ng-template pTemplate="body" let-stock>
            <tr>
              <td>{{ stock.id }}</td>
              <td>{{ stock.storeId }}</td>
              <td>{{ stock.productId }}</td>
              <td>{{ stock.quantity }}</td>
              <td>
                <p-button 
                  label="View History" 
                  icon="pi pi-history" 
                  size="small"
                  (onClick)="viewHistory(stock.storeId)">
                </p-button>
              </td>
            </tr>
          </ng-template>
        </p-table>
      </div>

      <!-- Stock History Section -->
      <div class="p-dashboard-card">
        <div class="card-header">
          <h3>Stock History</h3>
          <p-button 
            label="Refresh" 
            icon="pi pi-refresh" 
            (onClick)="loadHistory()">
          </p-button>
        </div>
        
        <p-table 
          [value]="stockHistory" 
          [tableStyle]="{ 'min-width': '50rem' }"
          styleClass="p-datatable-sm">
          <ng-template pTemplate="header">
            <tr>
              <th>History ID</th>
              <th>Store ID</th>
              <th>Product ID</th>
              <th>Quantity Change</th>
              <th>Reason</th>
              <th>Timestamp</th>
            </tr>
          </ng-template>
          <ng-template pTemplate="body" let-history>
            <tr>
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
          </ng-template>
        </p-table>
      </div>
    </div>
  `,
  styles: [`
    .content-sections {
      display: flex;
      flex-direction: column;
      gap: 2rem;
    }
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 1rem;
    }
    
    .card-header h3 {
      margin: 0;
    }
    
    .positive {
      color: var(--green-600);
      font-weight: bold;
    }
    
    .negative {
      color: var(--red-600);
      font-weight: bold;
    }
  `]
})
export class DashboardComponent implements OnInit {
  stores: Store[] = [];
  stocks: Stock[] = [];
  stockHistory: StockHistory[] = [];
  
  loadingStores = false;
  loadingStocks = false;
  loadingHistory = false;
  
  selectedStoreId: string | null = null;

  constructor(
    private storeService: StoreService,
    private stockService: StockService,
    private stockHistoryService: StockHistoryService,
    private messageService: MessageService
  ) {}

  ngOnInit() {
    this.loadStores();
    this.loadAllStocks();
    this.loadAllHistory();
  }

  loadStores() {
    this.loadingStores = true;
    this.storeService.getStores().subscribe({
      next: (stores) => {
        this.stores = stores;
        this.loadingStores = false;
      },
      error: (error) => {
        console.error('Error loading stores:', error);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to load stores'
        });
        this.loadingStores = false;
      }
    });
  }

  loadAllStocks() {
    this.loadingStocks = true;
    // For now, we'll load stocks from the first store
    // In a real application, you might want to load all stocks across all stores
    if (this.stores.length > 0) {
      this.stockService.getStocksByStoreId(this.stores[0].id).subscribe({
        next: (stocks) => {
          this.stocks = stocks;
          this.loadingStocks = false;
        },
        error: (error) => {
          console.error('Error loading stocks:', error);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Failed to load stocks'
          });
          this.loadingStocks = false;
        }
      });
    } else {
      this.loadingStocks = false;
    }
  }

  loadAllHistory() {
    this.loadingHistory = true;
    // For now, we'll load history from the first store
    if (this.stores.length > 0) {
      this.stockHistoryService.getStockHistoryForStore(this.stores[0].id).subscribe({
        next: (history) => {
          this.stockHistory = history;
          this.loadingHistory = false;
        },
        error: (error) => {
          console.error('Error loading stock history:', error);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Failed to load stock history'
          });
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
    this.stockService.getStocksByStoreId(storeId).subscribe({
      next: (stocks) => {
        this.stocks = stocks;
        this.loadingStocks = false;
      },
      error: (error) => {
        console.error('Error loading stocks for store:', error);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to load stocks for this store'
        });
        this.loadingStocks = false;
      }
    });
  }

  viewHistory(storeId: string) {
    this.selectedStoreId = storeId;
    this.loadingHistory = true;
    this.stockHistoryService.getStockHistoryForStore(storeId).subscribe({
      next: (history) => {
        this.stockHistory = history;
        this.loadingHistory = false;
      },
      error: (error) => {
        console.error('Error loading history for store:', error);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to load history for this store'
        });
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