import { Component, OnInit, inject, signal } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import SharedModule from 'app/shared/shared.module';
import { Account } from 'app/core/auth/account.model';
import { UserManagementService } from '../service/user-management.service';
import PasswordStrengthBarComponent from 'app/account/password/password-strength-bar/password-strength-bar.component';
import { IUser } from '../user-management.model';

@Component({
  selector: 'jhi-update-password',
  standalone: true,
  imports: [SharedModule, FormsModule, ReactiveFormsModule, PasswordStrengthBarComponent],
  templateUrl: './update-password.component.html',
  styleUrl: './update-password.component.scss',
})
export class UpdatePasswordComponent implements OnInit {
  user: IUser | null = null;
  isSaving = signal(false);
  doNotMatch = signal(false);
  error = signal(false);
  success = signal(false);
  passwordForm = new FormGroup({
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
    confirmPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
  });

  private readonly route = inject(ActivatedRoute);
  private readonly userService = inject(UserManagementService);

  ngOnInit(): void {
    this.user = this.route.snapshot.data['user'];
  }

  previousState(): void {
    window.history.back();
  }

  changePassword(): void {
    this.error.set(false);
    this.success.set(false);
    this.doNotMatch.set(false);

    const { password, confirmPassword } = this.passwordForm.getRawValue();
    if (password !== confirmPassword) {
      this.doNotMatch.set(true);
    } else if (this.user?.id != null) {
      this.isSaving.set(true);
      this.user.password = password;
      this.userService.updatePassword(this.user).subscribe({
        next: () => {
          this.success.set(true);
          this.isSaving.set(false);
          this.onSaveSuccess();
        },
        error: () => {
          this.error.set(true);
          this.isSaving.set(false);
          this.onSaveError();
        },
      });
    } else {
      this.error.set(true);
    }
  }

  private onSaveSuccess(): void {
    this.isSaving.set(false);
    this.previousState();
  }

  private onSaveError(): void {
    this.isSaving.set(false);
  }
}
