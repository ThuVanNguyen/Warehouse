import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../order.test-samples';

import { OrderFormService } from './order-form.service';

describe('Order Form Service', () => {
  let service: OrderFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrderFormService);
  });

  describe('Service methods', () => {
    describe('createOrderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            receiptDate: expect.any(Object),
            warehouseReceiptCode: expect.any(Object),
            warehouseReceiveNote: expect.any(Object),
            piecesReceivedCount: expect.any(Object),
            piecesLoadedCount: expect.any(Object),
            totalWeight: expect.any(Object),
            totalCubicMeter: expect.any(Object),
            extraFee: expect.any(Object),
            inlandFee: expect.any(Object),
            extraFeeDetail: expect.any(Object),
            waybill: expect.any(Object),
            note: expect.any(Object),
            warehouse: expect.any(Object),
            inlandLogistic: expect.any(Object),
            client: expect.any(Object),
            product: expect.any(Object),
            unit: expect.any(Object),
          }),
        );
      });

      it('passing IOrder should create a new form with FormGroup', () => {
        const formGroup = service.createOrderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            receiptDate: expect.any(Object),
            warehouseReceiptCode: expect.any(Object),
            warehouseReceiveNote: expect.any(Object),
            piecesReceivedCount: expect.any(Object),
            piecesLoadedCount: expect.any(Object),
            totalWeight: expect.any(Object),
            totalCubicMeter: expect.any(Object),
            extraFee: expect.any(Object),
            inlandFee: expect.any(Object),
            extraFeeDetail: expect.any(Object),
            waybill: expect.any(Object),
            note: expect.any(Object),
            warehouse: expect.any(Object),
            inlandLogistic: expect.any(Object),
            client: expect.any(Object),
            product: expect.any(Object),
            unit: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrder', () => {
      it('should return NewOrder for default Order initial value', () => {
        const formGroup = service.createOrderFormGroup(sampleWithNewData);

        const order = service.getOrder(formGroup) as any;

        expect(order).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrder for empty Order initial value', () => {
        const formGroup = service.createOrderFormGroup();

        const order = service.getOrder(formGroup) as any;

        expect(order).toMatchObject({});
      });

      it('should return IOrder', () => {
        const formGroup = service.createOrderFormGroup(sampleWithRequiredData);

        const order = service.getOrder(formGroup) as any;

        expect(order).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrder should not enable id FormControl', () => {
        const formGroup = service.createOrderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrder should disable id FormControl', () => {
        const formGroup = service.createOrderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
