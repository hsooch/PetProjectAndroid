package com.example.nahcoos.petproject.board.list;

/**
 * Created by KwonHyungsub on 2016-06-08.
 */
public class PetOwner {
/*    ImageView edit_photo;
    EditText edit_name, edit_whatKind,edit_registNumber, edit_address,edit_contactPoint;
    CheckBox edit_isOperation, edit_isRegularCheck;
    RadioButton edit_boy, edit_girl;
    RadioGroup edit_sex;*/

    int petOwner_id;
    String photo;
    String name;
    String whatKind;
    String registNumber;
    String address;
    String contactPoint;
    String isOperation;
    String isRegularCheck;
    String boy;
    String girl;
    String sex;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBoy() {
        return boy;
    }

    public void setBoy(String boy) {
        this.boy = boy;
    }

    public String getContactPoint() {
        return contactPoint;
    }

    public void setContactPoint(String contactPoint) {
        this.contactPoint = contactPoint;
    }

    public String getGirl() {
        return girl;
    }

    public void setGirl(String girl) {
        this.girl = girl;
    }

    public String getIsOperation() {
        return isOperation;
    }

    public void setIsOperation(String isOperation) {
        this.isOperation = isOperation;
    }

    public String getIsRegularCheck() {
        return isRegularCheck;
    }

    public void setIsRegularCheck(String isRegularCheck) {
        this.isRegularCheck = isRegularCheck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPetOwner_id() {
        return petOwner_id;
    }

    public void setPetOwner_id(int petOwner_id) {
        this.petOwner_id = petOwner_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRegistNumber() {
        return registNumber;
    }

    public void setRegistNumber(String registNumber) {
        this.registNumber = registNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getWhatKind() {
        return whatKind;
    }

    public void setWhatKind(String whatKind) {
        this.whatKind = whatKind;
    }
}
