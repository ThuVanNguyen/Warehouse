import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { combineLatest, debounceTime, distinctUntilChanged, map, Observable, of, Subject, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, SortState, sortStateSignal } from 'app/shared/sort';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { SORT } from 'app/config/navigation.constants';
import { ItemCountComponent } from 'app/shared/pagination';
import { AccountService } from 'app/core/auth/account.service';
import { UserManagementService } from '../service/user-management.service';
import { IUser, User } from '../user-management.model';
import UserManagementDeleteDialogComponent from '../delete/user-management-delete-dialog.component';
import { ClientService } from 'app/entities/client/service/client.service';
import { IClient } from 'app/entities/client/client.model';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FilteredClient } from './filtered-client';

const userTemplate = {} as IUser;
@Component({
  standalone: true,
  selector: 'jhi-user-mgmt',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss'],
  imports: [RouterModule, SharedModule, SortDirective, SortByDirective, ItemCountComponent, ReactiveFormsModule],
})
export default class UserManagementComponent implements OnInit {
  editingRecord = signal<number | null>(null);
  selectedClientCode: string | null = null;
  currentAccount = inject(AccountService).trackCurrentAccount();
  users = signal<User[] | null>(null);
  isLoading = signal(false);
  totalItems = signal(0);
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  sortState = sortStateSignal({});
  filteredClients: FilteredClient[] = [];
  dropdownVisible = false;

  editForm = new FormGroup({
    searchKeyword: new FormControl(''),
  });

  filteredClients$!: Observable<FilteredClient[]>;

  private readonly route = inject(ActivatedRoute);
  private readonly http = inject(HttpClient);

  private readonly userService = inject(UserManagementService);
  private readonly clientService = inject(ClientService);
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly sortService = inject(SortService);
  private readonly modalService = inject(NgbModal);
  private searchSubject = new Subject<string>();
  private searchKeyword: string | undefined = '';

  ngOnInit(): void {
    this.handleNavigation();

    this.filteredClients$ = this.fetchClients('').pipe(
      map(clients => {
        this.filteredClients = clients;
        return this.filteredClients;
      }),
    );
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
    this.filteredClients$ = this.fetchClients(keyword).pipe(
      map(clients => {
        this.filteredClients = clients;
        return this.filteredClients;
      }),
    );
    this.toggleDropdown(true);
  }

  setActive(user: User, isActivated: boolean): void {
    this.userService.update({ ...user, activated: isActivated }).subscribe(() => this.loadAll());
  }

  trackIdentity(item: User): number {
    return item.id!;
  }

  trackIdentityClient(item: IClient): number {
    return item.id;
  }

  onEditClick(id: number | null): void {
    this.editingRecord.set(id);
  }

  deleteUser(user: User): void {
    const modalRef = this.modalService.open(UserManagementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.user = user;
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  loadAll(): void {
    this.isLoading.set(true);
    this.userService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sortService.buildSortParam(this.sortState(), 'id'),
      })
      .subscribe({
        next: (res: HttpResponse<User[]>) => {
          this.isLoading.set(false);
          this.onSuccess(res.body, res.headers);
        },
        error: () => this.isLoading.set(false),
      });
  }

  selectClient(client: FilteredClient): void {
    this.searchKeyword = client.code ?? '';
    this.editForm.setValue({
      searchKeyword: client.code,
    });
    this.toggleDropdown(false);
  }

  toggleDropdown(visible: boolean): void {
    this.dropdownVisible = visible;
  }

  onSaveClick(userId: number | null): void {
    const usersValue = this.users();
    if (!usersValue) {
      return;
    }
    const user = usersValue.find(u => u.id === userId);

    if (user) {
      user.clientCode = this.editForm.get('searchKeyword')?.value;

      this.userService.update(user).subscribe(() => {
        this.editingRecord.set(null);
      });
    }
  }

  onClientChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    this.selectedClientCode = selectElement.value;
  }

  onCancelClick(): void {
    this.editingRecord.set(null);
    this.selectedClientCode = null;
  }

  transition(sortState?: SortState): void {
    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute.parent,
      queryParams: {
        page: this.page,
        sort: this.sortService.buildSortParam(sortState ?? this.sortState()),
      },
    });
  }

  private handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      this.page = +(page ?? 1);
      this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data.defaultSort));
      this.loadAll();
    });
  }

  private onSuccess(users: User[] | null, headers: HttpHeaders): void {
    this.totalItems.set(Number(headers.get('X-Total-Count')));
    this.users.set(users);
  }
}
