import { Component, inject } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import SharedModule from 'app/shared/shared.module';
import { OrderService } from '../service/order.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpEventType } from '@angular/common/http';
import { AlertService } from 'app/core/util/alert.service';
import { ITEM_IMPORT_EVENT } from 'app/config/navigation.constants';
import { OrderStatus } from '../order.model';
import dayjs from 'dayjs/esm';

@Component({
  standalone: true,
  templateUrl: './order-import-dialog.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrderImportDialogComponent {
  selectedFile: File | null = null;
  isUploading = false; // Tracks if the upload is in progress
  orderStatuses = [OrderStatus.BANG_TUONG_WARE_HOUSE, OrderStatus.LOADED_ON_VEHICLE];

  selectedStatusIndex = 0;
  protected orderService = inject(OrderService);
  protected activeModal = inject(NgbActiveModal);
  protected alertService = inject(AlertService);

  // Event handler for file selection
  protected transportVehicleDate: dayjs.Dayjs | null = null;
  protected transportVehicle?: string;

  onFileSelected(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    if (inputElement.files && inputElement.files.length > 0) {
      this.selectedFile = inputElement.files[0];
    }
  }

  // Submit the form to upload the file
  uploadFile(): void {
    if (!this.selectedFile) {
      alert('No file selected');
      return;
    }

    this.isUploading = true;

    const selectedStatus = this.orderStatuses[this.selectedStatusIndex];

    let date: Date | undefined;
    if (this.transportVehicleDate) {
      date = new Date(this.transportVehicleDate.year(), this.transportVehicleDate.month() - 1, this.transportVehicleDate.day());
    }

    const arrangeVehicle = this.transportVehicle;

    this.orderService.importExcelOrder(this.selectedFile, selectedStatus, date, arrangeVehicle).subscribe({
      next: event => {
        switch (event.type) {
          case HttpEventType.UploadProgress: {
            // uploading
            break;
          }
          case HttpEventType.Response: {
            this.isUploading = false;
            this.alertService.addAlert({
              type: 'success',
              message: `Done importing Orders by Excel file`,
              timeout: 3000,
            });
            this.activeModal.close(ITEM_IMPORT_EVENT);
            break;
          }
          default:
            break;
        }
      },
      error: err => {
        this.isUploading = false;
        this.alertService.addAlert({
          type: 'danger',
          message: `Error uploading file with message: ${err.message}`,
          timeout: 3000,
        });
      },
      complete: () => (this.isUploading = false),
    });
  }

  // Cancel button logic
  cancel(): void {
    this.selectedFile = null;
    this.isUploading = false;
    this.activeModal.dismiss();
  }
}
