package com.nautilus.sistema_001.api.dtos;

import com.nautilus.sistema_001.api.enums.ProfileEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UsersDto {

    private Long id;
    private String name;
    private ProfileEnum profile;
    private String email;
    private String emailConfirm;
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotEmpty(message = "Nome não pode ser vazio.")
    @Length(min = 3, max = 20, message = "Nome deve conter entre 3 e 20 caracteres.")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProfileEnum getProfile() {
        return profile;
    }

    public void setProfile(ProfileEnum profile) {
        this.profile = profile;
    }

    @NotEmpty(message = "Email não pode ser vazio.")
    @Length(min = 5, max = 75, message = "Email deve conter entre 5 e 75 caracteres.")
    @Email(message = "Email inválido.")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotEmpty(message = "Email de confirmação não pode ser vazio.")
    @Length(min = 5, max = 75, message = "Email de confirmação deve conter entre 5 e 75 caracteres.")
    @Email(message = "Email de confirmação inválido.")
    public String getEmailConfirm() {
        return emailConfirm;
    }

    public void setEmailConfirm(String emailConfirm) {
        this.emailConfirm = emailConfirm;
    }

    @NotEmpty(message = "Senha não pode ser vazia.")
    @Length(min = 3, max = 8, message = "Senha deve ter entre 3 e 8 caracteres.")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDto {" +
                "id = " + id +
                ", name = '" + name + '\'' +
                ", profile = " + profile +
                ", email = '" + email + '\'' +
                ", emailConfirm = '" + emailConfirm + '\'' +
                ", password = '" + password + '\'' +
                '}';
    }
}
