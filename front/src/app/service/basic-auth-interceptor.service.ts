import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BasicAuthInterceptorService implements HttpInterceptor {

  constructor() { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log("inside interceptor", sessionStorage.getItem('token'));
    // console.log(JSON.parse(sessionStorage.getItem('token') || '{}'));

    console.log(req);
    if (sessionStorage.getItem('username') && sessionStorage.getItem('token')){
      req = req.clone({
        setHeaders: {
          Authorization: JSON.parse(sessionStorage.getItem('token') || '{}')
        }
      });
    }
    
    return next.handle(req);
  }
}
