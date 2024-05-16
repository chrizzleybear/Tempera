import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectGroupsComponent } from './project-groups.component';

describe('ProjectGroupsComponent', () => {
  let component: ProjectGroupsComponent;
  let fixture: ComponentFixture<ProjectGroupsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProjectGroupsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProjectGroupsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
