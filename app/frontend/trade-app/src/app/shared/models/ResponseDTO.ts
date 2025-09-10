export interface ResponseDTO<T> {
  items: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}
