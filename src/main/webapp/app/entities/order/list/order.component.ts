import { Component, NgZone, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subject, Subscription, combineLatest, debounceTime, distinctUntilChanged, filter, takeUntil, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, ITEM_IMPORT_EVENT, ITEM_UPDATED_STATUS_EVENT, SORT } from 'app/config/navigation.constants';
import { IOrder, OrderStatus, QueryParamsOrder } from '../order.model';
import { EntityArrayResponseType, OrderService } from '../service/order.service';
import { OrderDeleteDialogComponent } from '../delete/order-delete-dialog.component';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { Authority } from 'app/config/authority.constants';
import { OrderImportDialogComponent } from '../import/order-import-dialog.component';
import { AlertService } from 'app/core/util/alert.service';
import FormatSplashDatePipe from 'app/shared/date/format-splash-date.pipe';
import { ClientService } from 'app/entities/client/service/client.service';
import { OrderStatusChangeDialogComponent } from '../modals/order-status-change-dialog.component';
import { TranslateService } from '@ngx-translate/core';

@Component({
  standalone: true,
  selector: 'jhi-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
  imports: [RouterModule, FormsModule, SharedModule, SortDirective, SortByDirective, FormatSplashDatePipe, ItemCountComponent],
})
export class OrderComponent implements OnInit, OnDestroy {
  subscription: Subscription | null = null;
  orders?: IOrder[];
  isLoading = false;
  clientCodes: string[] = [];
  sortState = sortStateSignal({});

  selectedDate = '';
  backendDate = '';
  statusOptions: any[] = [];
  selectedStatus: OrderStatus | string = '';
  clientCode = '';

  itemsPerPageOptions: number[] = [10, 20, 50, 100];
  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  Authority = Authority;
  account = signal<Account | null>(null);
  isAdminOrEditor = false;

  searchString = '';
  showAdvancedSearch = false;

  isAllSelected = false;
  selectedOrders: Set<number> = new Set<number>();

