import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductImage, NewProductImage } from '../product-image.model';

export type PartialUpdateProductImage = Partial<IProductImage> & Pick<IProductImage, 'id'>;

type RestOf<T extends IProductImage | NewProductImage> = Omit<T, 'createDate'> & {
  createDate?: string | null;
};

export type RestProductImage = RestOf<IProductImage>;

export type NewRestProductImage = RestOf<NewProductImage>;

export type PartialUpdateRestProductImage = RestOf<PartialUpdateProductImage>;

export type EntityResponseType = HttpResponse<IProductImage>;
export type EntityArrayResponseType = HttpResponse<IProductImage[]>;

@Injectable({ providedIn: 'root' })
export class ProductImageService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-images');

  create(productImage: NewProductImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productImage);
    return this.http
      .post<RestProductImage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(productImage: IProductImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productImage);
    return this.http
      .put<RestProductImage>(`${this.resourceUrl}/${this.getProductImageIdentifier(productImage)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(productImage: PartialUpdateProductImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productImage);
    return this.http
      .patch<RestProductImage>(`${this.resourceUrl}/${this.getProductImageIdentifier(productImage)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProductImage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProductImage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProductImageIdentifier(productImage: Pick<IProductImage, 'id'>): number {
    return productImage.id;
  }

  compareProductImage(o1: Pick<IProductImage, 'id'> | null, o2: Pick<IProductImage, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductImageIdentifier(o1) === this.getProductImageIdentifier(o2) : o1 === o2;
  }

  addProductImageToCollectionIfMissing<Type extends Pick<IProductImage, 'id'>>(
    productImageCollection: Type[],
    ...productImagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productImages: Type[] = productImagesToCheck.filter(isPresent);
    if (productImages.length > 0) {
      const productImageCollectionIdentifiers = productImageCollection.map(productImageItem =>
        this.getProductImageIdentifier(productImageItem),
      );
      const productImagesToAdd = productImages.filter(productImageItem => {
        const productImageIdentifier = this.getProductImageIdentifier(productImageItem);
        if (productImageCollectionIdentifiers.includes(productImageIdentifier)) {
          return false;
        }
        productImageCollectionIdentifiers.push(productImageIdentifier);
        return true;
      });
      return [...productImagesToAdd, ...productImageCollection];
    }
    return productImageCollection;
  }

  protected convertDateFromClient<T extends IProductImage | NewProductImage | PartialUpdateProductImage>(productImage: T): RestOf<T> {
    return {
      ...productImage,
    };
  }

  protected convertDateFromServer(restProductImage: RestProductImage): IProductImage {
    return {
      ...restProductImage,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProductImage>): HttpResponse<IProductImage> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProductImage[]>): HttpResponse<IProductImage[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
