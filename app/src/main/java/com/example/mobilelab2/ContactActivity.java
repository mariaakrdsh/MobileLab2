package com.example.mobilelab2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {
    Button all_contacts_btn, ivan_contacts_btn, back_btn;
    ListView lv_contactsList;
    ArrayAdapter contactsArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);

        all_contacts_btn = findViewById(R.id.all_contacts_btn);
        ivan_contacts_btn = findViewById(R.id.ivan_contacts_btn);
        back_btn = findViewById(R.id.back_btn);
        lv_contactsList = findViewById(R.id.lv_contactsList);

        all_contacts_btn.setOnClickListener(view -> {
            contactsArrayAdapter = new ArrayAdapter<>(ContactActivity.this, android.R.layout.simple_list_item_1, getPhoneContactsList());
            lv_contactsList.setAdapter(contactsArrayAdapter);
        });

        ivan_contacts_btn.setOnClickListener(view -> {
            contactsArrayAdapter = new ArrayAdapter<>(ContactActivity.this, android.R.layout.simple_list_item_1, getSortedContactsList());
            lv_contactsList.setAdapter(contactsArrayAdapter);
        });

        back_btn.setOnClickListener(v -> {
            finish();
        });

        lv_contactsList.setOnItemClickListener((adapterView, view, i, l) -> {
            Contact contact = (Contact) adapterView.getItemAtPosition(i);
            String address = contact.getAddress();
            Intent intent = new Intent(ContactActivity.this, GeolocationActivity.class);
            Log.i("ADDRESS", address);
            intent.putExtra("ADDRESS", address);
            startActivity(intent);
        });
    }

    private List<Contact> getPhoneContactsList() {
        List<Contact> returnList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(ContactActivity.this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactActivity.this, new String[]{android.Manifest.permission.READ_CONTACTS}, 0);
        }

        ContentResolver contentResolver = getContentResolver();
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        // First, get the contact names and numbers
        Cursor phoneCursor = contentResolver.query(phoneUri, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                String contactName = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String contactNumber = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String contactId = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

                // Now, query the address for this contact ID
                Uri addressUri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
                Cursor addressCursor = contentResolver.query(addressUri, null, ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?", new String[]{contactId}, null);

                String contactAddress = "";
                if (addressCursor != null && addressCursor.moveToFirst()) {
                    contactAddress = addressCursor.getString(addressCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
                    addressCursor.close();
                }

                Contact contact = new Contact(contactName, contactNumber, contactAddress);
                returnList.add(contact);
            }
            phoneCursor.close();
        }

        return returnList;
    }

    private List<Contact> getSortedContactsList() {
        List<Contact> returnList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactActivity.this, new String[] {Manifest.permission.READ_CONTACTS}, 0);
        }

        ContentResolver contentResolver = getContentResolver();
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        Cursor phoneCursor = contentResolver.query(phoneUri, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                String contactName = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String contactNumber = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String contactId = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

                // Split the name and check if the first part is "Ivan"
                String[] nameParts = contactName.split(" ");
                if (nameParts.length > 0 && nameParts[0].equalsIgnoreCase("Ivan")) {
                    // Query the address for this contact ID
                    Uri addressUri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
                    Cursor addressCursor = contentResolver.query(addressUri, null, ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?", new String[]{contactId}, null);

                    String contactAddress = "";
                    if (addressCursor != null && addressCursor.moveToFirst()) {
                        contactAddress = addressCursor.getString(addressCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
                        addressCursor.close();
                    }

                    Contact contact = new Contact(contactName, contactNumber, contactAddress);
                    returnList.add(contact);
                }
            }
            phoneCursor.close();
        }

        return returnList;
    }

}
