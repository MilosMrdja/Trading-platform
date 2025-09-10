import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AccountRequest} from '../../../shared/models/account-request';
import {AccountServiceService} from '../../../core/services/accounts/account-service.service';
import {AccountResponse} from '../../../shared/models/account-response';
import {NgIf} from '@angular/common';
import {ToastService} from '../../../core/services/toasts/toast.service';
import {ToastComponent} from '../../../shared/toast/toast.component';

@Component({
  selector: 'app-account-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    ToastComponent
  ],
  templateUrl: './account-form.component.html',
  styleUrl: './account-form.component.scss'
})
export class AccountFormComponent {
  @Input() accountToEdit?: AccountResponse;
  @Output() formSubmitted = new EventEmitter<void>();
  accountForm!: FormGroup;

  constructor(private fb: FormBuilder, private accountService: AccountServiceService, private toastr:ToastService) {}

  ngOnInit(): void {
    this.accountForm = this.fb.group({
      name: [{ value: '', disabled: false }, [Validators.required, Validators.minLength(2)]],
      userInfo: ['', [Validators.required, Validators.minLength(2)]],
    });

    if (this.accountToEdit) {
      this.accountForm.patchValue({
        name: this.accountToEdit.name,
        userInfo: this.accountToEdit.userInfo
      });
      this.name.disable();
    }
  }

  get name() {
    return this.accountForm.get('name')!;
  }

  get userInfo() {
    return this.accountForm.get('userInfo')!;
  }

  onSubmit(): void {
    if (this.accountForm.invalid) return;

    const request: AccountRequest = {
      name: this.accountToEdit ? this.accountToEdit.name : this.accountForm.value.name,
      userInfo: this.accountForm.value.userInfo
    };

    if (this.accountToEdit) {
      // Edit
      this.accountService.updateAccount(this.accountToEdit.id, request).subscribe({
        next: (res) =>{
          this.toastr.show('Account updated successfully', 'success');
          this.formSubmitted.emit();
        } ,
        error: (err) => {
          this.toastr.show(err, 'error');
        }

      });
    } else {
      // Add
      this.accountService.createAccount(request).subscribe({
        next: (res) => {
          this.toastr.show('Account created successfully', 'success');
          this.formSubmitted.emit();
        },
        error: (err) => {
          this.toastr.show(err, 'error');
      }
      });
    }

    this.accountForm.reset();
  }
}
