import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RoomThresholdsComponent} from './room-thresholds.component';

describe('RoomThresholdsComponent', () => {
  let component: RoomThresholdsComponent;
  let fixture: ComponentFixture<RoomThresholdsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoomThresholdsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RoomThresholdsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
