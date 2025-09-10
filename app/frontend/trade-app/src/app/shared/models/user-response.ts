import {Role} from '../enums/role';

export interface UserResponse {
  id: number;
  name: string;
  email: string;
  role: Role;
}
