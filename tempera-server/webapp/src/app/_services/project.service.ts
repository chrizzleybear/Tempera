import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {Project, ProjectDetailsDto} from "../models/project.model";
import {Group} from "../models/group.model";
import {ContributorAssignmentDTO, GroupAssignmentDTO, ProjectCreateDTO, ProjectUpdateDTO} from "../models/projectDtos";
import {User} from "../models/user.model";


@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private API_URL = 'http://localhost:8080/api/project/';

  constructor(private http: HttpClient) { }

  getAllProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(this.API_URL + 'all');
  }

  createProject(dto: ProjectCreateDTO): Observable<ProjectDetailsDto> {
    return this.http.post<ProjectDetailsDto>(`${this.API_URL}create`, dto);
  }

  updateProject(dto: ProjectUpdateDTO): Observable<Project> {
    return this.http.put<Project>(`${this.API_URL}update`, dto);
  }

  deleteProject(projectId: number): Observable<string> {
    return this.http.delete(`${this.API_URL}delete/${projectId}`, { responseType: 'text' })
      .pipe(
        catchError(error => throwError(() => new Error('Error deleting project: ' + error.message)))
      );
  }

  getProjectById(projectId: number): Observable<ProjectDetailsDto> {
    return this.http.get<ProjectDetailsDto>(`${this.API_URL}loadExtendedProject/${projectId}`);
  }

  addGroupToProject(dto: GroupAssignmentDTO): Observable<void> {
    return this.http.post<void>(`${this.API_URL}addGroup`, dto);
  }

    getGroups(projectId: number): Observable<Group[]> {
    return this.http.get<Group[]>(`${this.API_URL}getGroupsOfProject/${projectId}`);
  }

  deleteGroupFromProject(dto: GroupAssignmentDTO): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}removeGroup/${dto.projectId}/${dto.groupId}`);
  }

  addMemberToProject(dto: ContributorAssignmentDTO): Observable<Project> {
    console.log('Adding member to project:', dto);
    return this.http.post<Project>(`${this.API_URL}addContributor`, dto);
  }

  removeMemberFromProject(dto: ContributorAssignmentDTO): Observable<Project> {
    return this.http.delete<Project>(`${this.API_URL}removeContributor/${dto.projectId}/${dto.groupId}/${dto.contributorId}`);
  }

  getProjectsOfGroup(groupId: number): Observable<Project[]> {
    return this.http.get<Project[]>(`${this.API_URL}projectsOfGroup/${groupId}`);
  }

  getContributors(groupId: number, projectId: number | undefined): Observable<User[]> {
    return this.http.get<User[]>(`${this.API_URL}contributors/${groupId}/${projectId}`);
  }
}
