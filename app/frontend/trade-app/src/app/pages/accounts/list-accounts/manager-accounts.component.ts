import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AccountResponse} from '../../../shared/models/account-response';
import {ResponseDTO} from '../../../shared/models/ResponseDTO';
import {AccountServiceService} from '../../../core/services/accounts/account-service.service';
import {PaginationComponent} from '../../../shared/pagination/pagination.component';
import {AccountFormComponent} from '../account-form/account-form.component';
import {ConfirmationModalComponent} from '../../../shared/modals/confirmation-modal/confirmation-modal.component';
import {ToastService} from '../../../core/services/toasts/toast.service';
import {ToastComponent} from '../../../shared/toast/toast.component';
import {ErrorPageComponent} from '../../../shared/error-page/error-page.component';
import {LoadingComponent} from '../../../shared/loading/loading.component';

@Component({
  selector: 'app-manager-accounts',
  standalone: true,
  imports: [CommonModule, PaginationComponent, AccountFormComponent, ConfirmationModalComponent, ToastComponent, ErrorPageComponent, LoadingComponent],
  templateUrl: './manager-accounts.component.html',
  styleUrl: './manager-accounts.component.scss'
})
export class ManagerAccountsComponent {
  accounts: AccountResponse[] = [];
  loading = false;
  error: string | null = null;
  currentPage = 0;
  totalPages = 1;

  showModal = false;
  accountToEdit?: AccountResponse;

  showDeleteModal = false;
  accountToDelete?: AccountResponse;

  constructor(private accountService: AccountServiceService, private toastr: ToastService) {}

  ngOnInit(): void {
    this.fetchAccounts(this.currentPage);
  }

  fetchAccounts(page:number): void {
    this.loading = true;
    this.error = null;

    this.accountService.getAll(page).subscribe({
      next: (res: ResponseDTO<AccountResponse>) => {
        this.accounts = res.items;
        this.totalPages = res.totalPages;
        this.currentPage = res.page;
        this.loading = false;
      },
      error: (err: Error) => {
        this.error = err.message;
        this.loading = false;
      }
    });
  }

  goToPage(page: number) {
    this.currentPage = page;
    this.fetchAccounts(page);
  }


  openAddModal() {
    this.accountToEdit = undefined; // add mode
    this.showModal = true;
  }

  openEditModal(account: AccountResponse) {
    this.accountToEdit = account; // edit mode
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }

  onFormSubmitted() {
    this.closeModal();
    this.fetchAccounts(0);
  }
  openDeleteModal(account: AccountResponse) {
    this.accountToDelete = account;
    this.showDeleteModal = true;
  }

  closeDeleteModal() {
    this.showDeleteModal = false;
    this.accountToDelete = undefined;
  }

  confirmDelete() {
    if (!this.accountToDelete) return;

    this.accountService.deleteAccount(this.accountToDelete.id).subscribe({
      next: () => {
        this.fetchAccounts(0);
        this.closeDeleteModal();
        this.toastr.show('Account deleted successfully', 'success');
      },
      error: (err) => {
        this.toastr.show(err, 'error');
      }
    });
  }
}
