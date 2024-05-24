import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccessPointEditComponent } from './access-point-edit.component';

describe('AccessPointEditComponent', () => {
  let component: AccessPointEditComponent;
  let fixture: ComponentFixture<AccessPointEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccessPointEditComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AccessPointEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
