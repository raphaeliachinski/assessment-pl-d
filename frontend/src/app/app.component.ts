import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  private readonly router = inject(Router);

  isCalculateProfitActive(): boolean {
    return this.router.url.endsWith('/calculate-profit');
  }

  // Keeps the selected shipment on /shipments/{number}/calculate-profit the menu points to that same URL
  calculateProfitLink(): string {
    return this.isCalculateProfitActive() ? this.router.url : '/calculate-profit';
  }
}
