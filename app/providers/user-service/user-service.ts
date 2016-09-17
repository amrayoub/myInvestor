import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

export class User {
  name: string;
  password: string;
  email: string;

  constructor(name: string, password: string, email: string) {
    this.name = name;
    this.password = password;
    this.email = email;
  }
}

/*
  User service.
*/
@Injectable()
export class UserService {

  constructor(private http: Http) { }

}

