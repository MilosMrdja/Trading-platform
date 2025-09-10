import {InstrumentResponse} from './instrument-response';
import {AccountResponse} from './account-response';
import {Unit} from '../enums/unit';
import {Status} from '../enums/status';
import {DeliveryType} from '../enums/delivery-type';
import {Direction} from 'node:tty';

export interface TradeResponse {
  id:number,
  instrument: InstrumentResponse,
  account: AccountResponse,
  quantity:number,
  price:number,
  direction:Direction,
  deliveryType:DeliveryType,
  unit:Unit,
  status:Status,
}
