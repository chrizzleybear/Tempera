import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemperatureCo2ChartComponent } from './temperature-co2-chart.component';

describe('TemperatureCo2ChartComponent', () => {
  let component: TemperatureCo2ChartComponent;
  let fixture: ComponentFixture<TemperatureCo2ChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemperatureCo2ChartComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TemperatureCo2ChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
