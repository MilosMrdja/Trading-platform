import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';

export interface Toast {
  id: number;
  message: string;
  type: 'success' | 'error' | 'info';
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  private _toasts = new BehaviorSubject<Toast[]>([]);
  toasts$ = this._toasts.asObservable();
  private counter = 0;

  show(message: string, type: 'success' | 'error' | 'info' = 'info', duration = 4000) {
    const id = ++this.counter;
    const toast: Toast = { id, message, type };
    this._toasts.next([...this._toasts.value, toast]);

    setTimeout(() => this.remove(id), duration);
  }

  remove(id: number) {
    this._toasts.next(this._toasts.value.filter(t => t.id !== id));
  }
}
