import {Room} from "./room.model";

export interface AccessPoint {
  id: number;
  temperaStations: any; //TemperaStation[];
  room: Room;
  enabled: boolean;
}
