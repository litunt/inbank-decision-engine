import { Component } from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {LoaderService} from "./_services/loader.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent {
  title = 'Decision Engine UI';

  constructor(private translate: TranslateService,
              private loaderService: LoaderService) {
    translate.addLangs(['en']);
    translate.setDefaultLang('en');
  }

  get isLoading() {
    return this.loaderService.isLoading;
  }
}
