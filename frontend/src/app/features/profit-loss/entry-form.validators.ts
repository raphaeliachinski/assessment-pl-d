import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

/** Future improvement: remove hardcorded field names */
export const atLeastOneAmountValidator: ValidatorFn = (group: AbstractControl): ValidationErrors | null => {
  const amounts = ['income', 'cost', 'additionalCost'].map((name) => (group.get(name)?.value as number | null) ?? 0);
  return amounts.some((amount) => amount > 0) ? null : { allZero: true };
};

export const notBlankValidator: ValidatorFn = (control: AbstractControl<string>): ValidationErrors | null =>
  (control.value ?? '').trim() === '' ? { blank: true } : null;
