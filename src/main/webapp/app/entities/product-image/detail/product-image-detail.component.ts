import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IProductImage } from '../product-image.model';
import ImgCusComponent from 'app/shared/img-cus/img-cus.component';

@Component({
  standalone: true,
  selector: 'jhi-product-image-detail',
  templateUrl: './product-image-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, ImgCusComponent],
})
export class ProductImageDetailComponent {
  productImage = input<IProductImage | null>(null);

  previousState(): void {
    window.history.back();
  }
}
