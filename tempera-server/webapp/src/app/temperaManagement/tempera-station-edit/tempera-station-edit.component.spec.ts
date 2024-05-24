import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemperaStationEditComponent } from './tempera-station-edit.component';

describe('TemperaStationEditComponent', () => {
  let component: TemperaStationEditComponent;
  let fixture: ComponentFixture<TemperaStationEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemperaStationEditComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TemperaStationEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
