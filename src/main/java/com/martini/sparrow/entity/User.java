package com.martini.sparrow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * @author martini at 2020/12/13 16:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User implements Serializable {
    private static final long serialVersionUID = -8048856791247870451L;
    private Long uid;
    /**
     * phone number with area number, just like '+86-13900000001'
     */
    @NotBlank
    private String phone;
    private String nickname;
    private String email;
    private Integer gender;
    /**
     * avatar url
     */
    private String avatar;
    private String description;
    private String password;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private Integer coin;
}
