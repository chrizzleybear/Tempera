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

export interface GroupAssignmentDTO {
  projectId: number;
  groupId: number;
}

export interface ContributorAssignmentDTO {
  projectId: number;
  groupId: number;
  contributorId: string;
}
