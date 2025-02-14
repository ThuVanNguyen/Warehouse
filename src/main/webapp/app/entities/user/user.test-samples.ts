import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 21385,
  login: '1MZkbE',
};

export const sampleWithPartialData: IUser = {
  id: 791,
  login: "5-@gvvTIq\\8iIv1\\JYuCeEZ\\5eYt\\Lpg8Pn\\'CQ",
};

export const sampleWithFullData: IUser = {
  id: 29587,
  login: 'AO@if\\DV9Io\\ech\\dG',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
