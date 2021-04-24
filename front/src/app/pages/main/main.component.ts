import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/service/authentication.service';


@Component({
  selector: 'page-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {
  messages:any = [];
  messagesDate:any = [];

  constructor(
    private authService: AuthenticationService, 
    private http: HttpClient) { }

  ngOnInit(): void {
    console.log("is userLogged in? ", this.authService.isUserLoggedIn());

    this.http.get<any>("http://localhost:8081/api/message").subscribe((result:[]) => {
      console.log("result api: ", result);
      this.messages = result;
      result.forEach((el:any) => {
        this.messagesDate.push(el.messagePortion.sendingDate);        
      })
      
    },(error: HttpResponse<any>) => {
      console.error("error detail: ", error);
      if(error.status == 403){
        console.log("error is type of:", error.status);
        this.authService.logOut();//TODO add snack to tells user it has been deconnected https://medium.com/@tejozarkar/angular-custom-snackbar-using-service-ff24e20eda7f
      }
    }) 
  }


  public test(el:any){
    console.log("event value:", el);
    let date = new Date(el);
    console.log(date.getFullYear());

    let messageToSend = this.messages.find((message:any) => message.messagePortion.sendingDate === el);
    console.log("message to show," , messageToSend);
    

  }


}
