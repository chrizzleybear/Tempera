import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IrrTableComponent } from './irr-table.component';

describe('IrrTableComponent', () => {
  let component: IrrTableComponent;
  let fixture: ComponentFixture<IrrTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IrrTableComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(IrrTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
