import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AlertService } from 'app/core/util/alert.service';
import { OrderService } from '../service/order.service';
import { IOrder, OrderStatus } from '../order.model';
import SharedModule from 'app/shared/shared.module';
import { TranslateService } from '@ngx-translate/core';
import dayjs from 'dayjs/esm';
import { HttpResponse } from '@angular/common/http';
import { map } from 'rxjs';
import { ITEM_UPDATED_STATUS_EVENT } from 'app/config/navigation.constants';

@Component({
  selector: 'jhi-order-status-change-dialog',
  standalone: true,
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
  templateUrl: './order-status-change-dialog.component.html',
})
export class OrderStatusChangeDialogComponent {
  selectedOrders: Set<number> = new Set<number>();
  isSelectAll = false;
  statusCollection: [string, string][] = [];

  updateStatusForm: FormGroup;

  protected activeModal = inject(NgbActiveModal);
  protected orderService = inject(OrderService);
  protected alertService = inject(AlertService);
  protected translateService = inject(TranslateService);

  constructor() {
    this.statusCollection = [
      [
        OrderStatus.BANG_TUONG_WARE_HOUSE,
        this.translateService.instant(`wareHouseApp.order.statusEnum.${OrderStatus.BANG_TUONG_WARE_HOUSE}`),
      ],
      [OrderStatus.ARRIVED_AT_HN, this.translateService.instant(`wareHouseApp.order.statusEnum.${OrderStatus.ARRIVED_AT_HN}`)],
      [OrderStatus.LOADED_ON_VEHICLE, this.translateService.instant(`wareHouseApp.order.statusEnum.${OrderStatus.LOADED_ON_VEHICLE}`)],
      [OrderStatus.DELIVERED, this.translateService.instant(`wareHouseApp.order.statusEnum.${OrderStatus.DELIVERED}`)],
      [OrderStatus.CUSTOMER_SIGNED, this.translateService.instant(`wareHouseApp.order.statusEnum.${OrderStatus.CUSTOMER_SIGNED}`)],
      [OrderStatus.CANCELED, this.translateService.instant(`wareHouseApp.order.statusEnum.${OrderStatus.CANCELED}`)],
    ];

    this.updateStatusForm = new FormGroup({
      status: new FormControl<OrderStatus>(OrderStatus.BANG_TUONG_WARE_HOUSE, { nonNullable: true, validators: [Validators.required] }),
      date: new FormControl<dayjs.Dayjs>(dayjs(), { nonNullable: true, validators: [Validators.required] }),
    });
  }

  saveOrdersWithNewStatus(): void {
    if (this.updateStatusForm.invalid) {
      this.markFormGroupTouched(this.updateStatusForm);
      return;
    }

    const status: OrderStatus = this.updateStatusForm.get('status')?.value;
    const date: dayjs.Dayjs = this.updateStatusForm.get('date')?.value;
    this.orderService
      .orderStatusUpdate([...this.selectedOrders], status, date, this.isSelectAll)
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .subscribe((iOrders: IOrder[]) => {
        if (iOrders.length !== 0) {
          this.alertService.addAlert({
            type: 'info',
            message: this.translateService.instant('wareHouseApp.order.updateStatus.message.success'),
            timeout: 3000,
          });
        } else {
          this.alertService.addAlert({
            type: 'danger',
            message: this.translateService.instant('wareHouseApp.order.updateStatus.error.failed'),
            timeout: 3000,
          });
        }
        this.activeModal.close(ITEM_UPDATED_STATUS_EVENT);
      });
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(controlName => {
      const control = formGroup.get(controlName);
      if (control instanceof FormGroup) {
        // Recursively mark nested form groups
        this.markFormGroupTouched(control);
      } else {
        control?.markAsTouched();
      }
    });
  }
}
