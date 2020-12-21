import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { EventMessageError } from 'src/app/models/error.model';
import { AuthenticationService } from 'src/app/service/authentication.service';

@Component({
  selector: 'comp-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  error: EventMessageError = new EventMessageError();

  loginForm: FormGroup = this.fb.group({
    username: ['username', Validators.required],
    password: ['password', Validators.required]
  });;

  invalidLogin:boolean = false;
  
  constructor(
    private router: Router,
    private loginService: AuthenticationService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    console.log(this.loginForm);
  }

  checkLogin() {
    //TODO need to create object user and loop for each value
    console.log(this.loginForm.value);

    // console.log(sessionStorage.getItem("token"));
    // console.log(sessionStorage['token']);
    // console.log(JSON.parse(sessionStorage.getItem("token") || '{}'));
    
    this.loginService.authenticate(this.loginForm.value.username, this.loginForm.value.password).subscribe(
      result => {
        console.log("result", result);
        this.router.navigate([''])
        this.invalidLogin = false;
      },
      error => {
        this.invalidLogin = true;
        console.log(error);
        
        //TODO make more dynamic
        this.error.status = error.status;
        this.error.error = error.error;
        this.error.message = error.message;
      })
  }

  sendLoginForm() {
    console.log(this.loginForm);
    
  }

}
