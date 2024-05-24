import { State, Visibility } from './user.model';

export interface HomeData {
  temperature: number;
  humidity: number;
  irradiance: number;
  nmvoc: number;
  visibility: Visibility;
  state: State;
  stateTimestamp: string;
  project?: Project;
  colleagueStates: ColleagueState[];
}

export interface Project {
  id: number;
  name: string;
}

export interface ColleagueState {
  name: string;
  workplace: string;
  state: State;
}
