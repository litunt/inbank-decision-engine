import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {RouterModule} from "@angular/router";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {LoaderComponent} from "./_components/loader/loader.component";
import {FooterComponent} from "./_components/footer/footer.component";
import {DividerModule} from "primeng/divider";
import {MessageService, SharedModule} from "primeng/api";
import {ProgressBarModule} from "primeng/progressbar";
import {AppRoutingModule} from "./app-routing.module";
import { MainPageComponent } from './pages/main-page/main-page.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from "@angular/common/http";
import {TranslateLoader, TranslateModule, TranslateService} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {LoaderService} from "./_services/loader/loader.service";
import {CardModule} from "primeng/card";
import { DataCardComponent } from './_components/data-card/data-card.component';
import { RangeSliderComponent } from './_components/range-slider/range-slider.component';
import {SliderModule} from "primeng/slider";
import { InputNumberModule } from 'primeng/inputnumber';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ButtonModule} from "primeng/button";
import {TooltipModule} from "primeng/tooltip";
import { IdCodeSelectorComponent } from './_components/id-code-selector/id-code-selector.component';
import {DropdownModule} from "primeng/dropdown";
import {DecisionService} from "./_services/decision.service";
import {AppHttpInterceptor} from "./_services/interceptor/app.http.interceptor";
import {AppErrorService} from "./_services/interceptor/app-error.service";
import {ToastModule} from "primeng/toast";
import { DecisionModalComponent } from './_components/decision-modal/decision-modal.component';
import {DialogModule} from "primeng/dialog";

@NgModule({
  declarations: [
    AppComponent,
    LoaderComponent,
    FooterComponent,
    MainPageComponent,
    DataCardComponent,
    RangeSliderComponent,
    IdCodeSelectorComponent,
    DecisionModalComponent,
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
    ProgressBarModule,
    CardModule,
    SliderModule,
    InputNumberModule,
    FormsModule,
    ReactiveFormsModule,
    ButtonModule,
    TooltipModule,
    DropdownModule,
    ToastModule,
    DialogModule,
  ],
  providers: [
    LoaderService,
    DecisionService,
    AppErrorService,
    MessageService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AppHttpInterceptor,
      multi: true
    },
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
