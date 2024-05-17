export interface AccessPoint {
  id: UUID;
  temperaStations: TemperaStation[];
  room: Room;
  enabled: boolean;
}
