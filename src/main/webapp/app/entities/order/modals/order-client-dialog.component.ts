import { Component, inject, Input } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import SharedModule from '../../../shared/shared.module';
import { ClientUpdateComponent } from '../../client/update/client-update.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ClientFormGroup, ClientFormService } from '../../client/update/client-form.service';
import { ClientService } from '../../client/service/client.service';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { IClient } from '../../client/client.model';
import { finalize } from 'rxjs/operators';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-order-client-dialog',
  standalone: true,
  imports: [SharedModule, FormsModule, ClientUpdateComponent, ReactiveFormsModule],
  templateUrl: './order-client-dialog.component.html',
})
export class OrderClientDialogComponent {
  isSaving = false;

  protected activeModal = inject(NgbActiveModal);
  protected clientFormService = inject(ClientFormService);
  protected clientService = inject(ClientService);
  protected alertService = inject(AlertService);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  @Input() editForm: ClientFormGroup = this.clientFormService.createClientFormGroup();

  onEditFormChange(updatedForm: ClientFormGroup): void {
    this.editForm = updatedForm;
  }

  save(): void {
    this.isSaving = true;
    const client = this.clientFormService.getClient(this.editForm);
    if (client.id !== null) {
      this.subscribeToSaveResponse(this.clientService.update(client));
    } else {
      this.subscribeToSaveResponse(this.clientService.create(client));
    }
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClient>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.activeModal.close();
  }

  protected onSaveError(): void {
    this.alertService.addAlert({
      type: 'danger',
      message: 'Error creating Client',
      timeout: 3000,
    });
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }
}
