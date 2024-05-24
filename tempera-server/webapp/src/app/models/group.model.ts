import {User} from "./user.model";

export interface Group {

  id: string;
  name: string;
  description: string;
  groupLead: User;
  members: User[];
}
