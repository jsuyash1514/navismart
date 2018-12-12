package com.navismart.navismart.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.navismart.navismart.R;

import androidx.navigation.Navigation;

public class StartFragment extends Fragment {

    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_fragment, container, false);

        View.OnClickListener actionCreateListener = Navigation.createNavigateOnClickListener(R.id.create_acct_action);//navigate to create acct page using action
        Button createAcctButton = view.findViewById(R.id.startFragment_createAccountButton);
        createAcctButton.setOnClickListener(actionCreateListener);

        View.OnClickListener actionSignInListener = Navigation.createNavigateOnClickListener(R.id.sign_in_action);//navigate to sign in email page using action
        Button signInButton = view.findViewById(R.id.startFragment_signInButton);
        signInButton.setOnClickListener(actionSignInListener);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}
