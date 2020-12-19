import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private baseWsUrl: string = "http://localhost:8081/api";
  private userUri: string = "/user";

  constructor(private httpClient: HttpClient) { }

  authenticate(username: string, password: string) {
    return this.httpClient
      .post<any>(this.baseWsUrl + this.userUri + "/authenticate", {username, password}) 
      .pipe(
        map(userData => {
          sessionStorage.setItem("username", username);
          let tokenStr = "Bearer " + userData.token;
          sessionStorage.setItem("token", tokenStr);
          return userData;
        })
      );
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem("username");
    console.log(!(user === null));
    return !(user === null);
  }

  logOut() {
    sessionStorage.removeItem("username");
  }

}
