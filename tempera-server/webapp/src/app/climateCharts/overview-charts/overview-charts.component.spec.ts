import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OverviewChartsComponent } from './overview-charts.component';

describe('OverviewChartsComponent', () => {
  let component: OverviewChartsComponent;
  let fixture: ComponentFixture<OverviewChartsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OverviewChartsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(OverviewChartsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
