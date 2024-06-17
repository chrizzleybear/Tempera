import { Injectable } from '@angular/core';
import { LayoutService } from '../layout/service/app.layout.service';

@Injectable({
  providedIn: 'root'
})
export class ColorSchemeService {

  constructor(private layoutService: LayoutService) { }

  public toggleScheme() {
    const newColorScheme = this.layoutService.config.colorScheme === 'light' ? 'dark' : 'light';
    const newTheme = this.layoutService.config.theme === 'lara-light-indigo' ? 'lara-dark-indigo' : 'lara-light-indigo';
    this.changeTheme(newTheme, newColorScheme);
  }

  public changeScheme(colorScheme: string) {
    const newTheme = colorScheme === 'dark' ? 'lara-dark-indigo' : 'lara-light-indigo';
    this.changeTheme(newTheme, colorScheme);
  }

  private changeTheme(theme: string, colorScheme: string) {
    const themeLink = <HTMLLinkElement>document.getElementById('theme-css');
    const newHref = themeLink.getAttribute('href')!.replace(this.layoutService.config.theme, theme);

    this.replaceThemeLink(newHref, () => {
      this.layoutService.config.theme = theme;
      this.layoutService.config.colorScheme = colorScheme;
      this.layoutService.onConfigUpdate();
      localStorage.setItem('colorScheme', colorScheme);
    });
  }

  private replaceThemeLink(href: string, onComplete: Function) {
    const id = 'theme-css';
    const themeLink = <HTMLLinkElement>document.getElementById('theme-css');
    const cloneLinkElement = <HTMLLinkElement>themeLink.cloneNode(true);

    cloneLinkElement.setAttribute('href', href);
    cloneLinkElement.setAttribute('id', id + '-clone');

    themeLink.parentNode!.insertBefore(cloneLinkElement, themeLink.nextSibling);

    cloneLinkElement.addEventListener('load', () => {
      themeLink.remove();
      cloneLinkElement.setAttribute('id', id);
      onComplete();
    });
  }
}
