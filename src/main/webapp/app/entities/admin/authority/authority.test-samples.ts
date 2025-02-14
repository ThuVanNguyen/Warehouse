import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '80dd7227-320e-44f1-9e4f-5b2158a3555f',
};

export const sampleWithPartialData: IAuthority = {
  name: '81d6e3fb-11c6-45a9-8c39-7c98f95ac5a5',
};

export const sampleWithFullData: IAuthority = {
  name: '398de9ab-4fed-4937-af57-a7fe2fbff22a',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
