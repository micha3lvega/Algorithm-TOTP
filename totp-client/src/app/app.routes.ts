import { Routes } from '@angular/router';
import { SignupFormComponent } from './signup-form/signup-form.component';
import { LoginComponent } from './login/login.component';
import { TotpComponent } from './totp/totp.component';

export const routes: Routes = [
  { path: 'home', component: TotpComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupFormComponent },
  { path: '**', component: LoginComponent },
];
