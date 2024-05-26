export interface User {
  id: string;
  firstName: string;
  lastName: string;
  username: string;
  password: string;
  email: string;
  roles: string[];
  enabled: boolean;
  visibility: Visibility;
  state: State;
}

export enum Visibility {
  PUBLIC = 'PUBLIC',
  PRIVATE = 'PRIVATE',
  HIDDEN = 'HIDDEN',
}

export enum State {
  AVAILABLE = 'AVAILABLE',
  MEETING = 'MEETING',
  OUT_OF_OFFICE = 'OUT_OF_OFFICE',
  DEEPWORK = 'DEEPWORK',
}

export interface DropdownOptionUser {
  label: string;
  value: User;
}

export interface SimpleUserDto {
  username: string;
  firstName: string;
  lastName: string;
  email: string;
}
