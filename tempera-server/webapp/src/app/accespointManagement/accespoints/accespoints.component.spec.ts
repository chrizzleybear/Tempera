import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccespointsComponent } from './accespoints.component';

describe('AccespointsComponent', () => {
  let component: AccespointsComponent;
  let fixture: ComponentFixture<AccespointsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccespointsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AccespointsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
