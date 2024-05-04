import {User} from "./user.model";

export interface Group {

  id: string;
  name: string;
  description: string;
  members: User[];
}
