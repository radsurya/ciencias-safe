package com.example.cienciassafe;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Vibrator;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment implements View.OnClickListener {
    private View v = null;
    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView textView;
    BarcodeDetector barcodeDetector;
    private Button sendEmailButton;
    private MaterialCardView materialCard;
    private TextView textViewInfo;
    private RecyclerView recyclerView;

    private Button button;

    public ReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Asks user for camera permission
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, getTargetRequestCode());

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_report, container, false);

        if (v != null) {
            sendEmailButton = (Button) v.findViewById(R.id.submit_resource_info);
            surfaceView = (SurfaceView) v.findViewById(R.id.camerapreview);
            textView = (TextView) v.findViewById(R.id.textView);
            textViewInfo = (TextView) v.findViewById(R.id.textView5);
            materialCard = v.findViewById(R.id.material_card);
            recyclerView = v.findViewById(R.id.recycler_view);

            barcodeDetector = new BarcodeDetector.Builder(getActivity())
                    .setBarcodeFormats(Barcode.QR_CODE).build();

            cameraSource = new CameraSource.Builder(getActivity(), barcodeDetector)
                    .setRequestedPreviewSize(640, 480).build();

            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(@NonNull SurfaceHolder holder) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(holder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<Barcode> detections) {
                    final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                    if (qrCodes.size() != 0) {
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                sendEmailButton.setVisibility(sendEmailButton.VISIBLE);
                            }
                        });
                        sendEmailButton.setEnabled(true);

                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                // Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                                // vibrator.vibrate(1000);
                                textView.setText(qrCodes.valueAt(0).displayValue);
                            }
                        });

                        // Send Email with QR code info
                        sendEmailButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                senEmail(qrCodes);
                            }
                        });
                    }
                }
            });

        }

        Button button = (Button) v.findViewById(R.id.button7);
        button.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.button7:
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        sendEmailButton.setVisibility(sendEmailButton.INVISIBLE);
                        surfaceView.setVisibility(surfaceView.INVISIBLE);
                        materialCard.setVisibility(materialCard.INVISIBLE);
                        textViewInfo.setVisibility(textViewInfo.INVISIBLE);
                        recyclerView.setVisibility(recyclerView.VISIBLE);
                        Button button = (Button) v.findViewById(R.id.button7);
                        button.setVisibility(button.INVISIBLE);
                        displayReportHistoricData();
                    }
                });

                break;
        }
    }

    // Send email with QR code info
    private void senEmail(SparseArray<Barcode> qrCodes) {
        String mEmail = "cienciasafe.fcul@gmail.com";
        String mSubject;
        mSubject = getString(R.string.email_subject);
        String mMessage = qrCodes.valueAt(0).displayValue;

        Mail javaMailAPI = new Mail(getContext(), mEmail, mSubject, mMessage);
        javaMailAPI.execute();
        registerResourceReport(mMessage);

        Toast.makeText(getContext(), getString(R.string.email_sent),Toast.LENGTH_SHORT).show();
    }

    private void registerResourceReport(String mMessage) {
        DatabaseReference reff;
        reff = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> map = new HashMap<>();
        String uniqueID = UUID.randomUUID().toString();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        map.put("resource", new Resource(mMessage, formatter.format(date), formatter2.format(date)));
        reff.child("resourceReport").child("Report_ID_" + uniqueID).setValue(map);
    }

    private void displayReportHistoricData() {
        final TextView teste = v.findViewById(R.id.teste);
        DatabaseReference reff;
        reff = FirebaseDatabase.getInstance().getReference();
        reff.child("resourceReport").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    ArrayList<String> reportHistory = new ArrayList<>();

                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        reportHistory.add(d.child("resource").child("date2xz").getValue(String.class) + ";" + d.child("resource").child("date").getValue(String.class) + ";" + d.child("resource").child("mMessage").getValue(String.class));
                    }

                    Collections.sort(reportHistory);
                    Collections.reverse(reportHistory);

                    RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
                    ReportRecyclerViewAdapter recyclerViewAdapter = new ReportRecyclerViewAdapter(reportHistory);
                    recyclerView.setAdapter(recyclerViewAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("Failed to read value. " + error.toException());
            }
        });
    }


}