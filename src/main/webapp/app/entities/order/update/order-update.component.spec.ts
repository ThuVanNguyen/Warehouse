import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { WarehouseService } from 'app/entities/warehouse/service/warehouse.service';
import { IInlandLogistic } from 'app/entities/inland-logistic/inland-logistic.model';
import { InlandLogisticService } from 'app/entities/inland-logistic/service/inland-logistic.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IUnit } from 'app/entities/unit/unit.model';
import { UnitService } from 'app/entities/unit/service/unit.service';
import { IOrder } from '../order.model';
import { OrderService } from '../service/order.service';
import { OrderFormService } from './order-form.service';

import { OrderUpdateComponent } from './order-update.component';

describe('Order Management Update Component', () => {
  let comp: OrderUpdateComponent;
  let fixture: ComponentFixture<OrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderFormService: OrderFormService;
  let orderService: OrderService;
  let warehouseService: WarehouseService;
  let inlandLogisticService: InlandLogisticService;
  let clientService: ClientService;
  let productService: ProductService;
  let unitService: UnitService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OrderUpdateComponent],
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
      .overrideTemplate(OrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderFormService = TestBed.inject(OrderFormService);
    orderService = TestBed.inject(OrderService);
    warehouseService = TestBed.inject(WarehouseService);
    inlandLogisticService = TestBed.inject(InlandLogisticService);
    clientService = TestBed.inject(ClientService);
    productService = TestBed.inject(ProductService);
    unitService = TestBed.inject(UnitService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Warehouse query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const warehouse: IWarehouse = { id: 29523 };
      order.warehouse = warehouse;

      const warehouseCollection: IWarehouse[] = [{ id: 356 }];
      jest.spyOn(warehouseService, 'query').mockReturnValue(of(new HttpResponse({ body: warehouseCollection })));
      const additionalWarehouses = [warehouse];
      const expectedCollection: IWarehouse[] = [...additionalWarehouses, ...warehouseCollection];
      jest.spyOn(warehouseService, 'addWarehouseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(warehouseService.query).toHaveBeenCalled();
      expect(warehouseService.addWarehouseToCollectionIfMissing).toHaveBeenCalledWith(
        warehouseCollection,
        ...additionalWarehouses.map(expect.objectContaining),
      );
      expect(comp.warehousesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call InlandLogistic query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const inlandLogistic: IInlandLogistic = { id: 27853 };
      order.inlandLogistic = inlandLogistic;

      const inlandLogisticCollection: IInlandLogistic[] = [{ id: 8901 }];
      jest.spyOn(inlandLogisticService, 'query').mockReturnValue(of(new HttpResponse({ body: inlandLogisticCollection })));
      const additionalInlandLogistics = [inlandLogistic];
      const expectedCollection: IInlandLogistic[] = [...additionalInlandLogistics, ...inlandLogisticCollection];
      jest.spyOn(inlandLogisticService, 'addInlandLogisticToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(inlandLogisticService.query).toHaveBeenCalled();
      expect(inlandLogisticService.addInlandLogisticToCollectionIfMissing).toHaveBeenCalledWith(
        inlandLogisticCollection,
        ...additionalInlandLogistics.map(expect.objectContaining),
      );
      expect(comp.inlandLogisticsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Client query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const client: IClient = { id: 24258 };
      order.client = client;

      const clientCollection: IClient[] = [{ id: 15390 }];
      jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
      const additionalClients = [client];
      const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
      jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(
        clientCollection,
        ...additionalClients.map(expect.objectContaining),
      );
      expect(comp.clientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const product: IProduct = { id: 1140 };
      order.product = product;

      const productCollection: IProduct[] = [{ id: 26937 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Unit query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const unit: IUnit = { id: 30708 };
      order.unit = unit;

      const unitCollection: IUnit[] = [{ id: 20882 }];
      jest.spyOn(unitService, 'query').mockReturnValue(of(new HttpResponse({ body: unitCollection })));
      const additionalUnits = [unit];
      const expectedCollection: IUnit[] = [...additionalUnits, ...unitCollection];
      jest.spyOn(unitService, 'addUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(unitService.query).toHaveBeenCalled();
      expect(unitService.addUnitToCollectionIfMissing).toHaveBeenCalledWith(
        unitCollection,
        ...additionalUnits.map(expect.objectContaining),
      );
      expect(comp.unitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const order: IOrder = { id: 456 };
      const warehouse: IWarehouse = { id: 13254 };
      order.warehouse = warehouse;
      const inlandLogistic: IInlandLogistic = { id: 23331 };
      order.inlandLogistic = inlandLogistic;
      const client: IClient = { id: 5871 };
      order.client = client;
      const product: IProduct = { id: 19405 };
      order.product = product;
      const unit: IUnit = { id: 7783 };
      order.unit = unit;

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(comp.warehousesSharedCollection).toContain(warehouse);
      expect(comp.inlandLogisticsSharedCollection).toContain(inlandLogistic);
      expect(comp.clientsSharedCollection).toContain(client);
      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.unitsSharedCollection).toContain(unit);
      expect(comp.order).toEqual(order);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrder>>();
      const order = { id: 123 };
      jest.spyOn(orderFormService, 'getOrder').mockReturnValue(order);
      jest.spyOn(orderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: order }));
      saveSubject.complete();

      // THEN
      expect(orderFormService.getOrder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderService.update).toHaveBeenCalledWith(expect.objectContaining(order));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrder>>();
      const order = { id: 123 };
      jest.spyOn(orderFormService, 'getOrder').mockReturnValue({ id: null });
      jest.spyOn(orderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: order }));
      saveSubject.complete();

      // THEN
      expect(orderFormService.getOrder).toHaveBeenCalled();
      expect(orderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrder>>();
      const order = { id: 123 };
      jest.spyOn(orderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareWarehouse', () => {
      it('Should forward to warehouseService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(warehouseService, 'compareWarehouse');
        comp.compareWarehouse(entity, entity2);
        expect(warehouseService.compareWarehouse).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareInlandLogistic', () => {
      it('Should forward to inlandLogisticService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(inlandLogisticService, 'compareInlandLogistic');
        comp.compareInlandLogistic(entity, entity2);
        expect(inlandLogisticService.compareInlandLogistic).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareClient', () => {
      it('Should forward to clientService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(clientService, 'compareClient');
        comp.compareClient(entity, entity2);
        expect(clientService.compareClient).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProduct', () => {
      it('Should forward to productService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUnit', () => {
      it('Should forward to unitService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(unitService, 'compareUnit');
        comp.compareUnit(entity, entity2);
        expect(unitService.compareUnit).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
