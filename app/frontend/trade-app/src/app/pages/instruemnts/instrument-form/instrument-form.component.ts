import {Component, EventEmitter, Input, Output} from '@angular/core';
import {InstrumentRequest} from '../../../shared/models/instrument-request';
import {InstrumentResponse} from '../../../shared/models/instrument-response';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {InstrumentsService} from '../../../core/services/instruments/instruments.service';
import {ToastComponent} from '../../../shared/toast/toast.component';
import {NgIf} from '@angular/common';
import {ToastService} from '../../../core/services/toasts/toast.service';

@Component({
  selector: 'app-instrument-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    ToastComponent,
    NgIf
  ],
  templateUrl: './instrument-form.component.html',
  styleUrl: './instrument-form.component.scss'
})
export class InstrumentFormComponent {
  @Input() instrumentToEdit?: InstrumentResponse;
  @Output() formSubmitted = new EventEmitter<void>();

  instrumentForm!: FormGroup;
  minDate: string | undefined;

  constructor(private fb: FormBuilder, private instrumentService: InstrumentsService, private toast: ToastService) {}

  ngOnInit(): void {
    this.instrumentForm = this.fb.group({
      code: [{ value: '', disabled: false }, [Validators.required, Validators.min(1)]],
      maturityDate: ['', Validators.required],
    });

    if (this.instrumentToEdit) {
      this.instrumentForm.patchValue({
        code: this.instrumentToEdit.code,
        maturityDate: this.instrumentToEdit.maturityDate,
      });
      this.code.disable();
    }
    const today = new Date();
    this.minDate = today.toISOString().split('T')[0];
  }

  get code() {
    return this.instrumentForm.get('code')!;
  }

  get maturityDate() {
    return this.instrumentForm.get('maturityDate')!;
  }

  onSubmit(): void {
    if (this.instrumentForm.invalid) return;

    const formattedInstrument = {
      ...this.instrumentForm.value,
      maturityDate: this.instrumentForm.value.maturityDate
        ? new Date(this.instrumentForm.value.maturityDate).toISOString().slice(0, 19)
        : null
    };


    if (this.instrumentToEdit) {
      this.instrumentService.updateInstrument(this.instrumentToEdit.id, formattedInstrument).subscribe({
        next: () => {
          this.formSubmitted.emit()
          this.toast.show('Instrument updated successfully', 'success');
        },
        error: err => {
          this.toast.show(err, 'error')
        },
      });
    } else {
      this.instrumentService.createInstrument(formattedInstrument).subscribe({
        next: () => {
          this.formSubmitted.emit()
          this.toast.show('Instrument created successfully', 'success');
        },
        error: err => {
          this.toast.show(err, 'error')
        },
      });
    }

    this.instrumentForm.reset();
  }

  protected readonly Date = Date;
}
