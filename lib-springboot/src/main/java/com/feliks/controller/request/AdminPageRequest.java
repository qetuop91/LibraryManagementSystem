package com.feliks.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminPageRequest extends BaseRequest{
    private String username;
    private String phone;
    private String email;
}
