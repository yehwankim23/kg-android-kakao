package com.example.sam.kakao;

public class Member {
    int seq;
    String name, pass, email, phone, addr, photo;

    public int getSeq() {
        return seq;

    }

    public void setSeq(int seq) {
        this.seq = seq;

    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;

    }

    public String getPass() {
        return pass;

    }

    public void setPass(String pass) {
        this.pass = pass;

    }

    public String getEmail() {
        return email;

    }

    public void setEmail(String email) {
        this.email = email;

    }

    public String getPhone() {
        return phone;

    }

    public void setPhone(String phone) {
        this.phone = phone;

    }

    public String getAddr() {
        return addr;

    }

    public void setAddr(String addr) {
        this.addr = addr;

    }

    public String getPhoto() {
        return photo;

    }

    public void setPhoto(String photo) {
        this.photo = photo;

    }

    @Override
    public String toString() {
        return "Member{" +
                "seq=" + seq +
                ", name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", addr='" + addr + '\'' +
                ", photo='" + photo + '\'' +
                '}';

    }

}
