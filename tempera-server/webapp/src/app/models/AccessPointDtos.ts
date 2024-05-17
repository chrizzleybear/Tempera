import {Room} from "./room.model";

export interface AccessPointCreateDto {
  room: Room;
  enabled: boolean;
}

export interface AccessPointEditDto {
  id: number;
  room: Room;
  enabled: boolean;
}
