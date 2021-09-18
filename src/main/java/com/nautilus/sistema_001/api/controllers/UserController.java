package com.nautilus.sistema_001.api.controllers;

import com.nautilus.sistema_001.api.dtos.UsersDto;
import com.nautilus.sistema_001.api.entities.Users;
import com.nautilus.sistema_001.api.enums.ProfileEnum;
import com.nautilus.sistema_001.api.response.Response;
import com.nautilus.sistema_001.api.services.UsersService;
import com.nautilus.sistema_001.api.services.impl.UsersServiceImpl;
import com.nautilus.sistema_001.api.useful.PasswordUseful;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UsersService usersService;

    @Configuration
    @Import(UsersServiceImpl.class)
    static class Config {}

    @GetMapping(value = "/{id}")
    public ResponseEntity<Response<UsersDto>> getUser(@PathVariable("id") Long id) {
        log.info("Recuperando informação do usuario ID: {}", id);
        Response<UsersDto> response = new Response<>();
        Optional<Users> user = this.usersService.findById(id);

        if (!user.isPresent()) {
            String msg = "Usuário não encontrado para o id ";
            log.info(msg.concat("{}"), id);
            response.getErrors().add(msg.concat(id.toString()));
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.convertEntityToDto(user.get()));
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/getuserbyemail")
    public ResponseEntity<Response<UsersDto>> getUserByEmail(@RequestBody UsersDto userDto) {
        log.info("Recuperando usuário logado pelo Email: {}", userDto.getEmail());
        Response<UsersDto> response = new Response<>();
        Optional<Users> user = this.usersService.findByEmail(userDto.getEmail());

        if (!user.isPresent()) {
            String msg = "Usuário não encontrado para o email ";
            log.info(msg.concat("{}"), userDto.getEmail());
            response.getErrors().add(msg.concat(userDto.getEmail()));
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.convertEntityToDto(user.get()));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<UsersDto>> addUser(@Valid @RequestBody UsersDto userDto, BindingResult result) {
        log.info("Adicionado usuário: {}", userDto.toString());
        Response<UsersDto> response = new Response<>();
        this.validateExistData(userDto, result);
        Users users = this.convertDtoToEntity(userDto);

        if (result.hasErrors()) {
            log.error("Erro ao validar dados do usuário: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        this.usersService.persist(users);
        response.setData(this.convertEntityToDto(users));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<String>> removeUser(@PathVariable("id") Long id) {
        log.info("Removendo usuário: {}", id);
        Response<String> response = new Response<>();
        Optional<Users> user = this.usersService.findById(id);

        if (!user.isPresent()) {
            String msg = "Erro ao remover usuário. ID inexistente: ";
            log.error(msg.concat("{}"), id);
            response.getErrors().add(msg.concat(id.toString()));
            return ResponseEntity.badRequest().body(response);
        }

        this.usersService.remove(id);
        return ResponseEntity.ok(response);
    }

    private void validateExistData(UsersDto userDto, BindingResult result) {
        this.usersService.findByName(userDto.getName())
                .ifPresent(user -> result.addError(new ObjectError("name", "Nome já existente.")));
        this.usersService.findByEmail(userDto.getEmail())
                .ifPresent(user -> result.addError(new ObjectError("email", "Email já existente.")));

        if (!userDto.getEmail().equals(userDto.getEmailConfirm())) {
            result.addError(new ObjectError("emailConfirm", "Emails não são iguais."));
        }
    }

    private UsersDto convertEntityToDto(Users users) {
        UsersDto dto = new UsersDto();
        dto.setId(users.getId());
        dto.setEmail(users.getEmail());
        dto.setName(users.getName());
        dto.setProfile(users.getProfile());

        return dto;
    }

    private Users convertDtoToEntity(UsersDto userDto) {
        Users users = new Users();
        users.setName(userDto.getName());
        users.setEmail(userDto.getEmail());
        users.setProfile(ProfileEnum.ROLE_USER);
        users.setPassword(PasswordUseful.generateBCrypt(userDto.getPassword()));

        return users;
    }
}
