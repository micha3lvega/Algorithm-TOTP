import { Component } from '@angular/core';
import { User } from '../User';
import { Router } from '@angular/router';

import * as CryptoJS from 'crypto-js';

import { environment } from '../../environments/environment';

@Component({
  selector: 'app-totp',
  standalone: true,
  imports: [],
  templateUrl: './totp.component.html',
  styleUrl: './totp.component.css',
})
export class TotpComponent {

  decriptValue:string|null=null;

  constructor(private router: Router) {
    this.checkUserLoggedIn();
  }

  checkUserLoggedIn(): void {
    const storedUserString = localStorage.getItem('user');
    if (storedUserString) {
      const user: User = JSON.parse(storedUserString);
      this.decriptValue = this.decryptValue(user.secretkey);

      if (!this.decriptValue) {
        this.router.navigate(['/login']);
      }
    } else {
      this.router.navigate(['/login']);
    }
  }

  decryptValue(encryptedValue: string) {

    const ivText = environment.ivText;
    const secretKeyText = environment.secretKeyText;

    try {

      const secretKey = CryptoJS.enc.Base64.parse(secretKeyText);
      const iv = CryptoJS.enc.Base64.parse(ivText);

      const encryptedBytes = CryptoJS.enc.Base64.parse(encryptedValue);
      const cipherParams = CryptoJS.lib.CipherParams.create({
        ciphertext: encryptedBytes,
      });

      const decryptedBytes = CryptoJS.AES.decrypt(cipherParams, secretKey, {
        iv: iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7,
      });

      return decryptedBytes.toString(CryptoJS.enc.Utf8);
    } catch (error) {
      console.error('Error decrypting value:', error);
    }

    return null;
  }
}
