import {Room} from "./room.model";

export interface AccessPointCreateDto {
  room: string;
}

export interface AccessPointEditDto {
  id: string;
  room: string;
  enabled: boolean;
}
