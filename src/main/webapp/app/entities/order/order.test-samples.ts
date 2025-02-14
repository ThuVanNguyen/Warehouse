import dayjs from 'dayjs/esm';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 15920,
};

export const sampleWithPartialData: IOrder = {
  id: 3195,
  totalCubicMeter: 3119.67,
  extraFee: 23975.58,
  inlandFee: 9715.49,
  note: 'and',
};

export const sampleWithFullData: IOrder = {
  id: 25143,
  receiptDate: dayjs('2024-12-12'),
  warehouseReceiptCode: 'mutate',
  warehouseReceiveNote: 'salty certification',
  piecesReceivedCount: 5844,
  piecesLoadedCount: 30947,
  totalWeight: 7721.4,
  totalCubicMeter: 21659.95,
  extraFee: 22456.19,
  inlandFee: 15183.28,
  extraFeeDetail: 'incidentally',
  waybill: 'eek secularize',
  note: 'academics considerate',
};

export const sampleWithNewData: NewOrder = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
