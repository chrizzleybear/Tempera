import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemperaStationsComponent } from './tempera-stations.component';

describe('TemperaStationsComponent', () => {
  let component: TemperaStationsComponent;
  let fixture: ComponentFixture<TemperaStationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemperaStationsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TemperaStationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
