import { AfterViewInit, Component, Input, ViewChild } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { EntryResponse, ShipmentProfitLossResponse } from '../../../core/models/profit-loss.model';

@Component({
  selector: 'app-entries-table',
  standalone: true,
  imports: [DecimalPipe, MatTableModule, MatSortModule],
  templateUrl: './entries-table.component.html',
  styleUrl: './entries-table.component.scss'
})
export class EntriesTableComponent implements AfterViewInit {

  @ViewChild(MatSort) private sort!: MatSort;

  readonly columns = ['income', 'totalCosts', 'profitOrLoss'];
  readonly dataSource = new MatTableDataSource<EntryResponse>([]);
  totals: ShipmentProfitLossResponse | null = null;

  @Input() set shipment(value: ShipmentProfitLossResponse | null) {
    this.totals = value;
    this.dataSource.data = value?.entries ?? []
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
  }
}
