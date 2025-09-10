import {Component, EventEmitter, Input, Output} from '@angular/core';
import {UserResponse} from '../../../shared/models/user-response';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {UsersService} from '../../../core/services/users/users.service';
import {UserRequest} from '../../../shared/models/user-request';
import {ToastComponent} from '../../../shared/toast/toast.component';
import {NgIf} from '@angular/common';
import {ToastService} from '../../../core/services/toasts/toast.service';
import {Role} from '../../../shared/enums/role';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [
    ToastComponent,
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.scss'
})
export class UserFormComponent {
  @Input() userToEdit?: UserResponse;
  @Output() formSubmitted = new EventEmitter<void>();
  userForm!: FormGroup;
  showPassword = false;

  constructor(private fb: FormBuilder, private userService: UsersService, private toast:ToastService) {}

  ngOnInit(): void {
    this.userForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      role: ['', [Validators.required]],
      password: ['', this.userToEdit ? [] : [Validators.required, Validators.minLength(6)]]
    });

    if (this.userToEdit) {
      this.userForm.patchValue({
        name: this.userToEdit.name,
        email: this.userToEdit.email,
        role: this.userToEdit.role
      });
      this.userForm.get('email')?.disable();
      this.userForm.get('password')?.disable();
      this.userForm.get('role')?.disable();
    }
  }

  get name() { return this.userForm.get('name')!; }
  get email() { return this.userForm.get('email')!; }
  get role() { return this.userForm.get('role')!; }
  get password() { return this.userForm.get('password')!; }

  onSubmit(): void {
    if (this.userForm.invalid) return;

    const request: UserRequest = {
      name: this.userForm.value.name,
      email: this.userForm.value.email,
      password: this.userForm.value.password,
      role: this.userForm.value.role
    };

    if (this.userToEdit) {
      this.userService.updateUser(this.userToEdit.id, request).subscribe({
        next: () => {
          this.formSubmitted.emit();
          this.userForm.reset();
          this.toast.show('User updated successfully', 'success');
        },
        error: err => {
          this.toast.show(err, 'error')
        }
      });
    } else {
      if(request.role === Role.manager) {
        this.userService.createManager(request).subscribe({
          next: () => {
            this.formSubmitted.emit();
            this.userForm.reset();
            this.toast.show('Manager created successfully', 'success');
          },
          error: err => {
            this.toast.show(err, 'error')
          }
        });
      }else{
        this.userService.createBroker(request).subscribe({
          next: () => {
            this.formSubmitted.emit();
            this.userForm.reset();
            this.toast.show('Broker created successfully', 'success');
          },
          error: err => {
            this.toast.show(err, 'error')
          }
        });
      }

    }


  }
}
