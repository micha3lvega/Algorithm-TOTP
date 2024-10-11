import { Component } from '@angular/core';

import { MatToolbarModule } from '@angular/material/toolbar';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';

import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    CommonModule,
    MatToolbarModule,
    RouterModule,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMsj: string = '';
  infoMsj: string = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private _snackBar: MatSnackBar,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

  onSubmit() {
    this.errorMsj = '';
    this.infoMsj = '';

    if (this.loginForm.valid) {
      const loginData = this.loginForm.value;

      this.http.post('http://localhost:8080/login', loginData).subscribe({
        next: (response) => {
          localStorage.setItem('user', JSON.stringify(response));

          this.router.navigate(['/home']);
          this._snackBar.open(`Bienvienido`, 'Cerrar', {
            duration: 6000,
            verticalPosition: 'bottom',
            horizontalPosition: 'left',
          });
        },
        error: (error) => {
          this.errorMsj = `Error: ${error.error.message}`;
          this._snackBar.open(this.errorMsj, 'Cerrar', {
            duration: 6000,
            verticalPosition: 'bottom',
            horizontalPosition: 'left',
          });
        },
      });
    }
  }
}
