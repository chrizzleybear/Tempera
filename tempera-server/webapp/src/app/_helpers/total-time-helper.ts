import { Table } from 'primeng/table';
import { AccumulatedTimeDto } from '../../api';
import StateEnum = AccumulatedTimeDto.StateEnum;

export interface Duration {
  startTime: Date,
  endTime: Date,
}

export interface WithState {
  state: StateEnum,

}

export type TotalTimeWithStates = Record<StateEnum, number>;

export class TotalTimeHelper {
  /*
  * Calculates the total work time with the current active filters
 */
  public static calculate<T extends Duration & WithState>(entries: T[]) {
    let totalTimeTemp: number = 0;

    entries.filter(x => x.state !== StateEnum.OutOfOffice).forEach(entry => {
      totalTimeTemp += entry.endTime.getTime() - entry.startTime.getTime();
    });
    const hours = Math.floor(totalTimeTemp / 3600000);

    const remainingTime = totalTimeTemp % 3600000;

    const minutes = Math.floor(remainingTime / 60000);
    return  { hours: hours, minutes: minutes };
  }

  /*
 * Calculates the total work time in hours of each state with the current active filters
*/
  public static calculateWithState<T extends Duration & WithState>(entries: T[]) {
    let totalTimes: TotalTimeWithStates = {
      AVAILABLE: 0,
      MEETING: 0,
      DEEPWORK: 0,
      OUT_OF_OFFICE: 0,
    };

    entries.forEach(entry => {
      totalTimes[entry.state] += entry.endTime.getTime() - entry.startTime.getTime();
    });

    Object.keys(totalTimes).forEach(state => {
      const stateKey = state as StateEnum;
      totalTimes[stateKey] = Math.floor(totalTimes[stateKey] / 3600000)
    });

    return  totalTimes;
  }

  /*
  * Returns the entries that are currently displayed in a PrimeNG table, depending on the active filters.
  * @param table The PrimeNG table to get the entries from
  * @template T The type of the entries used in the table
 */
  public static getFilteredEntries<T>(table?: Table): T[] {
    const filters = table?.filters as any;
    if (filters === undefined) {
      return [];
    }

    const hasActiveFilter = Object.keys(filters).some(key => filters[key]?.value);

    if (hasActiveFilter) {
      return table?.filteredValue ?? [] as T[];
    } else {
      return  table?.value ?? [] as T[];
    }
  }
}
