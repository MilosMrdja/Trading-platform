import {AccountResponse} from './account-response';

export interface AccountStatusResponse {
  id: number,
  account: AccountResponse,
  ote: number,
}
