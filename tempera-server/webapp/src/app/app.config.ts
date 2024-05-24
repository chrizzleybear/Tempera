import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { RouterModule } from '@angular/router';

import { routes } from './app.routes';
import { HttpClientModule } from '@angular/common/http';
import { httpInterceptorProviders } from './_helpers/http.interceptor';
import { provideAnimations } from '@angular/platform-browser/animations';
import { MessageService } from 'primeng/api';

export const appConfig: ApplicationConfig = {
  providers: [
    importProvidersFrom(RouterModule.forRoot(routes, {
      scrollPositionRestoration: 'enabled',
      anchorScrolling: 'enabled',
    }), HttpClientModule),
    MessageService,
    provideAnimations(),
    httpInterceptorProviders],
};
