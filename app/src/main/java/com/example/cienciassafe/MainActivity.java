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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.api.ProximityObserver;
import com.estimote.proximity_sdk.api.ProximityObserverBuilder;
import com.estimote.proximity_sdk.api.ProximityZone;
import com.estimote.proximity_sdk.api.ProximityZoneBuilder;
import com.estimote.proximity_sdk.api.ProximityZoneContext;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {
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
                                showRoomOccupation(viewBeacon);
                            case "beetroot":
                                showRoomOccupation(viewBeacon);
                            case "candy":
                                showRoomOccupation(viewBeacon);
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

        //senEmail();


        /* End - Handle menu sidebar */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void showRoomOccupation(final TextView view) {
        if (view != null) {
            DatabaseReference reff;
            reff = FirebaseDatabase.getInstance().getReference();

            reff.child("rooms").child("c1").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        int count = 0;
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            if (count == 0) {
                                String room = d.getKey().replace("-", ".");
                                String occupation = d.child("occupation").getValue(String.class);
                                String occupationUpdate = "no_info";

                                if (room != null && occupation != null) {
                                    if (occupation.equals("no_info")) {
                                        occupationUpdate = "almost_empty";
                                        view.setText(getString(R.string.room_empty, room));
                                    } else if (occupation.equals("empty")) {
                                        occupationUpdate = "almost_empty";
                                        view.setText(getString(R.string.room_almost_empty, room));
                                    } else if (occupation.equals("almost_empty")) {
                                        occupationUpdate = "half_full";
                                        view.setText(getString(R.string.room_half_full, room));
                                    } else if (occupation.equals("half_full")) {
                                        occupationUpdate = "almost_full";
                                        view.setText(getString(R.string.room_almost_full, room));
                                    } else if (occupation.equals("almost_full")) {
                                        occupationUpdate = "full";
                                        view.setText(getString(R.string.room_full, room));
                                    } else if (occupation.equals("full")) {
                                        occupationUpdate = "full";
                                        view.setText(getString(R.string.room_full, room));
                                    }

                                    /* Save new occupation of room */
                                    String originalRoom = d.getKey();
                                    DatabaseReference reff2;
                                    reff2 = FirebaseDatabase.getInstance().getReference();
                                    String currentDateandTime = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(new Date());
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("occupation", occupationUpdate);
                                    map.put("time_report", currentDateandTime);
                                    reff2.child("rooms").child("c1").child(originalRoom).updateChildren(map);
                                    /* End - Save new occupation of room */

                                }

                                count = count + 1;
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                    System.out.println("Failed to read value. " + error.toException());
                }
            });
        }

    }

}