import {Room} from "./room.model";
import {TemperaStation} from "./temperaStation.model";

export interface AccessPoint {
  id: number;
  temperaStations: TemperaStation[];
  room: Room;
  enabled: boolean;
  isHealthy: boolean;
}
