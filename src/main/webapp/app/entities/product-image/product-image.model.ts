import dayjs from 'dayjs/esm';
import { IOrder } from 'app/entities/order/order.model';

export interface IProductImage {
  id: number;
  type?: string | null;
  base64?: string | null;
  order?: Pick<IOrder, 'id'> | null;
}

export type NewProductImage = Omit<IProductImage, 'id'> & { id: null };
