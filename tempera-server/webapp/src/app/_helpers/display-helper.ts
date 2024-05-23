import { ColleagueStateDto } from '../../api';
import StateEnum = ColleagueStateDto.StateEnum;
import { State } from '../models/user.model';

export class DisplayHelper {
  /**
   * Get color of colleague state badges
   */
  public static getStateSeverity(state: StateEnum) {
    switch (state) {
      case State.AVAILABLE:
        return 'success';
      case State.MEETING:
        return 'warning';
      case State.DEEPWORK:
        return 'info';
      case State.OUT_OF_OFFICE:
        return 'danger';
      default:
        return 'primary';
    }
  }

  /**
   * Get color of colleague state badges
   */
  public static getColleagueStateSeverity(colleague: ColleagueStateDto) {
    if (!colleague.isVisible) {
      return 'primary';
    }
    switch (colleague.state) {
      case State.AVAILABLE:
        return 'success';
      case State.MEETING:
        return 'warning';
      case State.DEEPWORK:
        return 'info';
      case State.OUT_OF_OFFICE:
        return 'danger';
      default:
        return 'primary';
    }
  }

  /**
   * Get display text for colleague state
   */
  public static showState(state: StateEnum | undefined) {
    switch (state) {
      case StateEnum.Available:
        return 'Available';
      case StateEnum.Meeting:
        return 'In a meeting';
      case StateEnum.Deepwork:
        return 'Deep work';
      case StateEnum.OutOfOffice:
        return 'Out of office';
      default:
        return 'Unknown';

    }
  }

  /**
   * Get display text for colleague state but also consider visibility
   */
  public static showColleagueState(colleague: ColleagueStateDto | undefined) {
    if (!colleague?.isVisible) {
      return 'Hidden';
    }
    switch (colleague.state) {
      case StateEnum.Available:
        return 'Available';
      case StateEnum.Meeting:
        return 'In a meeting';
      case StateEnum.Deepwork:
        return 'Deep work';
      case StateEnum.OutOfOffice:
        return 'Out of office';
      default:
        return 'Unknown';
    }
  }
}
