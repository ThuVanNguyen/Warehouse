import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { ProductImageService } from '../service/product-image.service';
import { IProductImage } from '../product-image.model';
import { ProductImageFormService } from './product-image-form.service';

import { ProductImageUpdateComponent } from './product-image-update.component';

describe('ProductImage Management Update Component', () => {
  let comp: ProductImageUpdateComponent;
  let fixture: ComponentFixture<ProductImageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productImageFormService: ProductImageFormService;
  let productImageService: ProductImageService;
  let orderService: OrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProductImageUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProductImageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductImageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productImageFormService = TestBed.inject(ProductImageFormService);
    productImageService = TestBed.inject(ProductImageService);
    orderService = TestBed.inject(OrderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Order query and add missing value', () => {
      const productImage: IProductImage = { id: 456 };
      const order: IOrder = { id: 32334 };
      productImage.order = order;

      const orderCollection: IOrder[] = [{ id: 3070 }];
      jest.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
      const additionalOrders = [order];
      const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
      jest.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productImage });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(
        orderCollection,
        ...additionalOrders.map(expect.objectContaining),
      );
      expect(comp.ordersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productImage: IProductImage = { id: 456 };
      const order: IOrder = { id: 28963 };
      productImage.order = order;

      activatedRoute.data = of({ productImage });
      comp.ngOnInit();

      expect(comp.ordersSharedCollection).toContain(order);
      expect(comp.productImage).toEqual(productImage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductImage>>();
      const productImage = { id: 123 };
      jest.spyOn(productImageFormService, 'getProductImage').mockReturnValue(productImage);
      jest.spyOn(productImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productImage }));
      saveSubject.complete();

      // THEN
      expect(productImageFormService.getProductImage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productImageService.update).toHaveBeenCalledWith(expect.objectContaining(productImage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductImage>>();
      const productImage = { id: 123 };
      jest.spyOn(productImageFormService, 'getProductImage').mockReturnValue({ id: null });
      jest.spyOn(productImageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productImage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productImage }));
      saveSubject.complete();

      // THEN
      expect(productImageFormService.getProductImage).toHaveBeenCalled();
      expect(productImageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductImage>>();
      const productImage = { id: 123 };
      jest.spyOn(productImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productImageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOrder', () => {
      it('Should forward to orderService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(orderService, 'compareOrder');
        comp.compareOrder(entity, entity2);
        expect(orderService.compareOrder).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
