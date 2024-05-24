import {User} from "./user.model";

export interface TemperaStation {
  id: string;
  user: User;
  enabled: boolean;
  isHealthy: boolean;
}
