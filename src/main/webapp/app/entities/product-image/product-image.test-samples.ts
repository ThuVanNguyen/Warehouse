import dayjs from 'dayjs/esm';

import { IProductImage, NewProductImage } from './product-image.model';

export const sampleWithRequiredData: IProductImage = {
  id: 11594,
};

export const sampleWithPartialData: IProductImage = {
  id: 7561,
  len: 5466,
  createDate: dayjs('2024-12-13'),
};

export const sampleWithFullData: IProductImage = {
  id: 28368,
  code: 'junior apud after',
  len: 28853,
  createDate: dayjs('2024-12-13'),
  type: 'owlishly thorough',
  base64: 'before',
};

export const sampleWithNewData: NewProductImage = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
