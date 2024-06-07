import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {Project} from "../models/project.model";
import {Group} from "../models/group.model";

import {User} from "../models/user.model";
import { ExtendedProjectDto, SimpleProjectDto } from '../../api';
import { ContributorAssignmentDTO } from '../models/projectDtos';


@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private API_URL = 'http://localhost:8080/api/project/';

  constructor(private http: HttpClient) { }

  getAllProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(this.API_URL + 'all');
  }

  createProject(dto: SimpleProjectDto): Observable<ExtendedProjectDto> {
    return this.http.post<ExtendedProjectDto>(`${this.API_URL}create`, dto);
  }

  updateProject(dto: SimpleProjectDto): Observable<Project> {
    return this.http.put<Project>(`${this.API_URL}update`, dto);
  }

  deleteProject(projectId: number): Observable<string> {
    return this.http.delete(`${this.API_URL}delete/${projectId}`, { responseType: 'text' })
      .pipe(
        catchError(error => throwError(() => new Error('Error deleting project: ' + error.message)))
      );
  }

  getProjectById(projectId: string): Observable<ExtendedProjectDto> {
    return this.http.get<ExtendedProjectDto>(`${this.API_URL}loadExtendedProject/${projectId}`);
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
