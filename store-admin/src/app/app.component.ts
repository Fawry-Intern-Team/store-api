import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: `
    <div class="app-container">
      <header class="app-header">
        <h1>Store Admin Dashboard</h1>
      </header>
      <main class="app-main">
        <router-outlet></router-outlet>
      </main>
    </div>
  `,
  styles: [`
    .app-container {
      min-height: 100vh;
      background-color: var(--surface-ground);
    }
    
    .app-header {
      background: var(--surface-card);
      padding: 1rem 2rem;
      box-shadow: 0 2px 1px -1px rgba(0,0,0,.2), 0 1px 1px 0 rgba(0,0,0,.14), 0 1px 3px 0 rgba(0,0,0,.12);
      margin-bottom: 2rem;
    }
    
    .app-header h1 {
      margin: 0;
      color: var(--text-color);
      font-size: 1.5rem;
      font-weight: 600;
    }
    
    .app-main {
      padding: 0 2rem 2rem;
    }
  `]
})
export class AppComponent {
  title = 'store-admin';
} 