package com.example.carpoolproject.Model.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;


public class Authentication {
    private static final Authentication instance = new Authentication();
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ;


    public static Authentication getInstance() {
        return instance;
    }

    public static void logIn(String inputEmail, String password, Authentication.OnSuccessCallback onSuccess,
                                  Authentication.OnFailureCallback onFailure) {
        mAuth.signInWithEmailAndPassword(inputEmail, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onSuccess.onSuccess("successful");

                        }
                        else {
                            onSuccess.onSuccess("wrong");
                        }
                    }

                }).addOnFailureListener(onFailure::onFailure);
    }


    public static void createUser(String inputEmail, String password, Authentication.OnSuccessCallback onSuccess,
                                     Authentication.OnFailureCallback onFailure) {

        mAuth.createUserWithEmailAndPassword(inputEmail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = task.getResult().getUser();
                                    onSuccess.onSuccess(user);

                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    if (errorMessage != null && errorMessage.contains("already in use")) {
                                        onSuccess.onSuccess("already registered");
                                    }

                                }
                            }
                        }
                )                .addOnFailureListener(onFailure::onFailure);

    }

    public interface OnSuccessCallback<T> {
        void onSuccess(T result);
    }

    public interface OnFailureCallback {
        void onFailure(Exception exception);
    }
}
