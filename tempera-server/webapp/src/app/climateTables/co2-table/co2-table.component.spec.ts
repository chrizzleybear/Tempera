import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Co2TableComponent } from './co2-table.component';

describe('Co2TableComponent', () => {
  let component: Co2TableComponent;
  let fixture: ComponentFixture<Co2TableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Co2TableComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(Co2TableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
