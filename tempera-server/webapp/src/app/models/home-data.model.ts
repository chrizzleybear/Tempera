export interface HomeData {
  temperature: number;
  humidity: number;
  brightness: number;
  co2: number;
  visibility: Visibility;
  state: State;
}

export enum Visibility {
  PUBLIC = "PUBLIC",
  PRIVATE = "PRIVATE",
  HIDDEN = "HIDDEN",
}

export enum State {
  AVAILABLE = "AVAILABLE",
  MEETING = "MEETING",
  OUT_OF_OFFICE = "OUT_OF_OFFICE",
  DEEPWORK = "DEEPWORK",
}
