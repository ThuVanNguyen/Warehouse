import { Component, inject, input, OnInit, signal } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IOrder } from '../order.model';
import { NgbCarouselModule } from '@ng-bootstrap/ng-bootstrap';
import { Account } from '../../../core/auth/account.model';
import { Authority } from 'app/config/authority.constants';
import { Subject, takeUntil } from 'rxjs';
import { AccountService } from '../../../core/auth/account.service';

@Component({
  standalone: true,
  selector: 'jhi-order-detail',
  templateUrl: './order-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, NgbCarouselModule],
})
export class OrderDetailComponent implements OnInit {
  order = input<IOrder | null>(null);

  Authority = Authority;
  account = signal<Account | null>(null);
  isAdminOrEditor = false;

  protected accountService = inject(AccountService);
  protected readonly destroy$ = new Subject<void>();

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe((account: Account | null) => {
        if (account?.email.trim()) {
          this.account.set(account);
          this.isAdminOrEditor = account.authorities.includes(Authority.ADMIN) || account.authorities.includes(Authority.EDITOR);
        }
      });
  }

  previousState(): void {
    window.history.back();
  }
}
