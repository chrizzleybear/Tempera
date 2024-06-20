import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OverviewTablesComponent } from './overview-tables.component';

describe('OverviewTablesComponent', () => {
  let component: OverviewTablesComponent;
  let fixture: ComponentFixture<OverviewTablesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OverviewTablesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(OverviewTablesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
