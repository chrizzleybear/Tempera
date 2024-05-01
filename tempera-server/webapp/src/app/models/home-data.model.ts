import { State, Visibility } from './user.model';

export interface HomeData {
  temperature: number;
  humidity: number;
  brightness: number;
  co2: number;
  visibility: Visibility;
  state: State;
  stateTimeStamp: Date;
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
