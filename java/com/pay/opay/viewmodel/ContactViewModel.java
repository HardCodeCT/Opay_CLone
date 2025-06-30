package com.pay.opay.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pay.opay.database.Contact;
import com.pay.opay.database.ContactDao;
import com.pay.opay.database.ContactDatabase;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {

    private final ContactDao contactDao;
    private final LiveData<List<Contact>> allContacts;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        ContactDatabase db = ContactDatabase.getInstance(application);
        contactDao = db.contactDao();
        allContacts = contactDao.getAllContacts();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }

    public void insert(Contact contact) {
        new Thread(() -> contactDao.insert(contact)).start();
    }

    public void delete(Contact contact) {
        new Thread(() -> contactDao.delete(contact)).start();
    }

    public void update(Contact contact) {
        new Thread(() -> contactDao.update(contact)).start();
    }

    public void insertIfNotExists(Contact contact) {
        new Thread(() -> {
            int count = contactDao.countByNameAndPhone(contact.name, contact.phone);
            if (count == 0) {
                contactDao.insert(contact);
            }
        }).start();
    }
}
