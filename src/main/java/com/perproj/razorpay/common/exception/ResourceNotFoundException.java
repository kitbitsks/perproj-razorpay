package com.perproj.razorpay.common.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private Object resourceId;
    private String resourceName;

    public ResourceNotFoundException( String resourceName, Object resourceId){
        super(resourceName + " not found with " + resourceId);
        this.resourceId = resourceId;
        this.resourceName = resourceName;
    }

}
