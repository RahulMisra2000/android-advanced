/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.fragmentcommunicate;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/* **** 
        The above v4 and v7 is old stuff ... you don't need it because now everything works of AndroidX  
****** */

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity implements SimpleFragment.OnFragmentInteractionListener {

    private Button mButton;
    private boolean isFragmentDisplayed = false;

    // Saved instance state keys.
    static final String STATE_FRAGMENT = "state_of_fragment";
    static final String STATE_CHOICE = "user_choice";

    // The radio button choice default is 2 = (no choice).
    // Initialize the radio button choice to the default.
    private int mRadioButtonChoice = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the button for opening and closing the fragment.
        mButton = findViewById(R.id.open_button);

        // If returning from a configuration change, get the
        // fragment state and set the button text.
        if (savedInstanceState != null) {
            isFragmentDisplayed = savedInstanceState.getBoolean(STATE_FRAGMENT);
            mRadioButtonChoice = savedInstanceState.getInt(STATE_CHOICE);
            if (isFragmentDisplayed) {
                // If the fragment is displayed, change button to "close".
                mButton.setText(R.string.close);
            }
        }

        // Set the click listener for the button.
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFragmentDisplayed) {
                    displayFragment();
                } else {
                    closeFragment();
                }
            }
        });
    }

    /**
     * This method is called when the user clicks the button
     * to open the fragment.
     */
    public void displayFragment() {
        // Instantiate the fragment.
        SimpleFragment simpleFragment = SimpleFragment.newInstance(mRadioButtonChoice);
        FragmentManager fMgr = getSupportFragmentManager();
        
        FragmentTransaction fTrx = fragmentManager.beginTransaction();

        /* ******* Adding the fragment to container widget defined in the activity's .xml layout file ****** */
        fTrx.add(R.id.fragment_container,simpleFragment).addToBackStack(null).commit();

        mButton.setText(R.string.close);       
        isFragmentDisplayed = true;
    }

    /**
     * This method is called when the user clicks the button to
     * close the fragment.
     */
    public void closeFragment() {
        // Get the FragmentManager.
        FragmentManager fMgr = getSupportFragmentManager();
        // Check to see if the fragment is already showing.
        SimpleFragment simpleFragment = (SimpleFragment) fMgr.findFragmentById(R.id.fragment_container);
        if (simpleFragment != null) {
            // Create and commit the transaction to remove the fragment.
            FragmentTransaction fTrx = fMgr.beginTransaction();
            fTrx.remove(simpleFragment).commit();
        }
   
        mButton.setText(R.string.open);      
        isFragmentDisplayed = false;
    }

    /**
     * This method keeps in memory the radio button choice the user selected
     * and displays a Toast to show it.
     *
     * @param choice The user's radio button choice.
     */
    @Override
    public void onRadioButtonChoice(int choice) {
        // Keep the radio button choice to pass it back to the fragment.
        mRadioButtonChoice = choice;
       
        Toast.makeText(this, "Choice is " + Integer.toString(choice),LENGTH_SHORT).show();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the state of the fragment (true=open, false=closed).
        savedInstanceState.putBoolean(STATE_FRAGMENT, isFragmentDisplayed);
        savedInstanceState.putInt(STATE_CHOICE, mRadioButtonChoice);
        super.onSaveInstanceState(savedInstanceState);
    }
}
