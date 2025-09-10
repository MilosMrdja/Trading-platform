import { Injectable, Inject } from '@angular/core';
import {API_BASE_URL} from '../../../app.token';
import {Client, IMessage} from '@stomp/stompjs';
import {Subject} from 'rxjs';
import SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private socketUrl = 'http://localhost:8080/ws'
  private client!: Client;
  private tradeSubject = new Subject<any>();
  public trades$ = this.tradeSubject.asObservable();
  constructor(@Inject(API_BASE_URL) private apiUrl:string) { }

  connect() {
    this.client = new Client({
      webSocketFactory: () => new SockJS(`${this.socketUrl}`),
      reconnectDelay: 5000
    });

    this.client.onConnect = () => {
      console.log(`Connected to WebSocket on ${this.socketUrl}`);

      // Subscribe to topic trades
      this.client.subscribe('/topic/trade', (message: IMessage) => {
        const trade = JSON.parse(message.body);
        this.tradeSubject.next(trade);
      });
    };

    this.client.activate();
  }

  disconnect() {
    if (this.client) {
      this.client.deactivate();
      console.log(`Disconnected from WebSocket on ${API_BASE_URL}${this.socketUrl}`);
    }
  }
}
