import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map, tap } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IProductImage } from '../product-image.model';
import { ProductImageService } from '../service/product-image.service';
import { ProductImageFormGroup, ProductImageFormService } from './product-image-form.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import dayjs from 'dayjs/esm';
import { ImageType } from 'app/config/image-type.constants';
import ImgCusComponent from 'app/shared/img-cus/img-cus.component';

@Component({
  standalone: true,
  selector: 'jhi-product-image-update',
  templateUrl: './product-image-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, ImgCusComponent],
})
export class ProductImageUpdateComponent implements OnInit {
  isSaving = false;
  productImage: IProductImage | null = null;

  imgSrc = '';
  imageTypeCollection: any[] = [];

  ordersSharedCollection: IOrder[] = [];

  protected productImageService = inject(ProductImageService);
  protected productImageFormService = inject(ProductImageFormService);
  protected orderService = inject(OrderService);
  protected activatedRoute = inject(ActivatedRoute);
  protected modalService = inject(NgbModal);
  protected translateService = inject(TranslateService);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductImageFormGroup = this.productImageFormService.createProductImageFormGroup();

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  ngOnInit(): void {
    this.imageTypeCollection = [
      [ImageType.AD, this.translateService.instant(`wareHouseApp.productImage.typeCode.${ImageType.AD}`)],
      [ImageType.WRAP, this.translateService.instant(`wareHouseApp.productImage.typeCode.${ImageType.WRAP}`)],
      [ImageType.REAL, this.translateService.instant(`wareHouseApp.productImage.typeCode.${ImageType.REAL}`)],
      [ImageType.WAYBILL_RECEIVE, this.translateService.instant(`wareHouseApp.productImage.typeCode.${ImageType.WAYBILL_RECEIVE}`)],
      [ImageType.OTHER, this.translateService.instant(`wareHouseApp.productImage.typeCode.${ImageType.OTHER}`)],
    ];

    this.activatedRoute.data.subscribe(({ productImage }) => {
      this.productImage = productImage;
      if (productImage) {
        this.updateForm(productImage);
      }

      this.loadRelationshipsOptions();
    });

    this.editForm.get('base64')?.valueChanges.subscribe(base64 => (this.imgSrc = base64 ?? ''));
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productImage = this.productImageFormService.getProductImage(this.editForm);
    if (productImage.id !== null) {
      this.subscribeToSaveResponse(this.productImageService.update(productImage));
    } else {
      this.subscribeToSaveResponse(this.productImageService.create(productImage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductImage>>): void {
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

  protected updateForm(productImage: IProductImage): void {
    this.productImage = productImage;
    this.productImageFormService.resetForm(this.editForm, productImage);
    this.ordersSharedCollection = this.orderService.addOrderToCollectionIfMissing<IOrder>(this.ordersSharedCollection, productImage.order);
  }

  protected loadRelationshipsOptions(): void {
    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.productImage?.order)))
      .subscribe((orders: IOrder[]) => (this.ordersSharedCollection = orders));
  }
}
