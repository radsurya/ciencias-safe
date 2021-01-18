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

import android.os.Vibrator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(1000);
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
                fragment = new ResourceReportHistory();
                replaceFragment(fragment);
                break;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_resource_report_history, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Send email with QR code info
    private void senEmail(SparseArray<Barcode> qrCodes) {
        String mEmail = "filipebastias94@gmail.com";
        String mSubject = "Falta de Recurso - Info";
        String mMessage = qrCodes.valueAt(0).displayValue;

        Mail javaMailAPI = new Mail(getContext(), mEmail, mSubject, mMessage);
        javaMailAPI.execute();
        registerResourceReport(mMessage);
    }

    private void registerResourceReport(String mMessage) {
        DatabaseReference reff;
        reff = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> map = new HashMap<>();
        String uniqueID = UUID.randomUUID().toString();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        map.put("resource", new Resource(mMessage, formatter.format(date)));
        reff.child("resourceReport").child("Report_ID_" + uniqueID).setValue(map);
    }

}