import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'wrapFn',
  standalone: true,
})
/**
 * Wraps a function in a pipe.
 * For displaying data in a template, using pipes can dramatically improve performance because of memoization.
 */
export class WrapFnPipe implements PipeTransform {
  transform<R, F extends (...args: any) => R>(func: F, ...args: Parameters<F>): R {
    return func(...args);
  }
}

