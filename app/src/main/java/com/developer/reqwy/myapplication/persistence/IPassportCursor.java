package com.developer.reqwy.myapplication.persistence;

import com.developer.reqwy.myapplication.base.Passport;

import java.util.Map;

public interface IPassportCursor {
    Map<String, String> getPassport();
    Passport getPassportEntity();
}
