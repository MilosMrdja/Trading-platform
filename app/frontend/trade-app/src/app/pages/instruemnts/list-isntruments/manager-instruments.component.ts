import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {InstrumentResponse} from '../../../shared/models/instrument-response';
import {ResponseDTO} from '../../../shared/models/ResponseDTO';
import {InstrumentsService} from '../../../core/services/instruments/instruments.service';
import {PaginationComponent} from '../../../shared/pagination/pagination.component';
import {InstrumentFormComponent} from '../instrument-form/instrument-form.component';
import {ConfirmationModalComponent} from '../../../shared/modals/confirmation-modal/confirmation-modal.component';
import {ToastComponent} from '../../../shared/toast/toast.component';
import {ToastService} from '../../../core/services/toasts/toast.service';
import {ErrorPageComponent} from '../../../shared/error-page/error-page.component';
import {LoadingComponent} from '../../../shared/loading/loading.component';

@Component({
  selector: 'app-manager-instruments',
  standalone: true,
  imports: [CommonModule, PaginationComponent, InstrumentFormComponent, ConfirmationModalComponent, ToastComponent, ErrorPageComponent, LoadingComponent],
  templateUrl: './manager-instruments.component.html',
  styleUrl: './manager-instruments.component.scss'
})
export class ManagerInstrumentsComponent {
  instruments: InstrumentResponse[] = [];
  loading = false;
  error: string | null = null;
  currentPage = 0;
  totalPages = 1;

  showModal = false;
  instrumentToEdit?: InstrumentResponse;

  showDeleteModal = false;
  instrumentToDelete?: InstrumentResponse;

  constructor(private instrumentService: InstrumentsService, private toast:ToastService) {}

  ngOnInit(): void {
    this.fetchInstruments(this.currentPage);
  }

  fetchInstruments(page: number): void {
    this.loading = true;
    this.error = null;

    this.instrumentService.getAll(page).subscribe({
      next: (res: ResponseDTO<InstrumentResponse>) => {
        this.instruments = res.items;
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
    this.fetchInstruments(page);
  }

  openAddModal() {
    this.instrumentToEdit = undefined;
    this.showModal = true;
  }

  openEditModal(instr: InstrumentResponse) {
    this.instrumentToEdit = instr;
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }

  onFormSubmitted() {
    this.closeModal();
    this.fetchInstruments(0);
  }

  openDeleteModal(instr: InstrumentResponse) {
    this.instrumentToDelete = instr;
    this.showDeleteModal = true;
  }

  closeDeleteModal() {
    this.showDeleteModal = false;
    this.instrumentToDelete = undefined;
  }

  confirmDelete() {
    if (!this.instrumentToDelete) return;

    this.instrumentService.deleteInstrument(this.instrumentToDelete.id).subscribe({
      next: () => {
        this.fetchInstruments(0);
        this.closeDeleteModal();
        this.toast.show('Instrument deleted successfully', 'success');
      },
      error: err => {
        this.toast.show(err, 'error')
      },
    });
  }
}
