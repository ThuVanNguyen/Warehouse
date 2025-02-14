import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { combineLatest, Observable } from 'rxjs';
import { catchError, finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { OrderService } from '../service/order.service';
import { IOrder, OrderStatus } from '../order.model';
import { OrderFormGroup, OrderFormService } from './order-form.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OrderClientDialogComponent } from '../modals/order-client-dialog.component';
import { IProductImage } from '../../product-image/product-image.model';
import { ProductImageService } from '../../product-image/service/product-image.service';
import dayjs from 'dayjs/esm';

@Component({
  standalone: true,
  selector: 'jhi-order-update',
  templateUrl: './order-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrderUpdateComponent implements OnInit {
  isSaving = false;
  order: IOrder | null = null;

  clientsSharedCollection: IClient[] = [];

  productIamgesSharedCollection: IProductImage[] = [];

  orderStatuses = Object.values(OrderStatus);

  protected orderService = inject(OrderService);
  protected orderFormService = inject(OrderFormService);
  protected clientService = inject(ClientService);
  protected productImageService = inject(ProductImageService);
  protected activatedRoute = inject(ActivatedRoute);
  protected modalService = inject(NgbModal);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrderFormGroup = this.orderFormService.createOrderFormGroup();

  compareClient = (o1: IClient | null, o2: IClient | null): boolean => this.clientService.compareClient(o1, o2);

  ngOnInit(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParams])
      .pipe(
        catchError((err: any) => {
          console.error(err);
          return []; // Trả về Observable thay thế
        }),
      )
      .subscribe(([{ order }, params]) => {
        if (order) {
          // Action update
          this.order = order;
          if (order) {
            this.updateForm(order);
          }

          this.loadRelationshipsOptions();
        } else if (Object.keys(params).length !== 0) {
          // Action duplicate
          const duplicatedOrder: any = {
            client: {
              id: +params['clientId'],
            },
            importVehicleNumber: params['importVehicleNumber'] || '',
            receiptDate: params['receiptDate'] ? dayjs(params['receiptDate']) : null,
            warehouseReceiptCode: params['warehouseReceiptCode'] || '',
            productCn: params['productCn'] || '',
            productVn: params['productVn'] || '',
            piecesReceivedCount: params['piecesReceivedCount'] || '',
            piecesWareHouseCount: params['piecesWareHouseCount'] || '',
            totalWeight: params['totalWeight'] || '',
            totalCubicMeter: params['totalCubicMeter'] || '',
            listCalculation: params['listCalculation'] || '',
            unitPriceWeight: params['unitPriceWeight'] || '',
            unitPriceCubicMeter: params['unitPriceCubicMeter'] || '',
            totalPrice: params['totalPrice'] || '',
            waybill: params['waybill'] || '',
            shippingAddressAndPhone: params['shippingAddressAndPhone'] || '',
            exportVehicleNumber: params['exportVehicleNumber'] || '',
            exportDate: params['exportDate'] ? dayjs(params['exportDate']) : null,
            exportQuantity: params['exportQuantity'] || '',
            note: params['note'] || '',
            status: params['status'] || '',
            bangTuongWareHouseDate: params['bangTuongWareHouseDate'] ? dayjs(params['bangTuongWareHouseDate']) : null,
            loadedOnVehicleDate: params['loadedOnVehicleDate'] ? dayjs(params['loadedOnVehicleDate']) : null,
            arrivedAtHNDate: params['arrivedAtHNDate'] ? dayjs(params['arrivedAtHNDate']) : null,
            deliveredDate: params['deliveredDate'] ? dayjs(params['deliveredDate']) : null,
            customerSignedDate: params['customerSignedDate'] ? dayjs(params['customerSignedDate']) : null,
            cancelDate: params['cancelDate'] ? dayjs(params['cancelDate']) : null,
            arrangeVehicle: params['arrangeVehicle'] || '',
          };
          this.updateForm(duplicatedOrder);
          this.loadRelationshipsOptions();
        }
      });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const order = this.orderFormService.getOrder(this.editForm);
    if (order.id !== null) {
      this.subscribeToSaveResponse(this.orderService.update(order));
    } else {
      this.subscribeToSaveResponse(this.orderService.create(order));
    }
  }

  addNewClient(): void {
    const modalRef = this.modalService.open(OrderClientDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.closed.pipe().subscribe(() => {
      this.loadRelationshipsOptions();
    });
  }

  // addNewProductImage(): void {
  //   const modalRef = this.modalService.open(OrderClientDialogComponent, { size: 'lg', backdrop: 'static' });
  //   modalRef.closed.pipe().subscribe(() => {
  //     this.loadRelationshipsOptions();
  //   });
  // }

  // load(): void {}

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrder>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(order: IOrder): void {
    this.order = order;
    this.orderFormService.resetForm(this.editForm, order);
    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing<IClient>(this.clientsSharedCollection, order.client);
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing<IClient>(clients, this.order?.client)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));

    // this.productImageService
    //   .query()
    //   .pipe(map((res: HttpResponse<IProductImage[]>) => res.body ?? []))
    //   .pipe(map((productImages: IProductImage[]) => this.productImageService.addProductImageToCollectionIfMissing<IProductImage>(productImages, this.order?.productImages)))
    //   .subscribe((productImages: IProductImage[]) => (this.productIamgesSharedCollection = productImages));
  }
}
