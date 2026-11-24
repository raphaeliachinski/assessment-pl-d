import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EntriesTableComponent } from './entries-table.component';
import { provideNoopAnimations } from '@angular/platform-browser/animations';

describe('EntriesTableComponent', () => {
  let component: EntriesTableComponent;
  let fixture: ComponentFixture<EntriesTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EntriesTableComponent],
      providers: [provideNoopAnimations()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EntriesTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
