import {Room} from "./room.model";

export interface AccessPointCreateDto {
  id: string;
  room: string;
}

export interface AccessPointEditDto {
  id: string;
  room: string;
  enabled: boolean;
}
