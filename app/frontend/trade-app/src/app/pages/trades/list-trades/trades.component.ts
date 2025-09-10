import {Component} from '@angular/core';
import {Direction} from '../../../shared/enums/direction';
import {TradeResponse} from '../../../shared/models/trade-response';
import {DeliveryType} from '../../../shared/enums/delivery-type';
import {Unit} from '../../../shared/enums/unit';
import {Status} from '../../../shared/enums/status';
import {NgForOf, NgIf} from '@angular/common';
import {PaginationComponent} from '../../../shared/pagination/pagination.component';
import {ToastService} from '../../../core/services/toasts/toast.service';
import {ResponseDTO} from '../../../shared/models/ResponseDTO';
import {TradeServiceService} from '../../../core/services/trades/trade-service.service';
import {TradeFormComponent} from '../trade-form/trade-form.component';
import {ConfirmationModalComponent} from '../../../shared/modals/confirmation-modal/confirmation-modal.component';
import {ToastComponent} from '../../../shared/toast/toast.component';
import {FormsModule} from '@angular/forms';
import {TradeFilter} from '../../../shared/models/trade-filter';
import {Subscription} from 'rxjs';
import {WebSocketService} from '../../../core/services/web-sockets/web-socket.service';
import {ErrorPageComponent} from '../../../shared/error-page/error-page.component';
import {LoadingComponent} from '../../../shared/loading/loading.component';



@Component({
  selector: 'app-trades',
  standalone: true,
  imports: [
    NgForOf,
    PaginationComponent,
    TradeFormComponent,
    ConfirmationModalComponent,
    ToastComponent,
    NgIf,
    FormsModule,
    ErrorPageComponent,
    LoadingComponent
  ],
  templateUrl: './trades.component.html',
  styleUrl: './trades.component.scss'
})
export class TradesComponent {
  trades: TradeResponse[] = [];
  tradeSub!: Subscription;
  loading = false;
  error: string | null = null;
  currentPage = 0;
  totalPages = 1;

  showModal = false;
  tradeToEdit?: TradeResponse;

  showDeleteModal = false;
  tradeToDelete?: TradeResponse;

  filteredTrades: any[] = [];
  filters: TradeFilter = {direction: '', status: '', price: null};
  private isToastShowing = false;

  constructor(private tradeService: TradeServiceService, private toast: ToastService,
              private webSocketService: WebSocketService) {}

  ngOnInit(): void {
    this.webSocketService.connect();
    this.fetchTrades(this.currentPage, {direction:'', status: '', price: null});
    if (!this.tradeSub) {
      this.tradeSub = this.webSocketService.trades$.subscribe({
        next: trade => {
          this.fetchTrades(this.currentPage, { direction: '', status: '', price: null });
          if (!this.isToastShowing) {
            this.isToastShowing = true;
            this.toast.show(`Trade list is updated`, 'success');

            setTimeout(() => {
              this.isToastShowing = false;
            }, 4000);
          }
        }
      });
    }

  }
  applyFilters() {
    //console.log('Sending filters to backend:', this.filters);
    this.fetchTrades(0, this.filters);
  }

  resetFilters() {
    this.filters = { direction: '', status: '', price: null };
    this.applyFilters();
  }

  fetchTrades(page: number, filters: TradeFilter): void {
    this.loading = true;
    this.error = null;

    this.tradeService.getAll(page, filters).subscribe({
      next: (res: ResponseDTO<TradeResponse>) => {
        this.trades = res.items;
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
    this.fetchTrades(page, this.filters);
  }

  openAddModal() {
    this.tradeToEdit = undefined;
    this.showModal = true;
  }

  openEditModal(trade: TradeResponse) {
    this.tradeToEdit = trade;
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }

  onFormSubmitted() {
    this.closeModal();
    this.fetchTrades(0,{direction:'', status: '', price: null});
  }

  openDeleteModal(trade: TradeResponse) {
    this.tradeToDelete = trade;
    this.showDeleteModal = true;
  }

  closeDeleteModal() {
    this.showDeleteModal = false;
    this.tradeToDelete = undefined;
  }

  confirmDelete() {
    if (!this.tradeToDelete) return;

    this.tradeService.deleteTrade(this.tradeToDelete.id).subscribe({
      next: () => {
        this.fetchTrades(0,{direction:'', status: '', price: null});
        this.closeDeleteModal();
        this.toast.show('Trade deleted successfully', 'success');
      },
      error: err => {
        this.toast.show(err, 'error')
      },
    });
  }
  formatEnum(value: string) {
    return value.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, c => c.toUpperCase());
  }

  exerciseTrade(trade: TradeResponse) {
      this.tradeService.exerciseTrade(trade.id, {instrumentId:trade.instrument.id}).subscribe({
        next: () => {
          this.toast.show('Trade exercised successfully', 'success');
          this.fetchTrades(0,{direction:'', status: '', price: null});
        },
        error: err => {
          this.toast.show(err, 'error')
        },
      })
  }

  protected readonly Status = Status;

  isTradeOpen(trade: TradeResponse) {
    return trade.status.toString().toUpperCase() !== Status[Status.open].toUpperCase() && trade.status.toString().toUpperCase() !== Status[Status.matched].toUpperCase();
  }

  ngOnDestroy(): void {
    if (this.tradeSub) {
      this.tradeSub.unsubscribe();
    }
  }
}