  public readonly router = inject(Router);
  protected readonly orderService = inject(OrderService);
  protected readonly alertService = inject(AlertService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected translateService = inject(TranslateService);
  protected clientService = inject(ClientService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  protected accountService = inject(AccountService);
  protected readonly destroy$ = new Subject<void>();
  private searchSubject = new Subject<string>();

  constructor() {
    this.statusOptions = [
      [OrderStatus.BANG_TUONG_WARE_HOUSE, `wareHouseApp.order.statusEnum.${OrderStatus.BANG_TUONG_WARE_HOUSE}`],
      [OrderStatus.LOADED_ON_VEHICLE, `wareHouseApp.order.statusEnum.${OrderStatus.LOADED_ON_VEHICLE}`],
      [OrderStatus.ARRIVED_AT_HN, `wareHouseApp.order.statusEnum.${OrderStatus.ARRIVED_AT_HN}`],
      [OrderStatus.DELIVERED, `wareHouseApp.order.statusEnum.${OrderStatus.DELIVERED}`],
      [OrderStatus.CUSTOMER_SIGNED, `wareHouseApp.order.statusEnum.${OrderStatus.CUSTOMER_SIGNED}`],
      [OrderStatus.CANCELED, `wareHouseApp.order.statusEnum.${OrderStatus.CANCELED}`],
    ];
  }

  trackId = (item: IOrder): number => this.orderService.getOrderIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();

    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe((account: Account | null) => {
        if (account?.login.trim()) {
          this.account.set(account);
          this.isAdminOrEditor = account.authorities.includes(Authority.ADMIN) || account.authorities.includes(Authority.EDITOR);
        }
      });

    this.searchSubject
      .pipe(
        debounceTime(300), // Delay by 300ms
        distinctUntilChanged(), // Ignore duplicate values
        takeUntil(this.destroy$), // Unsubscribe when the component is destroyed
      )
      .subscribe(searchTerm => {
        this.searchString = searchTerm;
        this.load();
      });

    this.getClientCodes();
    const today = new Date();
    this.selectedDate = this.formatDateForDisplay(today);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  delete(order: IOrder): void {
    const modalRef = this.modalService.open(OrderDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.order = order;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  // Emit search term on keyup
  onSearchKeyUp(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchSubject.next(input.value); // Emit the search term
  }

  import(): void {
    const modalRef = this.modalService.open(OrderImportDialogComponent, { size: 'lg', backdrop: 'static' });
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_IMPORT_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  export(): void {
    if (!this.isAllSelected && this.selectedOrders.size === 0) {
      this.alertService.addAlert({
        type: 'danger',
        message: this.translateService.instant('wareHouseApp.order.export.error.emptyOrderExport'),
        timeout: 3000,
      });
      return;
    }

    this.orderService.exportOrders(this.isAllSelected ? [] : [...this.selectedOrders]).subscribe({
      next: (blob: any) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = this.generateFileName(); // Default file name
        a.click();
        window.URL.revokeObjectURL(url); // Clean up
      },
      error: err => {
        this.alertService.addAlert({
          type: 'danger',
          message: this.translateService.instant('wareHouseApp.order.export.error.failed'),
          timeout: 3000,
        });
        console.error(err);
      },
    });
  }

  navigateToDuplicatePage(order: IOrder): void {
    const queryParamsDuplicatedOrder: QueryParamsOrder = {
      clientId: order.client?.id ?? undefined,
      importVehicleNumber: order.importVehicleNumber ?? undefined,
      receiptDate: order.receiptDate?.toISOString() ?? undefined,
      warehouseReceiptCode: order.warehouseReceiptCode ?? undefined,
      productCn: order.productCn ?? undefined,
      productVn: order.productVn ?? undefined,
      piecesReceivedCount: order.piecesReceivedCount ?? undefined,
      piecesWareHouseCount: order.piecesWareHouseCount ?? undefined,
      totalWeight: order.totalWeight ?? undefined,
      totalCubicMeter: order.totalCubicMeter ?? undefined,
      listCalculation: order.listCalculation ?? undefined,
      unitPriceWeight: order.unitPriceWeight ?? undefined,
      unitPriceCubicMeter: order.unitPriceCubicMeter ?? undefined,
      totalPrice: order.totalPrice ?? undefined,
      waybill: order.waybill ?? undefined,
      shippingAddressAndPhone: order.shippingAddressAndPhone ?? undefined,
      exportVehicleNumber: order.exportVehicleNumber ?? undefined,
      exportDate: order.exportDate?.toISOString() ?? undefined,
      exportQuantity: order.exportQuantity ?? undefined,
      note: order.note ?? undefined,
      status: order.status ?? undefined,
      bangTuongWareHouseDate: order.bangTuongWareHouseDate?.toISOString() ?? undefined,
      loadedOnVehicleDate: order.loadedOnVehicleDate?.toISOString() ?? undefined,
      arrivedAtHNDate: order.arrivedAtHNDate?.toISOString() ?? undefined,
      deliveredDate: order.deliveredDate?.toISOString() ?? undefined,
      customerSignedDate: order.customerSignedDate?.toISOString() ?? undefined,
      cancelDate: order.cancelDate?.toISOString() ?? undefined,
      arrangeVehicle: order.arrangeVehicle ?? undefined,
    };

    this.router.navigate(['/order/new'], {
      relativeTo: this.activatedRoute,
      queryParams: this.removeUndefinedFields(queryParamsDuplicatedOrder),
    });
  }

  formatDateForDisplay(date: Date): string {
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    return `${year}-${month}-${day}`;
  }

  formatDateForBackend(event: string): void {
    if (!event) {
      this.backendDate = '';
      return;
    }
    const date = new Date(event);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Thêm số 0 vào tháng
    const day = date.getDate().toString().padStart(2, '0'); // Thêm số 0 vào ngày
    this.backendDate = `${year}-${month}-${day}`;
  }

  getClientCodes(): void {
    this.clientService.query().subscribe({
      next: response => {
        if (response.body) {
          this.clientCodes = response.body
            .map(client => client.code) // Lấy mã code
            .filter((code): code is string => !!code); // Loại bỏ null và undefined
        }
      },
      error: err => {
        this.alertService.addAlert({
          type: 'danger',
          message: `Failed to fetch client codes: ${err.message}`,
          timeout: 3000,
        });
      },
    });
  }

  search(): void {
    this.load();
  }

  toggleAdvancedSearch(): void {
    this.showAdvancedSearch = !this.showAdvancedSearch;
  }

  toggleSelectAll(event: Event): void {
    this.isAllSelected = !this.isAllSelected;
    this.selectedOrders.clear();
  }

  selectOrder(order: IOrder): void {
    // no order has id = 0
    if (this.selectedOrders.has(order.id)) {
      this.selectedOrders.delete(order.id);
    } else {
      this.selectedOrders.add(order.id);
    }
    this.isAllSelected = this.selectedOrders.size === this.totalItems;
  }

  isOrderSelected(order: IOrder): boolean {
    return this.selectedOrders.has(order.id);
  }

  changeStatus(): void {
    if (!this.isAllSelected && this.selectedOrders.size === 0) {
      this.alertService.addAlert({
        type: 'danger',
        message: this.translateService.instant('wareHouseApp.order.updateStatus.error.noOrders'),
      });
      return;
    }
    const modalRef = this.modalService.open(OrderStatusChangeDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.selectedOrders = this.selectedOrders;
    modalRef.componentInstance.isSelectAll = this.isAllSelected;
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_UPDATED_STATUS_EVENT),
        tap(() => {
          this.load();
          this.isAllSelected = false;
          this.selectedOrders.clear();
        }),
      )
      .subscribe();
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState());
  }
  resetFormAndReload(): void {
    // Reset all the search fields
    this.searchString = '';
    this.selectedStatus = '';
    this.backendDate = '';
    this.selectedDate = '';
    this.clientCode = '';

    // Reset pagination if needed
    this.page = 1;

    // Reload the page by navigating to the current route
    this.router.navigate([], { relativeTo: this.activatedRoute, queryParams: {}, queryParamsHandling: 'merge' }).then(() => {
      // Optionally reload data explicitly
      this.load();
    });
  }

  updateItemsPerPage(newItemsPerPage: number): void {
    this.itemsPerPage = newItemsPerPage;
    this.page = 1;
    this.load();
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.orders = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IOrder[] | null): IOrder[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    const { page } = this;
    this.isLoading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      keyword: this.searchString,
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(this.sortState()),
      status: !this.selectedStatus || this.selectedStatus === 'null' ? '' : this.selectedStatus,
      date: this.backendDate,
      clientCode: this.clientCode,
    };
    return this.orderService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page: number, sortState: SortState): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(sortState),
    };
    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }

  private generateFileName(): string {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0'); // Months are 0-based
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    // Construct file name: YYYYMMDD-HHmm-orders.xlsx
    return `${year}${month}${day}-${hours}${minutes}-orders.xlsx`;
  }

  private removeUndefinedFields<T extends Record<string, any>>(obj: T): T {
    return Object.fromEntries(Object.entries(obj).filter(([_, value]) => value !== undefined)) as T;
  }
}
