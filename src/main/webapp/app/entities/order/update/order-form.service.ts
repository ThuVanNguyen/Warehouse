import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOrder, NewOrder } from '../order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrder for edit and NewOrderFormGroupInput for create.
 */
type OrderFormGroupInput = IOrder | PartialWithRequiredKeyOf<NewOrder>;

type OrderFormDefaults = Pick<NewOrder, 'id'>;

type OrderFormGroupContent = {
  id: FormControl<IOrder['id'] | NewOrder['id']>;
  importVehicleNumber: FormControl<IOrder['importVehicleNumber']>;
  receiptDate: FormControl<IOrder['receiptDate']>;
  warehouseReceiptCode: FormControl<IOrder['warehouseReceiptCode']>;
  productCn: FormControl<IOrder['productCn']>;
  productVn: FormControl<IOrder['productVn']>;
  piecesReceivedCount: FormControl<IOrder['piecesReceivedCount']>;
  piecesWareHouseCount: FormControl<IOrder['piecesWareHouseCount']>;
  totalWeight: FormControl<IOrder['totalWeight']>;
  totalCubicMeter: FormControl<IOrder['totalCubicMeter']>;
  listCalculation: FormControl<IOrder['listCalculation']>;
  unitPriceWeight: FormControl<IOrder['unitPriceWeight']>;
  unitPriceCubicMeter: FormControl<IOrder['unitPriceCubicMeter']>;
  totalPrice: FormControl<IOrder['totalPrice']>;
  waybill: FormControl<IOrder['waybill']>;
  shippingAddressAndPhone: FormControl<IOrder['shippingAddressAndPhone']>;
  exportVehicleNumber: FormControl<IOrder['exportVehicleNumber']>;
  exportDate: FormControl<IOrder['exportDate']>;
  exportQuantity: FormControl<IOrder['exportQuantity']>;
  note: FormControl<IOrder['note']>;
  client: FormControl<IOrder['client']>;
  status: FormControl<IOrder['status']>;
  bangTuongWareHouseDate: FormControl<IOrder['bangTuongWareHouseDate']>;
  loadedOnVehicleDate: FormControl<IOrder['loadedOnVehicleDate']>;
  arrivedAtHNDate: FormControl<IOrder['arrivedAtHNDate']>;
  deliveredDate: FormControl<IOrder['deliveredDate']>;
  customerSignedDate: FormControl<IOrder['customerSignedDate']>;
  cancelDate: FormControl<IOrder['cancelDate']>;
  arrangeVehicle: FormControl<IOrder['arrangeVehicle']>;
};

export type OrderFormGroup = FormGroup<OrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrderFormService {
  createOrderFormGroup(order: OrderFormGroupInput = { id: null }): OrderFormGroup {
    const orderRawValue = {
      ...this.getFormDefaults(),
      ...order,
    };
    return new FormGroup<OrderFormGroupContent>({
      id: new FormControl(
        { value: orderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      importVehicleNumber: new FormControl(orderRawValue.importVehicleNumber),
      receiptDate: new FormControl(orderRawValue.receiptDate, {
        nonNullable: true,
        validators: [Validators.required],
      }),
      warehouseReceiptCode: new FormControl(orderRawValue.warehouseReceiptCode, {
        nonNullable: true,
        validators: [Validators.required],
      }),
      productCn: new FormControl(orderRawValue.productCn),
      productVn: new FormControl(orderRawValue.productVn),
      piecesReceivedCount: new FormControl(orderRawValue.piecesReceivedCount),
      piecesWareHouseCount: new FormControl(orderRawValue.piecesWareHouseCount),
      totalWeight: new FormControl(orderRawValue.totalWeight),
      totalCubicMeter: new FormControl(orderRawValue.totalCubicMeter),
      listCalculation: new FormControl(orderRawValue.listCalculation),
      unitPriceWeight: new FormControl(orderRawValue.unitPriceWeight),
      unitPriceCubicMeter: new FormControl(orderRawValue.unitPriceCubicMeter),
      totalPrice: new FormControl(orderRawValue.totalPrice),
      waybill: new FormControl(orderRawValue.waybill),
      shippingAddressAndPhone: new FormControl(orderRawValue.shippingAddressAndPhone),
      exportVehicleNumber: new FormControl(orderRawValue.exportVehicleNumber),
      exportDate: new FormControl(orderRawValue.exportDate),
      exportQuantity: new FormControl(orderRawValue.exportQuantity),
      note: new FormControl(orderRawValue.note),
      client: new FormControl(orderRawValue.client),
      status: new FormControl(orderRawValue.status),
      bangTuongWareHouseDate: new FormControl(orderRawValue.bangTuongWareHouseDate),
      loadedOnVehicleDate: new FormControl(orderRawValue.loadedOnVehicleDate),
      arrivedAtHNDate: new FormControl(orderRawValue.arrivedAtHNDate),
      deliveredDate: new FormControl(orderRawValue.deliveredDate),
      customerSignedDate: new FormControl(orderRawValue.customerSignedDate),
      cancelDate: new FormControl(orderRawValue.cancelDate),
      arrangeVehicle: new FormControl(orderRawValue.arrangeVehicle),
    });
  }

  getOrder(form: OrderFormGroup): IOrder | NewOrder {
    return form.getRawValue() as IOrder | NewOrder;
  }

  resetForm(form: OrderFormGroup, order: OrderFormGroupInput): void {
    const orderRawValue = { ...this.getFormDefaults(), ...order };
    form.reset(
      {
        ...orderRawValue,
        id: { value: orderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrderFormDefaults {
    return {
      id: null,
    };
  }
}
