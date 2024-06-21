import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HumidityTableComponent } from './humidity-table.component';

describe('HumidityTableComponent', () => {
  let component: HumidityTableComponent;
  let fixture: ComponentFixture<HumidityTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HumidityTableComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HumidityTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
