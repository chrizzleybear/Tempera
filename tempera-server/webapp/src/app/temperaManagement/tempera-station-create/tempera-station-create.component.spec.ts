import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemperaStationCreateComponent } from './tempera-station-create.component';

describe('TemperaStationCreateComponent', () => {
  let component: TemperaStationCreateComponent;
  let fixture: ComponentFixture<TemperaStationCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemperaStationCreateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TemperaStationCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
