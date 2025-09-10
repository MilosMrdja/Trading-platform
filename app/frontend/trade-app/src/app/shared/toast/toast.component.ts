import { Component } from '@angular/core';
import {NgClass, NgForOf} from '@angular/common';
import {Toast, ToastService} from '../../core/services/toasts/toast.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [
    NgForOf,
    NgClass
  ],
  templateUrl: './toast.component.html',
  styleUrl: './toast.component.scss'
})
export class ToastComponent {
  toasts: Toast[] = [];

  constructor(public toastService: ToastService) {
    toastService.toasts$.subscribe(toasts => this.toasts = toasts);
  }
}
