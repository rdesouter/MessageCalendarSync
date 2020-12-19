import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/service/authentication.service';

@Component({
  selector: 'comp-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  // @Input() error: string | null;

  loginForm: FormGroup = this.fb.group({
    username: ['username', Validators.required],
    password: ['password', Validators.required]
  });;
  private username:string = "";
  private password:string = "";
  private invalidLogin:boolean = false;
  
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
    console.log(this.loginForm.value.username);
    
    this.loginService.authenticate(this.loginForm.value.username, this.loginForm.value.password).subscribe(
      result => {
        console.log(result);
        this.router.navigate([''])
        this.invalidLogin = false;
      },
      error => {
        this.invalidLogin = true;
        console.log(error);
        
      })
  }

  sendLoginForm() {
    console.log(this.loginForm);
    
  }

}
