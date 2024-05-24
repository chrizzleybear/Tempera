import {AccessPoint} from "./accessPoint.model";
import {Threshold} from "./threshold.model";

export interface Room {
  id: string;
  thresholds: Threshold[];
  accessPoint: AccessPoint;
}
