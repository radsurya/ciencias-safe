package com.example.cienciassafe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class BluetoothActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    TextView mStatusBlueTv, mPairedTv;
    ImageView mBlueIv;
    Button mOnBtn, mOffBtn, mDiscoverBtn, mPairedBtn;

    BluetoothAdapter mBlueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mStatusBlueTv = findViewById(R.id.statusBluetoothTv);
        mPairedTv = findViewById(R.id.pairedTv);
        mBlueIv = findViewById(R.id.bluetoothIv);
        mOnBtn = findViewById(R.id.onBtn);
        mOffBtn = findViewById(R.id.offBtn);
        mPairedBtn = findViewById(R.id.pairedBtn);

        // Adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        // Check if bluetooth is available or not
        if (mBlueAdapter == null) {
            if (mStatusBlueTv != null)
                mStatusBlueTv.setText("Bluetooth não está disponível");
        } else {
            if (mStatusBlueTv != null)
                mStatusBlueTv.setText("Bluetooth está disponível");

            // Set image according to bluetooth status (on/off)
            if (mBlueAdapter.isEnabled()) {
                if (mBlueIv != null)
                    mBlueIv.setImageResource(R.drawable.ic_bluetooth_on);
            } else {
                if (mBlueIv != null)
                    mBlueIv.setImageResource(R.drawable.ic_bluetooth_off);
            }

            // On button click
            if (mOnBtn != null) {
                mOnBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mBlueAdapter.isEnabled()) {
                            showToast("A ligar Bluetooth...");
                            // Intent to on bluetooth
                            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(intent, REQUEST_ENABLE_BT);
                        } else {
                            showToast("Bluetooth já está ligado");
                        }
                    }
                });
            }

            // Discover bluetooth button click
            if (mDiscoverBtn != null) {
                mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mBlueAdapter.isDiscovering()) {
                            showToast("A tornar o dispositivo detetável");
                            // Intent to on bluetooth
                            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                            startActivityForResult(intent, REQUEST_DISCOVER_BT);
                        }
                    }
                });
            }

            // Off button click
            if (mOffBtn != null) {
                mOffBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mBlueIv !=null && mBlueAdapter.isEnabled()) {
                            mBlueAdapter.disable();
                            showToast("A desligar Bluetooth...");
                            mBlueIv.setImageResource(R.drawable.ic_bluetooth_off);
                        } else {
                            showToast("Bluetooth já está desligado");
                        }
                    }
                });
            }

            // Get paired devices button click
            if (mOffBtn != null){
                mPairedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mBlueAdapter.isEnabled()) {
                            if (mPairedTv != null) {
                                mPairedTv.setText("Dispositivos emparelhados");
                                Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();
                                for (BluetoothDevice device : devices) {
                                    mPairedTv.append("\nDispositivo: " + device.getName() + "," + device);
                                }
                            }
                        } else {
                            // Bluetooth is off so can't get paired devices
                            showToast("Ligue o Bluetooth para obter dispositivos emparelhados");
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK && mBlueIv == null) {
                  mBlueIv.setImageResource(R.drawable.ic_bluetooth_on);
                  showToast("Bluetooth ligado!");
                } else {
                    // User deny to turn bluetooth on
                    showToast("Não foi possível ligar o Bluetooth!");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Toast message Function
     */
    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}