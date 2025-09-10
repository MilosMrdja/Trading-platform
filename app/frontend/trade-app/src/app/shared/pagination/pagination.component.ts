import {Component, EventEmitter, Input, Output} from '@angular/core';


@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [],
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.scss'
})
export class PaginationComponent {
  @Input() currentPage: number = 0;
  @Input() totalPages: number = 1;
  @Output() pageChange = new EventEmitter<number>();

  goToPrevious() {
    if (this.currentPage > 0) {
      this.pageChange.emit(this.currentPage - 1);
    }
  }

  goToNext() {
    if (this.currentPage < this.totalPages) {
      this.pageChange.emit(this.currentPage + 1);
    }
  }
}
