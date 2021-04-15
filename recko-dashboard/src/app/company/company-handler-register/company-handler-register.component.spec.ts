import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyHandlerRegisterComponent } from './company-handler-register.component';

describe('CompanyHandlerRegisterComponent', () => {
  let component: CompanyHandlerRegisterComponent;
  let fixture: ComponentFixture<CompanyHandlerRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CompanyHandlerRegisterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyHandlerRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
