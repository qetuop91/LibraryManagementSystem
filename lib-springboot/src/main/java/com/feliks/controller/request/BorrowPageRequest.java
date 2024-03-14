package com.feliks.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowPageRequest extends BaseRequest{
    private String bookName;
    private String bookNumber;
    private String userName;
    private String userUsername;
    private String status;
}
