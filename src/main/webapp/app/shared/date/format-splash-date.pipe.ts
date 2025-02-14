import { Pipe, PipeTransform } from '@angular/core';
import dayjs from 'dayjs';

@Pipe({
  standalone: true,
  name: 'formatSplashDate',
})
export default class FormatSplashDatePipe implements PipeTransform {
  transform(day: dayjs.Dayjs | null | undefined): string {
    return day ? day.format('YYYY/MM/DD') : '';
  }
}
