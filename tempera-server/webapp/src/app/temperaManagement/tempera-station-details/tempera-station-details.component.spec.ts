import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemperaStationDetailsComponent } from './tempera-station-details.component';

describe('TemperaStationDetailsComponent', () => {
  let component: TemperaStationDetailsComponent;
  let fixture: ComponentFixture<TemperaStationDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemperaStationDetailsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TemperaStationDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
