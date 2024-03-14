package com.feliks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowDto {
    private Integer account;
    private String bookName;
    private String bookNumber;
    private Integer nums;
    private Integer score;
    private String userName;
    private String userPhone;
    private String userUsername;

    @Override
    public String toString() {
        return "BorrowDto{" +
                "account=" + account +
                ", bookName='" + bookName + '\'' +
                ", bookNumber='" + bookNumber + '\'' +
                ", nums=" + nums +
                ", score=" + score +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userUsername='" + userUsername + '\'' +
                '}';
    }
}
