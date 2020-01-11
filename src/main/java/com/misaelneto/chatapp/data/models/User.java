package com.misaelneto.chatapp.data.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    private String uid;
    private String name;


}
