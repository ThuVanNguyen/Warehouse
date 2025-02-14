import { Component, inject, Input, OnChanges, SimpleChanges } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Component({
  standalone: true,
  selector: 'jhi-img',
  templateUrl: './img-cus.component.html',
})
export default class ImgCusComponent implements OnChanges {
  @Input() src!: string;
  @Input() alt!: string;
  safeImageUrl: SafeUrl | null = null;

  private sanitizer = inject(DomSanitizer);

  ngOnChanges(changes: SimpleChanges): void {
    this.safeImageUrl = this.sanitizer.bypassSecurityTrustUrl(this.src);
  }
}
