export interface AccessPointCreateDto {
  room: string;
  enabled: boolean;
}

export interface AccessPointEditDto {
  id: number;
  room: string;
  enabled: boolean;
}
