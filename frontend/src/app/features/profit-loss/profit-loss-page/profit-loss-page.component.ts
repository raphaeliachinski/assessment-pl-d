import { Component, OnInit, inject, Input } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import {
  FormGroupDirective,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';

import { ApiProblem, ShipmentProfitLossResponse, ShipmentResponse } from '../../../core/models/profit-loss.model';
import { ProfitLossService } from '../../../core/services/profit-loss.service';
import { ShipmentService } from '../../../core/services/shipment.service';
import { atLeastOneAmountValidator, notBlankValidator } from '../entry-form.validators';
import { AmountFieldComponent } from '../amount-field/amount-field.component';
import { EntriesTableComponent } from '../entries-table/entries-table.component';

@Component({
  selector: 'app-profit-loss-page',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule,
    EntriesTableComponent,
    AmountFieldComponent,
  ],
  templateUrl: './profit-loss-page.component.html',
  styleUrl: './profit-loss-page.component.scss',
})
export class ProfitLossPageComponent implements OnInit {

  private readonly fb = inject(NonNullableFormBuilder);
  private readonly service = inject(ProfitLossService);
  private readonly snackBar = inject(MatSnackBar);
  private readonly shipmentService = inject(ShipmentService);
  private readonly router = inject(Router);
  /** Shipments to choose from; choosing one navigates to its URL */
  shipments: ShipmentResponse[] = [];
  loading = false;
  /** Shipment totals */
  totals: ShipmentProfitLossResponse | null = null;
  readonly form = this.fb.group(
    {
      shipmentNumber: ['', [Validators.required, notBlankValidator]],
      income: this.fb.control<number | null>(null, Validators.required),
      cost: this.fb.control<number | null>(null, Validators.required),
      additionalCost: this.fb.control<number | null>(0),
    },
    { validators: atLeastOneAmountValidator },
  );

  ngOnInit(): void {
    this.shipmentService.findAll().subscribe({
      next: (shipments) => (this.shipments = shipments),
      error: (error: HttpErrorResponse) => this.showError(error),
    });
  }

  openShipment(shipmentNumber: string): void {
    this.router.navigate(['/shipments', shipmentNumber, 'calculate-profit']);
  }

  // Called on every route change
  @Input() set shipmentNumber(value: string | undefined) {
    this.form.controls.shipmentNumber.setValue(value ?? '');
    if (value) {
      this.loadProfitLoss(value);
    } else {
      this.totals = null;
    }
  }

  handleFormSubmit(formDirective: FormGroupDirective): void {
    const shipmentNumber = this.form.controls.shipmentNumber.value.trim();
    if (!shipmentNumber) {
      this.snackBar.open('Select a shipment first', 'Close', { duration: 6000 });
      return;
    }
    if (this.form.invalid || this.loading) {
      return;
    }
    const { income, cost, additionalCost } = this.form.getRawValue();

    this.loading = true;
    this.service
      .create(shipmentNumber, {
        income: income!,
        cost: cost!,
        additionalCost: additionalCost,
      })
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          formDirective.resetForm({ shipmentNumber, income: null, cost: null, additionalCost: 0 });
          this.snackBar.open('Profit or loss calculated', 'OK', { duration: 3000 });
          this.loadProfitLoss(shipmentNumber);
        },
        error: (error: HttpErrorResponse) => this.showError(error),
      });
  }

  private loadProfitLoss(shipmentNumber: string): void {
    this.loading = true;
    this.service
      .findByShipmentNumber(shipmentNumber)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (shipment) => {
          this.totals = shipment;
        },
        error: (error: HttpErrorResponse) => {
          this.totals = null;
          this.showError(error);
        },
      });
  }

  private showError(error: HttpErrorResponse): void {
    const problem = error.error as ApiProblem | null;
    const message = problem?.errors
      ? Object.values(problem.errors).join(' · ')
      : (problem?.detail ?? 'Could not reach the server. Please try again.');
    this.snackBar.open(message, 'Close', { duration: 6000 });
  }
}
