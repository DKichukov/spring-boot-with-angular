import { Injectable } from "@angular/core";
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from "@angular/common/http";
import { catchError, Observable, throwError } from "rxjs";
import { MatSnackBar } from "@angular/material/snack-bar";

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {
  constructor(private snackBar: MatSnackBar) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMsg = "An unknown error occurred!";

        if (error.error instanceof ErrorEvent) {
          errorMsg = `Error: ${error.error.message}`;
        } else {
          errorMsg = `Error Code: ${error.status}\nMessage: ${error.error}`;
        }

        this.snackBar.open(errorMsg, "Close", {
          duration: 3000,
        });

        return throwError(() => new Error(errorMsg));
      })
    );
  }
}
