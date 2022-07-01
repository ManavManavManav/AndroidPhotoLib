package com.example.photos;

import java.io.Serializable;

public class Tag implements Serializable {
    private String key;
    private String value;

    private static final long serialVersionUID = 1L;

    public Tag(String myName, String myVal) {
       value = myVal; key = myName;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public boolean equals(Tag myTag) {
        return key.equalsIgnoreCase(myTag.getKey()) && value.equalsIgnoreCase(myTag.getValue());
    }

    public String toString() {
        return key + ":" + value;
    }


}
