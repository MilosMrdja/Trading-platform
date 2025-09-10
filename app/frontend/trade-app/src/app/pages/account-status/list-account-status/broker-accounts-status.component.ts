import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AccountStatusResponse} from '../../../shared/models/account-status-response';
import {ToastService} from '../../../core/services/toasts/toast.service';
import {ResponseDTO} from '../../../shared/models/ResponseDTO';
import {PaginationComponent} from '../../../shared/pagination/pagination.component';
import {ToastComponent} from '../../../shared/toast/toast.component';
import {AccountStatusService} from '../../../core/services/account-status/account-status.service';
import {LoadingComponent} from '../../../shared/loading/loading.component';
import {ErrorPageComponent} from '../../../shared/error-page/error-page.component';

@Component({
  selector: 'app-broker-accounts-status',
  standalone: true,
  imports: [CommonModule, PaginationComponent, ToastComponent, LoadingComponent, ErrorPageComponent],
  templateUrl: './broker-accounts-status.component.html',
  styleUrl: './broker-accounts-status.component.scss'
})
export class BrokerAccountsStatusComponent {
  accountStatuses: AccountStatusResponse[] = [];
  loading = false;
  error: string | null = null;
  currentPage = 0;
  totalPages = 1;

  constructor(private accountStatusService: AccountStatusService, private toast: ToastService) {}

  ngOnInit(): void {
    this.fetchAccountStatuses(this.currentPage);
  }

  fetchAccountStatuses(page: number): void {
    this.loading = true;
    this.error = null;

    this.accountStatusService.getAll(page).subscribe({
      next: (res: ResponseDTO<AccountStatusResponse>) => {
        this.accountStatuses = res.items;
        this.totalPages = res.totalPages;
        this.currentPage = res.page;
        this.loading = false;
      },
      error: (err: Error) => {
        this.error = err.message;
        this.loading = false;
      },
    });
  }

  goToPage(page: number) {
    this.currentPage = page;
    this.fetchAccountStatuses(page);
  }
}
