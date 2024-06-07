export interface ProjectCreateDTO {
  name: string;
  description: string;
  manager: string;
}

export interface ProjectUpdateDTO {
  projectId: number;
  name: string;
  description: string;
  manager: string;
}

export interface ContributorAssignmentDTO {
  projectId: string;
  groupId: string;
  contributorId: string;
}
