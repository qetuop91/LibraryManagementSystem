package com.feliks.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPageRequest extends BaseRequest{
    private String name;
    private String phone;
}
