package com.wuzhulin.vo.param;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class LoginParam {

    private String account;

    private String password;

    private String nickname;

    @Range(min = 1,max = 10,message = "id的大小在1-10的范围内",groups = test1.class)
    @Range(min = 11,max = 20,message = "id的大小在11-20的范围内",groups = test2.class)
    private Integer id;

    public interface test1{}

    public interface test2{}
}
