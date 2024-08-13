package com.example.systemtaskmanagement.controller;


import com.example.systemtaskmanagement.service.BaseService;

public abstract class BaseController<S extends BaseService> {

    protected S service;

    protected BaseController(S service) {
        this.service = service;
    }
}
