import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HumidityIrradianceChartComponent } from './humidity-irradiance-chart.component';

describe('TemperatureCo2ChartComponent', () => {
  let component: HumidityIrradianceChartComponent;
  let fixture: ComponentFixture<HumidityIrradianceChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HumidityIrradianceChartComponent],
    })
      .compileComponents();

    fixture = TestBed.createComponent(HumidityIrradianceChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
