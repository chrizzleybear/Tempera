import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'wrapFn',
  standalone: true,
})
/**
 * Using functions in Angular templates can cause significant performance slowdowns.
 * While this is not a clean solution, wrapping a function in a pipe is a workaround to this issue.
 */
export class WrapFnPipe implements PipeTransform {
  transform<R, F extends (...args: any[]) => R>(
    func: F,
    ...args: Parameters<F>
  ): R {
    return func(...args);
  }
}
