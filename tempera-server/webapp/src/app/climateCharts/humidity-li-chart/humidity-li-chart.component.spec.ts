import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HumidityLiChartComponent } from './humidity-li-chart.component';

describe('HumidityLiChartComponent', () => {
  let component: HumidityLiChartComponent;
  let fixture: ComponentFixture<HumidityLiChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HumidityLiChartComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HumidityLiChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
