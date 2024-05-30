import { SimpleGroupxProjectDto } from '../../api';


export class OverlappingProjectHelper {

  /*
   * Calculates and returns the duplicated projects
   */
  static getDuplicatedProjects(availableProjects: SimpleGroupxProjectDto[]): Map<string, {
    projects: SimpleGroupxProjectDto[];
    originalName: string
  }> {
    let duplicatedProjects = new Map<string, { projects: SimpleGroupxProjectDto[], originalName: string }>();

    availableProjects?.map(x => x?.projectId!).forEach(id => {
      const duplicatedProjectsData = availableProjects?.filter(y => y.projectId === id) ?? [];
      if (duplicatedProjectsData?.length > 1) {
        duplicatedProjects.set(id, {
          projects: duplicatedProjectsData,
          originalName: duplicatedProjectsData[0].projectName!,
        });
      }
    });
    return duplicatedProjects;
  }

  /*
  * Renames projects that have the same projectId (happens when a user is assigned to a project from multiple groups)
   */
  static renameOverlappingProjects(duplicatedProjects: Map<string, { projects: SimpleGroupxProjectDto[], originalName: string }>, availableProjects: SimpleGroupxProjectDto[]) {
    // Rename projects (in availableProjects and tableEntries)
    if (duplicatedProjects.size > 0) {
      const duplicatedIds = Array.from(duplicatedProjects.keys());

      availableProjects?.forEach(project => {
        if (duplicatedIds.includes(project.projectId!)) {
          project.projectName = `${project.projectName} - ${project.groupName}`;
        }
      });
    }
  }
}
