import {Component, HostListener} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Router, RouterLink, RouterOutlet, RouterLinkActive} from '@angular/router';
import {AuthService} from '../../../core/services/auth.service';
import {TradeServiceService} from '../../../core/services/trades/trade-service.service';
import {WebSocketService} from '../../../core/services/web-sockets/web-socket.service';

@Component({
  selector: 'app-broker-shell',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './broker-shell.component.html',
  styleUrl: './broker-shell.component.scss'
})
export class BrokerShellComponent{
  isOpen = false;
  userName: string | null = null;
  constructor(private router: Router, private auth: AuthService, private webSocketService:WebSocketService) {}

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
      next: ()=> {
        this.webSocketService.disconnect();
        this.router.navigate(['/login'])
      },
      error: () => this.router.navigate(['/login'])
    });
  }
}
