import { IClient, NewClient } from './client.model';

export const sampleWithRequiredData: IClient = {
  id: 1382,
};

export const sampleWithPartialData: IClient = {
  id: 12617,
  name: 'helplessly jaggedly',
  phone: '971-609-0440 x1588',
};

export const sampleWithFullData: IClient = {
  id: 28777,
  code: 'since so',
  name: 'aha sell',
  email: 'Matilde_Trantow@hotmail.com',
  phone: '844.670.6559',
};

export const sampleWithNewData: NewClient = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
