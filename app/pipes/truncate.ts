import {Pipe} from '@angular/core';
 
@Pipe({
  name: 'truncate'
})
 
/*
Truncate the input value to specified length.
*/
export class Truncate {
  transform(value: string, args: string[]) : string {
      let limit = parseInt(args[0]);
      return value.length > limit ? value.substring(0, limit) + '...' : value;
    }
}