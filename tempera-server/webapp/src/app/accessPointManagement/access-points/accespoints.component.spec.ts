import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccesspointsComponent } from './accespoints.component';

describe('AccespointsComponent', () => {
  let component: AccesspointsComponent;
  let fixture: ComponentFixture<AccesspointsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccesspointsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccesspointsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
