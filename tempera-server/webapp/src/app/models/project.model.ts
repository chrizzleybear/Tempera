import {User} from "./user.model";
import {Group} from "./group.model";

export interface Project {
  projectId: number;
  name: string;
  description: string;
  manager: User;
  groups?: Group[];
  contributors?: User[];

}
