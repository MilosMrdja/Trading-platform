import {Direction} from '../enums/direction';
import {Unit} from '../enums/unit';
import {DeliveryType} from '../enums/delivery-type';

export interface TradeRequest {
  instrumentId: number,
  accountId: number,
  quantity: number,
  price: number,
  direction: Direction,
  deliveryType: DeliveryType,
  unit:Unit
}
