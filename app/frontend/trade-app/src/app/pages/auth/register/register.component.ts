import { Component } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {FormsModule, NgForm} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';
import {Role} from '../../../shared/enums/role';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  name = '';
  email = '';
  password = '';
  confirmPassword = '';
  role: Role | undefined = undefined;
  isLoading = false;
  errorMessage = '';

  constructor(private router: Router, private auth: AuthService) {}

  onRegister(form?: NgForm) {
    this.errorMessage = '';

    if (form && form.invalid) {
      this.errorMessage = 'Please fill in all required fields correctly.';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match.';
      return;
    }

    if (!this.role) {
      this.errorMessage = 'Please select a role.';
      return;
    }

    this.isLoading = true;
    this.auth.signup(this.name, this.email, this.password, this.role).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/login']);
      },
      error: (err: Error) => {
        this.isLoading = false;
        this.errorMessage = err.message || 'Registration failed.';
      }
    });
  }
}
