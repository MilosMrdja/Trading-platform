import {Component, HostListener} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Router, RouterLink, RouterOutlet, RouterLinkActive} from '@angular/router';
import {AuthService} from '../../../core/services/auth.service';

@Component({
  selector: 'app-manager-shell',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './manager-shell.component.html',
  styleUrl: './manager-shell.component.scss'
})
export class ManagerShellComponent{
  isOpen = false;
  userName: string | null = null;
  constructor(private router: Router, private auth: AuthService) {}

  toggleMenu(){ this.isOpen = !this.isOpen; }
  @HostListener('document:keydown.escape') onEsc(){ this.isOpen = false; }

  ngOnInit(){
    this.auth.me().subscribe({
      next: (user: any) => {
        this.userName = user?.name;
      }
    })
  }

  onLogout(){
    this.auth.logout().subscribe({
      next: () => this.router.navigate(['/login']),
      error: () => this.router.navigate(['/login'])
    });
  }
}
