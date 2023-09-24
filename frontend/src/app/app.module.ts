import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {RouterModule} from "@angular/router";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {LoaderComponent} from "./_components/loader/loader.component";
import {FooterComponent} from "./_components/footer/footer.component";
import {DividerModule} from "primeng/divider";
import {SharedModule} from "primeng/api";
import {ProgressBarModule} from "primeng/progressbar";
import {AppRoutingModule} from "./app-routing.module";
import { MainPageComponent } from './pages/main-page/main-page.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from "@angular/common/http";
import {TranslateLoader, TranslateModule, TranslateService} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {LoaderService} from "./_services/loader.service";

@NgModule({
  declarations: [
    AppComponent,
    LoaderComponent,
    FooterComponent,
    MainPageComponent,
  ],
  imports: [
    BrowserModule,
    RouterModule,
    AppRoutingModule,
    ProgressSpinnerModule,
    BrowserAnimationsModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    DividerModule,
    SharedModule,
    ProgressBarModule
  ],
  providers: [
    LoaderService,
    // {
    //   provide: HTTP_INTERCEPTORS,
    //   useClass: JwtInterceptor,
    //   multi: true
    // }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor(translate: TranslateService) {
    translate.use(translate.getDefaultLang());
  }
}

export function HttpLoaderFactory(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}
