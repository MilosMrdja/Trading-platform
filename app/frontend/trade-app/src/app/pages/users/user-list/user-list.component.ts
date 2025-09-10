import { Component } from '@angular/core';
import {UserResponse} from '../../../shared/models/user-response';
import {ResponseDTO} from '../../../shared/models/ResponseDTO';
import {UsersService} from '../../../core/services/users/users.service';
import {PaginationComponent} from '../../../shared/pagination/pagination.component';
import {NgForOf, NgIf} from '@angular/common';
import {UserFormComponent} from '../user-form/user-form.component';
import {ConfirmationModalComponent} from '../../../shared/modals/confirmation-modal/confirmation-modal.component';
import {ToastComponent} from '../../../shared/toast/toast.component';
import {ErrorPageComponent} from '../../../shared/error-page/error-page.component';
import {LoadingComponent} from '../../../shared/loading/loading.component';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [
    PaginationComponent,
    NgIf,
    UserFormComponent,
    ConfirmationModalComponent,
    ToastComponent,
    NgForOf,
    ErrorPageComponent,
    LoadingComponent
  ],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.scss'
})
export class UserListComponent {
  users: UserResponse[] = [];
  loading = false;
  error: string | null = null;
  currentPage = 0;
  totalPages = 1;

  showModal = false;
  userToEdit?: UserResponse;


  constructor(private userService: UsersService) {}

  ngOnInit(): void {
    this.fetchUsers(this.currentPage);
  }

  fetchUsers(page: number): void {
    this.loading = true;
    this.error = null;

    this.userService.getAll(page).subscribe({
      next: (res: ResponseDTO<UserResponse>) => {
        this.users = res.items;
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
    this.fetchUsers(page);
  }

  openAddModal() {
    this.userToEdit = undefined;
    this.showModal = true;
  }

  openEditModal(user: UserResponse) {
    this.userToEdit = user;
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }

  onFormSubmitted() {
    this.closeModal();
    this.fetchUsers(0);
  }


}
