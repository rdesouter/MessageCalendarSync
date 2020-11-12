package com.rdesouter.message;

public class EventTest {

    private String phone;
    private String begin;
    private String end;
    private String address;

    public EventTest() {
    }

    public EventTest(String phone, String begin, String end, String address) {
        this.phone = phone;
        this.begin = begin;
        this.end = end;
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }
    public String getBegin() {
        return begin;
    }
    public String getEnd() {
        return end;
    }
    public String getAddress() {
        return address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setBegin(String begin) {
        this.begin = begin;
    }
    public void setEnd(String end) {
        this.end = end;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "EventTest{" +
                "phone='" + phone + '\'' +
                ", begin='" + begin + '\'' +
                ", end='" + end + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
