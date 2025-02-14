import dayjs from 'dayjs/esm';
import { IProductImage } from '../product-image/product-image.model';
import { IClient } from '../client/client.model';

export enum OrderStatus {
  BANG_TUONG_WARE_HOUSE = 'BANG_TUONG_WARE_HOUSE',
  LOADED_ON_VEHICLE = 'LOADED_ON_VEHICLE',
  ARRIVED_AT_HN = 'ARRIVED_AT_HN',
  DELIVERED = 'DELIVERED',
  CUSTOMER_SIGNED = 'CUSTOMER_SIGNED',
  CANCELED = 'CANCELED',
}

export interface IOrder {
  id: number;
  importVehicleNumber?: string | null;
  receiptDate?: dayjs.Dayjs | null;
  warehouseReceiptCode?: string | null;
  productCn?: string | null;
  productVn?: string | null;
  piecesReceivedCount?: number | null;
  piecesWareHouseCount?: number | null;
  totalWeight?: number | null;
  totalCubicMeter?: number | null;
  listCalculation?: string | null;
  unitPriceWeight?: number | null;
  unitPriceCubicMeter?: number | null;
  totalPrice?: number | null;
  waybill?: string | null;
  shippingAddressAndPhone?: string | null;
  exportVehicleNumber?: string | null;
  exportDate?: dayjs.Dayjs | null;
  exportQuantity?: number | null;
  note?: string | null;
  client?: Pick<IClient, 'id' | 'code' | 'name'> | null;
  status?: OrderStatus | null;
  bangTuongWareHouseDate?: dayjs.Dayjs | null;
  loadedOnVehicleDate?: dayjs.Dayjs | null;
  arrivedAtHNDate?: dayjs.Dayjs | null;
  deliveredDate?: dayjs.Dayjs | null;
  customerSignedDate?: dayjs.Dayjs | null;
  cancelDate?: dayjs.Dayjs | null;
  arrangeVehicle?: string | null;

  // TODO use Pick
  productImages?: IProductImage[];
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };

export type QueryParamsOrder = {
  clientId?: number | null;
  id?: string;
  importVehicleNumber?: string;
  receiptDate?: string; // Treat as string, not Dayjs & string
  warehouseReceiptCode?: string;
  productCn?: string;
  productVn?: string;
  piecesReceivedCount?: number;
  piecesWareHouseCount?: number;
  totalWeight?: number;
  totalCubicMeter?: number;
  listCalculation?: string;
  unitPriceWeight?: number;
  unitPriceCubicMeter?: number;
  totalPrice?: number;
  waybill?: string;
  shippingAddressAndPhone?: string;
  exportVehicleNumber?: string;
  exportDate?: string; // Treat as string
  exportQuantity?: number;
  note?: string;
  status?: OrderStatus;
  bangTuongWareHouseDate?: string; // Treat as string
  loadedOnVehicleDate?: string; // Treat as string
  arrivedAtHNDate?: string; // Treat as string
  deliveredDate?: string; // Treat as string
  customerSignedDate?: string; // Treat as string
  cancelDate?: string; // Treat as string
  arrangeVehicle?: string;
};
