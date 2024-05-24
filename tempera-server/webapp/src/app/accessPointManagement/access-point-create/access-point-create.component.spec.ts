import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccessPointCreateComponent } from './access-point-create.component';

describe('AccessPointCreateComponent', () => {
  let component: AccessPointCreateComponent;
  let fixture: ComponentFixture<AccessPointCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccessPointCreateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AccessPointCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
