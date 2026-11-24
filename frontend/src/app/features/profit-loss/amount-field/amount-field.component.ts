import { Component, DestroyRef, Input, inject, OnInit } from '@angular/core';
import {
  ControlValueAccessor,
  FormControl,
  FormGroupDirective,
  NgControl,
  ReactiveFormsModule,
  ValidationErrors
} from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';


//it follows the same limits as the backend, up to 15 integer digits and 2 decimal places, (, or .).
const AMOUNT_PATTERN=/^\d{1,15}([.,]\d{1,2})?$/;

const ERROR_MESSAGES: Record<string, string> = {
  amount: 'Up to 15 digits and 2 decimals (e.g. 230,5)',
  required: 'Required'
}
@Component({
  selector: 'app-amount-field',
  standalone: true,
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule],
  templateUrl: './amount-field.component.html',
  styleUrl: './amount-field.component.scss'
})
export class AmountFieldComponent implements ControlValueAccessor, OnInit {

    @Input() inputId= '';

    private readonly ngControl = inject(NgControl, {self: true});
    private readonly parentForm = inject(FormGroupDirective, {optional: true})
    private readonly destroyRef = inject(DestroyRef);

    readonly text = new FormControl('', {nonNullable: true});

    private onChange: (value: number | null) => void = () => {};
    onTouched: () => void = () => {};

  readonly errorStateMatcher: ErrorStateMatcher = {
    isErrorState: () => {
      const control = this.ngControl.control;
      return !!control?.invalid && (control.touched || !!this.parentForm?.submitted);
    },
  };

  constructor() {
    this.ngControl.valueAccessor = this;
    this.text.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((text) => this.onChange(AmountFieldComponent.toNumber(text)));
  }

  ngOnInit(): void {
    const control = this.ngControl.control;
    control?.addValidators(() => this.validateFormat());
    control?.updateValueAndValidity({ emitEvent: false });
  }

  get errorMessage(): string {
    const errors = this.ngControl.control?.errors ?? {};
    const key = errors['amount'] ? 'amount' : Object.keys(errors)[0];
    return ERROR_MESSAGES[key] ?? 'Invalid value';
  }

  writeValue(value: number | null): void {
    this.text.setValue(value === null || value === undefined ? '' : String(value), { emitEvent: false });
  }

  registerOnChange(fn: (value: number | null) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(disabled: boolean): void {
    if (disabled) {
      this.text.disable({ emitEvent: false });
    } else {
      this.text.enable({ emitEvent: false });
    }
  }

  private validateFormat(): ValidationErrors | null {
    const text = this.text.value.trim();
    return text === '' || AMOUNT_PATTERN.test(text) ? null : { amount: true };
  }

  private static toNumber(text: string): number | null {
    const value = text.trim();
    return AMOUNT_PATTERN.test(value) ? Number(value.replace(',', '.')) : null;
  }
}
