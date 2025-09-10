import { Component } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {FormsModule, NgForm} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';
import {TradeServiceService} from '../../../core/services/trades/trade-service.service';
import {WebSocketService} from '../../../core/services/web-sockets/web-socket.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  email = '';
  password = '';
  isLoading = false;
  errorMessage = '';

  constructor(private router: Router, private auth: AuthService, private webSocketService: WebSocketService) {}

  onLogin(form?: NgForm) {
    this.errorMessage = '';
    if (form && form.invalid) {
      this.errorMessage = 'Please fill in all required fields correctly.';
      return;
    }

    this.isLoading = true;
    this.auth.login(this.email, this.password).subscribe({
      next: () => {
        this.isLoading = false;
        this.webSocketService.connect();
        this.router.navigate(['']);
      },
      error: (err: Error) => {
        this.isLoading = false;
        this.errorMessage = err.message || 'Sign in failed.';
      }
    });
  }
}
