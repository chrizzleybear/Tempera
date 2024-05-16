import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupsGroupleadComponent } from './groups-grouplead.component';

describe('GroupsGroupleadComponent', () => {
  let component: GroupsGroupleadComponent;
  let fixture: ComponentFixture<GroupsGroupleadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GroupsGroupleadComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GroupsGroupleadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
