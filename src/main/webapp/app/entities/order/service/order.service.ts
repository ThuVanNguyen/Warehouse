import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpEvent, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs, { Dayjs } from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrder, NewOrder, OrderStatus } from '../order.model';

export type PartialUpdateOrder = Partial<IOrder> & Pick<IOrder, 'id'>;

type RestOf<T extends IOrder | NewOrder> = Omit<T, 'receiptDate'> & {
  receiptDate?: string | null;
};

export type RestOrder = RestOf<IOrder>;

export type NewRestOrder = RestOf<NewOrder>;

export type PartialUpdateRestOrder = RestOf<PartialUpdateOrder>;

export type EntityResponseType = HttpResponse<IOrder>;

export type EntityArrayResponseType = HttpResponse<IOrder[]>;

@Injectable({ providedIn: 'root' })
export class OrderService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/orders');
  // TODO move to seperate server
  protected resourceExcelUrl = this.applicationConfigService.getEndpointFor('api/excel');

  create(order: NewOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(order);
    return this.http.post<RestOrder>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(order: IOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(order);
    return this.http
      .put<RestOrder>(`${this.resourceUrl}/${this.getOrderIdentifier(order)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(order: PartialUpdateOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(order);
    return this.http
      .patch<RestOrder>(`${this.resourceUrl}/${this.getOrderIdentifier(order)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrderIdentifier(order: Pick<IOrder, 'id'>): number {
    return order.id;
  }

  compareOrder(o1: Pick<IOrder, 'id'> | null, o2: Pick<IOrder, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrderIdentifier(o1) === this.getOrderIdentifier(o2) : o1 === o2;
  }

  addOrderToCollectionIfMissing<Type extends Pick<IOrder, 'id'>>(
    orderCollection: Type[],
    ...ordersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orders: Type[] = ordersToCheck.filter(isPresent);
    if (orders.length > 0) {
      const orderCollectionIdentifiers = orderCollection.map(orderItem => this.getOrderIdentifier(orderItem));
      const ordersToAdd = orders.filter(orderItem => {
        const orderIdentifier = this.getOrderIdentifier(orderItem);
        if (orderCollectionIdentifiers.includes(orderIdentifier)) {
          return false;
        }
        orderCollectionIdentifiers.push(orderIdentifier);
        return true;
      });
      return [...ordersToAdd, ...orderCollection];
    }
    return orderCollection;
  }

  // TODO for excel api, replace with seperated service later
  importOrders(file: File, status: OrderStatus): Observable<HttpEvent<any>> {
    const formData = new FormData();
    formData.append('file', file, file.name);

    return this.http.post<any>(`${this.resourceExcelUrl}/import?status=${status}`, formData, {
      reportProgress: true,
      observe: 'events',
    });
  }

  importExcelOrder(file: File, status: OrderStatus, date?: Date, arrangeVehicle?: string): Observable<HttpEvent<any>> {
    const formData = new FormData();
    formData.append('file', file, file.name);

    let endpoint = '';
    const params: Record<string, string> = {};

    if (date) {
      params.date = date.toISOString().split('T')[0];
    }
    if (arrangeVehicle) {
      params.arrangeVehicle = arrangeVehicle;
    }

    switch (status) {
      case OrderStatus.BANG_TUONG_WARE_HOUSE:
        endpoint = 'bang-tuong';
        break;
      case OrderStatus.LOADED_ON_VEHICLE:
        endpoint = 'loaded-vehicle';
        break;
    }

    return this.http.post<any>(`${this.resourceExcelUrl}/import/${endpoint}`, formData, {
      reportProgress: true,
      observe: 'events',
      params,
    });
  }

  exportOrders(orderIds: number[]): Observable<any> {
    return this.http.post(`${this.resourceExcelUrl}/export`, orderIds, { responseType: 'blob' });
  }

  orderStatusUpdate(orderIds: number[], status: OrderStatus, date: dayjs.Dayjs, isSelectAll: boolean): Observable<EntityArrayResponseType> {
    const params: HttpParams = new HttpParams()
      .set('newStatus', status)
      .set('date', date.local().format('YYYY-MM-DD')) // use local insteaqd of utc because choose 30/01/2025 with utc make it 19/01
      .set('selectAll', isSelectAll);
    return this.http
      .put<RestOrder[]>(`${this.resourceUrl}/update-status`, orderIds, { params, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  protected convertDateFromClient<T extends IOrder | NewOrder | PartialUpdateOrder>(order: T): T {
    return {
      ...order,
      receiptDate: order.receiptDate?.format(DATE_FORMAT) ?? null,
      exportDate: order.exportDate?.format(DATE_FORMAT) ?? null,
      bangTuongWareHouseDate: order.bangTuongWareHouseDate?.format(DATE_FORMAT) ?? null,
      loadedOnVehicleDate: order.loadedOnVehicleDate?.format(DATE_FORMAT) ?? null,
      arrivedAtHNDate: order.arrivedAtHNDate?.format(DATE_FORMAT) ?? null,
      deliveredDate: order.deliveredDate?.format(DATE_FORMAT) ?? null,
      customerSignedDate: order.customerSignedDate?.format(DATE_FORMAT) ?? null,
      cancelDate: order.cancelDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restOrder: RestOrder): IOrder {
    return {
      ...restOrder,
      receiptDate: restOrder.receiptDate ? dayjs(restOrder.receiptDate) : undefined,
      exportDate: restOrder.exportDate ? dayjs(restOrder.exportDate) : undefined,
      bangTuongWareHouseDate: restOrder.bangTuongWareHouseDate ? dayjs(restOrder.bangTuongWareHouseDate) : undefined,
      loadedOnVehicleDate: restOrder.loadedOnVehicleDate ? dayjs(restOrder.loadedOnVehicleDate) : undefined,
      arrivedAtHNDate: restOrder.arrivedAtHNDate ? dayjs(restOrder.arrivedAtHNDate) : undefined,
      deliveredDate: restOrder.deliveredDate ? dayjs(restOrder.deliveredDate) : undefined,
      customerSignedDate: restOrder.customerSignedDate ? dayjs(restOrder.customerSignedDate) : undefined,
      cancelDate: restOrder.cancelDate ? dayjs(restOrder.cancelDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOrder>): HttpResponse<IOrder> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOrder[]>): HttpResponse<IOrder[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
