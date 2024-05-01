import {User} from "./user.model";

export interface Project {
  id: number;
  name: string;
  description: string;
  manager: User;
  contributors?: User[];
}
