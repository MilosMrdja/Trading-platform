import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Router} from '@angular/router';
import {AuthService} from '../../core/services/auth.service';

@Component({
  selector: 'app-portal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './portal.component.html',
  styleUrl: './portal.component.scss'
})
export class PortalComponent implements OnInit {
  constructor(private auth: AuthService, private router: Router) {}
  ngOnInit() {
    this.auth.me().subscribe({
      next: (user: any) => {
        console.log(user);
        const role = String(user?.role || '').toLowerCase().replace(/^role_/, '');
        if (role === 'manager') {
          this.router.navigate(['/manager']);
        } else if (role === 'broker') {
          this.router.navigate(['/broker']);
        } else {
          this.router.navigate(['/login']);
        }
      },
      error: () => this.router.navigate(['/login'])
    });
  }
}
