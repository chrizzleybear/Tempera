import {User} from "./user.model";

export interface TemperaStation {
  id: string;
  user: string;
  enabled: boolean;
  isHealthy: boolean;
}
