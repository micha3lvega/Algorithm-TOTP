import { Component } from '@angular/core';
import { User } from '../User';
import { Router } from '@angular/router';

import * as CryptoJS from 'crypto-js';

@Component({
  selector: 'app-totp',
  standalone: true,
  imports: [],
  templateUrl: './totp.component.html',
  styleUrl: './totp.component.css',
})
export class TotpComponent {
  constructor(private router: Router) {
    this.checkUserLoggedIn();
  }

  checkUserLoggedIn(): void {
    const storedUserString = localStorage.getItem('user');
    if (storedUserString) {
      const user: User = JSON.parse(storedUserString);
      var decriptKey = this.decryptValue(user.secretkey);

      if (!decriptKey) {
        this.router.navigate(['/login']);
      }
    } else {
      this.router.navigate(['/login']);
    }
  }

  decryptValue(encryptedValue:string) {

    const ivText = 'hdYXiwXs4twHxJYBNQivyA==';
    const secretKeyText = 'WYIL06CsNqdsnjq+jrN7cw==';

    const secretKey = CryptoJS.enc.Base64.parse(secretKeyText);
    const iv = CryptoJS.enc.Base64.parse(ivText);

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

      const decryptedValue = decryptedBytes.toString(CryptoJS.enc.Utf8);
      console.log(
        'encrypt: ' + encryptedValue + ', decript: ' + decryptedValue
      );

      return decryptedValue;
    } catch (error) {
      console.error('Error decrypting value:', error);
    }

    return null;
  }
}
