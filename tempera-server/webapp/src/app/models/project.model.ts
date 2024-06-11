import {User} from "./user.model";
import {Group} from "./group.model";
import {SimpleGroupDto, SimpleUserDto} from "../../api";

export interface Project {
  projectId: number;
  name: string;
  description: string;
  manager: User;
  groups?: Group[];
  contributors?: User[];

}

export interface ProjectDetailsDto {
  simpleProjectDto: SimpleProjectDto;
  manager: SimpleUserDto;
  connectedGroups: SimpleGroupDto[];
  contributors: SimpleUserDto[];
}

export interface SimpleProjectDto {
  projectId: number;
  name: string;
  description: string;
  manager: string;
}
