import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccessPointTemperaComponent } from './access-point-tempera.component';

describe('AccessPointTemperaComponent', () => {
  let component: AccessPointTemperaComponent;
  let fixture: ComponentFixture<AccessPointTemperaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccessPointTemperaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AccessPointTemperaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
