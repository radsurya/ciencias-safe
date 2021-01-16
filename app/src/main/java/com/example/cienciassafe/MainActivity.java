package com.example.cienciassafe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;

import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.api.ProximityObserver;
import com.estimote.proximity_sdk.api.ProximityObserverBuilder;
import com.estimote.proximity_sdk.api.ProximityZone;
import com.estimote.proximity_sdk.api.ProximityZoneBuilder;
import com.estimote.proximity_sdk.api.ProximityZoneContext;

import java.util.List;
import java.util.Set;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    private String email, subject, message;
    private Button button;

    private ProximityObserver proximityObserver;
    private ProximityZone zone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Handle Estimote */
        EstimoteCloudCredentials cloudCredentials = new EstimoteCloudCredentials("cienciassafe-bly", "57d3410e9b3c0594fe5ba8da9b1f5070");

        // Create the Proximity Observer
        this.proximityObserver =
                new ProximityObserverBuilder(getApplicationContext(), cloudCredentials)
                        .onError(new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                // Log.e("app", "proximity observer error: " + throwable);
                                return null;
                            }
                        })
                        .withBalancedPowerMode()
                        .build();

        zone = new ProximityZoneBuilder()
                .forTag("cienciassafe-bly")
                // .inCustomRange(3.0)
                .inNearRange()
                .onEnter(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext context) {
                        String beaconTitle = context.getAttachments().get("cienciassafe-bly/title");
                        // TextView viewBeacon = findViewById(R.id.beaconTitle);
                        // viewBeacon.setText("Room" + beaconTitle);
                        // Log.d("app", "Welcome to " + deskOwner + "'s desk");
                        return null;
                    }
                })
                .onExit(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext context) {
                        // TextView viewBeacon = findViewById(R.id.beaconTitle);
                        // viewBeacon.setText("Beacon test");
                        // Log.d("app", "Bye bye, come again!");
                        return null;
                    }
                })
                .onContextChange(new Function1<Set<? extends ProximityZoneContext>, Unit>() {
                    @Override
                    public Unit invoke(Set<? extends ProximityZoneContext> contexts) {
                        List<String> deskOwners = new ArrayList<>();
                        for (ProximityZoneContext context : contexts) {
                            String beaconTitle = context.getAttachments().get("cienciassafe-bly/title");
                            deskOwners.add(beaconTitle);
                        }
                        TextView viewBeacon = findViewById(R.id.beaconTitle);
                        switch (deskOwners.get(0)) {
                            case "lemon":
                                viewBeacon.setText("Sala 1.1.36 está cheia!");
                            case "beetroot":
                                viewBeacon.setText("Sala 1.2.21 está vazia!");
                            case "candy":
                                viewBeacon.setText("Sala 1.2.22 está quase cheia!");
                        }


                        Log.d("app", "In range of desks: " + deskOwners);
                        return null;
                    }
                })
                .build();

        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(this,
                        // onRequirementsFulfilled
                        new Function0<Unit>() {
                            @Override public Unit invoke() {
                                Log.d("app", "requirements fulfilled");
                                proximityObserver.startObserving(zone);
                                return null;
                            }
                        },
                        // onRequirementsMissing
                        new Function1<List<? extends Requirement>, Unit>() {
                            @Override public Unit invoke(List<? extends Requirement> requirements) {
                                Log.e("app", "requirements missing: " + requirements);
                                return null;
                            }
                        },
                        // onError
                        new Function1<Throwable, Unit>() {
                            @Override public Unit invoke(Throwable throwable) {
                                Log.e("app", "requirements error: " + throwable);
                                return null;
                            }
                        });
        /* End -  Handle Estimote */

        /* Handle menu sidebar */
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        View imageMenu = findViewById(R.id.imageMenu);
        if (imageMenu != null) {
            imageMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        NavigationView navigationView = findViewById(R.id.navigationView);
        if (navigationView != null) {
            navigationView.setItemIconTintList(null);

            NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
            NavigationUI.setupWithNavController(navigationView, navController);

            final TextView textTitle = findViewById(R.id.textTitle);
            navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener(){
                @Override
                public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                    textTitle.setText(destination.getLabel());
                }
            });
        }

        senEmail();
        /* SEND EMAIL */
        /*email = "EMAIL FCUL DIREÇÃO";
        subject = "ASSUNTO";
        message = "TESTE";
        button = findViewById(R.id.button8);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senEmail();
            }
        });*/

        /* End - Handle menu sidebar */
    }

    private void senEmail() {
        String mEmail = "filipebastias94@gmail.com";
        String mSubject = "TESTE";
        String mMessage = "TESTE";

        Mail javaMailAPI = new Mail(this, mEmail, mSubject, mMessage);
        System.out.println("TESTE");
        javaMailAPI.execute();
    }

    /* public void goToScreen(View view) {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    } */

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}