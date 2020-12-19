import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/service/authentication.service';

@Component({
  selector: 'comp-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.scss']
})
export class LogoutComponent implements OnInit {

  constructor(
    private authService: AuthenticationService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.authService.logOut();
    this.router.navigate(['login']);
  }

}
