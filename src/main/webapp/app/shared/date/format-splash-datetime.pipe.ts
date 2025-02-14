import { Pipe, PipeTransform } from '@angular/core';
import dayjs from 'dayjs';

@Pipe({
  standalone: true,
  name: 'formatSplashDatetime',
})
export default class FormatSplashDatetimePipe implements PipeTransform {
  transform(day: dayjs.Dayjs | null | undefined): string {
    return day ? day.format('YYYY/MM/DD HH:mm:ss') : '';
  }
}
