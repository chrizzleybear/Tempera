import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccessPointDetailsComponent } from './access-point-details.component';

describe('AccessPointDetailsComponent', () => {
  let component: AccessPointDetailsComponent;
  let fixture: ComponentFixture<AccessPointDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccessPointDetailsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AccessPointDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
