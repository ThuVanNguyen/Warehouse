import { Component, OnInit, inject, signal } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import SharedModule from 'app/shared/shared.module';
import { LANGUAGES } from 'app/config/language.constants';
import { IUser } from '../user-management.model';
import { UserManagementService } from '../service/user-management.service';
import { HttpClient } from '@angular/common/http';
import { debounceTime, Subject, switchMap, of, Observable } from 'rxjs';

import { CommonModule } from '@angular/common';

import { ChangeDetectorRef } from '@angular/core';
import { ClientService } from 'app/entities/client/service/client.service';
import { IClient } from 'app/entities/client/client.model';

const userTemplate = {} as IUser;

const newUser: IUser = {
  langKey: 'en',
  activated: true,
} as IUser;

interface IClientResponse {
  content: { code: string; name: string | null; email: string | null }[];
}

@Component({
  standalone: true,
  selector: 'jhi-user-mgmt-update',
  templateUrl: './user-management-update.component.html',
  imports: [CommonModule, SharedModule, FormsModule, ReactiveFormsModule],
})
export default class UserManagementUpdateComponent implements OnInit {
  languages = LANGUAGES;
  authorities = signal<string[]>([]);
  isSaving = signal(false);
  clients: { code: string }[] = [];
  selectedClient: { code: string } | null = null;

  filteredClients: { code: string }[] = [];
  searchKeyword = '';
  dropdownVisible = false;
  debugInfo: string | null = null;

  editForm = new FormGroup({
    id: new FormControl(userTemplate.id),
    login: new FormControl(userTemplate.login, {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50),
        Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
      ],
    }),
    firstName: new FormControl(userTemplate.firstName, { validators: [Validators.maxLength(50)] }),
    lastName: new FormControl(userTemplate.lastName, { validators: [Validators.maxLength(50)] }),
    email: new FormControl(userTemplate.email, {
      nonNullable: true,
      validators: [Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),
    activated: new FormControl(userTemplate.activated, { nonNullable: true }),
    langKey: new FormControl(userTemplate.langKey, { nonNullable: true }),
    authorities: new FormControl(userTemplate.authorities, { nonNullable: true }),
    clientCode: new FormControl(userTemplate.clientCode),
    searchKeyword: new FormControl(''),
  });

  private readonly userService = inject(UserManagementService);
  private readonly route = inject(ActivatedRoute);
  private readonly http = inject(HttpClient);
  private readonly cdr: ChangeDetectorRef = inject(ChangeDetectorRef);
  private readonly clientService = inject(ClientService);

  private searchSubject = new Subject<string>();

  ngOnInit(): void {
    this.route.data.subscribe(({ user }) => {
      if (user) {
        this.editForm.reset(user);
      } else {
        this.editForm.reset(newUser);
      }
    });
    this.userService.authorities().subscribe(authorities => this.authorities.set(authorities));

    this.searchSubject
      .pipe(
        debounceTime(300),
        switchMap(keyword => this.fetchClients(keyword)),
      )
      .subscribe(clients => {
        this.clients = clients;
      });

    this.fetchClients('').subscribe(clients => {
      this.clients = clients;
      this.filteredClients = clients;
    });
  }
  previousState(): void {
    window.history.back();
  }

  fetchClients(keyword: string): Observable<{ code: string }[]> {
    const reqParams = { keyword, page: 0, size: 20 };
    return this.clientService
      .query(reqParams)
      .pipe(
        switchMap((response: { body?: IClient[] | null }) =>
          of((response.body ?? []).filter(client => client.code != null).map(client => ({ code: client.code! }))),
        ),
      );
  }

  onSearchClient(event: Event): void {
    const keyword = (event.target as HTMLInputElement).value.toLowerCase();
    this.filteredClients = this.clients.filter(client => client.code.toLowerCase().includes(keyword));
    this.toggleDropdown(true);
  }

  /**
   * Xử lý sự kiện khi người dùng chọn client từ danh sách
   */
  onSelectClient(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    const selectedCode = selectElement.value || null;

    this.editForm.patchValue({ clientCode: selectedCode });

    this.toggleDropdown(false);
  }

  selectClient(client: { code: string }): void {
    this.searchKeyword = client.code;

    this.debugInfo = `client.code: ${client.code}`;
    this.editForm.patchValue({ searchKeyword: client.code });
    this.editForm.patchValue({ clientCode: client.code });
    this.toggleDropdown(false);
    this.debugInfo = `Selected client: ${client.code}, Filtered count: ${this.filteredClients.length}`;
    this.cdr.detectChanges();
  }

  // Hiển thị/ẩn dropdown
  toggleDropdown(show: boolean): void {
    this.dropdownVisible = show;
    this.debugInfo = `Dropdown visibility: ${this.dropdownVisible}`;
  }

  save(): void {
    this.isSaving.set(true);
    const user = this.editForm.getRawValue();

    const saveObservable = user.id ? this.userService.update(user) : this.userService.create(user);
    saveObservable.subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  private onSaveSuccess(): void {
    this.isSaving.set(false);
    this.previousState();
  }

  private onSaveError(): void {
    this.isSaving.set(false);
  }
}
