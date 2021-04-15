import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyCredentialRegisterComponent } from './company-credential-register.component';

describe('CompanyCredentialRegisterComponent', () => {
  let component: CompanyCredentialRegisterComponent;
  let fixture: ComponentFixture<CompanyCredentialRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CompanyCredentialRegisterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyCredentialRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
