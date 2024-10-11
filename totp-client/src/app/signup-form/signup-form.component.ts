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

@Component({
  selector: 'app-signup-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    CommonModule,
    MatToolbarModule,
  ],
  templateUrl: './signup-form.component.html',
  styleUrl: './signup-form.component.css',
})
export class SignupFormComponent {
  signupForm: FormGroup;
  errorMsj: string = '';
  infoMsj: string = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private _snackBar: MatSnackBar,
    private router: Router
  ) {
    this.signupForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(5)]],
    });
  }

  onSubmit() {

    this.errorMsj = '';
    this.infoMsj = '';

    if (this.signupForm.valid) {

      const signupData = this.signupForm.value;

      this.http.post('http://localhost:8080/signup', signupData).subscribe({
        next: (response) => {
          this.infoMsj = `El usuario ${
            this.signupForm.get('username')?.value
          } fue creado con éxito`;
          this._snackBar.open(this.infoMsj, 'Cerrar', {
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

    console.log('onSubmit [end]');
  }
}
