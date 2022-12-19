package com.ujjwalkumar.contacts.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ujjwalkumar.contacts.R;
import com.ujjwalkumar.contacts.databinding.ActivityContactBinding;
import com.ujjwalkumar.contacts.models.Contact;

import java.util.Random;

public class ContactActivity extends AppCompatActivity {

    private ActivityContactBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private Contact contact;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Gson gson = new Gson();
        contact = gson.fromJson(getIntent().getStringExtra("obj"), Contact.class);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference(mAuth.getUid()).child("contacts").child(contact.getId());

        binding.textViewName.setText(contact.getName());
        binding.textViewNumber.setText(String.valueOf(contact.getNumber()));
        binding.textViewInitial.setText(Character.toLowerCase(contact.getName().charAt(0)) + "");

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        binding.textViewInitial.setTextColor(color);

        binding.imageViewBack.setOnClickListener(view -> super.onBackPressed());

        binding.imageViewCall.setOnClickListener(view -> {
            Intent in = new Intent();
            in.setAction(Intent.ACTION_CALL);
            in.setData(Uri.parse("tel:".concat(String.valueOf(contact.getNumber()))));
            startActivity(in);
        });

        binding.imageViewMessage.setOnClickListener(view -> {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(String.valueOf(contact.getNumber()), null, "hi", null, null);
        });

        binding.imageViewQr.setOnClickListener(view -> {
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.dialog_show_qr, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(promptsView);
            alertDialogBuilder.setCancelable(false);

            // create alert dialog and show it
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            final ImageView imageViewDismiss = promptsView.findViewById(R.id.imageViewDismiss);
            final ImageView imageViewQr = promptsView.findViewById(R.id.imageViewQr);

            QRCodeWriter writer = new QRCodeWriter();
            try {
//                String content = contact.getName() + "#" + contact.getNumber();
                String content = "tel:" + contact.getNumber();
                BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }
                imageViewQr.setImageBitmap(bmp);

            } catch (WriterException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            imageViewDismiss.setOnClickListener(view1 -> alertDialog.dismiss());
        });

        binding.imageViewEdit.setOnClickListener(view -> Toast.makeText(this, "Under development", Toast.LENGTH_SHORT).show());

        binding.imageViewDelete.setOnClickListener(view -> {
            ref.removeValue();
            Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
            finish();
        });

        binding.imageViewShare.setOnClickListener(view -> {
            String str = contact.getName() + ": \n" + contact.getNumber();
            Intent in = new Intent(Intent.ACTION_SEND);
            in.setType("text/plain");
            in.putExtra(Intent.EXTRA_TEXT, str);
            startActivity(Intent.createChooser(in, "Share as text"));
        });

        binding.textViewNumber.setOnLongClickListener(view -> {
            // Copy number to clipboard
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", String.valueOf(contact.getNumber()));
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Number copied to clipboard", Toast.LENGTH_SHORT).show();
            return false;
        });
    }
}