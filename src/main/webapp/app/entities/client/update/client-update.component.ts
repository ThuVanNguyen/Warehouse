import { Component, OnInit, inject, Input, Output, EventEmitter } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IClient } from '../client.model';
import { ClientService } from '../service/client.service';
import { ClientFormGroup, ClientFormService } from './client-form.service';

@Component({
  standalone: true,
  selector: 'jhi-client-update',
  templateUrl: './client-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClientUpdateComponent implements OnInit {
  @Input() hideButtons = false;
  @Input() inputEditForm?: ClientFormGroup;
  @Output() editFormChange = new EventEmitter<ClientFormGroup>();
  isSaving = false;
  client: IClient | null = null;

  protected clientService = inject(ClientService);
  protected clientFormService = inject(ClientFormService);
  protected activatedRoute = inject(ActivatedRoute);
  protected editForm: ClientFormGroup = this.clientFormService.createClientFormGroup();

  ngOnInit(): void {
    if (this.inputEditForm) {
      this.editForm = this.inputEditForm;
    }
    this.activatedRoute.data.subscribe(({ client }) => {
      this.client = client;
      if (client) {
        this.updateForm(client);
      }
    });
  }

  previousState(): void {
    window.history.back();
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClient>>): void {
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

  protected updateForm(client: IClient): void {
    this.client = client;
    this.clientFormService.resetForm(this.editForm, client);
    this.editFormChange.emit(this.editForm);
  }
}
