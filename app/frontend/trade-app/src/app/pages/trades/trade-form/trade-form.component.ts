import {Component, EventEmitter, Input, Output} from '@angular/core';
import {TradeRequest} from '../../../shared/models/trade-request';
import {ToastComponent} from '../../../shared/toast/toast.component';
import {NgForOf, NgIf} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {TradeResponse} from '../../../shared/models/trade-response';
import {ToastService} from '../../../core/services/toasts/toast.service';
import {TradeServiceService} from '../../../core/services/trades/trade-service.service';
import {InstrumentsService} from '../../../core/services/instruments/instruments.service';
import {AccountServiceService} from '../../../core/services/accounts/account-service.service';
import {forkJoin, map} from 'rxjs';

@Component({
  selector: 'app-trade-form',
  standalone: true,
  imports: [
    ToastComponent,
    NgIf,
    ReactiveFormsModule,
    NgForOf
  ],
  templateUrl: './trade-form.component.html',
  styleUrl: './trade-form.component.scss'
})
export class TradeFormComponent {
  @Input() tradeToEdit?: TradeResponse;
  @Output() formSubmitted = new EventEmitter<void>();

  @Output() closed = new EventEmitter<void>();

  tradeForm!: FormGroup;
  accountOptionsList: { id: number, name: string }[] = [];
  accountOptions: string[] = [];
  instrumentOptionsList: { id: number, code: number }[] = [];
  instrumentOptions: string[] = [];
  accountPage = 0;
  instrumentPage = 0;
  accountTotalPages = 1;
  instrumentTotalPages = 1;




  constructor(
    private fb: FormBuilder,
    private tradeService: TradeServiceService,
    private toast: ToastService,
    private accountService: AccountServiceService,
    private instrumentService: InstrumentsService,
  ) {}

  closeModal() {
    this.closed.emit();
  }

  ngOnInit(): void {
    this.tradeForm = this.fb.group({
      instrumentId: ['', Validators.required],
      accountName: ['', Validators.required],
      quantity: ['', [Validators.required, Validators.min(1)]],
      price: ['', [Validators.required, Validators.min(0.01)]],
      direction: ['', Validators.required],
      deliveryType: ['', Validators.required],
      unit: ['', Validators.required],
    });
      this.loadAccountsPage(0).subscribe(() => {
        this.loadInstrumentsPage(0).subscribe(() => {
          if (this.tradeToEdit) {
            this.tradeForm.patchValue({
              instrumentId: this.tradeToEdit.instrument.code,
              accountName: this.tradeToEdit.account.name,
              quantity: this.tradeToEdit.quantity,
              price: this.tradeToEdit.price,
              direction: this.tradeToEdit.direction,
              deliveryType: this.tradeToEdit.deliveryType,
              unit: this.tradeToEdit.unit,
            });
            this.instrumentId.disable();
            this.accountName.disable();
            this.direction.disable();
          }
        });
      });


  }

  loadAccountsPage(pageAcc: number) {
    return this.accountService.getAll(pageAcc).pipe(
      map(res => {
        const items = res.items.map(acc => ({ id: acc.id, name: acc.name.toString() }));
        this.accountOptionsList.push(...items);
        this.accountOptions.push(...items.map(i => i.name.toString()));
        this.accountTotalPages = res.totalPages;
        this.accountPage = res.page;
        return items;
      })
    );
  }

  loadInstrumentsPage(pageIn: number) {
    return this.instrumentService.getAll(pageIn).pipe(
      map(res => {
        const items = res.items.map(inst => ({ id: inst.id, code: inst.code }));
        this.instrumentOptionsList.push(...items);
        this.instrumentOptions.push(...items.map(i => i.code.toString() + ' ' + i.id.toString()));
        this.instrumentTotalPages = res.totalPages;
        this.instrumentPage = res.page;
        return items;
      })
    );
  }




  get instrumentId() {
    return this.tradeForm.get('instrumentId')!;
  }
  get accountName() { return this.tradeForm.get('accountName')!; }

  get quantity() {
    return this.tradeForm.get('quantity')!;
  }
  get price() {
    return this.tradeForm.get('price')!;
  }
  get direction() {
    return this.tradeForm.get('direction')!;
  }
  get deliveryType() {
    return this.tradeForm.get('deliveryType')!;
  }
  get unit() {
    return this.tradeForm.get('unit')!;
  }

  onSubmit(): void {
    if (this.tradeForm.invalid) return;

    let trade: TradeRequest;

    if (this.tradeToEdit) {
      trade = {
        accountId: this.tradeToEdit.account.id,
        instrumentId: this.tradeToEdit.instrument.id,
        quantity: this.tradeForm.get('quantity')!.value,
        price: this.tradeForm.get('price')!.value,
        direction: this.tradeForm.get('direction')!.value,
        deliveryType: this.tradeForm.get('deliveryType')!.value,
        unit: this.tradeForm.get('unit')!.value,
      };

      this.tradeService.updateTrade(this.tradeToEdit.id, trade).subscribe({
        next: () => {
          this.formSubmitted.emit();
          this.toast.show('Trade updated successfully', 'success');
        },
        error: err => {
          this.toast.show(err, 'error');
        },
      });
    } else {
      const selectedAccountId = +this.tradeForm.get('accountName')!.value;
      const selectedInstrumentId = +this.tradeForm.get('instrumentId')!.value;

      const account = this.accountOptionsList.find(acc => acc.id === selectedAccountId);
      const instrument = this.instrumentOptionsList.find(inst => inst.id === selectedInstrumentId);

      if (!account || !instrument) {
        this.toast.show('Invalid account or instrument selection', 'error');
        return;
      }

      trade = {
        accountId: account.id,
        instrumentId: instrument.id,
        quantity: this.tradeForm.get('quantity')!.value,
        price: this.tradeForm.get('price')!.value,
        direction: this.tradeForm.get('direction')!.value,
        deliveryType: this.tradeForm.get('deliveryType')!.value,
        unit: this.tradeForm.get('unit')!.value,
      };

      this.tradeService.createTrade(trade).subscribe({
        next: () => {
          this.formSubmitted.emit();
          this.toast.show('Trade created successfully', 'success');
        },
        error: err => {
          this.toast.show(err, 'error');
        },
      });
    }

    this.tradeForm.reset();
  }

}
