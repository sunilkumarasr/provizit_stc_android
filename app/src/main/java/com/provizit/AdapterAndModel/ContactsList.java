package com.provizit.AdapterAndModel;

import java.util.LinkedList;
import java.util.Objects;

public class ContactsList {

    private boolean checked;
    private String id;
    private String name;
    private String phone;
    private String email;
    private String phote;
    private LinkedList<String> listOfPhoneNumbers;
    private LinkedList<String> listOfEmailAddress;

    public ContactsList() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactsList model = (ContactsList) o;
        return id == model.id &&
                Objects.equals(phone, model.phone) &&
                Objects.equals(listOfPhoneNumbers, model.listOfPhoneNumbers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phone, listOfPhoneNumbers);
    }

    public ContactsList(boolean checked,String id,String name, String phone, String email, String phote) {
        this.checked = checked;
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.phote = phote;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhote() {
        return phote;
    }

    public void setPhote(String phote) {
        this.phote = phote;
    }


    public LinkedList<String> getListOfPhoneNumbers() {
        return listOfPhoneNumbers;
    }

    public void setListOfPhoneNumbers(LinkedList<String> listOfPhoneNumbers) {
        this.listOfPhoneNumbers = listOfPhoneNumbers;
    }

    public LinkedList<String> getListOfEmailAddress() {
        return listOfEmailAddress;
    }

    public void setListOfEmailAddress(LinkedList<String> listOfEmailAddress) {
        this.listOfEmailAddress = listOfEmailAddress;
    }

}
