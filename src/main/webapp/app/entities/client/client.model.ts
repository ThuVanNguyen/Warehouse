export interface IClient {
  id: number;
  code?: string | null;
  name?: string | null;
  email?: string | null;
  phone?: string | null;
}

export type NewClient = Omit<IClient, 'id'> & { id: null };
