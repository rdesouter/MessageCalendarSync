import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/service/authentication.service';

@Component({
  selector: 'page-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

  constructor(private authServie: AuthenticationService) { }

  ngOnInit(): void {
  }

  test(){
    this.authServie.authenticate("ronald", "ronald");
  }

}
