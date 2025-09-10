import {Status} from '../enums/status';
import {Direction} from 'node:tty';

export interface TradeFilter {
  status: string,
  direction: string,
  price:number | null
}
