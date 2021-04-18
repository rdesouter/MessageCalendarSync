import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router, RouterLink, RouterModule, Routes } from '@angular/router';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private baseWsUrl: string = "http://localhost:8081/api";
  private userUri: string = "/user";

  constructor(private httpClient: HttpClient, private router: Router) { }

  authenticate(username: string, password: string) {
    return this.httpClient
      .post<any>(this.baseWsUrl + this.userUri + "/authenticate", {username, password}) 
      .pipe(
        map(userData => {
          console.log("authService userData", userData);
          
          sessionStorage.setItem("username", username);
          let tokenStr = "Bearer " + userData.jwt;
          sessionStorage.setItem("token", tokenStr);
          return userData;
        })
      );
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem("username");
    // console.log(!(user === null));
    return !(user === null);
  }

  logOut() {
    this.router.navigateByUrl('/login');
    sessionStorage.removeItem("username");
  }

}
