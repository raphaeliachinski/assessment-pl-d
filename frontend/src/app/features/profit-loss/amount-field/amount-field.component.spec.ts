import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AmountFieldComponent } from './amount-field.component';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Component } from '@angular/core';

@Component({
  standalone: true,
  imports: [ReactiveFormsModule, AmountFieldComponent],
  template: `<form [formGroup]="form"><app-amount-field formControlName="amount" /></form>`,
})
class HostComponent {
  readonly form = new FormGroup({ amount: new FormControl<number | null>(null) });
}


describe('AmountFieldComponent', () => {

  let fixture: ComponentFixture<HostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HostComponent],
      providers: [provideNoopAnimations()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HostComponent);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(fixture.nativeElement.querySelector('app-amount-field')).toBeTruthy();
  });
});
